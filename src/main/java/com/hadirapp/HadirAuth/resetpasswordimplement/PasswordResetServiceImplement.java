/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.resetpasswordimplement;

import com.hadirapp.HadirAuth.entity.Users;
import com.hadirapp.HadirAuth.resetpasswordinterface.PasswordResetUserInterface;
import com.hadirapp.HadirAuth.resetpasswordrepository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author herli
 */
@Service
public class PasswordResetServiceImplement implements PasswordResetUserInterface{
    
    @Autowired
    private PasswordResetTokenRepository repository;

    @Override
    public Users findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public void save(Users users) {
        repository.save(users);
    }

    @Override
    public Users findUIID(String uiid) {
        return repository.findUIID(uiid);
    }
    
}
