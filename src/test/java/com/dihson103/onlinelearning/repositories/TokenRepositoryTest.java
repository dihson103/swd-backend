package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.Token;
import com.dihson103.onlinelearning.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static com.dihson103.onlinelearning.entities.Role.USER;
import static com.dihson103.onlinelearning.entities.TokenType.BEARER;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    private TokenRepository underTest;

    @Autowired
    private UserRepository userRepository;

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
        userRepository.save(user);

        Token token = Token.builder()
                .token("dinhSonTokenTest")
                .isAccessToken(true)
                .tokenType(BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
        underTest.save(token);
    }

    @Test
    void findAllValidTokenByUser() {
        //given
        String email = "dinhson1032001@gmail.com";
        UserEntity user = userRepository.findByEmail(email).orElseThrow();

        //when
        List<Token> tokens = underTest.FindAllValidTokenByUser(user.getId());

        //then
        assertThat(tokens.isEmpty()).isFalse();
    }

    @Test
    void canNotFindValidTokenByUser(){
        //given

        //when
        List<Token> tokens = underTest.FindAllValidTokenByUser(100);

        //then
        assertThat(tokens.isEmpty()).isTrue();
    }

    @Test
    void findByToken() {
        //given
        String token = "dinhSonTokenTest";

        //when
        Token foundToken = underTest.findByToken(token).orElseThrow();

        //then
        assertThat(foundToken.getToken()).isEqualTo(token);
    }

    @Test
    void canNotFindToken(){
        //given
        String token = "dinhSonTfsdfsdokenTest";

        //when
        Token foundToken = underTest.findByToken(token).orElse(null);

        //then
        assertThat(foundToken).isNull();
    }

    @Test
    void findByUserName() {
        //given
        String username = "dinhson103";

        //when
        List<Token> token = underTest.findByUserName(username);

        //then
        assertThat(token.isEmpty()).isFalse();
    }

    @Test
    void cannotFindByUsername(){
        //given
        String username = "dinhson1032001";

        //when
        List<Token> token = underTest.findByUserName(username);

        //then
        assertThat(token.isEmpty()).isTrue();
    }
}