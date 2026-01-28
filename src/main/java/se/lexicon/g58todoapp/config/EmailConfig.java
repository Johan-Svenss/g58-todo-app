package se.lexicon.g58todoapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Email Configuration Class
 * This class sets up the email properties needed to send emails via SMTP
 * It reads configuration from application.properties file
 */
@Configuration
public class EmailConfig {

    // These @Value annotations inject properties from application.properties
    // The part after ':' is a default value if the property is not found

    @Value("${email.smtp.host}")
    private String smtpHost;

    @Value("${email.smtp.port}")
    private int smtpPort;

    @Value("${email.smtp.auth}")
    private boolean smtpAuth;

    @Value("${email.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Value("${email.from.address}")
    private String fromAddress;

    @Value("${email.from.name}")
    private String fromName;

    /**
     * Creates a Properties object containing all SMTP settings
     * This is used by JavaMail API to configure the email session
     *
     * @return Properties object with SMTP configuration
     */
    @Bean(name = "emailProperties")
    public Properties getMailProperties() {
        Properties props = new Properties();

        // SMTP server address (e.g., smtp.gmail.com)
        props.put("mail.smtp.host", smtpHost);

        // SMTP server port (587 for TLS, 465 for SSL)
        props.put("mail.smtp.port", smtpPort);

        // Enable authentication (required for most email providers)
        props.put("mail.smtp.auth", smtpAuth);

        // Enable STARTTLS for secure connection
        props.put("mail.smtp.starttls.enable", starttlsEnable);

        // Additional security settings
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Connection timeout (10 seconds)
        props.put("mail.smtp.connectiontimeout", "10000");

        // Read timeout (10 seconds)
        props.put("mail.smtp.timeout", "10000");

        return props;
    }

    /**
     * Bean to provide email username
     * This is the email address used to authenticate with SMTP server
     */
    @Bean(name = "emailUsername")
    public String getUsername() {
        return username;
    }

    /**
     * Bean to provide email password
     * This should be an app-specific password, not your regular email password
     */
    @Bean(name = "emailPassword")
    public String getPassword() {
        return password;
    }

    /**
     * Bean to provide the "from" email address
     * This is the sender address that recipients will see
     */
    @Bean(name = "fromAddress")
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * Bean to provide the "from" display name
     * This is the sender name that recipients will see
     */
    @Bean(name = "fromName")
    public String getFromName() {
        return fromName;
    }
}