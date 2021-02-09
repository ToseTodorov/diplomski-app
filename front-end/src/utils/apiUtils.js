import {diplomskiApi, userManagementApi} from "../custom-axios/axios";
import {LOGGED_IN_USER} from "../constants"

export const userManagementApiUtils = {
    get: (url) => {
        return userManagementApi.get(url, {
            headers: {
                'user': user()
            }
        })
    },
    post: (url, body) => {
        const data = JSON.stringify(body);
        return userManagementApi.post(url, data, {
            headers: {
                'content-type': 'application/json',
                'user': user()
            }
        })
    }
};

export const diplomskiApiUtils = {
    get: (url) => {
        return diplomskiApi.get(url, {
            headers: {
                'user': user()
            }
        })
    },
    post: (url, body) => {
        const data = JSON.stringify(body);
        return diplomskiApi.post(url, data, {
            headers: {
                'content-type': 'application/json',
                'user': user()
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