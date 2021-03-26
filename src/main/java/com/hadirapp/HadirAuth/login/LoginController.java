/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.login;

import com.hadirapp.HadirAuth.entity.Users;
import com.hadirapp.HadirAuth.jwtfilter.JwtRequestFilter;
import com.hadirapp.HadirAuth.jwtutil.JwtUtil;
import com.hadirapp.HadirAuth.resetpasswordimplement.PasswordResetServiceImplement;

import com.hadirapp.HadirAuth.resetpasswordrepository.UserRepository;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author herli
 */
@RequestMapping("/api/auth")
@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Autowired
    private PasswordResetServiceImplement passwordResetServiceImplement;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MyUserDetailsService MyUserDetailsService;

    @RequestMapping("/checklogin")
    public String hello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nameLogin = auth.getName();
        JSONObject dataContent = new JSONObject();

        dataContent.put("status", "true");
        dataContent.put("description", "login success as " + nameLogin);
        return dataContent.toString();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        JSONObject dataContent = new JSONObject();

        Users returnActiveUser = new Users();

        int returnEmail = userRepository.findIfExistEmail(authenticationRequest.getUsername());
//        System.out.println(returnEmail);

        if (returnEmail == 0) {

            dataContent.put("status", "false");
            dataContent.put("description", "incorrect email");

            return dataContent.toJSONString();
        } else {
            returnActiveUser = userRepository.findByEmail(authenticationRequest.getUsername());

            String userActiveStatus = returnActiveUser.getUserActive();

            if (userActiveStatus.equalsIgnoreCase("false")) {
                dataContent.put("status", "false");
                dataContent.put("description", "your account has been deactivated");

                return dataContent.toJSONString();

            } else {
                try {
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                                    authenticationRequest.getPassword())
                    );
                } catch (BadCredentialsException e) {
                    //throw new Exception("incorect username or password"+e);
                    dataContent.put("status", "false");
                    dataContent.put("description", "incorrect password");

                    return dataContent.toString();
                }
            }
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

//        System.out.println("username to insert token: " + userDetails.getUsername());
        String uname = userDetails.getUsername();

        Users users = new Users();
        users = passwordResetServiceImplement.findByEmail(uname);
        String userToken = users.getUserToken();

        String id = users.getUserId();
        String email = users.getUserEmail();
        int roleId = users.getRoleId().getRoleId();
        String roleName = users.getRoleId().getRoleName();
        String name = users.getUserFullname();

//        System.out.println("data json: " + dataContent);
        jsonObject.put("status", "true");

        dataContent.put("userFullname", name);
        dataContent.put("userEmail", email);
        dataContent.put("userId", id);
        dataContent.put("roleId", roleId);
        dataContent.put("roleName", roleName);
        dataContent.put("divisionId", users.getDivisionId().getDivisionId().toString());
        dataContent.put("divisionName", users.getDivisionId().getDivisionName());

        final String jwt = jwtTokenUtil.generateToken(userDetails, dataContent);
        dataContent.put("userPhoto", users.getUserPhoto());
        //dataContent.put("status", "true");
        jsonArray.add(dataContent);
        jsonObject.put("userToken", jwt);
        jsonObject.put("data", jsonArray);

        users.setUserToken(jwt);
        userRepository.save(users);
//        userRepository.updateToken(jwt, id);
        //return ResponseEntity.ok(new AuthenticationResponse(jwt));
        return jsonObject.toJSONString();
    }

}
