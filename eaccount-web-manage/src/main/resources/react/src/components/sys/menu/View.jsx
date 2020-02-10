const { connect } = dva;
const { Button, Table, Icon } = antd;
const { Plink } = require('../../Widgets.jsx');
const { ToolbarGroup } = Plink;
const Constants = require('../../Constants.jsx');
import { namespace } from '../../../models/sys/Menu.jsx'

const formType = `${namespace}/openFormPage`;

function SysModule (props) {
    const { dataSource, loading, dispatch, expandedRowKeys } = props;
    return (
        <div style={{padding: 20}}>
            <div className='search-result-list'>
                <ToolbarGroup>
                    <Plink type='primary' sn="sys_menu_save" onClick={ () => dispatch({ type: formType }) }>新增</Plink>
                    <Button onClick={ () => dispatch({ type: `${namespace}/reload` }) } disabled={loading}>刷新</Button>
                </ToolbarGroup>
                <Table
                    dataSource={dataSource}
                    pagination={false}
                    loading={loading}
                    defaultExpandedRowKeys={expandedRowKeys}
                    onExpandedRowsChange={(expandedRowKeys) => dispatch({ type: `${namespace}/onExpandedRowsChange`, payload: { expandedRowKeys } })}
                    bordered
                    columns={ [ {
                        title: '菜单文本',
                        dataIndex: 'label',
                        width: '30%'
                    }, {
                        title: '菜单图标',
                        dataIndex: 'icon',
                        width: 110,
                        render: (text, record) => (
                            <Icon type={text}/>
                        )
                    }, {
                        title: 'uri',
                        dataIndex: 'path'
                    }, {
                        title: '权限标识',
                        dataIndex: 'sn'
                    }, {
                        title: '排序号',
                        dataIndex: 'weight',
                        width: 80
                    }, Plink.getTableColumnAction(
                        [ {
                            label: '编辑',
                            sn: "sys_menu_update",
                            onClick: (text, record) => dispatch({ type: formType, payload: { record }})
                        }, {
                            label: '删除',
                            sn: "sys_menu_delete",
                            confirmText: Constants.DELETE_TEXT,
                            onConfirm: (text, record) => dispatch({ type: `${namespace}/del`, payload: { record }})
                        }, {
                            label: '添加子节点',
                            sn: "sys_menu_save",
                            onClick: (text, record) => dispatch({ type: formType, payload: { record: { pid: record.key }, title: `(${record.label})子节点` }})
                        }, {
                            label: '添加资源',
                            sn: "sys_menu_updateRes",
                            onClick: (text, record) => dispatch({ type: `${namespace}/resourcePage`, payload: { record, url: 'selectResources' }})
                        }, {
                            label: '查看资源',
                            sn: "sys_menu_queryRes",
                            onClick: (text, record) => dispatch({ type: `${namespace}/resourcePage`, payload: { record, url: 'queryResources' }})
                        } ]
                    ) ] }
                />
            </div>
        </div>
    );
}

module.exports = connect(state => {
    const effects = state.loading.effects;
    return { ...state[namespace], loading: effects[`${namespace}/fetch`] || effects[`${namespace}/del`]  }
})(SysModule);