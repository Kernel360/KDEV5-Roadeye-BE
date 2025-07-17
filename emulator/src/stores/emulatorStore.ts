import { create } from 'zustand'
import { immer } from 'zustand/middleware/immer'
import { emulateCarPath } from '~/lib/emulator'
import type { DriveData } from '~/types/vehicle'
import type { Car } from './carStore'

export type GpsCoord = {
    lat: number
    lon: number
}

export type CarEmulatorState = {
    car: Car
    emulator: {
        ignition: {
            state: '시동OFF' | '시동ON',
            onTime: Date | null,
            offTime: Date | null
        }
        driving: {
            state: '주행' | '정지',
            mileageSum: number
        },
        coord: {
            start: GpsCoord | null,
            end: GpsCoord | null,
            current: GpsCoord
        }
        pathRoute: GpsCoord[],
        timer: ReturnType<typeof setInterval> | null,
        driveLogs: DriveData[]
    }
}

export type EmulatorState = {
    mapCenter: GpsCoord
    selectedCar: CarEmulatorState | null
    emulatorInstance: Map<Car['id'], Awaited<ReturnType<typeof emulateCarPath>>>

    setSelectedCar: (car: Car | null) => void
    setStartPoint: (point: GpsCoord) => void
    setCurrentPoint: (point: GpsCoord | null) => void
    setEndPoint: (point: GpsCoord) => void
    setMapCenter: (point: GpsCoord) => void
    setCarPathRoute: (route: GpsCoord[]) => void
    setCarEmulatorInstance: (instance: Awaited<ReturnType<typeof emulateCarPath>>) => void
    centerOnSelectedCar: () => void
    addDriveLog: (log: DriveData) => void
    clearDriveLogs: () => void
    turnOnIgnition: (onTime: Date, tuid: string) => void
    turnOffIgnition: (offTime: Date) => void
    getEmulatorInstance: (car: Car) => Awaited<ReturnType<typeof emulateCarPath>> | null
    startDriving: (timer: ReturnType<typeof setInterval>) => void
    stopDriving: () => void
    getPathRoute: () => GpsCoord[]
    getTimer: () => ReturnType<typeof setInterval> | null
    getSelectedCar: () => CarEmulatorState | null
}

export const useEmulatorStore = create<EmulatorState>()(
    immer(
        (set, get) => ({
            mapCenter: {
                lat: 37.499225,
                lon: 127.031477
            },
            selectedCar: null,
            emulatorInstance: new Map<Car['id'], Awaited<ReturnType<typeof emulateCarPath>>>(),

            setStartPoint: (point: GpsCoord) => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.coord.start = point
                }
            }),
            setEndPoint: (point: GpsCoord) => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.coord.end = point
                }
            }),
            setMapCenter: (point: GpsCoord) => set((state) => {
                state.mapCenter = point
            }),
            setCurrentPoint: (point: GpsCoord | null) => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.coord.current = point!
                }
            }),
            setSelectedCar: (car: Car | null) => set((state) => {
                if (car) {
                    state.selectedCar = {
                        car: car,
                        emulator: {
                            ignition: {
                                state: car.ignitionStatus === 'ON' ? '시동ON' : '시동OFF',
                                onTime: car.ignitionStatus === 'ON' ? new Date(car.ignitionOnAt) : null,
                                offTime: null
                            },
                            driving: {
                                state: car.activeTransactionId ? '주행' : '정지',
                                mileageSum: 0
                            },
                            coord: {
                                start: {
                                    lat: car.latitude,
                                    lon: car.longitude
                                },
                                end: null,
                                current: {
                                    lat: car.latitude,
                                    lon: car.longitude
                                }
                            },
                            pathRoute: [],
                            driveLogs: [],
                            timer: null
                        }
                    }
                } else {
                    state.selectedCar = null
                }
            }),
            setCarPathRoute: (route: GpsCoord[]) => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.pathRoute = route
                }
            }),
            setCarEmulatorInstance: (instance) => set((state) => {
                if (state.selectedCar) {
                    state.emulatorInstance.set(state.selectedCar.car.id, instance)
                }
            }),
            centerOnSelectedCar: () => set((state) => {
                if (state.selectedCar) {
                    state.mapCenter = {
                        lat: state.selectedCar.emulator.coord.current.lat,
                        lon: state.selectedCar.emulator.coord.current.lon
                    }
                }
            }),
            addDriveLog: (log: DriveData) => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.driveLogs.push(log)
                    state.selectedCar.emulator.coord.current = {
                        lat: log.lat,
                        lon: log.lon
                    }
                    console.log("addDriveLog", log)
                    console.log("currentPoint", state.selectedCar.emulator.coord.current)
                }
            }),
            clearDriveLogs: () => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.driveLogs.length = 0;
                }
            }),
            turnOnIgnition: (onTime: Date, tuid: string) => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.ignition.state = '시동ON'
                    state.selectedCar.emulator.ignition.onTime = onTime
                    state.selectedCar.car.activeTransactionId = tuid
                }
            }),
            turnOffIgnition: (offTime: Date) => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.ignition.state = '시동OFF'
                    state.selectedCar.emulator.ignition.offTime = offTime
                }
            }),
            getEmulatorInstance: (car: Car) => get().emulatorInstance.get(car.id) || null,
            startDriving: (timer: ReturnType<typeof setInterval>) => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.timer = timer
                    state.selectedCar.emulator.driving.state = '주행'
                }
            }),
            stopDriving: () => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.timer = null
                    state.selectedCar.emulator.driving.state = '정지'
                }
            }),
            getPathRoute: () => get().selectedCar?.emulator.pathRoute || [],
            getTimer: () => get().selectedCar?.emulator.timer || null,
            getSelectedCar: () => get().selectedCar || null,
        })
    )
)

export const useSelectedEmulatorCar = () => useEmulatorStore((state) => state.selectedCar?.car);
export const useSelectedEmulator = () => useEmulatorStore((state) => state.selectedCar?.emulator);
export const useSelectedEmulatorIgnition = () => useEmulatorStore((state) => state.selectedCar?.emulator.ignition);
export const useSelectedEmulatorDriving = () => useEmulatorStore((state) => state.selectedCar?.emulator.driving);
export const useSelectedEmulatorCoord = () => useEmulatorStore((state) => state.selectedCar?.emulator.coord);
export const useSelectedEmulatorStartPoint = () => useEmulatorStore((state) => state.selectedCar?.emulator.coord.start);
export const useSelectedEmulatorEndPoint = () => useEmulatorStore((state) => state.selectedCar?.emulator.coord.end);
export const useSelectedEmulatorCurrentPoint = () => useEmulatorStore((state) => state.selectedCar?.emulator.coord.current);
export const useSelectedEmulatorPathRoute = () => useEmulatorStore((state) => state.selectedCar?.emulator.pathRoute);
export const useSelectedEmulatorDriveLogs = () => useEmulatorStore((state) => state.selectedCar?.emulator.driveLogs);