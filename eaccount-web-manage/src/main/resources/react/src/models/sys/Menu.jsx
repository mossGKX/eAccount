const { message } = antd;
const { routerRedux } = dva.router;
import { post } from '../../utils/Request.jsx';
import ModelUtils, { listenReload, checkUrl } from '../../utils/ModelUtils.jsx'
import { arrayHasKey } from '../../utils/CheckUtils.jsx'
import { namespace as resourcesNamespace, baseUrl as resourcesUrl } from './Resources.jsx'

export const namespace = 'sysMenu';
export const baseUrl = 'sys/menu';

const model = ModelUtils(baseUrl, namespace);

export default {
    ...model,
    reducers: {
        ...model.reducers,
        reInit(state, { payload }) {
            return state;
        },
        addResource(state, { payload: { record } }) {
            const dataSource = state.edit.dataSource;
            if(!dataSource) {
                message.error('数据未初始化');
                return state;
            }
            if(arrayHasKey(dataSource, record.key)) {
                return state;
            }
            return { ...state, edit: { ...state.edit, dataSource: [ ...dataSource, record ] } };
        },
        delResource(state, { payload: { index } }) {
            const dataSource = state.edit.dataSource;
            dataSource.splice(index, 1);
            return { ...state, edit: { ...state.edit, dataSource: [ ...dataSource ] } };
        }
    },
    effects: {
        ...model.effects,
        *resourcePage({ payload: { record, url } }, { call, put }) {// 打开选择/查看资源页面
            yield put(routerRedux.push({ pathname: `/${baseUrl}/${url}` }));
            const { data: dataSource } = yield call(post, `${resourcesUrl}/getByMenuId/${record.key}`);
            yield put({ type: 'setEditData', payload: { dataSource, record } });
        }
    },
    subscriptions: {
        setup({ dispatch, history }) {
            return history.listen(({ pathname, query }) => {
                listenReload(dispatch, baseUrl, pathname);
                if(checkUrl(pathname, `${baseUrl}/selectResources`)) {
                    dispatch({ type: `${resourcesNamespace}/reload`, payload: { pagination: { page: 1 } } });
                }
            });
        }
    }
};