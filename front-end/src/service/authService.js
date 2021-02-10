import {userManagementApiUtils} from '../utils/apiUtils'

const Auth = {
    login: (request) => {
        return userManagementApiUtils.post('/auth/login', request);
    }
};

export default Auth;