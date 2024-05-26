package com.presidio.hiringchallenge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.presidio.hiringchallenge.entity.Users;

public interface UserRepo extends JpaRepository<Users,String>{
	  public Users findByEmail(@Param("email") String email);
}
