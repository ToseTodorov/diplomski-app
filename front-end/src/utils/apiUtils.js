import {diplomskiApi, userManagementApi} from "../custom-axios/axios";
import {LOGGED_IN_USER, LOGGED_IN_ROLE} from "../constants"

export const userManagementApiUtils = {
    get: (url) => {
        return userManagementApi.get(url, {
            headers: {
                'user': user(),
                'role': role()
            }
        })
    },
    post: (url, body) => {
        const data = JSON.stringify(body);
        return userManagementApi.post(url, data, {
            headers: {
                'content-type': 'application/json',
                'user': user(),
                'role': role()
            }
        })
    }
};

export const diplomskiApiUtils = {
    get: (url) => {
        return diplomskiApi.get(url, {
            headers: {
                'user': user(),
                'role': role()
            }
        })
    },
    post: (url, body) => {
        const data = JSON.stringify(body);
        return diplomskiApi.post(url, data, {
            headers: {
                'content-type': 'application/json',
                'user': user(),
                'role': role()
            }
        })
    }
};

function user() {
    if(localStorage.getItem(LOGGED_IN_USER)) {
        return localStorage.getItem(LOGGED_IN_USER);
    }
    return '';
}

function role(){
    if(localStorage.getItem(LOGGED_IN_ROLE)) {
        return localStorage.getItem(LOGGED_IN_ROLE);
    }
    return '';
}