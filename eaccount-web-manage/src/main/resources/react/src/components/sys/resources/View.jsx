const { connect } = dva;
const { Input, Table } = antd;
const { BaseSearchForm, Plink } = require('../../Widgets.jsx');
const { ToolbarGroup } = Plink;
const Constants = require('../../Constants.jsx');
import { namespace } from '../../../models/sys/Resources.jsx'

const formType = `${namespace}/openFormPage`;

function SysModule (props) {
    const { searchData, dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{padding: 20}}>
            <BaseSearchForm
                loading= {loading}
                searchData={ searchData }
                handleSearch={ (values) => dispatch({ type: `${namespace}/fetch`, payload: { pagination: { page: 1 }, searchData: values }}) }>
                <Input name='groupName' label='模块' />
                <Input name='name' label='资源名称' />
            </BaseSearchForm>
            <div className='search-result-list'>
                <ToolbarGroup>
                    <Plink type='primary' sn="sys_res_save" onClick={ () => dispatch({ type: formType }) }>新增</Plink>
                </ToolbarGroup>
                <Table
                    dataSource={dataSource}
                    pagination={pagination}
                    loading={loading}
                    onChange={( pagination ) => { dispatch({ type: `${namespace}/paginationLoad`, payload: { pagination }}) }}
                    bordered
                    columns={ [ {
                        title: '模块',
                        dataIndex: 'groupName'
                    }, {
                        title: '资源名称',
                        dataIndex: 'name'
                    }, {
                        title: '资源地址',
                        dataIndex: 'url'
                    }, Plink.getTableColumnAction(
                        [ {
                            label: '编辑',
                            sn: 'sys_res_update',
                            onClick: (text, record) => dispatch({ type: formType, payload: { record }})
                        }, {
                            label: '删除',
                            sn: 'sys_res_delete',
                            confirmText: Constants.DELETE_TEXT,
                            onConfirm: (text, record) => dispatch({ type: `${namespace}/del`, payload: { record }})
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