package org.jruchel.carworkshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class CarworkshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarworkshopApplication.class, args);
    }

}

//TODO it is now possible to override clients data if you input their phone number or email and override the remaining with yours
// since sending another order these values are updated
//TODO a solution to this would be to require a clients email address and require the client to confirm their order through the email
