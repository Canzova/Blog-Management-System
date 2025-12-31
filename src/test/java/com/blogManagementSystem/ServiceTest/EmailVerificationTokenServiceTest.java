package com.blogManagementSystem.ServiceTest;

import com.blogManagementSystem.entity.EmailVerificationToken;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.repository.EmailVerificationTokenRepository;
import com.blogManagementSystem.repository.UserRepository;
import com.blogManagementSystem.service.EmailService;
import com.blogManagementSystem.service.EmailVerificationTokenService;
import com.blogManagementSystem.service.EmailVerificationTokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class EmailVerificationTokenServiceTest {
    @Mock
    private  EmailService emailService;

    @Mock
    private  EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private  UserRepository userRepository;

    @InjectMocks
    private EmailVerificationTokenServiceImpl emailVerificationTokenService;

    private static User user;
    private static String body = "This is the body of mail.";
    private static String subject = "This is the subject of the mail";

    @BeforeAll
    public static void init(){
        user  = new User();
        user.setUserId(1L);
        user.setUserEmail("nihal.singh@gmail.com");
        user.setFirstName("Nihal");
        user.setLastName("Singh");

    }

    @Test
    public void generateVerificationToken_shouldGenerateTokenAndSendEmailNotification(){

        emailVerificationTokenService.generateVerificationToken(user);

        ArgumentCaptor<EmailVerificationToken> captorEmailVerificationToken =
                ArgumentCaptor.forClass(EmailVerificationToken.class);

        Mockito.verify(emailVerificationTokenRepository).save(
                captorEmailVerificationToken.capture());

        Assertions.assertEquals(captorEmailVerificationToken.getValue().getUser(), user);

        ArgumentCaptor<String> captureEmailNotificationDetails =
                ArgumentCaptor.forClass(String.class);

        Mockito.verify(emailService, Mockito.times(1)).
                sendEmailNotification(any()
                        ,captureEmailNotificationDetails.capture()
                        ,any());

//        ArgumentCaptor<String> captureEmailNotificationDetails =
        Assertions.assertEquals(user.getUserEmail(), captureEmailNotificationDetails.getValue());
    }

}
