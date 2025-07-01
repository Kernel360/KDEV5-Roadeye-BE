import dateFormat from "dateformat";
import { useCallback } from "react";
import * as api from "~/lib/api";
import type { CarEmulatorState } from "~/stores/emulatorStore";
import type { CycleLogPayload, IgnitionOffPayload, IgnitionOnPayload } from "~/types/vehicle";

export function useMdtApi() {
    const baseUrl = import.meta.env.VITE_API_HUB_URL;

    const sendIgnitionOn = useCallback(async (car: CarEmulatorState) => {
        const now = new Date();
        const oTime = dateFormat(now, "yyyymmddHHMM");
        const payload: IgnitionOnPayload = {
            tid: "TID",
            mid: "MID",
            pv: "1",
            did: "DID",
            mdn: car.car.id,
            onTime: oTime,
            gcd: "A",
            lat: car.emulator.coord.current.lat,
            lon: car.emulator.coord.current.lng,
            ang: 0,
            spd: 0,
            sum: 0
        }
        const tuid = car.car.activeTransactionId;
        return await api.sendIgnitionOn(baseUrl, payload, tuid!);
    }, [baseUrl]);

    const sendIgnitionOff = useCallback(async (car: CarEmulatorState) => {
        if (!car.emulator.ignition.onTime) {
            throw new Error("On time is not set");
        }

        if (!car.emulator.ignition.offTime) {
            throw new Error("Off time is not set");
        }

        const payload: IgnitionOffPayload = {
            tid: "TID",
            mid: "MID",
            pv: "1",
            did: "DID",
            mdn: car.car.id,
            onTime: dateFormat(car.emulator.ignition.onTime, "yyyymmddHHMM"),
            offTime: dateFormat(car.emulator.ignition.offTime, "yyyymmddHHMM"),
            gcd: "A",
            lat: car.emulator.coord.current.lat,
            lon: car.emulator.coord.current.lng,
            ang: 0,
            spd: 0,
            sum: car.emulator.driving.mileageSum
        }
        const tuid = car.car.activeTransactionId;
        return await api.sendIgnitionOff(baseUrl, payload, tuid!);
    }, [baseUrl]);

    const sendCycleLog = useCallback(async (car: CarEmulatorState) => {
        const now = new Date();
        const oTime = dateFormat(now, "yyyymmddHHMM");
        const payload: CycleLogPayload = {
            tid: "TID",
            mid: "MID",
            pv: "1",
            did: "DID",
            mdn: car.car.id,
            oTime: oTime,
            cCnt: car.emulator.driveLogs.length,
            cList: car.emulator.driveLogs
        }
        const tuid = car.car.activeTransactionId;
        return await api.sendCycleLog(baseUrl, payload, tuid!);
    }, [baseUrl]);

    return {
        sendIgnitionOn,
        sendIgnitionOff,
        sendCycleLog
    }
}