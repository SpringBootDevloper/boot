package com.presidio.hiringchallenge.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
	 int status;
	 String message;
	 String errorMessage;
	 Object data;
}
