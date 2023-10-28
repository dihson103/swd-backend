package com.dihson103.onlinelearning.services;

import com.dihson103.onlinelearning.entities.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public interface IJwtService {

    String generateToken(UserEntity user, Collection<SimpleGrantedAuthority> authorities);
    String generateRefreshToken(UserEntity user, Collection<SimpleGrantedAuthority> authorities);

}
