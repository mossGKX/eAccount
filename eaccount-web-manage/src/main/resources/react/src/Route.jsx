const { Route, IndexRoute } = dva.router;
import { getRoutePath } from './utils/RouteUtils.jsx'
import App from './components/app/App.jsx'
import Center from './components/app/Center.jsx'
import MenuSelectResources from './components/sys/menu/SelectResources.jsx'
import MenuQueryResources from './components/sys/menu/QueryResources.jsx'
import RoleSelectMenu from './components/sys/role/SelectMenu.jsx'
import RoleQueryMenu from './components/sys/role/QueryMenu.jsx'

const formName = '数据表单';

const routes = (
    <Route path='/' component={require('./components/Index.jsx')}>
        <Route path='login' component={require('./components/Login.jsx')} />
        <Route component={App}>
            <IndexRoute component={Center}/>
            <Route path='index' component={Center} />
            <Route path='sys'>
                <Route path="resources">
                    <IndexRoute component={require('./components/sys/resources/View.jsx')}/>
                    <Route path="form" name={formName} component={require('./components/sys/resources/Form.jsx')} />
                </Route>
                <Route path="menu">
                    <IndexRoute component={require('./components/sys/menu/View.jsx')}/>
                    <Route path="form" name={formName} component={require('./components/sys/menu/Form.jsx')} />
                    <Route path="selectResources" name='选择资源' component={MenuSelectResources} />
                    <Route path="queryResources" name='查看资源' component={MenuQueryResources} />
                </Route>
                <Route path="role">
                    <IndexRoute component={require('./components/sys/role/View.jsx')}/>
                    <Route path="form" name={formName} component={require('./components/sys/role/Form.jsx')} />
                    <Route path="selectMenu" name='添加权限' component={RoleSelectMenu} />
                    <Route path="queryMenu" name='查看权限' component={RoleQueryMenu} />
                </Route>
                <Route path="user">
                    <IndexRoute component={require('./components/sys/user/View.jsx')}/>
                    <Route path="form" name={formName} component={require('./components/sys/user/Form.jsx')} />
                    <Route path="password" name='修改密码' component={require('./components/sys/user/UpdatePassword.jsx')} />
                </Route>
                <Route path="config">
                    <IndexRoute component={require('./components/sys/config/View.jsx')}/>
                    <Route path="form" name={formName} component={require('./components/sys/config/Form.jsx')} />
                </Route>
                <Route path="schedule">
                    <IndexRoute component={require('./components/sys/schedule/View.jsx')}/>
                    <Route path="form" name={formName} component={require('./components/sys/schedule/Form.jsx')}  />
                </Route>
                <Route path="organization">
                    <IndexRoute component={require('./components/sys/organization/View.jsx')}/>
                    <Route path="form" name={formName} component={require('./components/sys/organization/Form.jsx')} />
                </Route>
                <Route path="gen">
                    <IndexRoute component={require('./components/sys/gen/View.jsx')}/>
                    <Route path="form" name={formName} component={require('./components/sys/gen/Form.jsx')} />
                </Route>
            </Route>
            <Route path='ec'>
                <Route path="user">
                    <IndexRoute component={require('./components/ec/user/View.jsx')}/>
                    <Route path="form" name={formName} component={require('./components/ec/user/Form.jsx')} />
                </Route>
                <Route path="account">
                    <IndexRoute component={require('./components/ec/account/View.jsx')}/>
                </Route>
		        <Route path="history">
                    <IndexRoute component={require('./components/ec/history/View.jsx')}/>
                </Route>
            </Route>
            <Route path='personal'>
                <Route path="updatePasswordPage" component={require('./components/personal/UpdatePassword.jsx')} />
                <Route path="userInfo" component={require('./components/personal/UpdateUserInfo.jsx')} />
            </Route>
            <Route path="invalid" component={require('./components/error/Invalid.jsx')} />
            <Route path="403" component={require('./components/error/403.jsx')} />
            <Route path="*" component={require('./components/error/404.jsx')} />
        </Route>
    </Route>
);

const breadCrumbRouteMap = new Map();
getRoutePath(routes, '', breadCrumbRouteMap);

module.exports = {
    routes: routes,
    breadCrumbMap: breadCrumbRouteMap
};