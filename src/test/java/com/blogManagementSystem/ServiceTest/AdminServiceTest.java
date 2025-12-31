package com.blogManagementSystem.ServiceTest;

import com.blogManagementSystem.dto.DeletedUserDTO;
import com.blogManagementSystem.entity.User;
import com.blogManagementSystem.exception.ResourceNotFoundException;
import com.blogManagementSystem.repository.UserRepository;
import com.blogManagementSystem.service.AdminServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private static Long userId = null;
    private static User user = null;

    @BeforeAll
    public static void init(){
        System.out.println("Initialize your data before all test");
        userId = 1L;

        user  = new User();
        user.setUserId(userId);
        user.setUserEmail("nihal.singh@gmail.com");
        user.setFirstName("Nihal");
        user.setLastName("Singh");
    }

    @BeforeEach
    public void initBeforeEach(){
        System.out.println("Initialize something before each test");
    }


    @Test
    public void deleteUserByUserId_shouldDeleteUser(){

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        // This is optional, Mockito does NOTHING by default when a void method is called. No exception, no side effect, no return value.
        Mockito.doNothing().when(userRepository).delete(user);
        Mockito.when(modelMapper.map(user, DeletedUserDTO.class)).thenReturn(new DeletedUserDTO(1L, "nihal.singh@gmail.com"));

        DeletedUserDTO deletedUserDTO = adminService.deleteUserByUserId(userId);

        Assertions.assertEquals(user.getUserId(), deletedUserDTO.getUserId());
        Assertions.assertEquals(user.getUserEmail(), deletedUserDTO.getUserEmail());
    }

    @Test
    public void deleteUserByUserId_shouldThrowResourceNotFoundException(){
        ResourceNotFoundException resourceNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class,
                ()-> adminService.deleteUserByUserId(2L));
        Assertions.assertEquals("User not found with id " + 2, resourceNotFoundException.getMessage());

        Mockito.verify(userRepository, Mockito.never()).delete(any(User.class));
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
