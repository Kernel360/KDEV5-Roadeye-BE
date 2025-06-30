import { useState, useCallback, useRef } from 'react';
import { GraphHopperAPI } from '../lib/graphhopper';
import type { GraphHopperRequest, GraphHopperResponse } from '../lib/graphhopper';

interface UseGraphHopperOptions {
    baseUrl?: string;
    apiKey?: string;
    autoExecute?: boolean;
}

interface UseGraphHopperState {
    data: GraphHopperResponse | null;
    loading: boolean;
    error: string | null;
}

interface UseGraphHopperReturn extends UseGraphHopperState {
    execute: (request: GraphHopperRequest) => Promise<void>;
    executeWithDefaults: (startPoint: [number, number], endPoint: [number, number]) => Promise<void>;
    executeWithProvidedData: () => Promise<void>;
    setApiKey: (apiKey: string) => void;
    setBaseUrl: (baseUrl: string) => void;
    reset: () => void;
}

/**
 * GraphHopper API를 사용하는 React hook
 * @param options - Hook 설정 옵션
 * @returns GraphHopper API 상태와 메서드들
 */
export function useGraphHopper(options: UseGraphHopperOptions = {}): UseGraphHopperReturn {
    const { baseUrl, apiKey, autoExecute = false } = options;

    const [state, setState] = useState<UseGraphHopperState>({
        data: null,
        loading: false,
        error: null,
    });

    const apiRef = useRef<GraphHopperAPI>(new GraphHopperAPI(baseUrl, apiKey));

    // API 키 설정
    const setApiKey = useCallback((apiKey: string) => {
        apiRef.current.setApiKey(apiKey);
    }, []);

    // Base URL 설정
    const setBaseUrl = useCallback((baseUrl: string) => {
        apiRef.current.setBaseUrl(baseUrl);
    }, []);

    // 상태 초기화
    const reset = useCallback(() => {
        setState({
            data: null,
            loading: false,
            error: null,
        });
    }, []);

    // 일반 경로 계산 실행
    const execute = useCallback(async (request: GraphHopperRequest) => {
        setState(prev => ({ ...prev, loading: true, error: null }));

        try {
            const data = await apiRef.current.calculateRoute(request);
            setState(prev => ({ ...prev, data, loading: false }));
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '알 수 없는 오류가 발생했습니다.';
            setState(prev => ({ ...prev, error: errorMessage, loading: false }));
        }
    }, []);

    // 기본 설정으로 경로 계산 실행
    const executeWithDefaults = useCallback(async (startPoint: [number, number], endPoint: [number, number]) => {
        setState(prev => ({ ...prev, loading: true, error: null }));

        try {
            const data = await apiRef.current.calculateRouteWithDefaults(startPoint, endPoint);
            setState(prev => ({ ...prev, data, loading: false }));
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '알 수 없는 오류가 발생했습니다.';
            setState(prev => ({ ...prev, error: errorMessage, loading: false }));
        }
    }, []);

    // 제공된 데이터로 경로 계산 실행
    const executeWithProvidedData = useCallback(async () => {
        setState(prev => ({ ...prev, loading: true, error: null }));

        try {
            const data = await apiRef.current.calculateRouteWithProvidedData();
            setState(prev => ({ ...prev, data, loading: false }));
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '알 수 없는 오류가 발생했습니다.';
            setState(prev => ({ ...prev, error: errorMessage, loading: false }));
        }
    }, []);

    return {
        ...state,
        execute,
        executeWithDefaults,
        executeWithProvidedData,
        setApiKey,
        setBaseUrl,
        reset,
    };
}

/**
 * 특정 좌표에 대한 경로 계산을 위한 전용 hook
 * @param startPoint - 시작점 [경도, 위도]
 * @param endPoint - 도착점 [경도, 위도]
 * @param options - Hook 설정 옵션
 * @returns 경로 계산 결과와 상태
 */
export function useRouteCalculation(
    startPoint: [number, number],
    endPoint: [number, number],
    options: UseGraphHopperOptions = {}
) {
    const { baseUrl, apiKey, autoExecute = false } = options;

    const [state, setState] = useState<UseGraphHopperState>({
        data: null,
        loading: false,
        error: null,
    });

    const apiRef = useRef<GraphHopperAPI>(new GraphHopperAPI(baseUrl, apiKey));

    const calculateRoute = useCallback(async () => {
        setState(prev => ({ ...prev, loading: true, error: null }));

        try {
            const data = await apiRef.current.calculateRouteWithDefaults(startPoint, endPoint);
            setState(prev => ({ ...prev, data, loading: false }));
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : '알 수 없는 오류가 발생했습니다.';
            setState(prev => ({ ...prev, error: errorMessage, loading: false }));
        }
    }, [startPoint, endPoint]);

    const setApiKey = useCallback((apiKey: string) => {
        apiRef.current.setApiKey(apiKey);
    }, []);

    const setBaseUrl = useCallback((baseUrl: string) => {
        apiRef.current.setBaseUrl(baseUrl);
    }, []);

    const reset = useCallback(() => {
        setState({
            data: null,
            loading: false,
            error: null,
        });
    }, []);

    return {
        ...state,
        calculateRoute,
        setApiKey,
        setBaseUrl,
        reset,
    };
} 