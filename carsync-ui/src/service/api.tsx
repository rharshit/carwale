/* eslint-disable @typescript-eslint/no-explicit-any */
const baseUrl = 'http://localhost:8080/api';

const call = (url: string, method: string, data: any) => {
    const options: RequestInit = {
        method: method,
        headers: new Headers({
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            "Access-Control-Allow-Origin": "*", // Required for CORS support to work
        }),
    };
    if (method != 'GET') {
        options.body = JSON.stringify(data);
    }
    return fetch(baseUrl + url, options);
}

export const get = (url: string) => {
    return call(url, 'GET', {});
}

export const post = (url: string, data: any) => {
    return call(url, 'POST', data);
}

export const put = (url: string, data: any) => {
    return call(url, 'PUT', data);
}

export const del = (url: string, data: any) => {
    return call(url, 'DELETE', data);
}