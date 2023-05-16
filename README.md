# 🎫 FCFS Coupon

> 특정 시간에 특정 수량만큼 선착순으로 쿠폰을 발급하는 웹 API

## 🎯 Goal
동시에 몰리는 요청 처리 학습
- race condition 관리
- traffic 관리

## 🛠️ Tech Stack
- Gradle (Kotlin DSL)
- Java 17
- Spring Boot 3.0
- Spring Data Redis
- Embedded Redis(ozimov)
- RestAssured

## 🚀 Core Features

### 쿠폰 지급 내역 관리
- In-memory DB Redis 활용한 고성능 처리
- Transaction 적용으로 배타적 순차 처리 보장


## ⚠️ 주의
> embedded redis 실행 전 port가 사용 가능한지 Windows 명령어를 실행하여 확인합니다.
> 
> Windows가 아닌 다른 OS에서 실행하기 위해서는 해당 로직 수정이 필요합니다.
> (`config/EmbeddedRedisStarter.isAvailable(int port)`)

mac 사용자는 [향로님의 글](https://jojoldu.tistory.com/297)을 참고해서 수정할 수 있습니다.

