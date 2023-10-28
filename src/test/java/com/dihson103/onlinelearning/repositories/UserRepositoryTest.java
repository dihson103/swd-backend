package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.dihson103.onlinelearning.entities.Role.USER;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

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
        underTest.save(user);

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
        underTest.save(user2);
    }

    @Test
    void findAllByStatusIsTrue() {
        //given

        //when
        List<UserEntity> users = underTest.findAllByStatusIsTrue();

        //then
        assertThat(users.stream().count()).isEqualTo(1);
    }

    @Test
    void findByIdAndStatusIsTrue() {
        //given
        Integer userId = 1;

        // When
        Optional<UserEntity> optionalUser = underTest.findFirstByIdAndStatusIsTrue(userId);

        // Then
        assertTrue(optionalUser.isPresent(), "User should be present");

        UserEntity user = optionalUser.get();
        assertEquals(userId, user.getId(), "User ID should match");
        assertTrue(user.getStatus(), "User status should be true");
    }

    @Test
    void cannotFindByIdAndStatusIsTrueWithWrongId(){
        //given
        Integer userId = 200;

        //when
        UserEntity user = underTest.findFirstByIdAndStatusIsTrue(userId).orElse(null);

        //then
        assertThat(user).isNull();
    }

    @Test
    void cannotFindByIdAndStatusIsTrueWithStatusIsFalse(){
        //given
        Integer userId = 2;

        //when
        UserEntity user = underTest.findFirstByIdAndStatusIsTrue(userId).orElse(null);

        //then
        assertThat(user).isNull();
    }

    @Test
    void findAllByUsernameAndStatusIsTrue() {
        //given
        String username = "dinhson103";

        //when
        UserEntity user = underTest.findByUsernameAndStatusIsTrue(username).orElseThrow();

        //then
    }

    @Test
    void cannotFindAllByUsernameAndStatusIsTrueWithWrongUsername(){
        //given
        String username = "dinhson103200001";

        //when
        UserEntity user = underTest.findByUsernameAndStatusIsTrue(username).orElse(null);

        //then
        assertThat(user).isNull();
    }

    @Test
    void cannotFindAllByUsernameAndStatusIsTrueWithStatusIsFalse(){
        //given
        String username = "son103";

        //when
        UserEntity user = underTest.findByUsernameAndStatusIsTrue(username).orElse(null);

        //then
        assertThat(user).isNull();
    }

    @Test
    void findByEmailAndStatusIsTrue() {
        //given
        String email = "dinhson1032001@gmail.com";

        //when
        UserEntity user = underTest.findByEmailAndStatusIsTrue(email).orElseThrow();

        //then
    }

    @Test
    void cannotFindByEmailAndStatusIsTrueWithWrongEmail() {
        //given
        String email = "dinhson10dsfs01@gmail.com";

        //when
        UserEntity user = underTest.findByEmailAndStatusIsTrue(email).orElse(null);

        //then
        assertThat(user).isNull();
    }

    @Test
    void cannotFindByEmailAndStatusIsTrueWithWrongStatus() {
        //given
        String email = "dinhson@gmail.com";

        //when
        UserEntity user = underTest.findByEmailAndStatusIsTrue(email).orElse(null);

        //then
        assertThat(user).isNull();
    }

}