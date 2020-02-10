const { connect, router: { routerRedux } } = dva;
const { Icon, Button, Table } = antd;
import { namespace, baseUrl } from '../../../models/sys/Menu.jsx'
import { groupName, resourceName, resourceUrl } from './SelectResources.jsx'

function SelectResources (props) {
    const { edit: { dataSource, record }, loading, dispatch } = props;
    return (
        <div style={{padding: '30px 20px 20px 20px'}}>
            <div className='search-toolbar'>
                <Icon type='check' /><span style={{ paddingLeft: 2 }}>{`(${record ? record.label : ''})已选择的资源`}</span>
                <Button style={{ marginLeft: 8 }} onClick={() => dispatch(routerRedux.push({ pathname: baseUrl }))}>返回</Button>
            </div>
            <Table
                dataSource={dataSource}
                pagination={false}
                loading={loading}
                bordered
                columns={ [ groupName, resourceName, resourceUrl ] }
            />
        </div>
    )
}

export default connect(state => {
    return { ...state[namespace], loading: state.loading.effects[`${namespace}/resourcePage`] }
})(SelectResources);
