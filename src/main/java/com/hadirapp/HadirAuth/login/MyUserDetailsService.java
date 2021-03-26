/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.login;

import com.hadirapp.HadirAuth.entity.Users;
import com.hadirapp.HadirAuth.resetpasswordimplement.PasswordResetServiceImplement;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

/**
 *
 * @author herli
 */
@Controller
public class MyUserDetailsService implements UserDetailsService{
    
    @Autowired
    private PasswordResetServiceImplement userImpl;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        
        System.out.println("load by username: "+userName);
        
        ThisUser user = validasi(userName);
        
        User.UserBuilder builder = null;
        
        if(user != null){
            builder = org.springframework.security.core.userdetails.User.withUsername(userName);
            builder.password(user.getPassword());
            builder.roles(user.getRoles());
//            
//            System.out.println("username after validation: "+user.getUsername());
//            System.out.println("password after validation: "+user.getPassword());
//            System.out.println("roles after validation: "+user.getRoles());
        }else{
            JSONObject jsonObject = new JSONObject();
            System.out.println("user salah");
        }
        
        return builder.build();
    }

    private ThisUser validasi(String userName) {
        System.out.println("cek username di databse: "+userName);
        
        String password = null;
        String role = null;
        String email = null;
        
        Users us = userImpl.findByEmail(userName);
        
        if(us != null){
            email = us.getUserEmail();
            password = us.getUserPassword();
            role = us.getRoleId().getRoleName();    
//            
//            System.out.println("username: "+userName);
//            System.out.println("password: "+password);
//            System.out.println("role: "+role);
            
        }else{
            System.out.println("access denied");
        }
        
        if(userName.equalsIgnoreCase(email)){
            return new ThisUser(userName, password, role);
        }
        
        return null;
    }
    
}
