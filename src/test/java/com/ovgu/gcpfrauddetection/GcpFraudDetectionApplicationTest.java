package com.ovgu.gcpfrauddetection;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ovgu.gcpfrauddetection.controller.FraudDetectionController;

@SpringBootTest
class GcpFraudDetectionApplicationTest {
	
	@Autowired
	private FraudDetectionController fraudController;

	@Test
	//Check the Spring context is creating the controller
	void contextLoads() {
		assertTrue(fraudController != null);
	}

}
