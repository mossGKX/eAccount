const { Select, Select: { Option } } = antd;
import * as Request from '../../utils/Request.jsx';

class RemoteSelect extends React.Component {
    state = {
        options: []
    };
    async componentDidMount() {
        const { optionKey, optionName } = this.props, options = [];
        const { data } = await Request.post(this.props.url);
        data.forEach((value) => {
            options.push({
                value: value[optionKey ? optionKey : 'key'],
                name: value[optionName ? optionName : 'name']
            })
        });
        this.setState({ options });
    }
    getOption() {
        const options = [];
        this.state.options.forEach((option) => {
            options.push(<Option value={option.value}>{option.name}</Option>)
        });
        return options;
    }
    render() {
        return (
            <Select
                showSearch
                allowClear
                optionFilterProp="children"
                filterOption={(input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
                {...this.props}
            >
                {this.getOption()}
            </Select>
        );
    }
}

module.exports = RemoteSelect;
