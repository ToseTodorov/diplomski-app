import {diplomskiApi, userManagementApi} from "../custom-axios/axios";
import {LOGGED_IN_USER} from "../constants"

export const userManagementApiUtils = {
    get: (url) => {
        return userManagementApi.get(url, {
            headers: {
                'user': user().userId,
                'role': user().roleName
            }
        })
    },
    post: (url, body) => {
        const data = JSON.stringify(body);
        return userManagementApi.post(url, data, {
            headers: {
                'content-type': 'application/json',
                'user': user().userId,
                'role': user().roleName
            }
        })
    }
};

export const diplomskiApiUtils = {
    get: (url) => {
        return diplomskiApi.get(url, {
            headers: {
                'user': user().userId,
                'role': user().roleName
            }
        })
    },
    post: (url, body) => {
        const data = JSON.stringify(body);
        return diplomskiApi.post(url, data, {
            headers: {
                'content-type': 'application/json',
                'user': user().userId,
                'role': user().roleName
            }
        })
    }
};

function user() {
    if(localStorage.getItem(LOGGED_IN_USER)) {
        return JSON.parse(localStorage.getItem(LOGGED_IN_USER));
    }
    return {
        userId: '',
        roleName: '',
        username: ''
    };
}