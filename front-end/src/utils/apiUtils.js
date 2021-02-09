import {diplomskiApi, userManagementApi} from "../custom-axios/axios";
import {MODULE} from "../constants"

const apiUtils = {
    get: (url, module) => {
        if(module === MODULE.DIPLOMSKI){
            return diplomskiApi.get(url);
        }
        return userManagementApi.get(url);
    },
    post: (url, body, module) => {
        const data = JSON.stringify(body);
        const contentType = {'content-type': 'application/json'}

        if(module === MODULE.DIPLOMSKI){
            return diplomskiApi.post(url, data, {headers: contentType});
        }
        return userManagementApi.post(url, data, {headers: contentType});
    }
};

export default apiUtils;