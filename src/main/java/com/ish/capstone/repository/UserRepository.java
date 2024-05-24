package com.ish.capstone.repository;

import com.ish.capstone.dto.UserDTO;
import com.ish.capstone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmailId(@Param("email") String email);
    List<UserDTO> getAllUser();
    List<String> getAllAdmin();
    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);
    User findByEmail(String email);
}
