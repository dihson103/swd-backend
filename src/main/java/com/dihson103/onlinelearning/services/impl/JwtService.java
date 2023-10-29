package com.dihson103.onlinelearning.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dihson103.onlinelearning.entities.UserEntity;
import com.dihson103.onlinelearning.services.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService implements IJwtService {

    @Value("${application.config.security.key}")
    private String security_key;

    @Override
    public String generateToken(UserEntity user, Collection<SimpleGrantedAuthority> authorities) {
        Algorithm algorithm = Algorithm.HMAC256(security_key.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 50 * 60 *1000 * 10))
                .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
    }

    @Override
    public String generateRefreshToken(UserEntity user, Collection<SimpleGrantedAuthority> authorities) {
        Algorithm algorithm = Algorithm.HMAC256(security_key.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 70 * 60 *1000 * 10))
                .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .sign(algorithm);
    }

}
