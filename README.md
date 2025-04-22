## 👋 프로젝트 소개

하나의 기능을 구현하는 방법은 다양합니다. 누군가의 피드백을 통해 내 코드의 방향성을 점검하고, 더 나은 구현 방식을 고민할 수 있다면 개발 실력은 훨씬 빠르게 성장할 수 있습니다.  
이런 생각에서 출발한 **RevUP**은, 개발자가 자신의 코드를 공유하고 리뷰받을 수 있도록 돕는 **코드 리뷰 요청 플랫폼**입니다.


<br/>

## 👥 팀원 소개

<table>
  <tr>
    <td align="center" width="200px">
      <img src="https://avatars.githubusercontent.com/jmd5314?v=4" width="180px" alt="jmd5314"/><br />
      <a href="https://github.com/jmd5314"><b>조믿음 (팀장)</b></a><br />
      질문 & 답변
    </td>
    <td align="center" width="200px">
      <img src="https://avatars.githubusercontent.com/power-minu?v=4" width="180px" alt="power-minu"/><br />
      <a href="https://github.com/power-minu"><b>김민우</b></a><br />
      라이브 피드백 & 채팅
    </td>
    <td align="center" width="200px">
      <img src="https://avatars.githubusercontent.com/rinklove?v=4" width="180px" alt="rinklove"/><br />
      <a href="https://github.com/rinklove"><b>이준호 (PM)</b></a><br />
      회원 & 업적
    </td>
  </tr>
</table>


<br/>

## 🛠️ 기술 스택

- **Language**: Java  
- **Framework**: Spring Boot, Spring Security  
- **DB**: MySQL, Redis  
- **ORM / Query**: JPA, QueryDSL  
- **Realtime**: STOMP, WebSocket  
- **Authentication**: JWT, OAuth 2.0
- **Infra**: Docker, GitHub Actions, AWS EC2 / S3  
- **Etc**: Notion, Swagger, Slack  


<br/>

## ✨ 주요 기능

- 회원가입 및 로그인 기능 (JWT & OAuth 2.0)
- 코드 리뷰 질문 및 답변 기능
- 1:1 실시간 채팅 기능
- 코드 라이브 피드백 기능
- 사용자 업적 기능


<br/>

## 🗂 ERD

![RevUp ERD](https://github.com/user-attachments/assets/3955847e-110e-4d06-aa9d-7f5c445f8f2c)


<br/>

## 🧱 아키텍처

<img src="https://github.com/user-attachments/assets/adae7ccf-53db-4f22-9c70-3b54469412a2" alt="Architecture Diagram" width="100%"/>


<br/>

## 🧪 트러블슈팅

- [OAuth 2.0으로 로그인 시, 중복 데이터가 들어가는 현상](https://www.notion.so/prgrms/OAuth-2-0-abd095cd07a04dd5810a2d410eecbce0?pvs=4)  
- [페이징 처리 성능 개선기](https://jmd5314.tistory.com/entry/JPA-%ED%8C%A8%EC%B9%98%EC%A1%B0%EC%9D%B8%EC%97%90-%EC%96%BD%ED%9E%8C-%ED%8E%98%EC%9D%B4%EC%A7%95-%EC%B2%98%EB%A6%AC-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0%EA%B8%B0)  
- [이메일 전송 작업이 포함된 API의 응답 속도 개선 과정](https://www.notion.so/prgrms/API-c8e0571ad5ec4287b9d50db9a6f5d87b?pvs=4)  
- [업적 달성 기능 성능 개선 과정](https://www.notion.so/prgrms/c1b96efc1fca4e29a0b422c0e422d71c?pvs=4)  
- [답변 저장 로직 - 데드락 발생](https://jmd5314.tistory.com/entry/DB-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88-%EB%B0%8F-%EB%8D%B0%EB%93%9C%EB%9D%BD-%EB%B0%9C%EC%83%9D-%ED%95%B4%EA%B2%B0)  
- [실시간 동시편집 기능 구현 과정](https://www.notion.so/prgrms/06c5de7148654196a8afbeb3c92f112c?pvs=4)
