/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.login;

import com.hadirapp.HadirAuth.entity.Users;
import com.hadirapp.HadirAuth.jwtutil.JwtUtil;
import com.hadirapp.HadirAuth.resetpasswordimplement.PasswordResetServiceImplement;
import java.util.Date;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author herli
 */
@RestController
public class LoginController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private MyUserDetailsService userDetailsService;
    
    @Autowired
    private JwtUtil jwtTokenUtil;
    
    @Autowired
    private PasswordResetServiceImplement passwordResetServiceImplement;
    
    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }
    
    @RequestMapping(value="/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        
        System.out.println("running login controller");
        try{
            authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), 
                    authenticationRequest.getPassword())
            );
        }catch(BadCredentialsException e){
            throw new Exception("incorect username or password"+e);
        }
        
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        
        System.out.println("username to insert token: "+userDetails.getUsername());
        
        String uname = userDetails.getUsername();
        
        Users users = new Users();
        users = passwordResetServiceImplement.findByEmail(uname);
        
        String id = users.getUserId();
        String email = users.getUserEmail();
        int roleId = users.getRoleId().getRoleId();
        String roleName = users.getRoleId().getRoleName();
        
        System.out.println("id: "+id);
        System.out.println("email: "+email);
        System.out.println("role id: "+roleId);
        System.out.println("role name: "+roleName);
        
        JSONArray data = new JSONArray();
        JSONObject role = new JSONObject();
        role.put("roleID", roleId);
        role.put("roleName", roleName);
        
        JSONObject dataContent = new JSONObject();
        dataContent.put("email", email);
        dataContent.put("id", id);
        dataContent.put("role", role);
        
//        data.add(dataContent);
        System.out.println("data json: "+dataContent);
        
        
        final String jwt = jwtTokenUtil.generateToken(userDetails, dataContent);
        
        Date exp = jwtTokenUtil.extractExpiration(jwt);
        System.out.println("expiration date: "+exp);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    
}
