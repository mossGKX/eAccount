const { connect } = dva;
const { Table, Button } = antd;
const { Plink: { ToolbarGroup }, Plink } = require('../../Widgets.jsx');
const Constants = require('../../Constants.jsx');
import { namespace } from '../../../models/sys/Organization.jsx'

const formType = `${namespace}/openFormPage`;

function SysModule (props) {
    const { dataSource, loading, dispatch, expandedRowKeys } = props;
    return (
        <div style={{padding: 20}}>
            <div className='search-result-list'>
                <ToolbarGroup>
                    <Plink type='primary' sn="sys_org_save" onClick={ () => dispatch({ type: formType }) }>新增</Plink>
                    <Button onClick={ () => dispatch({ type: `${namespace}/reload` }) } disabled={loading}>刷新</Button>
                </ToolbarGroup>
                <Table
                    dataSource={dataSource}
                    pagination={false}
                    loading={loading}
                    bordered
                    defaultExpandedRowKeys={expandedRowKeys}
                    onExpandedRowsChange={(expandedRowKeys) => {dispatch({ type: `${namespace}/onExpandedRowsChange`, payload: { expandedRowKeys } })} }
                    columns={ [ {
                        title: '名称',
                        dataIndex: 'name',
                        width: '30%'
                    }, {
                        title: '描述',
                        dataIndex: 'description'
                    }, {
                        title: '优先级',
                        dataIndex: 'weight',
                        width: 100
                    },  Plink.getTableColumnAction(
                        [ {
                            label: '修改',
                            sn: 'sys_org_update',
                            onClick: (text, record) => dispatch({ type: formType, payload: { record }})
                        }, {
                            label: '删除',
                            sn: 'sys_org_delete',
                            confirmText: Constants.DELETE_TEXT,
                            onConfirm: (text, record) => dispatch({ type: `${namespace}/del`, payload: { record }})
                        }, {
                            label: '添加子节点',
                            sn: 'sys_org_save',
                            onClick: (text, record) => dispatch({ type: formType, payload: {record: { parentId: record.key }, title: `(${record.name})子组织录入` } })
                        } ]
                    ) ] }
                />
            </div>
        </div>
    );
}

module.exports = connect(state => {
    const effects = state.loading.effects;
    return { ...state[namespace], loading: effects[`${namespace}/fetch`] || effects[`${namespace}/del`] }
})(SysModule);