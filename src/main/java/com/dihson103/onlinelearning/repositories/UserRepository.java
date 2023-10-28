package com.dihson103.onlinelearning.repositories;

import com.dihson103.onlinelearning.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findAllByStatusIsTrue();

    Optional<UserEntity> findFirstByIdAndStatusIsTrue(Integer userId);

    Optional<UserEntity> findByUsernameAndStatusIsTrue(String username);

    Optional<UserEntity> findByEmailAndStatusIsTrue(String email);

    Optional<UserEntity> findByUsernameAndPassword(String username, String password);
}
