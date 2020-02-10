const { connect } = dva;
const { Layout, Spin } = antd;
const { Header, Sider, Content } = Layout;
import AppMenu from './AppMenu.jsx'
import AppHeader from './AppHeader.jsx'
import { namespace } from '../../models/App.jsx'

function App (props) {
    const { collapsed, children, loading } = props, overflow = { overflow: 'auto' };
    return (
        <Spin spinning={!!loading} tip="注销中...">
            <Layout style={{ height: '100vh' }}>
                <Sider
                    style={ collapsed ? {} : overflow }
                    trigger={null}
                    collapsible={true}
                    collapsed={collapsed}
                >
                    <AppMenu />
                </Sider>
                <Layout style={ overflow }>
                    <Header style={{ background: '#fff', padding: 0 }}>
                        <AppHeader />
                    </Header>
                    <Layout style={ overflow }>
                        <Content className='content'>{children}</Content>
                    </Layout>
                </Layout>
            </Layout>
        </Spin>
    );
}

export default connect(state => {
    const effects = state.loading.effects;
    return {
        ...state[namespace],
        loading: effects[`${namespace}/logout`]
    };
})(App);