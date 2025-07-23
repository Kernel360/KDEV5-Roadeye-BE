# RoadEye 차량 시뮬레이터

## 설치 및 실행

### 웹

1. 의존성 설치:

```bash
npm install
```

2. 개발 서버 실행:

```bash
npm run dev
```

3. 브라우저에서 `http://localhost:5173` 접속

### GraphHopper 길찾기 데모

GraphHopper API를 사용한 길찾기 시연 페이지가 포함되어 있습니다.

1. `index.html`에서 KakaoMap API 키를 설정하세요:
   ```html
   <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_MAP_API_KEY&libraries=services,clusterer"></script>
   ```

2. `/routes/graphhopper-demo` 경로에서 길찾기 데모를 확인할 수 있습니다.

3. GraphHopper API 키를 입력하고 시작점과 도착점을 설정하여 경로를 계산할 수 있습니다.

### K6 스크립트

1. 차량 데이터를 src/data/cars.json에 저장하기 (bootstrapper로 데이터 생성 후 json으로 저장)
2. 위치 데이터를 src/data/stations.json에 저장하기.

- [한국철도공사/역 위치 정보](https://www.data.go.kr/data/15127532/fileData.do)
- [국토교통부/버스 정류장 위치 정보](https://www.data.go.kr/data/15067528/fileData.do)

3. `npm run prepare`

4. K6 스크립트 실행:
    - 시동 ON 이벤트: `npm run k6:1`
    - 주기 정보 이벤트: `npm run k6:2`
    - 시동 OFF 이벤트: `npm run k6:3`
    - 에뮬레이터: `npm run k6:4`
