// https://github.com/graphhopper/directions-api-js-client/blob/master/src/GHUtil.js


export function clone(obj: any): any {
    let newObj: any = {};
    for (let prop in obj) {
        if (obj.hasOwnProperty(prop)) {
            newObj[prop] = obj[prop];
        }
    }
    return newObj;
}

export function decodePath(encoded: string, is3D: boolean = false): number[][] {
    let len = encoded.length;
    let index = 0;
    let array: number[][] = [];
    let lat = 0;
    let lng = 0;
    let ele = 0;

    while (index < len) {
        let b: number;
        let shift = 0;
        let result = 0;
        do {
            b = encoded.charCodeAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        } while (b >= 0x20);
        let deltaLat = ((result & 1) ? ~(result >> 1) : (result >> 1));
        lat += deltaLat;

        shift = 0;
        result = 0;
        do {
            b = encoded.charCodeAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        } while (b >= 0x20);
        let deltaLon = ((result & 1) ? ~(result >> 1) : (result >> 1));
        lng += deltaLon;

        if (is3D) {
            // elevation
            shift = 0;
            result = 0;
            do {
                b = encoded.charCodeAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            let deltaEle = ((result & 1) ? ~(result >> 1) : (result >> 1));
            ele += deltaEle;
            array.push([lng * 1e-5, lat * 1e-5, ele / 100]);
        } else
            array.push([lng * 1e-5, lat * 1e-5]);
    }
    // let end = new Date().getTime();
    // console.log("decoded " + len + " coordinates in " + ((end - start) / 1000) + "s");
    return array;
}

export function extractError(res: any, url: string): Error {
    let msg: any;

    if (res && res.data) {
        if (typeof res.data === 'object' && res.data !== null) {
            msg = res.data;
            if (msg.hints && msg.hints[0] && msg.hints[0].message)
                msg = msg.hints[0].message;
            else if (msg.message)
                msg = msg.message;
        } else {
            msg = String(res.data);
        }
    } else {
        msg = String(res);
    }

    return new Error(String(msg) + " - for url " + url);
}

export function isArray(value: any): boolean {
    let stringValue = Object.prototype.toString.call(value);
    return (stringValue.toLowerCase() === "[object array]");
}

export function isObject(value: any): boolean {
    let stringValue = Object.prototype.toString.call(value);
    return (stringValue.toLowerCase() === "[object object]");
}

export function isString(value: any): boolean {
    return (typeof value === 'string');
}