package de.example.haegertime.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;


@Component
public class EmailService {

    private final JavaMailSender sender;

    @Autowired
    public EmailService(JavaMailSender sender){
        this.sender = sender;
    }

    public void send(String to, String subject, String text){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("josalongmartin@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text);
        };
        this.sender.send(messagePreparator);
    }

    public String getEmailText(String first, String accountActivity){
        return "Hallo " + first + ", \n \n" +
                "Ein Admin hat deinen Haegertime Account mit folgender Aktion bearbeitet:\n \n"
                + accountActivity + "\n \n"+
                "bei weiteren Fragen melde dich gerne bei Cedrik und gehe ihm so richtig auf die Nerven.";
    }

}
