import { useKakaoLoader as useKakaoLoaderOrigin } from "react-kakao-maps-sdk";

export default function useKakaoMap() {
    useKakaoLoaderOrigin({
        appkey: import.meta.env.VITE_KAKAO_MAP_KEY,
        libraries: ["clusterer", "drawing", "services"],
        url: "https://dapi.kakao.com/v2/maps/sdk.js",
    })
}