package com.example.bredexTest.userLogout.repository;

import com.example.bredexTest.userLogout.model.UserLogout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogoutRepository extends JpaRepository<UserLogout, Long> {
}
