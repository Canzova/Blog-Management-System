package com.blogManagementSystem.ServiceTest;

import com.blogManagementSystem.entity.EmailVerificationToken;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.EmailVerificationTokenException;
import com.blogManagementSystem.repository.EmailVerificationTokenRepository;
import com.blogManagementSystem.repository.UserRepository;
import com.blogManagementSystem.service.EmailService;
import com.blogManagementSystem.service.EmailVerificationTokenService;
import com.blogManagementSystem.service.EmailVerificationTokenServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class EmailVerificationTokenServiceTest {
    @Mock
    private  EmailService emailService;

    @Mock
    private  EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Mock
    private  UserRepository userRepository;

    @Mock
    private EmailVerificationToken emailVerificationToken;

    @InjectMocks
    private EmailVerificationTokenServiceImpl emailVerificationTokenService;

    private static User user;
    private static String body = "This is the body of mail.";
    private static String subject = "This is the subject of the mail";
    private static String token;

    @BeforeAll
    public static void init(){
        user  = new User();
        user.setUserId(1L);
        user.setUserEmail("nihal.singh@gmail.com");
        user.setFirstName("Nihal");
        user.setLastName("Singh");

        token = UUID.randomUUID().toString();
    }

    @BeforeEach
    public void initBeforeEach(){
        emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setEmailVerificationTokenId(1L);
        emailVerificationToken.setUser(user);
        emailVerificationToken.setExpirationTime(LocalTime.now().plusMinutes(15));
        emailVerificationToken.setVerificationToken(token);
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

    @Test
    public void verifyEmailVerificationToken_shouldVerifyToken(){
        Mockito.when(emailVerificationTokenRepository.existsByVerificationToken(token)).thenReturn(true);
        Mockito.when(emailVerificationTokenRepository.findByVerificationToken(token)).thenReturn(emailVerificationToken);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.doNothing().when(emailVerificationTokenRepository).deleteByVerificationToken(token);

        emailVerificationTokenService.verifyEmailVerificationToken(token);
    }

    @Test
    public void verifyEmailVerificationToken_shouldThrowEmailVerificationTokenException(){
        Mockito.when(emailVerificationTokenRepository.existsByVerificationToken(token)).thenReturn(false);

        EmailVerificationTokenException emailVerificationTokenException = Assertions.assertThrows(EmailVerificationTokenException.class,
                ()-> emailVerificationTokenService.verifyEmailVerificationToken(token));

        Mockito.verify(emailVerificationTokenRepository, Mockito.never()).findByVerificationToken(token);
    }

    @Test
    public void verifyEmailVerificationToken_shouldThrowEmailVerificationTokenExceptionWhenTokenIsExpired(){

        emailVerificationToken.setExpirationTime(LocalTime.now().minusMinutes(20));

        Mockito.when(emailVerificationTokenRepository.existsByVerificationToken(token)).thenReturn(true);
        Mockito.when(emailVerificationTokenRepository.findByVerificationToken(token)).thenReturn(emailVerificationToken);

        EmailVerificationTokenException emailVerificationTokenException = Assertions.assertThrows(EmailVerificationTokenException.class,
                ()-> emailVerificationTokenService.verifyEmailVerificationToken(token));

        Mockito.verify(userRepository, Mockito.never()).save(user);
    }
}
