const { connect, router: { routerRedux } } = dva;
const { Icon, Button, Table } = antd;
import { namespace, baseUrl } from '../../../models/sys/Role.jsx'
import { columns } from './SelectMenu.jsx'

function SelectMenu (props) {
    const { edit: { dataSource, record }, loading, dispatch } = props;
    return (
        <div style={{padding: '10px 20px 20px 20px'}}>
            <div className='search-toolbar'>
                <Icon type="bars" /><span style={{ paddingLeft: 2 }}>{`(${record ? record.name : ''})权限列表`}</span>
                <Button style={{ marginLeft: 8 }} onClick={() => dispatch(routerRedux.push({ pathname: baseUrl }))}>返回</Button>
            </div>
            <Table
                dataSource={dataSource}
                pagination={false}
                loading={loading}
                bordered
                columns={ columns }
            />
        </div>
    )
}

export default connect(state => {
    return { ...state[namespace], loading: state.loading.effects[`${namespace}/menuPage`] }
})(SelectMenu);
