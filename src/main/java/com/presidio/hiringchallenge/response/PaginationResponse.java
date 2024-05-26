package com.presidio.hiringchallenge.response;

import java.util.List;

import com.presidio.hiringchallenge.dto.PropertyDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse {
	private List<PropertyDto> content;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;	
	private boolean lastPage;
}
