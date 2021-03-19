/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.resetpasswordcontroller;

import com.hadirapp.HadirAuth.entity.Users;
import com.hadirapp.HadirAuth.resetpasswordimplement.PasswordResetServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author herli
 */
@RestController
public class PasswordForgotController {
    
    @Autowired
    private PasswordResetServiceImplement prsi;
    
//    @RequestMapping(value="/api/auth/resetpassword/request/{id}", method = RequestMethod.GET) //use this for get id
//    public String resetPassword(@PathVariable("id") String id){
    
    @ResponseBody
    @RequestMapping(value="/api/auth/resetpassword/request", method = RequestMethod.GET)
    public String resetPassword(){
        System.out.println("reset password running");
        System.out.println("validasi username running");
        
//        Users user = new Users();
//        user = prsi.findByEmail(id);
        
//        String username = null;
//        
//        username = user.getUserEmail();
//        
//        System.out.println("username: "+user.getUserEmail());
//        
//        if(username == null){
//            return "email tidak di temukan";
//        } else{
//            System.out.println("username ditemukan: "+username);
//        }
        
        
        
        return "success";
    }
    
    
}
