package com.ovgu.gcpfrauddetection.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ovgu.gcpfrauddetection.dto.TransactionDto;
import com.ovgu.gcpfrauddetection.entity.Transaction;
import com.ovgu.gcpfrauddetection.service.TransactionValidationService;

@WebMvcTest(controllers = FraudDetectionController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class FraudDetectionControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean(name = "fraud")
	private TransactionValidationService transValidateService;
	
	@InjectMocks
    FraudDetectionController fraudController;
	
	
	//Check the endpoint returns success and true
	@Test
	void testFraudDetectionEndpointStatus() throws Exception{
		
		Transaction trans = new Transaction(99.99, "2703186189652095", "2019-01-01 00:00:18", 36.0, 36.0);
		
		when(this.transValidateService.transform(ArgumentMatchers.any(TransactionDto.class))).thenReturn(trans);
		
		when(this.transValidateService.isTransactionValid(ArgumentMatchers.any(Transaction.class))).thenReturn(true);

		MvcResult result = this.mockMvc.perform(post("/api/isTransactionValid")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"dateTime\":\"2019-01-01 00:00:18\",\"cardNo\":\"2703186189652095\",\"amount\":99.99,\"latitude\":36.0,\"longitude\":36.0}")).andReturn();
		
		assertEquals(200, result.getResponse().getStatus());
		assertEquals("true", result.getResponse().getContentAsString());

	}
	
	
	//Check the endpoint returns false
	@Test
	void testFraudDetectionEndpointStatusFalse() throws Exception{
		
		Transaction trans = new Transaction(99.99, "2703186189652095", "2019-01-01 00:00:18", 36.0, 36.0);
		
		when(this.transValidateService.transform(ArgumentMatchers.any(TransactionDto.class))).thenReturn(trans);
		
		when(this.transValidateService.isTransactionValid(ArgumentMatchers.any(Transaction.class))).thenReturn(false);

		MvcResult result = this.mockMvc.perform(post("/api/isTransactionValid")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"dateTime\":\"2019-01-01 00:00:18\",\"cardNo\":\"2703186189652095\",\"amount\":99.99,\"latitude\":36.0,\"longitude\":36.0}")).andReturn();
		
	
		assertEquals(200, result.getResponse().getStatus());
		assertEquals("false", result.getResponse().getContentAsString());

	}
	
	/*
	 * Convert object to JSON string.
	 */
	public static String asJsonString(final Object obj) {
	    try {
	    	String jsonString = new ObjectMapper().writeValueAsString(obj);
	        return jsonString;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	

}
