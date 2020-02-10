import { post, get } from '../utils/Request.jsx';
import ModelUtils from '../utils/ModelUtils.jsx'

export const namespace = 'imageCaptcha';

export default {
    namespace,
    state: {},
    reducers: {
        reInit: ModelUtils(namespace).reducers.reInit,
        deleteCaptcha(state, { payload: { namespace } }) {
            const newState = { ...state };
            newState[namespace] = null;
            return newState;
        },
        saveCaptcha(state, { payload: { namespace, captcha } }) {
            const newState = { ...state };
            newState[namespace] = captcha;
            return newState;
        }
    },
    effects: {
        *getCaptcha({ payload: { namespace: ns } }, { call, put, select }) {
            const captcha = yield select(state => state[namespace][ns]);
            yield put({ type: 'deleteCaptcha', payload: { namespace: ns } });
            const { data } = yield call(get, `captcha?securityCode=${captcha ? captcha.securityCode : ''}`);
            yield put({ type: 'saveCaptcha', payload: { namespace: ns, captcha: data } });
        }
    }
};