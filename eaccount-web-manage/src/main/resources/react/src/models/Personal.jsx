const { routerRedux } = dva.router;
const { message: { success } } = antd;
import { post } from '../utils/Request.jsx';
import { checkUrl } from '../utils/ModelUtils.jsx'
import { namespace as appNamespace } from './App.jsx'

export const namespace = 'personal';
export const baseUrl = 'personal';

export default {
    namespace: namespace,
    state: {
        userInfo: {}
    },
    reducers: {
        reInit(state, { payload }) {
            return { ...payload };
        },
        setUserInfo(state, { payload: { userInfo } }) {
            return { ...state, userInfo };
        }
    },
    effects: {
        *updatePassword({ payload: { values } }, { call, put }) {// 修改个人密码
            const { msg } = yield call(post, `${baseUrl}/updatePassword`, values);
            yield call(success, msg);
            yield put({ type: `${appNamespace}/reloadLoginData`, payload: { pathname: '/index' } });
        },
        *getUserInfo(action, { call, put }) {// 获取个人信息
            const { data } = yield call(post, `${baseUrl}/getUserInfo`);
            yield put({ type: 'setUserInfo', payload: { userInfo: data} });
        },
        *updateUserInfo({ payload: { values } }, { call, put }) {// 修改个人密码
            const { msg } = yield call(post, `${baseUrl}/updateUserInfo`, values);
            yield call(success, msg);
            yield put(routerRedux.push({ pathname: '/index' }));
        }
    },
    subscriptions: {
        setup({ dispatch, history }) {
            return history.listen(({ pathname, query }) => {
                if (checkUrl('personal/userInfo', pathname)) {
                    dispatch({ type: 'getUserInfo' });
                }
            });
        }
    }
};