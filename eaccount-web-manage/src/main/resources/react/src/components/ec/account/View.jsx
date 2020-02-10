const { connect } = dva;
const { Input, Table, Tag, Select } = antd;
const { BaseSearchForm, Plink } = require('../../Widgets.jsx');
const { ToolbarGroup } = Plink;
const Constants = require('../../Constants.jsx');
import { namespace } from '../../../models/ec/Account.jsx'

const formType = `${namespace}/openFormPage`;

function ecAccount (props) {
    const { searchData, dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{ padding: 20 }}>
            <BaseSearchForm
                loading={ loading }
                searchData={ searchData }
                handleSearch={ (values) => dispatch({ type: `${namespace}/fetch`, payload: { pagination: { page: 1 }, searchData: values }}) }>
                <Select name="accountType" label="账户类型" placeholder="请选择账户类型" defaultValue="all">
                    <Option value="all">全部</Option>
                    <Option value="fund">资方账户</Option>
                    <Option value="baitiao">白条账户</Option>
                </Select>
                    <Input name='userNo' label='用户编号' />
                    <Input name='accountNo' label='账户编号' />
            </BaseSearchForm>
            <div className='search-result-list'>
                <Table
                    className="ant-table-wrapper-tag"
                    dataSource={dataSource}
                    pagination={pagination}
                    loading={loading}
                    onChange={( pagination ) => { dispatch({ type: `${namespace}/paginationLoad`, payload: { pagination }}) }}
                    bordered
                    columns={ [
                        {
                            title: '用户编号',
                            dataIndex: 'userNo'
                        },
                        {
                            title: '账户编号',
                            dataIndex: 'accountNo'
                        },
                        {
                            title: '账户类型',
                            dataIndex: 'accountType',
                            render: (text, record) => {
                                let type = '白条账户';
                                if (text == 'fund') {
                                    type = '资金账户'
                                }
                                return <span>{type}</span>
                            }
                        },
                        {
                            title: '总支出',
                            dataIndex: 'totalExpend'
                        },
                        {
                            title: '今日支出',
                            dataIndex: 'todayExpend'
                        },
                        {
                            title: '可结算金额',
                            dataIndex: 'settAmount'
                        },
                        {
                            title: '保证金',
                            dataIndex: 'securityMoney'
                        },
                        {
                            title: '总收益',
                            dataIndex: 'totalIncome'
                        },
                        {
                            title: '今日收益',
                            dataIndex: 'todayIncome'
                        },
                        {
                            title: '授信额度',
                            dataIndex: 'creditLine'
                        },
                        {
                            title: '状态',
                            dataIndex: 'status',
                            render: (text, record) =>
                                <span>{ text == 0 ? <Tag color="green">正常</Tag> : <Tag color="red">禁用</Tag> }</span>
                        },
                        {
                            title: '不可用余额',
                            dataIndex: 'unbalance'
                        },
                        {
                            title: '账户余额',
                            dataIndex: 'balance'
                        }
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
})(ecAccount);