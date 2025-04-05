package lea.rams.bis.bridge.condition.service.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LeaRamsBisBridgeConditionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaRamsBisBridgeConditionServiceApplication.class, args);
	}

}
