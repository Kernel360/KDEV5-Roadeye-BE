export interface GraphHopperRequest {
    points: [number, number][];
    profile: string;
    elevation: boolean;
    instructions: boolean;
    locale: string;
    points_encoded: boolean;
    points_encoded_multiplier: number;
    details: string[];
    snap_preventions: string[];
}

export interface GraphHopperResponse {
    // GraphHopper API 응답 타입 정의
    paths?: any[];
    message?: string;
    hints?: any;
}

/**
 * GraphHopper API 클라이언트
 */
export class GraphHopperAPI {
    private baseUrl: string;
    private apiKey?: string;

    constructor(baseUrl: string = 'https://graphhopper.com/api/1/route', apiKey?: string) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    /**
     * baseUrl 설정
     * @param baseUrl - GraphHopper API 기본 URL
     */
    setBaseUrl(baseUrl: string): void {
        this.baseUrl = baseUrl;
    }

    /**
     * API 키 설정
     * @param apiKey - GraphHopper API 키
     */
    setApiKey(apiKey: string): void {
        this.apiKey = apiKey;
    }

    /**
     * GraphHopper API를 사용하여 경로 계산을 요청합니다.
     * @param request - GraphHopper API 요청 객체
     * @returns Promise<GraphHopperResponse>
     */
    async calculateRoute(request: GraphHopperRequest): Promise<GraphHopperResponse> {
        // API 키가 있으면 URL에 추가
        const url = this.apiKey
            ? `${this.baseUrl}?key=${this.apiKey}`
            : this.baseUrl;

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            });

            if (!response.ok) {
                throw new Error(`GraphHopper API 오류: ${response.status} ${response.statusText}`);
            }

            const data = await response.json();
            return data;
        } catch (error) {
            console.error('GraphHopper API 요청 실패:', error);
            throw error;
        }
    }

    /**
     * 기본 설정으로 경로 계산을 요청하는 편의 함수
     * @param startPoint - 시작점 [경도, 위도]
     * @param endPoint - 도착점 [경도, 위도]
     * @returns Promise<GraphHopperResponse>
     */
    async calculateRouteWithDefaults(
        startPoint: [number, number],
        endPoint: [number, number]
    ): Promise<GraphHopperResponse> {
        const request: GraphHopperRequest = {
            points: [startPoint, endPoint],
            profile: "car",
            elevation: true,
            instructions: true,
            locale: "ko",
            points_encoded: true,
            points_encoded_multiplier: 1000000,
            details: [
                "road_class",
                "road_environment",
                "max_speed",
                "average_speed"
            ],
            snap_preventions: ["ferry"]
        };

        return this.calculateRoute(request);
    }

    /**
     * 사용자가 제공한 JSON과 동일한 요청을 보내는 함수
     * @returns Promise<GraphHopperResponse>
     */
    async calculateRouteWithProvidedData(): Promise<GraphHopperResponse> {
        const request: GraphHopperRequest = {
            points: [
                [128.21998106475232, 36.36750666198695],
                [128.23853823346025, 36.40757642460686]
            ],
            profile: "car",
            elevation: true,
            instructions: true,
            locale: "ko",
            points_encoded: true,
            points_encoded_multiplier: 1000000,
            details: [
                "road_class",
                "road_environment",
                "max_speed",
                "average_speed"
            ],
            snap_preventions: ["ferry"]
        };

        return this.calculateRoute(request);
    }
}

// 기본 인스턴스 생성 (기존 함수들과의 호환성을 위해)
const defaultGraphHopperAPI = new GraphHopperAPI();

/**
 * 기본 GraphHopper API 인스턴스를 사용하여 경로 계산을 요청합니다.
 * @param request - GraphHopper API 요청 객체
 * @param apiKey - GraphHopper API 키 (선택사항)
 * @returns Promise<GraphHopperResponse>
 */
export async function calculateRoute(
    request: GraphHopperRequest,
    apiKey?: string
): Promise<GraphHopperResponse> {
    if (apiKey) {
        defaultGraphHopperAPI.setApiKey(apiKey);
    }
    return defaultGraphHopperAPI.calculateRoute(request);
}

/**
 * 기본 설정으로 경로 계산을 요청하는 편의 함수
 * @param startPoint - 시작점 [경도, 위도]
 * @param endPoint - 도착점 [경도, 위도]
 * @param apiKey - GraphHopper API 키 (선택사항)
 * @returns Promise<GraphHopperResponse>
 */
export async function calculateRouteWithDefaults(
    startPoint: [number, number],
    endPoint: [number, number],
    apiKey?: string
): Promise<GraphHopperResponse> {
    if (apiKey) {
        defaultGraphHopperAPI.setApiKey(apiKey);
    }
    return defaultGraphHopperAPI.calculateRouteWithDefaults(startPoint, endPoint);
}

/**
 * 사용자가 제공한 JSON과 동일한 요청을 보내는 함수
 * @param apiKey - GraphHopper API 키 (선택사항)
 * @returns Promise<GraphHopperResponse>
 */
export async function calculateRouteWithProvidedData(
    apiKey?: string
): Promise<GraphHopperResponse> {
    if (apiKey) {
        defaultGraphHopperAPI.setApiKey(apiKey);
    }
    return defaultGraphHopperAPI.calculateRouteWithProvidedData();
}
