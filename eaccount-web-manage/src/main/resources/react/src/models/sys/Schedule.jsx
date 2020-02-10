const {message} = antd;
import {post} from '../../utils/Request.jsx';
import ModelUtils from '../../utils/ModelUtils.jsx';

export const namespace = 'schedule';
export const baseUrl = 'sys/schedule';
export const listUrl = 'list';

const model = ModelUtils(baseUrl, namespace, listUrl);

export default {
    ...model,
    effects: {
        ...model.effects,
        *start({payload : key}, {call, put}) {
            const {msg} = yield call(post, `${baseUrl}/start/${key}`);
            yield call(message.success, msg);
            yield put({type: 'reload'});
        },
        *stop({payload : key}, {call, put}) {
            const {msg} = yield call(post, `${baseUrl}/stop/${key}`);
            yield call(message.success, msg);
            yield put({type: 'reload'});
        },
    }
};