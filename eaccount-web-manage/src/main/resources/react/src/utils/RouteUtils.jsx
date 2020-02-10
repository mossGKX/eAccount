import { isArray, isEmpty } from './CheckUtils.jsx'

export function getRoutePath(routes, parentPath, breadCrumbMap) {
    if(!routes) {
        return;
    }
    routes = isArray(routes) ? routes : [routes];
    routes.forEach(v => {
        const { path, name, children } = v.props;
        const _path = (parentPath === '/' ? '' : parentPath ) + (path ? ('/' + (path === '/' ? '' : path)) : '');
        !isEmpty(name) ? breadCrumbMap.set(_path, name) : '';
        getRoutePath(children, _path, breadCrumbMap);
    });
}