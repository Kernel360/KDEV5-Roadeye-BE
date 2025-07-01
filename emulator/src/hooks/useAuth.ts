import { useAuthStore, useUser, useIsAuthenticated, useAuthLoading, useAuthError, useAuthInitialized } from '~/stores/authStore';

export default function useAuth() {
    const user = useUser();
    const isAuthenticated = useIsAuthenticated();
    const isLoading = useAuthLoading();
    const error = useAuthError();
    const isInitialized = useAuthInitialized();
    const { login, logout, clearError, checkSession } = useAuthStore();

    return {
        user,
        login,
        logout,
        checkSession,
        isInitialized,
        isAuthenticated,
        isLoading,
        error,
        clearError
    };
} 