package pesko.orgasms.app.configurations.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import com.google.common.net.HttpHeaders;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfiguration {


    @Value(value = "${application.jwt.secretKey}")
    private String secretKey;
    @Value(value = "${application.jwt.tokenPrefix}")
    private String tokenPrefix;
    @Value(value = "${application.jwt.tokenExpirationAfterDays}")
    private Integer tokenExpirationAfterDays;

    public JwtConfiguration() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }

    @Bean
    public SecretKey getSecretKeyForSigning(){
       return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }
}
