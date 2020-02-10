const { message } = antd;
import ModelUtils from '../../utils/ModelUtils.jsx';
import { post } from '../../utils/Request.jsx';

export const namespace =  'user';
export const baseUrl =  'sys/user';

const model = ModelUtils(baseUrl, namespace);

export default {
    ...model,
    effects: {
        ...model.effects,
        *shotOff({ payload: { record: { account: { account } }} }, { call, put }) {
            const formData = new FormData();
            formData.append("username", account);
            const { msg } = yield call(post, `${baseUrl}/shotOff`, formData);
            yield call(message.success, msg);
            yield put({ type: 'reload' });
        }
    }
};