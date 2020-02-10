const { TreeSelect } = antd;
import * as Request from '../../utils/Request.jsx';

class RemoteSelect extends React.Component {
    state = {
        treeNodes: []
    };
    async componentDidMount() {
        const { optionKey, optionName } = this.props;
        const { data } = await Request.post(this.props.url);
        this.setState({ treeNodes: this.handleData(data, optionKey ? optionKey : 'key', optionName ? optionName : 'name') });
    }
    handleData(datas, optionKey, optionName) {
        const treeNodes = [], me = this;
        if(!datas)
            return treeNodes;
        datas.forEach((value) => {
            treeNodes.push({
                value: value[optionKey],
                label: value[optionName],
                children: me.handleData(value.children, optionKey, optionName)
            });
        });
        return treeNodes;
    }
    render() {
        return (
            <TreeSelect
                allowClear
                {...this.props}
                treeData={this.state.treeNodes}
            />
        );
    }
}

module.exports = RemoteSelect;
