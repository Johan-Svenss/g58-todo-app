package se.lexicon.g58todoapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Email Configuration Class
 * Configures the JavaMailSender for sending emails
 * This version works with both real SMTP servers and test servers
 */
@Configuration
public class EmailConfig {

    // Inject email properties from application.properties
    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private int mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${email.from.address}")
    private String fromAddress;

    @Value("${email.from.name}")
    private String fromName;

    /**
     * Creates and configures the JavaMailSender bean
     * This is what Spring uses to send emails
     *
     * @return configured JavaMailSender
     */
    @Bean
    public JavaMailSender javaMailSender() {
        // Create a new mail sender implementation
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Set basic SMTP configuration
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        // Set additional properties
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "false");  // No auth for testing
        props.put("mail.smtp.starttls.enable", "false");  // No TLS for testing
        props.put("mail.debug", "true");  // Enable debug output

        return mailSender;
    }

    /**
     * Provides the "from" email address as a bean
     */
    @Bean(name = "fromAddress")
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * Provides the "from" display name as a bean
     */
    @Bean(name = "fromName")
    public String getFromName() {
        return fromName;
    }
}