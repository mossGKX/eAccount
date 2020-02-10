const { connect } = dva;
const { Input, Table } = antd;
const { BaseSearchForm, Plink } = require('../../../Widgets.jsx');
const { ToolbarGroup } = Plink;
const Constants = require('../../../Constants.jsx');
import { namespace } from '../../../../models/ec/User/PayInfo.jsx'

const formType = `${namespace}/openFormPage`;

function ecUserPayInfo (props) {
    const { searchData, dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{ padding: 20 }}>
            <BaseSearchForm
                loading={ loading }
                searchData={ searchData }
                handleSearch={ (values) => dispatch({ type: `${namespace}/fetch`, payload: { pagination: { page: 1 }, searchData: values }}) }>
                    <Input name='payWayCode' label='通道编号' />
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
                            title: '备注',
                            dataIndex: 'remark'
                        },
                        {
                            title: '支付通道名称',
                            dataIndex: 'payWayName'
                        },
                        {
                            title: '用户编号',
                            dataIndex: 'userNo'
                        },
                        {
                            title: '状态',
                            dataIndex: 'status'
                        },
                        {
                            title: '通道编号',
                            dataIndex: 'payWayCode'
                        },
                        {
                            title: '支付商户ID',
                            dataIndex: 'merchantId'
                        },
                        {
                            title: '支付appid',
                            dataIndex: 'appId'
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
})(ecUserPayInfo);