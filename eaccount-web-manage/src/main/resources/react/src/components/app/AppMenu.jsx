const { connect, router: { routerRedux } } = dva;
const { Menu } = antd;
import { namespace } from '../../models/App.jsx'

function AppMenu (props) {
    const { collapsed, menus, dispatch, pathNames, openKeys } = props;
    return (
        <div>
            <div
                className='logo logo-background'
                onClick={ () => dispatch(routerRedux.push({ pathname: '/index' })) }
            />
            <Menu
                theme="dark"
                mode='inline'
                inlineCollapsed={collapsed}
                selectedKeys={pathNames}
                defaultOpenKeys={openKeys || pathNames}
                onOpenChange={ (openKeys) => dispatch({ type: `${namespace}/onOpenMenuChange`, payload: { openKeys } }) }
            >
                {menus}
            </Menu>
        </div>
    );
}

export default connect(state => state[namespace])(AppMenu);