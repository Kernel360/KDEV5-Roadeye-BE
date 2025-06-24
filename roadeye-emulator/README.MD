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
