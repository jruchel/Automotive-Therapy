package org.jruchel.carworkshop.utils;


import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Getter
@Component("Properties")
public class Properties implements InitializingBean {


    @Value("${spring.datasource.username}")
    private String datasourceUsername;
    @Value("${spring.datasource.password}")
    private String datasourcePassword;
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    @Value("${pattern.phone}")
    private String phonePattern;
    @Value("${pattern.email}")
    private String emailPattern;
    @Value("${moderator.username}")
    private String moderatorUsername;
    @Value("${moderator.password}")
    private String moderatorPassword;
    @Value("${administrator.key}")
    private String administratorKey;
    @Value("${frontend.domain.name}")
    private String domainName;
    @Value("${frontend.opinion.form}")
    private String opinionForm;
    @Value("${mailing.generic.subject}")
    private String genericMailingSubject;
    @Value("${mailing.generic.content}")
    private String genericMailingContent;
    @Value("${mailing.complete.subject}")
    private String completeMailingSubject;
    @Value("${mailing.complete.content}")
    private String completeMailingContent;
    @Value("${workshop.address}")
    private String workshopAddress;
    @Value("${workshop.working-hours}")
    private String workingHours;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean startTLS;


    @Override
    public void afterPropertiesSet() {
        System.out.println(datasourceUrl);
        System.out.println(datasourceUsername);
        System.out.println(datasourcePassword);
    }
}
