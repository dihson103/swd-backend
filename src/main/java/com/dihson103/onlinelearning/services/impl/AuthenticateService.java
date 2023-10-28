package com.dihson103.onlinelearning.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dihson103.onlinelearning.dto.auth.AuthenticationRequest;
import com.dihson103.onlinelearning.dto.auth.AuthenticationResponse;
import com.dihson103.onlinelearning.dto.user.UserResponse;
import com.dihson103.onlinelearning.entities.Role;
import com.dihson103.onlinelearning.entities.Token;
import com.dihson103.onlinelearning.entities.TokenType;
import com.dihson103.onlinelearning.entities.UserEntity;
import com.dihson103.onlinelearning.exceptions.WrongTokenException;
import com.dihson103.onlinelearning.repositories.TokenRepository;
import com.dihson103.onlinelearning.repositories.UserRepository;
import com.dihson103.onlinelearning.services.IAuthenticateService;
import com.dihson103.onlinelearning.services.IJwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AuthenticateService implements IAuthenticateService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final IJwtService jwtService;
    private final ModelMapper modelMapper;
    private final TokenRepository tokenRepository;
    @Value("${application.config.security.key}")
    private String security_key;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
           authenticationRequest.getEmail(),
           authenticationRequest.getPassword()
        ));
        UserEntity user = userRepository.findByEmailAndStatusIsTrue(authenticationRequest.getEmail()).orElseThrow();

        return createTokenHandle(user);
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new WrongTokenException("Token is wrong.");
        }
        refreshToken = authHeader.substring(7);
        Algorithm algorithm = Algorithm.HMAC256(security_key.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        username = decodedJWT.getSubject();

        UserEntity user = userRepository.findByUsernameAndStatusIsTrue(username).orElseThrow();

        return createTokenHandle(user);
    }

    private AuthenticationResponse createTokenHandle(UserEntity user){
        Role role = user.getRole();

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));

        var jwtToken = jwtService.generateToken(user, authorities);
        var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);

        revokedAllTokenBefore(user);
        saveToken(user, jwtToken, true);
        saveToken(user, jwtRefreshToken, false);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(jwtRefreshToken)
                .userResponse(modelMapper.map(user, UserResponse.class))
                .build();
    }

    private void saveToken(UserEntity user, String jwtToken, Boolean isAccessToken){
        var token = Token.builder()
                .token(jwtToken)
                .user(user)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .isAccessToken(isAccessToken)
                .build();
        tokenRepository.save(token);
    }

    private void revokedAllTokenBefore(UserEntity user){
        var validUserTokens = tokenRepository.FindAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
