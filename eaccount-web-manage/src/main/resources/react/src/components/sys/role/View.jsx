const { connect } = dva;
const { Input, Table } = antd;
const { BaseSearchForm, Plink, Plink: { ToolbarGroup } } = require('../../Widgets.jsx');
const Constants = require('../../Constants.jsx');
import { namespace } from '../../../models/sys/Role.jsx'

const formType = `${namespace}/openFormPage`;

function SysModule (props) {
    const { searchData, dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{padding: 20}}>
            <BaseSearchForm
                loading= {loading}
                searchData={ searchData }
                handleSearch={ (values) => dispatch({ type: `${namespace}/fetch`, payload: { pagination: { page: 1 }, searchData: values }}) }>
                <Input name="name" label="角色名称" />
            </BaseSearchForm>
            <div className='search-result-list'>
                <ToolbarGroup>
                    <Plink type='primary' sn="sys_role_save" onClick={ () => dispatch({ type: formType }) }>新增</Plink>
                </ToolbarGroup>
                <Table
                    dataSource={dataSource}
                    pagination={pagination}
                    loading={loading}
                    onChange={( pagination ) => { dispatch({ type: `${namespace}/paginationLoad`, payload: { pagination }}) }}
                    bordered
                    columns={ [ {
                        title: '角色名称',
                        dataIndex: 'name'
                    }, {
                        title: '说明',
                        dataIndex: 'description'
                    }, Plink.getTableColumnAction(
                        [ {
                            label: '编辑',
                            sn: 'sys_role_update',
                            onClick: (text, record) => dispatch({ type: formType, payload: { record }})
                        }, {
                            label: '删除',
                            sn: 'sys_role_delete',
                            confirmText: Constants.DELETE_TEXT,
                            onConfirm: (text, record) => dispatch({ type: `${namespace}/del`, payload: { record }})
                        }, {
                            label: '添加权限',
                            sn: 'sys_role_updateMenu',
                            onClick: (text, record) => dispatch({ type: `${namespace}/menuPage`, payload: { record, url: 'selectMenu' }})
                        }, {
                            label: '查看权限',
                            sn: 'sys_role_queryMenu',
                            onClick: (text, record) => dispatch({ type: `${namespace}/menuPage`, payload: { record, url: 'queryMenu' }})
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