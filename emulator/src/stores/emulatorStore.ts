import { create } from 'zustand'
import type { Car } from './carStore'

export type GpsCoord = {
    lat: number
    lng: number
}

interface EmulatorState {
    mapCenter: GpsCoord
    startPoint: GpsCoord | null
    currentPoint: GpsCoord | null
    endPoint: GpsCoord | null
    pathRoute: GpsCoord[]
    selectedCar: Car | null

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
    setSelectedCar: (car: Car | null) => set({ selectedCar: car }),
    centerOnSelectedCar: () => set((state) => {
        if (state.selectedCar) {
            return { mapCenter: { lat: state.selectedCar.latitude, lng: state.selectedCar.longitude } }
        }
        return state
    })
}))
