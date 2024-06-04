package am.ysu.encryptorsa.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;

@Data
@Configuration
public class AppConfig {

    @Bean
    public KeyPair getKeyPair() {
        return new KeyPair(null, null);
    }
}
