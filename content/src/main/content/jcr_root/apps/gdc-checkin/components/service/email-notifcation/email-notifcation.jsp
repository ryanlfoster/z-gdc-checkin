
<%--

 This is a dummy Email Notification component that tests the LMS Notification service.
    Requirements : Configure the CQ5 Mail Service. 
                   Provide Email Notification template inside /etc/notification/email/

--%>
<%@include file="/libs/foundation/global.jsp"%>
<%@page session="false" %>
<%@page import="com.aem.lms.services.api.EmailService, java.util.*,javax.mail.internet.InternetAddress"%>
<%

//Set the list of email recipients here
List<InternetAddress> recipient = new ArrayList<InternetAddress>();
recipient.add(new InternetAddress("upasana.chaube@gmail.com"));
recipient.add(new InternetAddress("abulqasimkp@gmail.com"));


//Set the dynamic vaiables of your email template
Map<String, String> params = new HashMap<String,String>();
params.put("body","hello there");

//customize the sender email address - if require
params.put("sendToEmail","abcd@example.com");

EmailService emailService = sling.getService(EmailService.class);
Boolean isEmailSent = emailService.sendEmail("/etc/notification/email/lmsEmailTemplate/emailtemplate.txt",  recipient, params);


if(isEmailSent == true) 
    out.println("Email has been sent successfully");
else 
    out.println("Email sent failed. Please check the log for more information");


%>