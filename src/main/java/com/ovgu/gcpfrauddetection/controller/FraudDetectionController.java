package com.ovgu.gcpfrauddetection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ovgu.gcpfrauddetection.dto.TransactionDto;
import com.ovgu.gcpfrauddetection.entity.Transaction;
import com.ovgu.gcpfrauddetection.service.TransactionValidationService;

@Controller
public class FraudDetectionController {
	
	@Autowired
	private TransactionValidationService transValidateService;
	
	@RequestMapping(
			  value = "/api/isTransactionValid",
			  method = RequestMethod.POST,
			  consumes = "application/json",
			  produces = "application/json"
			)
	@ResponseBody
	public String isAFraudTransaction(@RequestBody TransactionDto transactionDto) {
		Transaction transaction = transValidateService.transform(transactionDto);
		boolean response = transValidateService.isTransactionValid(transaction);
		return Boolean.toString(response);
	}

}
