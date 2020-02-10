const { connect } = dva;
const { Input, Table } = antd;
const { BaseSearchForm, Plink } = require('../../Widgets.jsx');
const { ToolbarGroup } = Plink;
const Constants = require('../../Constants.jsx');
import { namespace } from '../../../models/ec/History.jsx'

const formType = `${namespace}/openFormPage`;

function ecHistory (props) {
    const { searchData, dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{ padding: 20 }}>
            <BaseSearchForm
                loading= {loading}
                searchData={ searchData }
                handleSearch={ (values) => dispatch({ type: `${namespace}/fetch`, payload: { pagination: { page: 1 }, searchData: values }}) }>
                <Input name='name' label='用户账户' />
            </BaseSearchForm>
            <div className='search-result-list'>
                <ToolbarGroup>
                    <Plink type='primary' //sn='sn_save'
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
                            title: '账户编号',
                            dataIndex: 'accountNo'
                        },
                        {
                            title: '用户编号',
                            dataIndex: 'userNo'
                        },
                        {
                            title: '金额',
                            dataIndex: 'amount'
                        },
                        {
                            title: '风险预存期',
                            dataIndex: 'riskDay'
                        },
                        {
                            title: '修改时间',
                            dataIndex: 'editTime'
                        },
                        {
                            title: '是否允许结算',
                            dataIndex: 'isAllowSett'
                        },
                        {
                            title: '备注',
                            dataIndex: 'remark'
                        },
                        {
                            title: '业务类型',
                            dataIndex: 'trxType'
                        },
                        {
                            title: '请求号',
                            dataIndex: 'requestNo'
                        },
                        {
                            title: '银行流水号',
                            dataIndex: 'bankTrxNo'
                        },
                        {
                            title: '资金变动方向',
                            dataIndex: 'fundDirection'
                        },
                        {
                            title: '创建时间',
                            dataIndex: 'createTime'
                        },
                        {
                            title: '账户余额',
                            dataIndex: 'balance'
                        },
                        {
                            title: '是否完成结算',
                            dataIndex: 'isCompleteSett'
                        },
                        Plink.getTableColumnAction( [ {
                            label: '编辑',
                            //sn: 'sn_update',
                            onClick: (text, record) => dispatch({ type: formType, payload: { record }})
                        }, {
                            label: '删除',
                            //sn: 'sn_delete',
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
        loading: effects[`${namespace}/fetch`] || effects[`${namespace}/del`]
    }
})(ecHistory);