/* eslint-disable @typescript-eslint/no-explicit-any */
const baseUrl = 'http://localhost:8080/api';

const call = (url: string, method: string, data: any, json: boolean, absolute: boolean) => {
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
    return json
        ? fetch((absolute ? '' : baseUrl) + url, options).then(res => res.json())
        : fetch((absolute ? '' : baseUrl) + url, options)
}

export const get = (url: string, json: boolean = true, absolute: boolean = false) => {
    return call(url, 'GET', {}, json, absolute);
}

export const post = (url: string, data: any, json: boolean = true, absolute: boolean = false) => {
    return call(url, 'POST', data, json, absolute);
}

export const put = (url: string, data: any, json: boolean = true, absolute: boolean = false) => {
    return call(url, 'PUT', data, json, absolute);
}

export const del = (url: string, data: any, json: boolean = true, absolute: boolean = false) => {
    return call(url, 'DELETE', data, json, absolute);
}