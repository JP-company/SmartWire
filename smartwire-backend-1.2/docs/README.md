## 회원가입
- **`/join` GET Mapping**
  - **Controller**
    1. 로그인 안 된 상태
       - 정상 접근
    2. 로그인 된 상태
       - 접근 불가
    3. 회원가입 실패 후
       - 오류 메시지 출력


- **`/join` POST Mapping**
   - **Controller**
     1. 입력값 MemberJoinDto 로 변환
     2. 입력값 검증
        - 실패 시 => `/join` Redirect
     3. `MemberJoinService.join(MemberJoinDto)` 호출
     4. 성공 시 이메일 인증 창 응답

   - **Service** `MemberJoinService.join(MemberJoinDto)`
     1. 비밀번호 단방향 암호화
     2. 메일 인증 토큰 생성, 할당
     3. `MemberRepository.save(MemberJoinDto)` 호출
     4. 정상적으로 DB에 저장되면 인증 메일 전송
        - 메일 전송 오류 시 => 사용자에게 재전송 버튼 클릭 요청 / 회원가입은 성공

   - **Repository** `MemberRepository.save(MemberJoinDto)`
     1. DB에 MemberJoinDto 저장
        - DB 저장 오류 시 => 회원 가입 실패