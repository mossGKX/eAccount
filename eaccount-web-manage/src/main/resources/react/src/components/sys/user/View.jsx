const { connect } = dva;
const { Input, Table, Tag } = antd;
const { BaseSearchForm, Plink } = require('../../Widgets.jsx');
const { ToolbarGroup } = Plink;
const Constants = require('../../Constants.jsx');
import { namespace } from '../../../models/sys/User.jsx'

const formType = `${namespace}/openFormPage`;

function SysModule (props) {
    const { searchData, dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{padding: 20}}>
            <BaseSearchForm
                loading= {loading}
                searchData={ searchData }
                handleSearch={ (values) => dispatch({ type: `${namespace}/fetch`, payload: { pagination: { page: 1 }, searchData: values }}) }>
                <Input name='name' label='用户姓名' />
            </BaseSearchForm>
            <div className='search-result-list'>
                <ToolbarGroup>
                    <Plink type='primary' sn="sys_user_save" onClick={ () => dispatch({ type: formType }) }>新增</Plink>
                </ToolbarGroup>
                <Table
                    className="ant-table-wrapper-tag"
                    dataSource={dataSource}
                    pagination={pagination}
                    loading={loading}
                    onChange={( pagination ) => { dispatch({ type: `${namespace}/paginationLoad`, payload: { pagination }}) }}
                    bordered
                    columns={ [ {
                        title: '登录名',
                        dataIndex: 'account.account'
                    }, {
                        title: '用户姓名',
                        dataIndex: 'name'
                    }, {
                        title: '固定电话',
                        dataIndex: 'telephone'
                    }, {
                        title: '联系手机',
                        dataIndex: 'mobilephone'
                    }, {
                        title: '联系邮箱',
                        dataIndex: 'email'
                    }, {
                        title: '角色',
                        dataIndex: 'account.role.name'
                    }, {
                        title: '性别',
                        dataIndex: 'gender',
                        render: (text, record) => <span>{ text === '1' ? '男' :  '女'  }</span>
                    }, {
                        title: '状态',
                        dataIndex: 'account.status',
                        render: (text, record) =>
                            Plink.getPopconfirm(<span>{ text === 0 ? <Tag color="green">正常</Tag> : <Tag color="red">禁用</Tag> }</span>,
                                () => dispatch({ type: `${namespace}/postKey`, payload: { record: record, uri: 'updateStatus' }}),
                                'sys_user_update', "你确定要反转(启用/禁用)该用户状态吗？")
                    }, {
                        title: '组织',
                        dataIndex: 'org.name'
                    }, Plink.getTableColumnAction(
                        [ {
                            label: '编辑',
                            sn: 'sys_user_update',
                            onClick: (text, record) => dispatch({ type: formType, payload: { record: record }})
                        }, {
                            label: '删除',
                            sn: 'sys_user_delete',
                            confirmText: Constants.DELETE_TEXT,
                            onConfirm: (text, record) => dispatch({ type: `${namespace}/del`, payload: { record }})
                        }, {
                            label: '编辑密码',
                            sn: 'sys_user_updatePwd',
                            onClick: (text, record) => dispatch({ type: `${namespace}/openEditPage`, payload: { editData: { record }, url: 'password' }})
                        }, {
                            label: '下线',
                            sn: 'sys_user_shotOff',
                            onClick: (text, record) => dispatch({ type: `${namespace}/shotOff`, payload: { record }})
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
        loading: effects[`${namespace}/fetch`] || effects[`${namespace}/del`] || effects[`${namespace}/updateStatus`]
    }
})(SysModule);