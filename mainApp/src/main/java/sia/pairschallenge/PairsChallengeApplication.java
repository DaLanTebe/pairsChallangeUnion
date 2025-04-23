package sia.pairschallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "sia")
@EnableJpaAuditing
public class PairsChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PairsChallengeApplication.class, args);
    }

}
