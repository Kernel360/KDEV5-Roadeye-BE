# 사용 방법

## 준비

1. 차량 데이터를 src/data/cars.json에 저장하기 (bootstrapper로 데이터 생성 후 json으로 저장)
2. 위치 데이터를 src/data/stations.json에 저장하기.

- [한국철도공사/역 위치 정보](https://www.data.go.kr/data/15127532/fileData.do)
- [국토교통부/버스 정류장 위치 정보](https://www.data.go.kr/data/15067528/fileData.do)

3. `npm run prepare`

## 실행

1. 시동 ON 이벤트

- `npm run k6:1`

2. 주기 정보 이벤트

- `npm run k6:2`

3. 시동 OFF 이벤트

- `npm run k6:3`

4. 에뮬레이터

- `npm run k6:4`

