import { create } from 'zustand'
import type { Car } from './carStore'

export type GpsCoord = {
    lat: number
    lng: number
}

type CarEmulatorState = {
    car: Car
    emulator: {
        ignition: '시동OFF' | '시동ON'
        driving: '주행' | '정지'
    }
}

interface EmulatorState {
    mapCenter: GpsCoord
    startPoint: GpsCoord | null
    currentPoint: GpsCoord | null
    endPoint: GpsCoord | null
    pathRoute: GpsCoord[]
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
    startPoint: null,
    currentPoint: null,
    endPoint: null,
    pathRoute: [],
    selectedCar: null,

    setStartPoint: (point: GpsCoord) => set({ startPoint: point }),
    setEndPoint: (point: GpsCoord) => set({ endPoint: point }),
    setMapCenter: (point: GpsCoord) => set({ mapCenter: point }),
    setPathRoute: (route: GpsCoord[]) => set({ pathRoute: route }),
    setCurrentPoint: (point: GpsCoord | null) => set({ currentPoint: point }),
    setSelectedCar: (car: Car | null) => {
        if (car) {
            set({
                selectedCar: {
                    car: car,
                    emulator: {
                        ignition: car.ignitionStatus === 'ON' ? '시동ON' : '시동OFF',
                        driving: car.activeTransactionId ? '주행' : '정지'
                    }
                }
            })
        } else {
            set({ selectedCar: null })
        }
    },
    centerOnSelectedCar: () => set((state) => {
        if (state.selectedCar) {
            return { mapCenter: { lat: state.selectedCar.car.latitude, lng: state.selectedCar.car.longitude } }
        }
        return state
    })
}))

export const useSelectedEmulatorCar = () => useEmulatorStore((state) => state.selectedCar?.car);