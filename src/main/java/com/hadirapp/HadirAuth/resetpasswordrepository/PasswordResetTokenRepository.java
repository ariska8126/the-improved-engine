/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.resetpasswordrepository;

import com.hadirapp.HadirAuth.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author herli
 */
public interface PasswordResetTokenRepository extends CrudRepository<Users, String>{
    
    @Query(value="SELECT * FROM users WHERE user_email = ?1", nativeQuery = true)
    public Users findByEmail(@Param ("id") String email);
    
    @Query(value="SELECT * FROM users WHERE user_unixcode_value = ?1", nativeQuery = true)
    public Users findUIID(@Param ("id") String uiid);
    
    
    
    
    
}
