/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.resetpasswordrepository;

import com.hadirapp.HadirAuth.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author creative
 */
@Repository
public interface UserRepository extends JpaRepository<Users, String>{
    @Query(value = "UPDATE users set users.user_token = :token where user_id = :userId", nativeQuery = true)
    public String updateToken(@Param("token") String token, @Param("userId") String userId);
}
