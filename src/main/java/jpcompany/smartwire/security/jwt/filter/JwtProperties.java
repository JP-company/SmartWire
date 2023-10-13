package jpcompany.smartwire.security.jwt.filter;

public interface JwtProperties {
    String SECRET = "전정표"; // 우리 서버만 알고 있는 비밀값
    long EXPIRATION_TIME = 8640000000L*2;// 100일 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}