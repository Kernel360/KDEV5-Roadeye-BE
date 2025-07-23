import { create } from 'zustand';
import * as api from '~/lib/api2';

interface User {
    id: string;
    name: string;
    email: string;
}

interface AuthState {
    initialized: boolean;
    user: User | null;
    isAuthenticated: boolean;
    isLoading: boolean;
    error: string | null;
}

interface AuthActions {
    login: (companyId: string, username: string, password: string) => Promise<void>;
    logout: () => void;
    clearError: () => void;
    checkSession: () => Promise<boolean>;
}

type AuthStore = AuthState & AuthActions;

export const useAuthStore = create<AuthStore>()(
    (set) => ({
        initialized: false,
        user: null,
        isAuthenticated: false,
        isLoading: false,
        error: null,

        login: (companyId: string, username: string, password: string) => {
            set({ isLoading: true, error: null });

            return new Promise((resolve, reject) => {
                const baseUrl = import.meta.env.VITE_API_WEB_URL;
                api.login(baseUrl, companyId, username, password)
                    .then(() => api.getMyInfo(baseUrl))
                    .then((res) => res.json())
                    .then((data) => {
                        set({
                            initialized: true,
                            user: data,
                            isAuthenticated: true,
                            isLoading: false,
                            error: null
                        });
                        resolve();
                    })
                    .catch((err) => {
                        set({
                            initialized: true,
                            isLoading: false,
                            error: err instanceof Error ? err.message : '로그인에 실패했습니다.'
                        });
                        reject(err);
                    });
            })
        },

        logout: () => {
            set({
                initialized: true,
                user: null,
                isAuthenticated: false,
                isLoading: false,
                error: null
            });
        },

        clearError: () => {
            set({ error: null });
        },

        checkSession: () => {
            set({ isLoading: true, error: null });

            return new Promise((resolve, reject) => {
                const baseUrl = import.meta.env.VITE_API_WEB_URL;
                api.getMyInfo(baseUrl)
                    .then((res) => {
                        if (res.status === 200) {
                            return res.json();
                        } else {
                            throw new Error("세션 확인에 실패했습니다.");
                        }
                    })
                    .then((data) => {
                        set({
                            initialized: true,
                            user: data,
                            isAuthenticated: true,
                            isLoading: false,
                            error: null
                        });
                        resolve(true);
                    })
                    .catch((err) => {
                        set({
                            initialized: true,
                            user: null,
                            isAuthenticated: false,
                            isLoading: false,
                            error: err instanceof Error ? err.message : '세션 확인에 실패했습니다.',
                        });
                        reject(err);
                    });
            })
        },
    })
);

export const useUser = () => useAuthStore((state) => state.user);
export const useAuthInitialized = () => useAuthStore((state) => state.initialized);
export const useIsAuthenticated = () => useAuthStore((state) => state.isAuthenticated);
export const useAuthLoading = () => useAuthStore((state) => state.isLoading);
export const useAuthError = () => useAuthStore((state) => state.error);

export const authStore = {
    getState: () => useAuthStore.getState(),
    setState: (partial: Partial<AuthState>) => useAuthStore.setState(partial),
    subscribe: (listener: (state: AuthStore) => void) => useAuthStore.subscribe(listener),

    getUser: () => useAuthStore.getState().user,
    isAuthenticated: () => useAuthStore.getState().isAuthenticated,
    login: (companyId: string, username: string, password: string) =>
        useAuthStore.getState().login(companyId, username, password),
    logout: () => useAuthStore.getState().logout(),
    clearError: () => useAuthStore.getState().clearError(),
    getMyInfo: () => useAuthStore.getState().checkSession()
}; 