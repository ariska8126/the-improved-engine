/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.resetpasswordcontroller;

import com.hadirapp.HadirAuth.entity.Division;
import com.hadirapp.HadirAuth.entity.Role;
import com.hadirapp.HadirAuth.entity.Users;
import com.hadirapp.HadirAuth.mail.SpringMailServices;
import com.hadirapp.HadirAuth.resetpasswordimplement.PasswordResetServiceImplement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author herli
 */
@RestController
public class PasswordForgotController {

    @Autowired
    private PasswordResetServiceImplement prsi;

    @Autowired
    private SpringMailServices serviceMail;

    @PostMapping("/api/auth/resetpassword/request")
    public String test(@RequestBody Map<String, ?> input) {

//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        System.out.println("reset password running");

        String id = (String) input.get("id");
        System.out.println("validasi username running");
        System.out.println("username: " + id);

        int exists = prsi.ifExists(id);
        System.out.println("exists reply: " + exists);

        if (exists == 0) {
            return "email tidak di temukan";
        } else {
            Users user = new Users();
            user = prsi.findByEmail(id);

            String userId = user.getUserId(); //for safe update
            String userFullname = user.getUserFullname(); //for safe update
            String userEmail = user.getUserEmail(); //for safe update
            String userPassword = user.getUserPassword(); //for safe update
            String userActive = user.getUserActive(); //for safe update

            String uuid = UUID.randomUUID().toString(); //define uuid
            System.out.println("uuid: " + uuid);

            String uuid6 = uuid.substring(0, 6); //get 6 digit uid
            System.out.println("uuid 6 digit: " + uuid6);

//            String uuid6Bcrypt = encoder.encode(uuid6); //encrypt 
//            System.out.println("uuid6Bcrypt: "+uuid6Bcrypt);
//            boolean a = encoder.matches(uuid6, uuid6); //matching value before after encrypt
//            System.out.println("status: "+a);
            String userUnixcodeValue = uuid6; // for safe update

            Date userUnixcodeDate = new Date(); // for safe update

            String userPhoto = user.getUserPhoto(); //for safe update
            int roleID = user.getRoleId().getRoleId(); //for safe update
            int divisionId = user.getDivisionId().getDivisionId(); //for safe update

            Users updateUuid = new Users(userId, userFullname, userEmail,
                    userPassword, userActive, userUnixcodeValue,
                    userUnixcodeDate, userPhoto, new Role(roleID),
                    new Division(divisionId));

            prsi.save(updateUuid);

            System.out.println("update save uuid berhasil");

            //mail
            String tt = "Password Reset";
            String content = "Use code: " + uuid6 + ", or click on button bellow";
            String link = "http://localhost:8082/api/auth/resetpassword/request/" + userUnixcodeValue;

            System.out.println("send mail running");

            Map<String, Object> model = new HashMap<>();
            model.put("title", tt);
            model.put("name", userFullname);
            model.put("messg", content);
            model.put("tiketID", link);

            serviceMail.sendMail(model, tt, userEmail);
            return "success";
        }

    }

    @RequestMapping(value = "/api/auth/resetpassword/request/{id}", method = RequestMethod.GET)
    public String verifyUUID(@PathVariable("id") String id) {

        System.out.println("UUID: " + id);

        int cekUUID = prsi.findUIID(id);
        System.out.println("uuid check: " + cekUUID);

        if (cekUUID < 1) {
            System.out.println("uuid not found");
            return "uuid not found";
        } else {
            System.out.println("uuid ditemukan");
            Users user = prsi.findByUIID(id);
            System.out.println("user: " + user.getUserEmail());
            Date expiredUuid = user.getUserUnixcodeDate();
            System.out.println("expired uuid date: " + expiredUuid);
            Date now = new Date();
            System.out.println("now: " + now);
            
            long difference_in_time = now.getTime() - expiredUuid.getTime();
            long difference_in_minutes = TimeUnit.MILLISECONDS.toMinutes(difference_in_time);
            System.out.println("seleisih waktu: "+difference_in_minutes);
            
            if(difference_in_minutes > 30){
                System.out.println("uuid expired");
                return "uuid expired";
            } else{
                System.out.println("uuid availabe");
                return "uuid available to change password";
            }
            
//            if (now.compareTo(expiredUuid) > 0) {
//                System.out.println("now lebih besar");
//                System.out.println("uuid expired");
//                return "uuid expired";
//            } else {
//                System.out.println("uuid available to change password");
//            }

        }

//        return "succes";
    }

    @PostMapping("/api/auth/resetpassword/savenewpassword")
    public String saveNewpassword(@RequestBody Map<String, ?> input) {
        
        System.out.println("save new password running");
        String uuid = (String) input.get("uuid");
        String password = (String) input.get("password");

        Users user = prsi.findByUIID(uuid);

        String userId = user.getUserId(); //for safe update
        String userFullname = user.getUserFullname(); //for safe update
        String userEmail = user.getUserEmail(); //for safe update
        String userPassword = password; //update password
        String userActive = user.getUserActive(); //for safe update
        String userUnixcodeValue = uuid; // for safe update
        Date userUnixcodeDate = user.getUserUnixcodeDate(); // for safe update
        String userPhoto = user.getUserPhoto(); //for safe update
        int roleID = user.getRoleId().getRoleId(); //for safe update
        int divisionId = user.getDivisionId().getDivisionId(); //for safe update
        
        Users updatepassword = new Users(userId, userFullname, userEmail,
                    userPassword, userActive, userUnixcodeValue,
                    userUnixcodeDate, userPhoto, new Role(roleID),
                    new Division(divisionId));

            prsi.save(updatepassword);

            System.out.println("save new password berhasil");
        return "save done";
    }

}
