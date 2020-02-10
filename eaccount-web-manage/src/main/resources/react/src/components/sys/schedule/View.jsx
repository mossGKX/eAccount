const { connect } = dva;
const { Input, Table, Tag, Popconfirm } = antd;
const { BaseSearchForm, Plink } = require('../../Widgets.jsx');
const { ToolbarGroup } = Plink;
const Constants = require('../../Constants.jsx');
import { namespace } from '../../../models/sys/Schedule.jsx'

const formType = `${namespace}/openFormPage`;

function SysSchedule (props) {
    const { searchData, dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{padding: 20}}>
            <BaseSearchForm
                loading= {loading}
                searchData={ searchData }
                handleSearch={ (values) => dispatch({ type: `${namespace}/fetch`, payload: { pagination: { page: 1 }, searchData: values }}) }>
                <Input name='taskCode' label='任务码' />
                <Input name='taskName' label='任务名称' />
            </BaseSearchForm>
            <div className='search-result-list'>
                <ToolbarGroup>
                    <Plink type='primary' sn='sys_schedule_save' onClick={ () => dispatch({ type: formType }) }>新增</Plink>
                </ToolbarGroup>
                <Table
                    className="ant-table-wrapper-tag"
                    dataSource={dataSource}
                    pagination={pagination}
                    loading={loading}
                    onChange={( pagination ) => { dispatch({ type: `${namespace}/paginationLoad`, payload: { pagination }}) }}
                    bordered
                    columns={ [ {
                        title: '任务码',
                        dataIndex: 'taskCode'
                    }, {
                        title: '任务名称',
                        dataIndex: 'taskName'
                    }, {
                        title: '定时配置',
                        dataIndex: 'taskConf'
                    }, {
                        title: '执行地址',
                        dataIndex: 'taskClass'
                    }, {
                        title: '指定IP',
                        dataIndex: 'taskServerIp'
                    }, {
                        title: '状态',
                        dataIndex: 'status',
                        render: (text, record) =>
                            Plink.getPopconfirm(<span>{ text === '1' ? <Tag color="green">启用</Tag> : <Tag color="orange">禁用</Tag> }</span>,
                                () => dispatch({ type: `${namespace}/postKey`, payload: { record: record, uri: 'updateStatus' }}),
                                'sys_user_update')
                    }, Plink.getTableColumnAction(
                        [ {
                            label: '编辑',
                            sn: 'sys_schedule_update',
                            onClick: (text, record) => dispatch({ type: formType, payload: { record: record }})
                        }, {
                            label: '删除',
                            sn: 'sys_schedule_delete',
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
    return {
        ...state[namespace],
        loading: effects[`${namespace}/fetch`] || effects[`${namespace}/del`] || effects[`${namespace}/start`] || effects[`${namespace}/stop`]
    }
})(SysSchedule);