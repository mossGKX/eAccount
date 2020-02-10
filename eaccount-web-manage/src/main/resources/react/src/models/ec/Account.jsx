import ModelUtils from '../../utils/ModelUtils.jsx';
import {post} from '../../utils/Request.jsx';

export const namespace =  'ecAccount';
export const baseUrl =  'ec/account';

const model = ModelUtils(baseUrl, namespace);

export default {
    ...model,
    effects: {
        ...model.effects,
        *fetch({payload: {pagination: {page, size = 10}, searchData}}, {call, put}) {
            if (searchData && searchData.accountType && searchData.accountType == 'all') {
                searchData.accountType = '';
            }
            const {data: {rows, pagination}} = yield call(post, `${baseUrl}/list?page=${page}&size=${size}`, searchData);
            yield put({type: 'saveRet', payload: {rows, pagination, searchData}});
        },
    }
};