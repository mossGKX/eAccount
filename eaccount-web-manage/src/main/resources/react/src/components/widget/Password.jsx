const { Icon, Input } = antd;

module.exports = React.createClass( {
    getInitialState() {
        return {
            show: false
        };
    },
    toggle() {
        this.setState( {
            show: !this.state.show
        } );
    },
    render() {
        return (
            <Input type={this.state.show ? 'text' : 'password'}
                   suffix={<Icon type={this.state.show ? 'eye' : 'eye-o'}
                                 style={{fontSize: this.props.fontSize, cursor: 'pointer'}}
                                 onClick={this.toggle} />}
                   {...this.props}
            />
        );
    }
});