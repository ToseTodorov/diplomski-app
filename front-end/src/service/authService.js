import {userManagementApiUtils} from '../utils/apiUtils'

const Auth = {
    login: (request) => {
        return userManagementApiUtils.post('', request);
    }
};

export default Auth;