package com.presidio.hiringchallenge.Interface;

import com.presidio.hiringchallenge.dto.PropertyDto;
import com.presidio.hiringchallenge.response.ApiResponse;
import com.presidio.hiringchallenge.response.PaginationResponse;

public interface PropertyInterface {
public ApiResponse postProperty(PropertyDto propertyDto);
public PaginationResponse getAllProperties(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
public PaginationResponse getSellerProperties(Integer pageNumber,Integer pageSize,String sortBy,String sortDir,String email);
public ApiResponse updateSellerProperty(PropertyDto propertyDto);
public ApiResponse deleteProperty(int id);
public ApiResponse shareDetails(String email,int id);
}
