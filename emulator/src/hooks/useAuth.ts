import { useAuthStore, useUser, useIsAuthenticated, useAuthLoading, useAuthError } from '~/stores/authStore';

export default function useAuth() {
    const user = useUser();
    const isAuthenticated = useIsAuthenticated();
    const isLoading = useAuthLoading();
    const error = useAuthError();
    const { login, logout, clearError } = useAuthStore();

    return {
        user,
        login,
        logout,
        isAuthenticated,
        isLoading,
        error,
        clearError
    };
} 