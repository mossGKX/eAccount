const { routerRedux } = dva.router;
import ModelUtils, { listenReload, checkUrl } from '../../utils/ModelUtils.jsx'
import { post } from '../../utils/Request.jsx'
import { namespace as menuNamespace, baseUrl as menuUrl } from './Menu.jsx'

export const namespace = 'sysRole';
export const baseUrl = 'sys/role';
const model = ModelUtils(baseUrl, namespace);

function getPermissionTreeKeys(permissions, selectedRowKeys) {
    permissions.forEach(value => {
        selectedRowKeys.push(value.key);
        getPermissionTreeKeys(value.children || [], selectedRowKeys);
    });
}

export default {
    ...model,
    effects: {
        ...model.effects,
        *menuPage({ payload: { record, url } }, { call, put }) {// 打开选择/查看资源页面
            yield put(routerRedux.push({ pathname: `/${baseUrl}/${url}` }));
            const { data: dataSource } = yield call(post, `${menuUrl}/getRoleMenu/${record.key}`);
            const selectedRowKeys = [];
            getPermissionTreeKeys(dataSource, selectedRowKeys);// 获取权限树的所有key
            yield put({ type: 'setEditData', payload: { dataSource, record, selectedRowKeys } });
        }
    },
    subscriptions: {
        setup({ dispatch, history }) {
            return history.listen(({ pathname, query }) => {
                listenReload(dispatch, baseUrl, pathname);
                if(checkUrl(pathname, `${baseUrl}/selectMenu`)) {
                    dispatch({ type: `${menuNamespace}/reload` });
                }
            });
        }
    }
};