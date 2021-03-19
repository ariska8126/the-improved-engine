/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.resetpasswordinterface;

import com.hadirapp.HadirAuth.entity.Users;

/**
 *
 * @author herli
 */
public interface PasswordResetUserInterface {
    
    public Users findByEmail(String email);
    
    void save(Users users);
    
    public Users findUIID(String uiid);
}
