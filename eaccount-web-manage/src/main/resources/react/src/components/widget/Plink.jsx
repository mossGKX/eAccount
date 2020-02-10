const { Popconfirm, Menu, Dropdown, Icon, Button, Modal } = antd;
const { Item } = Menu;
const confirm = Modal.confirm;
import { isArray } from '../../utils/CheckUtils.jsx';

const Plink = React.createClass({
    render: function () {
        if(this.props.type) {
            return <Button {...this.props}>{this.props.children}</Button>;
        }
        return <a href="javascript:void(0);" {...this.props}>{this.props.children}</a>;
    }
});
Plink.Group = React.createClass({
    render : function() {
        const pls = [], dropdowns = [];
        let { children } = this.props;
        children = isArray(children) ? children : [ children ];
        children.forEach((v, i) => {
            if(i < 2) {
                pls.push(v);
                i + 1 !== children.length ? pls.push(<span className="ant-divider"/>) : '';
            } else {
                dropdowns.push(<Item key={i}>{v.props.children}</Item>)
            }
        });
        if(dropdowns.length > 0) {
            pls.push(
                <Dropdown overlay={<Menu onClick={(e) => {children[e.key].props.onClick()}}>{dropdowns}</Menu>}>
                    <Plink className='ant-dropdown-link'>
                        更多 <Icon type='down' />
                    </Plink>
                </Dropdown>
            );
        }
        return (
            <span>{ pls }</span>
        );
    }
});
Plink.ToolbarGroup = React.createClass({
    render : function() {
        const toolbars = [];
        let { children } = this.props;
        children = isArray(children) ? children : [ children ];
        children.forEach(v => {
            if(v.props.sn && !hasPermission(v.props.sn))
                return;
            toolbars.push(v);
        });
        return (
            <div className={ toolbars.length > 0 ? 'search-toolbar' : '' }>{ toolbars }</div>
        );
    }
});

Plink.getTableColumnAction = (plinks) => {
    let length = 0, width = 0;
    plinks.forEach((v) => {
        if(v.sn && !hasPermission(v.sn))
            return;
        length++;
        length < 3 ? width += 26 + 12 * v.label.length : (length === 3 ? width += 50 : '');
    });
    return {
        title: '操作',
        key: 'TableColumnAction',
        width: width,
        className: length > 0 ? '' : 'none',
        render: (text, record) => (
            <Plink.Group>
                {getPlinks(plinks, text, record)}
            </Plink.Group>
        )
    }
};
const getPlinks = (plinks, text, record) => {
    const pls = [];
    plinks.forEach((v, i) => {
        let component;
        if(v.confirmText) {
            if(i >= 2) {
                component = <Plink {...v} onClick={() => {
                    confirm({
                        title: v.confirmText,
                        okText: '是',
                        cancelText: '否',
                        onOk() {
                            v.onConfirm(text, record);
                        },
                        onCancel() {},
                    });
                }}>{v.label}</Plink>
            } else {
                component = <Popconfirm placement="topRight" title={v.confirmText}
                                        onConfirm={() => v.onConfirm(text, record)}
                                        okText="是" cancelText="否">
                    <Plink {...v}>{v.label}</Plink>
                </Popconfirm>
            }
        } else {
            component = <Plink {...v} onClick={() => v.onClick(text, record)}>{v.label}</Plink>;
        }
        !v.sn || hasPermission(v.sn) ? pls.push(component) : '';
    });
    return pls;
};

const hasPermission = (sn) => {
    const { sns } = window.sys || {};
    return sns && sns.has(sn);
};
Plink.hasSn = hasPermission;

// 如果没有权限不现实确认操作
Plink.getPopconfirm = (component, onConfirm, sn, title) => {
    if(hasPermission(sn)) {
        return <Popconfirm placement="topRight" title={title || "你确定要反转该用户状态吗？"} onConfirm={onConfirm}>
            {component}
        </Popconfirm>
    } else {
        return component;
    }
};

module.exports = Plink;