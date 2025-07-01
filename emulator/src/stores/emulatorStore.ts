import { create } from 'zustand'
import { produce } from 'immer'
import type { Car } from './carStore'

export type GpsCoord = {
    lat: number
    lng: number
}

type CarEmulatorState = {
    car: Car
    emulator: {
        ignition: '시동OFF' | '시동ON'
        driving: '주행' | '정지',
        startPoint: GpsCoord | null,
        endPoint: GpsCoord | null
        currentPoint: GpsCoord | null
        pathRoute: GpsCoord[]
    }
}

interface EmulatorState {
    mapCenter: GpsCoord
    selectedCar: CarEmulatorState | null

    setStartPoint: (point: GpsCoord) => void
    setEndPoint: (point: GpsCoord) => void
    setMapCenter: (point: GpsCoord) => void
    setPathRoute: (route: GpsCoord[]) => void
    setCurrentPoint: (point: GpsCoord | null) => void
    setSelectedCar: (car: Car | null) => void
    centerOnSelectedCar: () => void
}

export const useEmulatorStore = create<EmulatorState>((set) => ({
    mapCenter: {
        lat: 37.499225,
        lng: 127.031477
    },
    selectedCar: null,

    setStartPoint: (point: GpsCoord) => set(
        produce((state) => {
            if (state.selectedCar) {
                state.selectedCar.emulator.startPoint = point
            }
        })
    ),
    setEndPoint: (point: GpsCoord) => set(
        produce((state) => {
            if (state.selectedCar) {
                state.selectedCar.emulator.endPoint = point
            }
        })
    ),
    setMapCenter: (point: GpsCoord) => set(
        produce((state) => {
            state.mapCenter = point
        })
    ),
    setPathRoute: (route: GpsCoord[]) => set(
        produce((state) => {
            if (state.selectedCar) {
                state.selectedCar.emulator.pathRoute = route
            }
        })
    ),
    setCurrentPoint: (point: GpsCoord | null) => set(
        produce((state) => {
            if (state.selectedCar) {
                state.selectedCar.emulator.currentPoint = point
            }
        })
    ),
    setSelectedCar: (car: Car | null) => set(
        produce((state) => {
            if (car) {
                state.selectedCar = {
                    car: car,
                    emulator: {
                        ignition: car.ignitionStatus === 'ON' ? '시동ON' : '시동OFF',
                        driving: car.activeTransactionId ? '주행' : '정지',
                        startPoint: {
                            lat: car.latitude,
                            lng: car.longitude
                        },
                        endPoint: null,
                        currentPoint: {
                            lat: car.latitude,
                            lng: car.longitude
                        },
                        pathRoute: []
                    }
                }
            } else {
                state.selectedCar = null
            }
        })
    ),
    centerOnSelectedCar: () => set(
        produce((state) => {
            if (state.selectedCar) {
                state.mapCenter = {
                    lat: state.selectedCar.car.latitude,
                    lng: state.selectedCar.car.longitude
                }
            }
        })
    )
}))

export const useSelectedEmulatorCar = () => useEmulatorStore((state) => state.selectedCar?.car);