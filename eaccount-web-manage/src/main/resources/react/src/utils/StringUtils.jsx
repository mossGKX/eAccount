export function generateUUID() {
    const pattern = 'xxxxxxxx-xxxx-4xxx-Rxxx-xMxxxxxxxxxx'.split(''),
        hex = '0123456789abcdef'.split(''),
        length = pattern.length,
        parts = [];
    for (let r, c, i = 0; i < length; ++i) {
        c = pattern[i];
        if (c !== '-' && c !== '4') {
            r = Math.random() * 16;
            r = (c === 'R') ? (r & 3 | 8) : (r | ((c === 'M') ? 1 : 0));
            c = hex[r]; // above floors r so always 0-15
        }
        parts[i] = c;
    }
    return parts.join('');
}

export function getUriPath(path) {
    const list = [];
    if(!path)
        return list;
    list.push('/');
    path.split('/').forEach((v, i) => {
        const str = i === 0 ? '' : '/';
        v && v.length > 0 ? list.push(list[list.length - 1] + str + v) : '';
    });
    return list;
}

export function rsaEncrypt(text) {
    const rsaKey = new RSAKey(), rsa = window.sys.rsa;
    rsaKey.setPublic(b64tohex(rsa.modulus), b64tohex(rsa.exponent));
    return hex2b64(rsaKey.encrypt(text));
}