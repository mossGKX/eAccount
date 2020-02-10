const { connect } = dva;
const { Input, Table } = antd;
const { BaseSearchForm, Plink } = require('${react.path}/Widgets.jsx');
const { ToolbarGroup } = Plink;
const Constants = require('${react.path}/Constants.jsx');
import { namespace } from '${react.path}/../models/${react.modelPath}.jsx'

const formType = `${_$}{namespace}/openFormPage`;

function ${react.namespace} (props) {
    const { searchData, dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{ padding: 20 }}>
            <#if (query?size>0) >
            <BaseSearchForm
                loading={ loading }
                searchData={ searchData }
                handleSearch={ (values) => dispatch({ type: `${_$}{namespace}/fetch`, payload: { pagination: { page: 1 }, searchData: values }}) }>
                <#list query as attr>
                    <Input name='${attr.javaName}' label='${attr.des}' />
                </#list>
            </BaseSearchForm>
            </#if>
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
                    onChange={( pagination ) => { dispatch({ type: `${_$}{namespace}/paginationLoad`, payload: { pagination }}) }}
                    bordered
                    columns={ [
                    <#list list as attr>
                        {
                            title: '${attr.des}',
                            dataIndex: '${attr.javaName}'
                        },
                    </#list>
                        Plink.getTableColumnAction( [ {
                            label: '编辑',
                            //sn: 'sn_update',
                            onClick: (text, record) => dispatch({ type: formType, payload: { record }})
                        }, {
                            label: '删除',
                            //sn: 'sn_delete',
                            confirmText: Constants.DELETE_TEXT,
                            onConfirm: (text, record) => dispatch({ type: `${_$}{namespace}/del`, payload: { record }})
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
        loading: effects[`${_$}{namespace}/fetch`] || effects[`${_$}{namespace}/del`]
    }
})(${react.namespace});