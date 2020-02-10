const { connect } = dva;
const { Input, Table, Popconfirm, Tag } = antd;
const { BaseSearchForm, Plink } = require('../../Widgets.jsx');
const { ToolbarGroup } = Plink;
const Constants = require('../../Constants.jsx');
import { namespace } from '../../../models/sys/Config.jsx'

const formType = `${namespace}/openFormPage`;

function sysConfig (props) {
    const { searchData, dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{ padding: 20 }}>
            <BaseSearchForm
                loading={ loading }
                searchData={ searchData }
                handleSearch={ (values) => dispatch({ type: `${namespace}/fetch`, payload: { pagination: { page: 1 }, searchData: values }}) }>
                    <Input name='groupName' label='参数组' />
                    <Input name='name' label='参数名称' />
            </BaseSearchForm>
            <div className='search-result-list'>
                <ToolbarGroup>
                    <Plink type='primary' sn='sys_config_save' onClick={ () => dispatch({ type: formType }) }>新增</Plink>
                </ToolbarGroup>
                <Table
                    className="ant-table-wrapper-tag"
                    dataSource={dataSource}
                    pagination={pagination}
                    loading={loading}
                    onChange={( pagination ) => { dispatch({ type: `${namespace}/paginationLoad`, payload: { pagination }}) }}
                    bordered
                    columns={ [
                        {
                            title: '参数组',
                            dataIndex: 'groupName'
                        },
                        {
                            title: '参数名称',
                            dataIndex: 'name'
                        },
                        {
                            title: '参数值',
                            dataIndex: 'value'
                        },
                        {
                            title: '描述',
                            dataIndex: 'des'
                        },
                        {
                            title: '状态',
                            dataIndex: 'status',
                            render: (text, record) =>
                                Plink.getPopconfirm(<span>{ text === '0' ? <Tag color="green">正常</Tag> : <Tag color="red">禁用</Tag> }</span>,
                                    () => dispatch({ type: `${namespace}/postKey`, payload: { record: record, uri: 'updateStatus' }}),
                                    'sys_config_update')
                        },
                        Plink.getTableColumnAction( [ {
                            label: '编辑',
                            sn: 'sys_config_update',
                            onClick: (text, record) => dispatch({ type: formType, payload: { record }})
                        }, {
                            label: '删除',
                            sn: 'sys_config_delete',
                            confirmText: Constants.DELETE_TEXT,
                            onConfirm: (text, record) => dispatch({ type: `${namespace}/del`, payload: { record }})
                        } ])
                    ] }
                    />
            </div>
        </div>
    );
}

module.exports = connect(state => {
    const effects = state.loading.effects;
    return {
        ...state[namespace],
        loading: effects[`${namespace}/fetch`] || effects[`${namespace}/del`] || effects[`${namespace}/updateStatus`]
    }
})(sysConfig);