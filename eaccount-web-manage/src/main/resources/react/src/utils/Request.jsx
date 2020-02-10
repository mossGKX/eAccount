const { browserHistory } = dva.router;
const { message: { error } } = antd;

function checkStatus(response, responseText) {

    const { status } = response;
    if (status >= 200 && status < 300) {
        return;
    }

    let errorMsg;
    let logout = false;
    if(status === 401) {// 未登录
        try {
            window.sys = window.sys || {};
            window.sys = JSON.parse(responseText).data;// crsf token重新赋值(Request.jsx)
        } catch (e) {
            console.debug(e.message);
        }
        // browserHistory.push('/login');
        logout = true;
    } else if(status === 403 && responseText.indexOf('CSRF') > 0) {// 无效的csrf token
        errorMsg = "页面数据已过期，请更新页面数据";
        browserHistory.push('/invalid');
    } else if(response.status === 403) {
        errorMsg = "权限不足";
    } else {
        errorMsg = "网络异常";
    }
    const error = new Error(errorMsg);
    error.logout = logout;
    throw error;
}

async function request(url, options) {

    options = options || {};
    options.credentials  = options.credentials || 'include';// Fetch 请求默认是不带 cookie 的，需要设置
    // 如果ctx不为空，跨域处理
    const response = await fetch((ctx || '') + url, options);
    let responseText = await response.text();

    checkStatus(response, responseText);

    try {
        responseText = JSON.parse(responseText);
    } catch (e) {
        console.debug(e);
    }
    if(responseText.code !== undefined && responseText.code !== 0) {
        if(responseText.code > 0) {
            error(responseText.msg);
        } else {
            const error = new Error(responseText.msg);
            error.imageCaptcha = options.imageCaptcha;// 用于判断是否刷新图片验证码
            throw error;
        }
    }
    return responseText;
}

export function get(url, options) {
    const opts = {
        ...options,
        method: 'GET'
    };
    return request(url, opts);
}

export function post(url, values, options) {
    const isForm = values && typeof values === 'object' && values.toString() === '[object FormData]';
    const opts = {
        ...options,
        method: 'POST',
        headers: {},
        body: isForm ? values : JSON.stringify(values || {})
    };
    !isForm ? opts.headers['Content-Type'] = 'application/json' : '';

    return request(url, addCrsf(opts));
}

export function del(url, options) {
    const opts = {
        ...options,
        method: 'DELETE'
    };
    return request(url, addCrsf(opts));
}

function addCrsf(options) {
    if(!(window.sys && window.sys.id)) {
        throw new Error('csrf id isEmpty');
    }
    return {
        ...options,
        headers: {
            ...options.headers,
            'X-CSRF-TOKEN': window.sys.id
        }
    };
}
