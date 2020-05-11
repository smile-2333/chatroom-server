package org.hj.chatroomserver.repository;

import org.hj.chatroomserver.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
