package com.dihson103.onlinelearning.services;

import com.dihson103.onlinelearning.dto.user.UserRequest;
import com.dihson103.onlinelearning.dto.user.UserResponse;
import com.dihson103.onlinelearning.entities.UserEntity;
import com.dihson103.onlinelearning.repositories.UserRepository;
import com.dihson103.onlinelearning.services.impl.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.dihson103.onlinelearning.entities.Role.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService underTest;
    @Captor
    private ArgumentCaptor<UserEntity> userCaptor;
    private List<UserEntity> list;

    @BeforeEach
    void setUp() {
        UserEntity user = UserEntity.builder()
                .email("dinhson1032001@gmail.com")
                .username("dinhson103")
                .password("12345")
                .address("ha noi")
                .dob(new Date())
                .phone("0976099351")
                .role(USER)
                .status(true)
                .build();

        UserEntity user2 = UserEntity.builder()
                .email("dinhson@gmail.com")
                .username("son103")
                .password("12345")
                .address("ha noi")
                .dob(new Date())
                .phone("0976099351")
                .role(USER)
                .status(false)
                .build();

        list = List.of(user, user2);
    }

    @Test
    void getUserByUsername() {
        //given
        String username = "dinhson103";
        UserEntity user = UserEntity.builder()
                .email("dinhson1032001@gmail.com")
                .username(username)
                .password("12345")
                .address("ha noi")
                .dob(new Date())
                .phone("0976099351")
                .role(USER)
                .status(true)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserResponse userResponseMock = UserResponse.builder().username(username).build();
        when(modelMapper.map(user, UserResponse.class)).thenReturn(userResponseMock);

        //when
        UserResponse userResponse = underTest.getUserByUsername(username);

        //then
        assertEquals(username, userResponse.getUsername(), "Username should be same.");

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void canNotGetUserByUsername(){
        //given
        String username = "dihson103";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> underTest.getUserByUsername(username)
        );

        //then
        assertEquals(exception.getMessage(), "Can not find user has username: " + username);

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getUsers() {
        //given
        when(userRepository.findAllByStatusIsTrue()).thenReturn(list);

        //when
        List<UserResponse> users =  underTest.getUsers();

        //then
        assertFalse(users.isEmpty());
        assertEquals(list.size(), users.size());

        verify(userRepository, times(1)).findAllByStatusIsTrue();
    }

    @Test
    void canNotGetUsers(){
        //given
        when(userRepository.findAllByStatusIsTrue()).thenReturn(new ArrayList<>());

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            underTest.getUsers();
        });

        //then
        verify(userRepository, times(1)).findAllByStatusIsTrue();
    }

    @Test
    void register() {
        // Given
        String username = "dihson103";
        String email = "dinhson@gmail.com";
        UserRequest userRequest = UserRequest.builder()
                .address("Ha noi")
                .phone("0976099351")
                .email(email)
                .username(username)
                .password("123456")
                .dob(new Date())
                .build();
        UserEntity userMock = UserEntity.builder().username(username).email(email).build();

        lenient().when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        lenient().when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);

        lenient().when(userRepository.save(userCaptor.capture())).thenReturn(userMock);

        UserResponse userResponseMock = UserResponse.builder().username(username).build();
        lenient().when(modelMapper.map(userRequest, UserEntity.class)).thenReturn(userMock);

        // When
        underTest.register(userRequest);

        // Then
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserEntity userCaptured = userCaptor.getValue();

        assertEquals(username, userCaptured.getUsername());
        assertEquals(email, userCaptured.getEmail());
    }

    @Test
    void register_duplicateUsername_throwException(){
        //given
        String username = "dinhson103";
        String email = "dinhson@gmail.com";
        UserRequest userRequest = UserRequest.builder()
                .address("Ha noi")
                .phone("0976099351")
                .email(email)
                .username(username)
                .password("123456")
                .dob(new Date())
                .build();
        UserEntity userMock = UserEntity.builder().username(username).email(email).build();
        lenient().when(userRepository.findByUsername(username)).thenReturn(Optional.of(userMock));

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> underTest.register(userRequest));

        //then
        assertEquals(exception.getMessage(), "Username is already exist.");
    }

    @Test
    void register_duplicateEmail_throwException(){
        //given
        String username = "dinhson103";
        String email = "dinhson@gmail.com";
        UserRequest userRequest = UserRequest.builder()
                .address("Ha noi")
                .phone("0976099351")
                .email(email)
                .username(username)
                .password("123456")
                .dob(new Date())
                .build();
        UserEntity userMock = UserEntity.builder().username(username).email(email).build();
        lenient().when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(userMock));

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> underTest.register(userRequest));

        //then
        assertEquals(exception.getMessage(), "Email is already exist.");
    }


    @Test
    @Disabled
    void createUser() {

    }

    @Test
    @Disabled
    void deleteUser() {
    }



    @Test
    @Disabled
    void updateUser() {
    }

    @Test
    @Disabled
    void updateUserWithRole() {
    }
}