const { connect, router: { IndexLink } } = dva;
const { Row, Col } = antd;

function Error404 (props) {
    return (
        <div className="error-page">
            <Row type="flex" justify="space-around" align="middle" style={{ height: 'calc(100vh - 90px)' }}>
                <Col span={6}>
                    <img style={{ height: 413, maxHeight: 'calc(100vh - 94px)' }} src="resources/image/404.png"/>
                </Col>
                <Col span={6}>
                    <p className="text-center error-state">404</p>
                    <p className="text-center error-text">你要找的页面不存在</p>
                    <p className="text-center"><IndexLink to="/" style={{ fontSize: 18 }}>返回首页</IndexLink></p>
                </Col>
            </Row>
        </div>
    );
}

module.exports = connect(state => state.app)(Error404);