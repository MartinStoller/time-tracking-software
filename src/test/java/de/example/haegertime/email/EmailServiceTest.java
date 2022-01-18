package de.example.haegertime.email;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP) //starts greenmail server
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("abcd", "efgh"))
            .withPerMethodLifecycle(false); //by default greenmail would start a new server for each test which is unnecessary

    @Autowired
    private EmailService testedEmailService;

    @Test
    void shouldNotifyUserViaEmail() throws MessagingException {
        //given
        String to = "martin.stoller2@gmx.de";
        String subject = "lululu";
        String text = "abc dfg.";

        //when
        testedEmailService.send(to, subject, text);

        //then
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages(); //checks if exactly 1 email was received
        assertEquals(1, receivedMessages.length);

        MimeMessage receivedMessage = receivedMessages[0];
        assertEquals("abc dfg.", GreenMailUtil.getBody(receivedMessage));
        assertEquals("martin.stoller2@gmx.de", receivedMessage.getAllRecipients()[0].toString());

    }
}