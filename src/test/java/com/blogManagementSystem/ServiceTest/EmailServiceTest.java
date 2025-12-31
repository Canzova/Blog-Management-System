package com.blogManagementSystem.ServiceTest;

import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.service.EmailServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private static String body = "This is the body of the mail.";
    private static String subject = "This is the subject of the mail";
    private static String emailId = "nihal.singh@gamil.com";

    @BeforeAll
    public static void init(){
        System.out.println("Initialize your data before all test");
    }

    @BeforeEach
    public void initBeforeEach(){
        System.out.println("Initialize something before each test");
    }


    @Test
    void sendEmailNotification_shouldSendCorrectEmail() {

        emailService.sendEmailNotification(body, emailId, subject);

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        // When this method is called in the argument SimpleMailMessage was sent, so capture it
        Mockito.verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();

        Assertions.assertNotNull(sentMessage.getTo());
        Assertions.assertEquals(emailId, sentMessage.getTo()[0]);
        Assertions.assertEquals(subject, sentMessage.getSubject());
        Assertions.assertEquals(body, sentMessage.getText());
    }



    @AfterEach
    public void destroyAfterEach(){
        System.out.println("Destroy your data after each test");
    }

    @AfterAll
    public static void destroy(){
        System.out.println("Destroy your data after all tests");
    }
}
