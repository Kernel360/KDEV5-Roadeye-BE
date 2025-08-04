# 🚗 RoadEye

실시간 차량 위치 추적부터 운행 통계 집계까지 가능한 **통합 차량 관제 시스템**입니다.

운행 로그의 실시간 처리 성능을 확보하고, 관리자에게 직관적이고 신뢰할 수 있는 운행 정보를 제공합니다.

---

## 📌 프로젝트 소개

**RoadEye**는 다음과 같은 문제를 해결하기 위해 개발되었습니다:

- 차량 위치를 실시간으로 추적하고 운행 상황을 직관적으로 파악하고 싶다.
- 다수 차량의 주기적인 운행 정보를 **1분 내 15,000건 이상 처리할 수 있는 시스템**이 필요하다.
- 통계 기반의 운영 개선을 위해 월별·일별 운행 통계를 제공하고 싶다.

이를 위해 **Spring Boot + RabbitMQ + MySQL + Spring Batch** 기반의 확장 가능한 관제 시스템을 설계하고 구현하였습니다.

---

## 🌟 주요 기능

- 실시간 차량 관제: 차량의 현재 위치, 경로, 속도 등을 실시간으로 확인
- 운행 일지: 차량별 운행 내역 조회 (운행 시작/종료 시각, 거리 등)
- 통계: 월별/일별 운행 통계, 시간대별 운행수
- 사용자 관리: 관리자, 운전자 사용자 추가 및 상태 변경
- 차량 관리: 차량 정보 등록, 수정, 운행 상태 표시

---

## 🔍 세부 기능

### ✅ 실시간 차량 관제

- 차량의 주기 정보(위치, 속도)를 1초 간격으로 수신
- 지도 위에 실시간 위치 표시 및 궤적 추적

### ✅ 고성능 데이터 처리 (15,000TPS)

- `K6` 기반 부하 테스트를 통해 **1분 내 15,000건 요청 처리** 검증
- `Hub` 서버 → `RabbitMQ` → `Consumer` 서버 구조로 병렬 처리
- 설정 튜닝 (`Hikari`, `JDBC Batch`, `MQ Batch`, `Concurrency`)으로 안정적 처리 성능 확보

### ✅ 일별/월별 통계 집계

- **Spring Batch**로 매일 새벽 운행 데이터를 자동 집계
- 일별 운행 거리, 시간, 횟수 및 시간대별 운행량 저장
- 통계 데이터는 관리 화면에서 시각화 제공

---

## ⚙️ 기술 스택

### 🖥️ Front-end

[React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)

---

### 🧠 Back-end

[Java](https://img.shields.io/badge/Java%2017-007396?style=for-the-badge&logo=openjdk&logoColor=white)

[Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)

[Spring Batch](https://img.shields.io/badge/Spring%20Batch-6DB33F?style=for-the-badge&logo=spring&logoColor=white)

[Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-007396?style=for-the-badge&logo=spring&logoColor=white)

[QueryDSL](https://img.shields.io/badge/QueryDSL-000000?style=for-the-badge&logo=apachemaven&logoColor=white)

[RabbitMQ](https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)

---

### 🏗️ Infra

[AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)

[AWS RDS](https://img.shields.io/badge/AWS%20RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)

[MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

[Undertow](https://img.shields.io/badge/Undertow-000000?style=for-the-badge&logo=java&logoColor=white)

---

### 🛠 Tools

[K6](https://img.shields.io/badge/K6-7D64FF?style=for-the-badge&logo=k6&logoColor=white)

[HikariCP](https://img.shields.io/badge/HikariCP-0064a5?style=for-the-badge&logo=postgresql&logoColor=white)

[JDBC](https://img.shields.io/badge/JDBC-BD2130?style=for-the-badge&logo=databricks&logoColor=white)

[GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)

[IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)

---

## 🏗 프로젝트 아키텍처

![제목 없는 다이어그램.drawio-3.png](attachment:9d338372-0bf9-4c37-a165-15b295bcb4cc:제목_없는_다이어그램.drawio-3.png)

## **💁‍♂️ 프로젝트 팀원**

| 이름 | 역할 | 담당 |
| --- | --- | --- |
| 윤여일 | 백엔드 개발 | 인프라, CICD, Spring Security, hub, consumer, emulator,  |
| 조윤수 | 백엔드 개발 | CRUD, spring batch |
