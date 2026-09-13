package com.dsk.user_service;

import com.dsk.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
@Slf4j
class UserServiceApplicationTests {

    public static final int NUMBER_OF_USERS = 10;
    @Autowired
    UserRepository userRepository;


	@Test
	void contextLoads() {
	}

    @Disabled
    @Test
    void createUsers(){

        for(int i = 0; i< NUMBER_OF_USERS; i++){
            User user = User.builder()
                    .name("User"+i)
                    .email("user"+i+"@example.com")
                    .address("Address"+i)
                    .alerting(i%2==1)
                    .energyAlertingThreshold(1000.00+i)
                    .createdAt(Instant.now())
                    .isDeleted(false)
                    .build();

            userRepository.save(user);
        }
        log.info("User Repository populated successfully");
    }
}
