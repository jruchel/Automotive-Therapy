package org.jruchel.carworkshop.configuration;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;



@Component("Properties")
public class Properties {

    public String getPhonePattern() {
        return phonePattern;
    }

    public String getEmailPattern() {
        return emailPattern;
    }

    public String getModeratorUsername() {
        return moderatorUsername;
    }

    public String getModeratorPassword() {
        return moderatorPassword;
    }

    public String getAdministratorKey() {
        return administratorKey;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getOpinionForm() {
        return opinionForm;
    }

    public String getGenericMailingSubject() {
        return genericMailingSubject;
    }

    public String getGenericMailingContent() {
        return genericMailingContent;
    }

    public String getCompleteMailingSubject() {
        return completeMailingSubject;
    }

    public String getCompleteMailingContent() {
        return completeMailingContent;
    }

    public String getWorkshopAddress() {
        return workshopAddress;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isStartTLS() {
        return startTLS;
    }

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


}
