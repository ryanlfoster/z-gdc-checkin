
package com.adobe.gdc.checkin;

import java.util.Map;
import javax.mail.internet.InternetAddress;

/**
 * A service interface for sending a generic template based Email Notification.
 * 
 */
public interface EmailService {

    /**
     * Construct an email based on a template and send it to one or more
     * recipients.
     * 
     * @param templatePath Absolute path of the template used to send the email.
     * @param emailParams Replacement variable map to be injected in the template
     * @param recipients recipient email addresses
     * 
     * @return boolean - true if email sent, false otherwise
     */
    boolean sendEmail(String templatePath, Map<String, String> emailParams,
        InternetAddress... recipients);

    /**
     * Construct an email based on a template and send it to one or more
     * recipients.
     * 
     * @param templatePath Absolute path of the template used to send the email.
     * @param emailParams Replacement variable map to be injected in the template
     * @param recipients recipient email addresses. Invalid email addresses are skipped.
     * 
     * @return boolean - true if email sent, false otherwise
     */
    boolean sendEmail(String templatePath, Map<String, String> emailParams, String... recipients);
}
