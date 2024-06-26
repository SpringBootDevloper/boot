package com.presidio.hiringchallenge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.presidio.hiringchallenge.entity.Property;

public interface PropertyRepo extends JpaRepository<Property,Integer>{
	@Query(value="select property from Property property")
	Page<Property> getPageableData(Pageable page);
	@Query(value="select * from Property where posted_by=?1",nativeQuery = true)
	Page<Property> getSellerProperties(Pageable page,String email);
}
