import Models from './Models.jsx'
import { isEmpty } from './utils/CheckUtils.jsx'
import { namespace as captchaNamespace } from './models/ImageCaptcha.jsx'
import { namespace as appNamespace } from './models/App.jsx'
const { message: { error } } = antd;
const { Router, browserHistory } = dva.router;

// Initialize
const app = dva.default({
    history: browserHistory,
    onError(e, dispatch) {
        // 登陆过期退出清理缓存数据
        const logout = e.logout;
        if(logout === true) {
            dispatch({ type: `${appNamespace}/clearData` });
            browserHistory.push('/login');
            return;
        }
        // 自动刷新图片验证码
        const imageCaptcha = e.imageCaptcha;
        if(!isEmpty(imageCaptcha))
            dispatch({ type: `${captchaNamespace}/getCaptcha`, payload: { namespace: imageCaptcha } });
        // 弹出错误信息
        const msg = e.message;
        !(isEmpty(msg) || msg.indexOf('csrf id isEmpty') !== -1) ? error(msg) : '';
    }
});

// Plugins
app.use(require('./utils/DvaLoading.jsx').createLoading({ effects: true }));// 启用effects级别的loding

// 注册models()
Models.forEach(v => app.model(v));

// 配置路由
app.router(({ history }) => <Router history={history}>{require('./Route.jsx').routes}</Router>);

// Start
app.start('#root');