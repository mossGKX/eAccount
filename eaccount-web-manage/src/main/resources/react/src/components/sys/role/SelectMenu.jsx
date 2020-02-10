const { connect, router: { routerRedux } } = dva;
const { Icon, Button } = antd;
const { Plink, SelectTreeTable } = require('../../Widgets.jsx');
import { namespace, baseUrl } from '../../../models/sys/Role.jsx'
import { namespace as menuNamespace } from '../../../models/sys/Menu.jsx'

function handleSubmit(dispatch, { key, version }, selectedRowKeys) {
    const role = { key, version };
    role.menus = [];
    selectedRowKeys.forEach(value => {
        role.menus.push({ key: value })
    });
    dispatch({ type: `${namespace}/save`, payload: { values: role, url: 'updateMenu' } })
}

function getChildrenKeys(children, keys) {// 子节点选择
    if(!children)
        return;
    children.forEach(v => {
        keys.add(v.key);
        getChildrenKeys(v.children, keys);
    })
}

export const columns = [ {
        title: '菜单文本',
        dataIndex: 'label',
        width: '40%'
    }, {
        title: '菜单图标',
        dataIndex: 'icon',
        width: '10%',
        render: (text, record) => <Icon type={text}/>
    }, {
        title: '组件名',
        dataIndex: 'panel',
        width: '25%'
    }, {
        title: '排序号',
        dataIndex: 'weight'
    } ];

function Selectmenu (props) {
    const { edit, edit: { record, selectedRowKeys },
        loading, menuLoading, menuDataSource, expandedRowKeys, dispatch } = props;
    return (
        <div style={{padding: '10px 20px 20px 20px'}} className="search-result-list">
            <div className="search-toolbar">
                <Icon type="bars" /><span style={{ paddingLeft: 2 }}>{`(${record ? record.name : ''})权限列表`}</span>
                <Button
                    onClick={handleSubmit.bind(this, dispatch, record, selectedRowKeys)}
                    type="primary" disabled={loading} style={{ marginLeft: 8 }}>提交</Button>
                <Button onClick={() => dispatch(routerRedux.push({ pathname: baseUrl }))}>返回</Button>
            </div>
            <SelectTreeTable
                dataSource={menuDataSource}
                pagination={false}
                loading={menuLoading}
                defaultExpandedRowKeys={expandedRowKeys}
                onExpandedRowsChange={(expandedRowKeys) => dispatch({ type: `${menuNamespace}/onExpandedRowsChange`, payload: { expandedRowKeys } })}
                rowSelection={{
                    selectedRowKeys,
                    onChange: selectedRowKeys => dispatch({ type: `${namespace}/setEditData`, payload: { ...edit, selectedRowKeys } })
                }}
                columns={ [ ...columns, {
                    title: '操作',
                    key: 'Action',
                    width: 100,
                    render: (text, record) => (
                        <span>
                            <Plink onClick={() => {
                                const set = new Set(selectedRowKeys);
                                set.add(record.key);
                                getChildrenKeys(record.children, set);
                                dispatch({ type: `${namespace}/setEditData`, payload: { ...edit, selectedRowKeys: Array.from(set) } })
                            }}>全选子节点</Plink>
                        </span>
                    )
                } ] }
            />
        </div>
    )
}

export default connect(state => {
    const effects = state.loading.effects;
    return {
        ...state[namespace],
        loading: effects[`${namespace}/save`],
        menuLoading: effects[`${menuNamespace}/fetch`] || effects[`${namespace}/menuPage`],
        menuDataSource: state[menuNamespace].dataSource,
        expandedRowKeys: state[menuNamespace].expandedRowKeys
    }
})(Selectmenu);