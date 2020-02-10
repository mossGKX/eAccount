const { connect } = dva;
const { Input, Table, Tag, Select  } = antd;
const { BaseSearchForm, Plink } = require('../../Widgets.jsx');
const { ToolbarGroup } = Plink;
const Constants = require('../../Constants.jsx');
const Option = Select.Option;
import { namespace } from '../../../models/ec/User.jsx'

const formType = `${namespace}/openFormPage`;

function ecUser (props) {
    const { searchData, dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{ padding: 20 }}>
            <BaseSearchForm
                loading={ loading }
                searchData={ searchData }
                handleSearch={ (values) => dispatch({ type: `${namespace}/fetch`, payload: { pagination: { page: 1 }, searchData: values }}) }>
                <Select name="userType" label="用户类型" placeholder="请选择用户类型" defaultValue="all">
                    <Option value="all">全部</Option>
                    <Option value="user">普通用户</Option>
                    <Option value="employer">资方</Option>
                    <Option value="merchant">商户</Option>
                </Select>
                    <Input name='loginName' label='登录名' />
                    <Input name='userName' label='用户姓名' />
                    <Input name='accountNo' label='账户编号' />
                    <Input name='userNo' label='用户编号' />
            </BaseSearchForm>
            <div className='search-result-list'>
                <ToolbarGroup>
                    <Plink type='primary' sn='ec_user_save'
                        onClick={ () => dispatch({ type: formType }) }>新增</Plink>
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
                            title: '登录名',
                            dataIndex: 'loginName'
                        },
                        {
                            title: '用户姓名',
                            dataIndex: 'userName'
                        },
                        {
                            title: '账户编号',
                            dataIndex: 'accountNo'
                        },
                        {
                            title: '用户编号',
                            dataIndex: 'userNo'
                        },
                        {
                            title: '状态',
                            dataIndex: 'status',
                            render: (text, record) =>
                                Plink.getPopconfirm(<span>{ text == 0 ? <Tag color="green">正常</Tag> : <Tag color="red">禁用</Tag> }</span>,
                                    () => dispatch({ type: `${namespace}/postKey`, payload: { record: record, uri: 'updateStatus' }}),
                                    'ec_user_update', "你确定要反转(启用/禁用)该用户状态吗？")
                        },
                        {
                            title: '用户类型',
                            dataIndex: 'userType',
                            render: (text, record) => {
                                let type = "普通用户";
                                if (text == "employer") {
                                    type = "资方";
                                }
                                if (text == "merchant") {
                                    type = "商户";
                                }
                                return (<span>{type}</span>);
                            }
                        },
                        Plink.getTableColumnAction( [ {
                            label: '编辑',
                            sn: 'ec_user_update',
                            onClick: (text, record) => dispatch({ type: formType, payload: { record }})
                        }])
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
        loading: effects[`${namespace}/fetch`] || effects[`${namespace}/del`]
    }
})(ecUser);