import { authStore } from '~/stores/authStore';

// React 외부에서 인증 상태 확인
export const checkAuthStatus = () => {
    return authStore.isAuthenticated();
};

// React 외부에서 사용자 정보 가져오기
export const getCurrentUser = () => {
    return authStore.getUser();
};

// React 외부에서 로그아웃
export const logoutUser = () => {
    authStore.logout();
};

// React 외부에서 인증 상태 변경 감지
export const subscribeToAuthChanges = (callback: (isAuthenticated: boolean) => void) => {
    return authStore.subscribe((state) => {
        callback(state.isAuthenticated);
    });
};

// API 요청 시 인증 상태 확인하는 헬퍼 함수
export const withAuthCheck = async <T>(
    apiCall: () => Promise<T>,
    onUnauthorized?: () => void
): Promise<T | null> => {
    if (!checkAuthStatus()) {
        onUnauthorized?.();
        return null;
    }

    try {
        return await apiCall();
    } catch (error) {
        // 401 에러 등 인증 관련 에러 처리
        if (error instanceof Error && error.message.includes('401')) {
            logoutUser();
            onUnauthorized?.();
        }
        throw error;
    }
};

// 페이지 새로고침 시 인증 상태 복원 확인
export const initializeAuth = () => {
    const user = getCurrentUser();
    if (user) {
        console.log('인증 상태가 복원되었습니다:', user.name);
    }
}; 