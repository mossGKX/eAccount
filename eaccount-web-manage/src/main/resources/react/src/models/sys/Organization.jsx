import ModelUtils from '../../utils/ModelUtils.jsx'

export const namespace =  'sysOrganization';
export const baseUrl = 'sys/organization';

const model = ModelUtils(baseUrl, namespace);

export default {
    ...model,
    effects: {
        ...model.effects
    }
};