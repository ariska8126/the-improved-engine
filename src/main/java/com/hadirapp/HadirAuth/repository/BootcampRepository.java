/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadirapp.HadirAuth.repository;

import com.hadirapp.HadirAuth.entity.Bootcamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author herli
 */
@Repository
public interface BootcampRepository extends JpaRepository<Bootcamp, String>{
    
    @Query(value = "SELECT b.bootcamp_id FROM bootcamp b JOIN bootcamp_detail bd ON b.bootcamp_id = bd.bootcamp_id JOIN users u ON bd.user_id = u.user_id WHERE u.user_id = ?1", nativeQuery = true)
    public String getBootcampId(@Param ("id") String id);
    
    @Query(value = "SELECT b.bootcamp_name FROM bootcamp b JOIN bootcamp_detail bd ON b.bootcamp_id = bd.bootcamp_id JOIN users u ON bd.user_id = u.user_id WHERE u.user_id = ?1", nativeQuery = true)
    public String getBootcampName(@Param ("id") String id);
}
