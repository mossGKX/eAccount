const { connect, router } = dva;
const { IndexLink } = router;
const { Row, Col } = antd;

function Error403 (props) {
    return (
        <div className="error-page">
            <Row type="flex" justify="space-around" align="middle" style={{ height: 'calc(100vh - 90px)' }}>
                <Col span={6}>
                    <img style={{ height: 513, maxHeight: 'calc(100vh - 94px)' }} src="resources/image/403.png"/>
                </Col>
                <Col span={6}>
                    <p className="text-center error-state" style={{ fontSize: 36, lineHeight: '56px' }}>你没有访问权限</p>
                    <p className="text-center error-text">如有疑问，请联系管理员</p>
                    <p className="text-center"><IndexLink to="/" style={{ fontSize: 18 }}>返回首页</IndexLink></p>
                </Col>
            </Row>
        </div>
    );
}

module.exports = connect(state => state.app)(Error403);