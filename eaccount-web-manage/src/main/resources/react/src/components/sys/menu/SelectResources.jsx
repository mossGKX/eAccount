const { connect, router: { routerRedux } } = dva;
const { Icon, Button, Table, Input, Form } = antd;
const { Plink } = require('../../Widgets.jsx');
import { namespace, baseUrl } from '../../../models/sys/Menu.jsx'
import { namespace as resourcesNamespace } from '../../../models/sys/Resources.jsx'

export const groupName = { title: '模块', dataIndex: 'groupName', width: '30%' };
export const resourceName = { title: '资源名称', dataIndex: 'name' };
export const resourceUrl = { title: '资源地址', dataIndex: 'url', width: '40%' };
export const action = { title: '操作', key: 'Action', width: 80 };

function handleSubmit(dispatch, { key }, dataSource) {
    const menu = { key };
    menu.resources = [];
    dataSource.forEach(value => {
        menu.resources.push({ key: value.key })
    });
    dispatch({ type: `${namespace}/save`, payload: { values: menu, url: 'updateResources' } })
}

function SelectResources (props) {
    const { edit: { dataSource, record }, loading, buttonLoading, dispatch } = props;
    return (
        <div>
            <div className='search-toolbar'>
                <Icon type='check' /><span style={{ paddingLeft: 2 }}>{`(${record ? record.label : ''})已选择的资源`}</span>
                <Button type='primary' disabled={buttonLoading} style={{ marginLeft: 8 }}
                    onClick={handleSubmit.bind(this, dispatch, record, dataSource)}>提交</Button>
                <Button onClick={() => dispatch(routerRedux.push({ pathname: baseUrl }))}>返回</Button>
            </div>
            <Table
                dataSource={dataSource}
                pagination={false}
                loading={loading}
                bordered
                columns={ [
                    groupName,
                    resourceName,
                    resourceUrl, {
                    ...action,
                    render: (text, record, index) => (
                        <Plink onClick={ () => dispatch({ type: `${namespace}/delResource`, payload: { index } }) }>删除</Plink>
                    )
                } ] }
            />
        </div>
    )
}

const SelectResourcesConnect = connect(state => {
    const effects = state.loading.effects;
    return {
        ...state[namespace],
        loading: effects[`${namespace}/resourcePage`],
        buttonLoading: effects[`${namespace}/save`]
    }
})(SelectResources);

const Resources = React.createClass( {
    componentDidMount() {
        const { form: { setFieldsValue }, searchData } = this.props;
        setFieldsValue(searchData || {});
    },
    onSearch() {
        const { dispatch, form: { getFieldsValue } } = this.props;
        const values = getFieldsValue();
        dispatch({
            type: `${resourcesNamespace}/fetch`,
            payload: { pagination: { page: 1 }, searchData: values }
        })
    },
    render() {
        const { form: { getFieldDecorator }, dataSource, pagination, loading, dispatch } = this.props;
        return (
            <div className='search-result-list'>
                <div className='search-toolbar'>
                    <Icon type='bars'/><span style={{ paddingLeft: 2 }}>资源列表</span>
                </div>
                <Table
                    dataSource={dataSource}
                    pagination={pagination}
                    loading={loading}
                    onChange={( pagination ) => { dispatch({ type: `${resourcesNamespace}/paginationLoad`, payload: { pagination }}) }}
                    bordered
                    columns={ [ {
                        ...groupName,
                        filterDropdown: (<div className='custom-filter-dropdown'>
                            {getFieldDecorator('groupName')(
                                <Input placeholder="输入查询条件" onPressEnter={this.onSearch} />
                            )}
                        </div>)
                    }, {
                        ...resourceName,
                        filterDropdown: (<div className='custom-filter-dropdown'>
                            {getFieldDecorator('name')(
                                <Input placeholder="输入查询条件" onPressEnter={this.onSearch} />
                            )}
                        </div>)
                    }, resourceUrl, {
                        ...action,
                        render: (text, record) => (
                            <Plink onClick={ () => dispatch({ type: `${namespace}/addResource`, payload: { record } }) }>添加</Plink>
                        )
                    } ] }
                />
            </div>
        )
    }
} );

const ResourcesConnect = connect(state => {
    return { ...state[resourcesNamespace], loading: state.loading.effects[`${resourcesNamespace}/fetch`] }
})(Form.create()(Resources));

export default function () {
    return (
        <div style={{padding: '30px 20px 20px 20px'}}>
            <SelectResourcesConnect/>
            <ResourcesConnect/>
        </div>
    )
};
