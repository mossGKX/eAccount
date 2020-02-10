const { connect, router: { Link } } = dva;
const { Breadcrumb, Icon, Tooltip } = antd;
import { namespace } from '../../models/App.jsx'

function AppHeader (props) {
    const { collapsed, breadCrumbs, username, dispatch } = props;
    return (
        <div>
            <div style={{ float: 'left', paddingLeft: 20, width: '60%' }}>
                <div
                    style={{ float: 'left', paddingRight: 20, cursor: 'pointer' }}
                    onClick={() => dispatch({ type: `${namespace}/toggle` })}
                >
                    <Icon className="trigger" type={collapsed ? 'menu-unfold' : 'menu-fold'} />
                </div>
                <Breadcrumb>
                    {breadCrumbs}
                </Breadcrumb>
            </div>
            <div className='wzz-header'>
                <Link to="/personal/userInfo" style={{ color: '#404040', textDecoration: 'none' }}>
                    <Icon type='user' /><span style={{ fontSize: 14 }}>{username}</span>
                </Link>
                <Icon type='question-circle-o' style={{ cursor: 'pointer' }}/>
                <Tooltip placement='bottomRight' title='退出登录'>
                    <Icon type='logout' style={{ cursor: 'pointer' }} onClick={() => dispatch({type: 'app/logout'})}/>
                </Tooltip>

            </div>
        </div>
    );
}

export default connect(state => state[namespace])(AppHeader);