import ModelUtils from '../../utils/ModelUtils.jsx';
import { rsaEncrypt } from '../../utils/StringUtils.jsx';
import {post} from '../../utils/Request.jsx';
const {message} = antd;
const { routerRedux } = dva.router;

export const namespace =  'ecUser';
export const baseUrl =  'ec/user';

const model = ModelUtils(baseUrl, namespace);

export default {
    ...model,
    effects: {
        ...model.effects,
        *fetch({ payload: { pagination: { page, size = 10 }, searchData } }, { call, put }) {
            if (searchData && searchData.userType && searchData.userType == 'all') {
                searchData.userType = '';
            }
            const { data: { rows, pagination } } = yield call(post, `${baseUrl}/list?page=${page}&size=${size}`, searchData);
            yield put({ type: 'saveRet', payload: { rows, pagination, searchData }});
        },
        *save({ payload: { values, url } }, { call, put }) {
            if (url == "update") {
                let entity = {};
                entity.key = values.key;
                entity.userName = values.userName;
                values = entity;
            } else {
                // 密码加密
                values.password = rsaEncrypt(values.password);
                values.payPwd = rsaEncrypt(values.payPwd);
            }
            const { msg } = yield call(post, `${baseUrl}/${url}`, values);
            yield call(message.success, msg);
            yield put({ type: 'clearEditData' });// 清空编辑信息
            yield put(routerRedux.push({ pathname: `/${baseUrl}` }));
        }
    }
};