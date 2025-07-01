import dateFormat from "dateformat";
import { useCallback } from "react";
import * as api from "~/lib/api";
import type { CarEmulatorState } from "~/stores/emulatorStore";
import type { CycleLogPayload, IgnitionOffPayload, IgnitionOnPayload } from "~/types/vehicle";

const baseUrl = import.meta.env.VITE_API_HUB_URL;

export function useMdtApi() {
    const sendIgnitionOn = useCallback(async (car: CarEmulatorState, tuid: string) => {
        const onTime = new Date();
        const payload: IgnitionOnPayload = {
            tid: "TID",
            mid: "MID",
            pv: 1,
            did: "DID",
            mdn: car.car.id,
            onTime: dateFormat(onTime, "yyyymmddHHMMss"),
            gcd: "A",
            lat: car.emulator.coord.current.lat,
            lon: car.emulator.coord.current.lng,
            ang: 0,
            spd: 0,
            sum: 0
        }
        return await api.sendIgnitionOn(baseUrl, payload, tuid)
            .then((res) => ({ res, onTime, tuid }));
    }, []);

    const sendIgnitionOff = useCallback(async (car: CarEmulatorState) => {
        if (!car.emulator.ignition.onTime) {
            throw new Error("On time is not set");
        }

        const offTime = new Date();
        const payload: IgnitionOffPayload = {
            tid: "TID",
            mid: "MID",
            pv: 1,
            did: "DID",
            mdn: car.car.id,
            onTime: dateFormat(car.emulator.ignition.onTime, "yyyymmddHHMMss"),
            offTime: dateFormat(offTime, "yyyymmddHHMMss"),
            gcd: "A",
            lat: car.emulator.coord.current.lat,
            lon: car.emulator.coord.current.lng,
            ang: 0,
            spd: 0,
            sum: car.emulator.driving.mileageSum
        }
        const tuid = car.car.activeTransactionId;

        return await api.sendIgnitionOff(baseUrl, payload, tuid!)
            .then((res) => ({ offTime, res }));
    }, []);

    const sendCycleLog = useCallback(async (car: CarEmulatorState) => {
        const oTime = new Date();
        const payload: CycleLogPayload = {
            tid: "TID",
            mid: "MID",
            pv: 1,
            did: "DID",
            mdn: car.car.id,
            oTime: dateFormat(oTime, "yyyymmddHHMM"),
            cCnt: car.emulator.driveLogs.length,
            cList: car.emulator.driveLogs
        }
        const tuid = car.car.activeTransactionId;
        return await api.sendCycleLog(baseUrl, payload, tuid!)
            .then((res) => ({ res }));
    }, []);

    return {
        sendIgnitionOn,
        sendIgnitionOff,
        sendCycleLog
    }
}