const { routerRedux } = dva.router;
import { genMenu, getbreadCrumb } from '../utils/AppUtils.jsx';
import { post, get } from '../utils/Request.jsx';
import { getUriPath, rsaEncrypt } from '../utils/StringUtils.jsx'
import Models from '../Models.jsx'
import { namespace as captchaNamespace } from './ImageCaptcha.jsx'

const { menus, username } = window.sys || {};

export const namespace = 'app';

export default {
    namespace,
    state: {
        loginSuccess: !!menus,
        collapsed: false,
        breadCrumbs: [],
        pathNames: [],
        menus: genMenu(menus || [], 0),// 生成菜单
        username: username
    },
    reducers: {
        reInit(state, { payload }) {
            return { ...payload };
        },
        toggle(state) {// 菜单打开关闭方法
            return { ...state, collapsed: !state.collapsed }
        },
        setbreadCrumb(state, { payload: pathname }) {// 设置面包屑方法
            let pathNames;
            if(pathname === 'index' || pathname === 'index/') {
                pathNames = ['/', '/personal', '/index'];
            } else {
                pathNames = getUriPath(pathname);
            }
            return { ...state, breadCrumbs: getbreadCrumb(pathNames), pathNames }
        },
        onOpenMenuChange(state, { payload: { openKeys } }) {// 记录打开菜单KEY
            return { ...state, openKeys }
        },
        openCaptcha(state) {// 打开登陆验证码
            return { ...state, loginCaptcha: true }
        },
        loginSuccess(state, { payload: { data: { id, rsa, sns, menus, username } } }) {// 登陆成功
            const sys = (window.sys = window.sys || {});
            sys.id = id;// crsf token重新赋值(Request.jsx)
            sys.rsa = rsa;// rsa公钥重新赋值
            sys.sns = new Set(sns);
            return {
                ...state,
                loginSuccess: true,
                menus: genMenu(menus || [], 0),// 生成菜单
                username: username
            }
        }
    },
    effects: {
        *login({ payload }, { call, put }) {// 登陆
            // imageCaptcha参数传入图片验证码的命名空间，业务错误进行自动刷新操作
            const { code, data } = yield call(post, 'login', payload);
            if(code === 1) {
                yield put({ type: 'openCaptcha' });
                yield put({ type: `${captchaNamespace}/getCaptcha`, payload: { namespace } });
                return;
            }
            yield put({ type: 'loginSuccess', payload: { data } });
            yield put(routerRedux.push({ pathname: '/index' }));
        },
        *reloadLoginData({ payload: { pathname } }, { call, put }) {// 重新加载登陆数据
            const { data } = yield call(get, 'login');
            yield put({ type: 'loginSuccess', payload: { data } });
            if(pathname === '-1') {
                history.go(-1);
            } else {
                yield put(routerRedux.push({ pathname: pathname }));
            }
        },
        *logout(active, { call, put }) {// 退出登录
            yield call(post, 'logout');
        },
        *clearData(active, { call, put }) {// 退出登录
            // 重新初始化数据
            for(let i = 0; i < Models.length; i++) {
                const model = Models[i];
                if(model.namespace === namespace) {
                    yield put({ type: `reInit`, payload: model.state });
                } else {
                    yield put({ type: `${model.namespace}/reInit`, payload: model.state });
                }
            }
        }
    },
    subscriptions: {
        setup({ dispatch, history }) {
            return history.listen(({ pathname, query }) => {
                // 设置面包屑
                dispatch({ type: 'setbreadCrumb', payload: pathname });
                if(pathname === '' || pathname === '/') {
                    dispatch(routerRedux.push({ pathname: '/index' }));
                }
                // 监听window.sys数据是否初始化
                if(!window.sys) {
                    dispatch({ type: 'reloadLoginData', payload: { pathname } });
                }
            });
        }
    }
};