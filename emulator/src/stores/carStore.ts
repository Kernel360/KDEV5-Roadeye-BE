import { create } from 'zustand';
import * as api from '~/lib/api2';

export interface Car {
    id: string;
    latitude: number;
    longitude: number;
    name?: string;
    plateNumber?: string;
    status?: string;
}

interface CarState {
    cars: Car[];
    isLoading: boolean;
    error: string | null;
}

interface CarActions {
    fetchCars: () => Promise<void>;
    clearError: () => void;
    setCars: (cars: Car[]) => void;
}

type CarStore = CarState & CarActions;

export const useCarStore = create<CarStore>()(
    (set) => ({
        cars: [],
        isLoading: false,
        error: null,

        fetchCars: async () => {
            set({ isLoading: true, error: null });

            try {
                const baseUrl = import.meta.env.VITE_API_WEB_URL;
                const response = await api.getAllCarList(baseUrl);

                if (!response.ok) {
                    throw new Error('차량 목록을 가져오는데 실패했습니다.');
                }

                const { data } = await response.json();
                set({
                    cars: data,
                    isLoading: false,
                    error: null
                });
            } catch (err) {
                const errorMessage = err instanceof Error ? err.message : '차량 목록을 가져오는데 실패했습니다.';
                set({
                    isLoading: false,
                    error: errorMessage
                });
            }
        },

        clearError: () => {
            set({ error: null });
        },

        setCars: (cars: Car[]) => {
            set({ cars });
        }
    }),
);

export const useCars = () => useCarStore((state) => state.cars);
export const useCarLoading = () => useCarStore((state) => state.isLoading);
export const useCarError = () => useCarStore((state) => state.error);

export const carStore = {
    getState: () => useCarStore.getState(),
    setState: (partial: Partial<CarState>) => useCarStore.setState(partial),
    subscribe: (listener: (state: CarStore) => void) => useCarStore.subscribe(listener),

    getCars: () => useCarStore.getState().cars,
    fetchCars: () => useCarStore.getState().fetchCars(),
    clearError: () => useCarStore.getState().clearError(),
    setCars: (cars: Car[]) => useCarStore.getState().setCars(cars)
}; 