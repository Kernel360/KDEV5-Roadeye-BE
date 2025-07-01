import { useEffect } from 'react';
import { useCarStore, useCars, useCarLoading, useCarError } from '../stores/carStore';

export const useCarList = () => {
    const cars = useCars();
    const isLoading = useCarLoading();
    const error = useCarError();
    const { fetchCars, clearError } = useCarStore();

    useEffect(() => {
        fetchCars();
    }, [fetchCars]);

    return {
        cars,
        isLoading,
        error,
        refetch: fetchCars,
        clearError
    };
};
