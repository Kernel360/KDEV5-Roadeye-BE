import { create } from 'zustand'
import type { Car } from './carStore'
import { emulateCarPath } from '~/lib/emulator'
import { findRoute } from '~/lib/graphhopper'
import type { DriveData } from '~/types/vehicle'
import { immer } from 'zustand/middleware/immer'

export type GpsCoord = {
    lat: number
    lng: number
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
        driveLogs: DriveData[]
    }
}

export interface EmulatorState {
    mapCenter: GpsCoord
    selectedCar: CarEmulatorState | null
    emulatorInstance: Map<Car['id'], ReturnType<typeof emulateCarPath>>

    setSelectedCar: (car: Car | null) => void
    setStartPoint: (point: GpsCoord) => void
    setCurrentPoint: (point: GpsCoord | null) => void
    setEndPoint: (point: GpsCoord) => void
    setMapCenter: (point: GpsCoord) => void
    centerOnSelectedCar: () => void
    findPathRoute: (start: GpsCoord, end: GpsCoord) => Promise<GpsCoord[]>
    addDriveLog: (log: DriveData) => void
    clearDriveLogs: () => void
    turnOnIgnition: () => void
    turnOffIgnition: () => void
}

export const useEmulatorStore = create<EmulatorState>()(
    immer(
        (set, get) => ({
            mapCenter: {
                lat: 37.499225,
                lng: 127.031477
            },
            selectedCar: null,
            emulatorInstance: new Map<Car['id'], ReturnType<typeof emulateCarPath>>(),

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
            findPathRoute: async (start: GpsCoord, end: GpsCoord) => {
                const state = get();

                if (!state.selectedCar) {
                    throw new Error('Selected car not found')
                }

                const instance = state.emulatorInstance.get(state.selectedCar.car.id) || emulateCarPath({
                    start: start,
                    end: end,
                    initSpdKmh: 0,
                    maxSpdKmh: 0,
                    acc: 0
                })
                state.emulatorInstance.set(state.selectedCar.car.id, instance)

                return findRoute("car", start, end)
                    .then((res) => {
                        return res.paths[0].points.coordinates
                    })
            },
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
                                start: null,
                                end: null,
                                current: {
                                    lat: car.latitude,
                                    lng: car.longitude
                                }
                            },
                            pathRoute: [],
                            driveLogs: []
                        }
                    }
                } else {
                    state.selectedCar = null
                }
            }),
            centerOnSelectedCar: () => set((state) => {
                if (state.selectedCar) {
                    state.mapCenter = {
                        lat: state.selectedCar.emulator.coord.current.lat,
                        lng: state.selectedCar.emulator.coord.current.lng
                    }
                    state.selectedCar.emulator.ignition.onTime = new Date()
                    state.selectedCar.emulator.driveLogs = []
                    state.selectedCar.emulator.pathRoute = []
                }
            }),
            addDriveLog: (log: DriveData) => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.driveLogs.push(log)
                }
            }),
            clearDriveLogs: () => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.driveLogs = []
                }
            }),
            turnOnIgnition: () => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.ignition.state = '시동ON'
                    state.selectedCar.emulator.ignition.onTime = new Date()
                }
            }),
            turnOffIgnition: () => set((state) => {
                if (state.selectedCar) {
                    state.selectedCar.emulator.ignition.state = '시동OFF'
                    state.selectedCar.emulator.ignition.offTime = new Date()
                }
            })
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
export const useSelectedEmulatorPathRoute = () => useEmulatorStore((state) => state.selectedCar?.emulator.pathRoute);
export const useSelectedEmulatorDriveLogs = () => useEmulatorStore((state) => state.selectedCar?.emulator.driveLogs);