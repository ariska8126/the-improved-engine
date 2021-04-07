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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequestMapping("/api/auth")
@Api(tags = "Forgot Password")
public class PasswordForgotController {

    @Autowired
    private PasswordResetServiceImplement prsi;

    @Autowired
    private SpringMailServices serviceMail;

    @PostMapping("/resetpassword/request")
    @ApiOperation(value = "reqeust forgot password by email")
    public String test(@RequestBody Map<String, ?> input) {

        JSONObject jSONObject = new JSONObject();
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        System.out.println("reset password running");

        String id = (String) input.get("id");
        String baseUrl = (String) input.get("baseUrl");
        System.out.println("validasi username running");
        System.out.println("username: " + id);

        int exists = prsi.ifExists(id);
        System.out.println("exists reply: " + exists);

        if (exists == 0) {
            jSONObject.put("status", "false");
            jSONObject.put("description", "email not found");

            return jSONObject.toJSONString();
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
            String link = baseUrl + "/" + userUnixcodeValue;

            System.out.println("send mail running");

            Map<String, Object> model = new HashMap<>();
            model.put("title", tt);
            model.put("name", userFullname);
            model.put("messg", content);
            model.put("tiketID", link);

            serviceMail.sendMail(model, tt, userEmail);
            jSONObject.put("status", "true");
            jSONObject.put("description", "check your email");

            return jSONObject.toJSONString();
        }

    }

    @RequestMapping(value = "/resetpassword/request/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "validate uuid")
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
            System.out.println("seleisih waktu: " + difference_in_minutes);

            if (difference_in_minutes > 30) {
                System.out.println("uuid expired");
                return "uuid expired";
            } else {
                System.out.println("uuid availabe");
                return "uuid available to change password";
            }

        }

    }

    @PostMapping("/resetpassword/savenewpassword")
    @ApiOperation(value = "save new password")
    public String saveNewpassword(@RequestBody Map<String, ?> input) {
        String uuid = (String) input.get("uuid");
        String password = (String) input.get("password");

        JSONObject jSONObject = new JSONObject();
        //check uuid
        int cekUUID = prsi.findUIID(uuid);
        System.out.println("uuid check: " + cekUUID);

        if (cekUUID < 1) {
            System.out.println("uuid not found");
            jSONObject.put("status", "false");
            jSONObject.put("description", "uid not found");

            return jSONObject.toJSONString();

        }

        System.out.println("uuid ditemukan");
        Users user = prsi.findByUIID(uuid);
        System.out.println("user: " + user.getUserEmail());
        Date expiredUuid = user.getUserUnixcodeDate();
        System.out.println("expired uuid date: " + expiredUuid);
        Date now = new Date();
        System.out.println("now: " + now);

        long difference_in_time = now.getTime() - expiredUuid.getTime();
        long difference_in_minutes = TimeUnit.MILLISECONDS.toMinutes(difference_in_time);
        System.out.println("seleisih waktu: " + difference_in_minutes);

        if (difference_in_minutes > 30) {
            System.out.println("uuid expired");
            jSONObject.put("status", "false");
            jSONObject.put("description", "uid expired");

            return jSONObject.toJSONString();
        }

        //save new password
        System.out.println("save new password running");
        
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        String userId = user.getUserId(); //for safe update
        String userFullname = user.getUserFullname(); //for safe update
        String userEmail = user.getUserEmail(); //for safe update
        String userPassword = encoder.encode(password); //update password
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
        System.out.println("uuid expired");
        jSONObject.put("status", "true");
        jSONObject.put("description", "update success");

        return jSONObject.toJSONString();
    }

}
