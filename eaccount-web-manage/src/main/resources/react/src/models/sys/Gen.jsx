import ModelUtils from '../../utils/ModelUtils.jsx'

export const namespace = 'sysGen';
export const baseUrl = 'sys/gen';

export default {
    ...ModelUtils(baseUrl, namespace)
};