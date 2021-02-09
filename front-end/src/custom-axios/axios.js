import axios from 'axios';
import {API_DIPLOMSKI_URL, API_USER_MANAGEMENT_URL} from "../constants";

export const diplomskiApi = axios.create({
    baseURL: API_DIPLOMSKI_URL,
    headers: {
        'Access-Control-Allow-Origin': '*'
    },
});

export const userManagementApi = axios.create({
    baseURL: API_USER_MANAGEMENT_URL,
    headers: {
        'Access-Control-Allow-Origin': '*'
    },
});