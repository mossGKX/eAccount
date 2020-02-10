const { connect } = dva;
const { Table } = antd;
const { Plink, Plink: { ToolbarGroup } } = require('../../Widgets.jsx');
const Constants = require('../../Constants.jsx');
import { namespace } from '../../../models/sys/Gen.jsx'

const formType = `${namespace}/openFormPage`;

function SysModule (props) {
    const { dataSource, pagination, loading, dispatch } = props;
    return (
        <div style={{padding: 20}}>
            <div className='search-result-list'>
                <ToolbarGroup>
                    <Plink type='primary' sn="sys_gen" onClick={ () => dispatch({ type: formType }) }>新增</Plink>
                </ToolbarGroup>
                <Table
                    dataSource={dataSource}
                    pagination={pagination}
                    loading={loading}
                    onChange={( pagination ) => { dispatch({ type: `${namespace}/paginationLoad`, payload: { pagination }}) }}
                    bordered
                    columns={ [ {
                        title: '包名',
                        dataIndex: 'packageName'
                    }, {
                        title: '实体名',
                        dataIndex: 'entityName'
                    }, {
                        title: 'uri',
                        dataIndex: 'uri'
                    }, {
                        title: '数据库表',
                        dataIndex: 'dataTable'
                    }, Plink.getTableColumnAction(
                        [ {
                            label: '代码生成',
                            sn: 'sys_gen',
                            onClick: (text, record) => dispatch({ type: formType, payload: { record }})
                        }, {
                            label: '删除',
                            sn: 'sys_gen',
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