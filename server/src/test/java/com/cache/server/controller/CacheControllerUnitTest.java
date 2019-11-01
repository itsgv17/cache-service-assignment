package com.cache.server.controller;

import static org.mockito.Mockito.lenient;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.cache.server.exception.HttpExceptionHandler;
import com.cache.server.model.CacheResp;
import com.cache.server.service.CacheService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(MockitoJUnitRunner.class)
public class CacheControllerUnitTest {

	@Mock
	private CacheService cacheService;

	@InjectMocks
	private CacheController cacheController;

	@InjectMocks
	private HttpExceptionHandler httpExceptionHandler;

	@Before
	public void initialiseRestAssuredMockMvcStandalone() {
		RestAssuredMockMvc.standaloneSetup(cacheController, httpExceptionHandler);
	}

	@Test
	public void givenValidRequestHeadersWhenGetThenRespondWithStatusOk() {

		lenient().when(this.cacheService.get("Registration", "/register")).thenReturn(new CacheResp("Ok"));
		 
		Map<String, String> headers= new HashMap<>();
		headers.put("collection", "Registration");
		headers.put("key", "/register");
		
		RestAssuredMockMvc.given().accept("application/json").contentType("application/json;charset=UTF-8").headers(headers)
		.when().get("/get").then().log().ifValidationFails()
		.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void givenValidRequestHeadersAndBodyWhenPutThenRespondWithStatusOk() {

		lenient().when(this.cacheService.put("Registration", "/register",new CacheResp("Ok"))).thenReturn(true);
		 
		Map<String, String> headers= new HashMap<>();
		headers.put("collection", "Registration");
		headers.put("key", "/register");
		
		RestAssuredMockMvc.given().accept("application/json").contentType("application/json;charset=UTF-8").headers(headers).body(new CacheResp("Ok"))
		.when().put("/put").then().log().ifValidationFails()
		.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void givenInvalidHttpMethodWhenPutThenRespondWithStatus405MethodNotAllowed() {

		lenient().when(this.cacheService.put("Registration", "/register",new CacheResp("Ok"))).thenReturn(true);
		 
		Map<String, String> headers= new HashMap<>();
		headers.put("collection", "Registration");
		headers.put("key", "/register");
		
		RestAssuredMockMvc.given().accept("application/json").contentType("application/json;charset=UTF-8").headers(headers).body(new CacheResp("Ok"))
		.when().get("/put").then().log().ifValidationFails()
		.statusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
	}
	
	
	@Test
	public void givenValidRequestHeadersWhenDeleteThenRespondWithStatusOk() {

		lenient().when(this.cacheService.put("Registration", "/register",new CacheResp("Ok"))).thenReturn(true);
		 
		Map<String, String> headers= new HashMap<>();
		headers.put("collection", "Registration");
		headers.put("key", "/register");
		
		RestAssuredMockMvc.given().accept("application/json").contentType("application/json;charset=UTF-8").headers(headers)
		.when().delete("/delete").then().log().ifValidationFails()
		.statusCode(HttpStatus.OK.value());
	}
}
