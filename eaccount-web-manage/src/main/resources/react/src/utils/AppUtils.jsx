const { router: { Link } } = dva;
const { Icon, Menu: { SubMenu, Item }, Breadcrumb } = antd;
import { isEmpty } from './CheckUtils.jsx'
import { generateUUID } from './StringUtils.jsx'

const breadCrumbRouteMap = require('../Route.jsx').breadCrumbMap;// 路由的面包屑数据
const breadCrumbMenuMap = new Map();// 菜单的面包屑数据

// 生成菜单（递归）
export function genMenu(datas, level) {
    const menus = [];
    datas.forEach((data, index) => {
        const hasPath = isEmpty(data.path), key = hasPath ? data.key : data.path;
        if (data.children && data.children.length > 0) {// 非根节点
            !hasPath ? breadCrumbMenuMap.set(data.path, { label: data.label, notClick: true }) : '';
            menus.push(<SubMenu key={key} title={<span><Icon type={data.icon}/><span>{data.label}</span></span>}>
                {genMenu(data.children, level + 1)}
            </SubMenu>);
        } else {
            !hasPath ? breadCrumbMenuMap.set(data.path, { label: data.label, notClick: false }) : '';
            menus.push(<Item key={key}>
                <Link to={data.path} style={{ paddingTop: 2 }}>
                    {data.icon ? <Icon type={data.icon}/> : ""}<span>{data.label}</span>
                </Link>
            </Item>);
        }
    });
    return menus;
}

// 获取面包屑items，数据由菜单和路由提供
export function getbreadCrumb(pathNames) {
    const { Item } = Breadcrumb, items = [];
    pathNames.forEach(value => {
        const { label, notClick } = breadCrumbMenuMap.get(value) || { label: breadCrumbRouteMap.get(value) };
        if(!label)
            return;
        items.push(<Item key={generateUUID()}>{ notClick ? label : <Link to={value}>{label}</Link> }</Item>);
    });
    return items;
}