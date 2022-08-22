package com.ovgu.gcpfrauddetection.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ovgu.gcpfrauddetection.entity.Transaction;
import com.ovgu.gcpfrauddetection.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionValidationServiceTest {
	
	@Mock
	private TransactionRepository transactionRepository;
	
	@InjectMocks
	private TransactionValidationService validationService;
	
	/*
	 * Test if the transaction amount is valid when amount is lesser than the threshold. 
	 * invalid transaction = amount > 500
	 * valid transaction = amount <= 500
	 */
	@Test
	void testTransactionAmountIsValidAmountIsLesserThanThreshold() {
		double amount = 100.25;
		assertTrue(validationService.isTransactionAmountValid(amount));
	}
	
	
	/*
	 * Test if the transaction amount is valid when amount is greater than the threshold. 
	 * invalid transaction = amount > 500
	 * valid transaction = amount <= 500
	 */
	@Test
	void testTransactionAmountIsValidWhenAmountIsGreaterThanThreshold() {
		double amount = 600.25;
		assertFalse(validationService.isTransactionAmountValid(amount));
	}
	
	/*
	 * Test if the transaction amount is valid when amount is equal to the threshold. 
	 * invalid transaction = amount > 500
	 * valid transaction = amount <= 500
	 */
	@Test
	void testTransactionAmountIsValidWhenAmountEqualsTheThreshold() {
		double amount = 500;
		assertTrue(validationService.isTransactionAmountValid(amount));
	}

	/*
	 * Test if the total transaction amount exceeds the daily limit when total transaction amount in the db is lesser than the threshold.
	 * total limit = current transaction amount + daily total retrieved from the database.
	 * invalid transaction = total limit > 1000
	 * valid transaction = total limit <= 1000
	 */
	@Test
	void testDailyTransactionLimitIsValidWhenDailyTotalIsLesser() {
		double amount = 200.25;
		String cardNo = "2703186189652095";
		double dailyTotal = 700.00;
		when(transactionRepository.findTodaysTransactionAmountTotal(cardNo)).thenReturn(dailyTotal);
		Transaction trans = new Transaction();
		trans.setAmount(amount);
		trans.setCardNo(cardNo);
		assertTrue(validationService.isDailyTransactionLimitValid(trans));
		//verify mocked method has called once
		verify(transactionRepository, times(1)).findTodaysTransactionAmountTotal(cardNo);
	}
	
	
	/*
	 * Test if the total transaction amount exceeds the daily limit when total transaction amount in the db is null.
	 * total limit = current transaction amount + daily total retrieved from the database.
	 * invalid transaction = total limit > 1000
	 * valid transaction = total limit <= 1000
	 */
	@Test
	void testDailyTransactionLimitIsValidWhenDailyTotalIsNull() {
		double amount = 200.25;
		String cardNo = "2703186189652095";
		when(transactionRepository.findTodaysTransactionAmountTotal(cardNo)).thenReturn(null);
		Transaction trans = new Transaction();
		trans.setAmount(amount);
		trans.setCardNo(cardNo);
		assertTrue(validationService.isDailyTransactionLimitValid(trans));
		//verify mocked method has called once
		verify(transactionRepository, times(1)).findTodaysTransactionAmountTotal(cardNo);
	}
	
	
	/*
	 * Test if the total transaction amount exceeds the daily limit when total transaction amount in the db equal to the threshold.
	 * total limit = current transaction amount + daily total retrieved from the database.
	 * invalid transaction = total limit > 1000
	 * valid transaction = total limit <= 1000
	 */
	@Test
	void testDailyTransactionLimitIsValidWhenDailyTotalEqualsTheThreshold() {
		double amount = 200.25;
		String cardNo = "2703186189652095";
		double dailyTotal = 1000.00;
		when(transactionRepository.findTodaysTransactionAmountTotal(cardNo)).thenReturn(dailyTotal);
		Transaction trans = new Transaction();
		trans.setAmount(amount);
		trans.setCardNo(cardNo);
		assertFalse(validationService.isDailyTransactionLimitValid(trans));
		//verify mocked method has called once
		verify(transactionRepository, times(1)).findTodaysTransactionAmountTotal(cardNo);
	}
	
	
	/*
	 * Test if the daily transaction count exceeds when the count in the db is lesser than the threshold.
	 * invalid transaction = count > 5
	 * valid transaction = count <= 5
	 */
	@Test
	void testDailyTransactionCountWhenCountIsLesser() {
		String cardNo = "2703186189652095";
		when(transactionRepository.findTodaysTransactionCount(cardNo)).thenReturn(4);
		Transaction trans = new Transaction();
		trans.setCardNo(cardNo);
		assertTrue(validationService.isDailyTransactionCountValid(trans));
		//verify mocked method has called once
		verify(transactionRepository, times(1)).findTodaysTransactionCount(cardNo);
	}
	
	
	/*
	 * Test if the daily transaction count exceeds when the count in the db is lesser than the threshold.
	 * invalid transaction = count > 5
	 * valid transaction = count <= 5
	 */
	@Test
	void testDailyTransactionCountWhenThereAreNoTransactionsInTheDB() {
		String cardNo = "2703186189652095";
		when(transactionRepository.findTodaysTransactionCount(cardNo)).thenReturn(0);
		Transaction trans = new Transaction();
		trans.setCardNo(cardNo);
		assertTrue(validationService.isDailyTransactionCountValid(trans));
		//verify mocked method has called once
		verify(transactionRepository, times(1)).findTodaysTransactionCount(cardNo);
	}
	
	
	/*
	 * Test if the daily transaction count exceeds when the count in the db exceeds the threshold.
	 * invalid transaction = count > 5
	 * valid transaction = count <= 5
	 */
	@Test
	void testDailyTransactionCountWhenCountExceeds() {
		String cardNo = "2703186189652095";
		when(transactionRepository.findTodaysTransactionCount(cardNo)).thenReturn(5);
		Transaction trans = new Transaction();
		trans.setCardNo(cardNo);
		assertFalse(validationService.isDailyTransactionCountValid(trans));
		//verify mocked method has called once
		verify(transactionRepository, times(1)).findTodaysTransactionCount(cardNo);
	}
	
	
	/*
	 * Test if the subsequent transaction time difference is less than 5 seconds when there are no transactions in the db.
	 * invalid transaction = subsequent transaction time < 5s
	 * valid transaction = subsequent transaction time > 5s
	 */
	@Test
	void testSubsequentTransactionTimeDifferenceWhenThereAreNoTransactionsInTheDB() {
		String cardNo = "2703186189652095";
		when(transactionRepository.findTimeOfTodaysLastTransaction(cardNo)).thenReturn(null);
		Transaction trans = new Transaction();
		trans.setCardNo(cardNo);
		assertTrue(validationService.isTransactionTimeValid(trans));
		//verify mocked method has called once
		verify(transactionRepository, times(1)).findTimeOfTodaysLastTransaction(cardNo);
	}
	
	
	/*
	 * Test if the subsequent transaction time difference is less than 5 seconds when there are transactions.
	 * invalid transaction = subsequent transaction time < 5s
	 * valid transaction = subsequent transaction time > 5s
	 */
	@Test
	void testSubsequentTransactionTimeDifferenceWhenThereAreTransactionsInTheDB() {
		String cardNo = "2703186189652095";
		String date = "2022-02-27 00:00:18";
		when(transactionRepository.findTimeOfTodaysLastTransaction(cardNo)).thenReturn(date);
		Transaction trans = new Transaction();
		trans.setCardNo(cardNo);
		trans.setDateTime("2022-02-27 00:00:28");
		assertTrue(validationService.isTransactionTimeValid(trans));
		verify(transactionRepository, times(1)).findTimeOfTodaysLastTransaction(cardNo);
	}
	
	
	/*
	 * Test if the subsequent transaction time difference is less than 5 seconds when there are transactions.
	 * invalid transaction = subsequent transaction time < 5s
	 * valid transaction = subsequent transaction time > 5s
	 */
	@Test
	void testSubsequentTransactionTimeDifferenceWhenThereAreTransactionsInTheDBNegative() {
		String cardNo = "2703186189652095";
		String date = "2022-02-27 00:00:59";
		when(transactionRepository.findTimeOfTodaysLastTransaction(cardNo)).thenReturn(date);
		Transaction trans = new Transaction();
		trans.setCardNo(cardNo);
		trans.setDateTime("2022-02-27 00:01:02");
		assertFalse(validationService.isTransactionTimeValid(trans));
		verify(transactionRepository, times(1)).findTimeOfTodaysLastTransaction(cardNo);
	}
	
	
	/*
	 * Test calculating the distance in kilometers for longitude and latitude.
	 */
	@Test
	void testCalculatingDistance() {
		double lat1 = 53.3186;
		double longt1 = -1.6997;
		double lat2 = 53.3205;
		double longt2 = -1.7297;
		assertEquals(2, validationService.calculateDistance(lat1, longt1, lat2, longt2));
	}
	
	/*
	 * Test calculating the distance in kilometers when longitude and latitude are same.
	 */
	@Test
	void testCalculatingDistanceForSameCordinates() {
		double lat1 = 53.3186;
		double longt1 = -1.6997;
		assertEquals(0, validationService.calculateDistance(lat1, longt1, lat1, longt1));
	}
	
	
	/*
	 * Test subsequent transaction locations are inside the distance threshold when there are no today's transactions.
	 * invalid transaction = subsequent location distance > 1000km
	 * valid transaction = subsequent location distance <= 1000km
	 */
	@Test
	void testSubsequentTransactionDistanceIsValidWhenThereAreNoTransactionsInTheDB() {
		Transaction trans2 = new Transaction();
		trans2.setLatitude(53.3186);
		trans2.setLongitude(-1.6997);
		trans2.setCardNo("2703186189652095");
		String cardNo = "2703186189652095";
		when(transactionRepository.findLongitudeLatitudeOfTodaysLastTransaction(cardNo)).thenReturn(null);
		assertTrue(validationService.isTransactionDistanceValid(trans2));
		verify(transactionRepository, times(1)).findLongitudeLatitudeOfTodaysLastTransaction(cardNo);
	}
	
	
	/*
	 * Test subsequent transaction locations are inside the distance threshold when there are transactions.
	 * invalid transaction = subsequent location distance > 1000km
	 * valid transaction = subsequent location distance <= 1000km
	 */
	@Test
	void testSubsequentTransactionDistanceIsValidWhenThereAreTransactionsInTheDB() {
		Transaction trans1 = new Transaction();
		trans1.setLatitude(53.3205);
		trans1.setLongitude(-1.7297);
		trans1.setCardNo("2703186189652095");
		Transaction trans2 = new Transaction();
		trans2.setLatitude(53.3186);
		trans2.setLongitude(-1.6997);
		String cardNo = "2703186189652095";
		when(transactionRepository.findLongitudeLatitudeOfTodaysLastTransaction(cardNo)).thenReturn(trans2);
		assertTrue(validationService.isTransactionDistanceValid(trans1));
		verify(transactionRepository, times(1)).findLongitudeLatitudeOfTodaysLastTransaction(cardNo);
	}
	
	
	/*
	 * Test subsequent transaction locations are inside the distance threshold when there are transactions.
	 * invalid transaction = subsequent location distance > 1000km
	 * valid transaction = subsequent location distance <= 1000km
	 */
	@Test
	void testSubsequentTransactionDistanceIsValidWhenThereAreTransactionsInTheDBNegative() {
		Transaction trans1 = new Transaction();
		trans1.setLatitude(53.3205);
		trans1.setLongitude(-1.7297);
		trans1.setCardNo("2703186189652095");
		Transaction trans2 = new Transaction();
		trans2.setLatitude(23.3205);
		trans2.setLongitude(-6.7297);
		String cardNo = "2703186189652095";
		when(transactionRepository.findLongitudeLatitudeOfTodaysLastTransaction(cardNo)).thenReturn(trans2);
		assertFalse(validationService.isTransactionDistanceValid(trans1));
		verify(transactionRepository, times(1)).findLongitudeLatitudeOfTodaysLastTransaction(cardNo);
	}
	
	
	/*
	 * Test transaction is valid when all the rules are matched.
	 */
	@Test
	void testTransactionIsValidWhenAllRulesAreMatched() {
		Transaction trans1 = new Transaction();
		trans1.setLatitude(53.3205);
		trans1.setLongitude(-1.7297);
		trans1.setCardNo("2703186189652095");
		trans1.setAmount(200);
		trans1.setDateTime("2022-02-27 00:00:18");
		
		assertTrue(validationService.isTransactionValid(trans1));
		
	}
	
	
	/*
	 * Test transaction is valid when one rule mismatched.(amount > 500)
	 */
	@Test
	void testTransactionIsValidWhenOneRuleMisMatched1() {
		Transaction trans1 = new Transaction();
		trans1.setLatitude(53.3205);
		trans1.setLongitude(-1.7297);
		trans1.setCardNo("2703186189652095");
		trans1.setAmount(750);
		trans1.setDateTime("2022-02-27 00:00:18");
		
		assertFalse(validationService.isTransactionValid(trans1));
		
	}
	
	
	/*
	 * Test transaction is valid when one rule mismatched.(daily limit > 1000)
	 */
	@Test
	void testTransactionIsValidWhenOneRuleMisMatched2() {
		Transaction trans1 = new Transaction();
		trans1.setLatitude(53.3205);
		trans1.setLongitude(-1.7297);
		trans1.setCardNo("2703186189652095");
		trans1.setAmount(250);
		trans1.setDateTime("2022-02-27 00:00:18");
		
		double dailyTotal = 1000.00;
		when(transactionRepository.findTodaysTransactionAmountTotal(trans1.getCardNo())).thenReturn(dailyTotal);
		assertFalse(validationService.isTransactionValid(trans1));
		
	}
	
	
	/*
	 * Test transaction is valid when one rule mismatched.(daily transaction count > 5)
	 */
	@Test
	void testTransactionIsValidWhenOneRuleMisMatched3() {
		Transaction trans1 = new Transaction();
		trans1.setLatitude(53.3205);
		trans1.setLongitude(-1.7297);
		trans1.setCardNo("2703186189652095");
		trans1.setAmount(250);
		trans1.setDateTime("2022-02-27 00:00:18");
		
		when(transactionRepository.findTodaysTransactionCount(trans1.getCardNo())).thenReturn(5);
		assertFalse(validationService.isTransactionValid(trans1));
		
	}
	
	
	/*
	 * Test transaction is valid when one rule mismatched.(subsequent transaction time < 5s)
	 */
	@Test
	void testTransactionIsValidWhenOneRuleMisMatched4() {
		Transaction trans1 = new Transaction();
		trans1.setLatitude(53.3205);
		trans1.setLongitude(-1.7297);
		trans1.setCardNo("2703186189652095");
		trans1.setAmount(250);
		trans1.setDateTime("2022-02-27 00:00:18");
		
		String date = "2022-02-27 00:00:14";
		when(transactionRepository.findTimeOfTodaysLastTransaction(trans1.getCardNo())).thenReturn(date);
		assertFalse(validationService.isTransactionValid(trans1));
		
	}
	
	
	/*
	 * Test transaction is valid when one rule mismatched.(subsequent transaction distance > 1000km)
	 */
	@Test
	void testTransactionIsValidWhenOneRuleMisMatched5() {
		Transaction trans1 = new Transaction();
		trans1.setLatitude(53.3205);
		trans1.setLongitude(-1.7297);
		trans1.setCardNo("2703186189652095");
		trans1.setAmount(250);
		trans1.setDateTime("2022-02-27 00:00:18");
		
		Transaction trans2 = new Transaction();
		trans2.setLatitude(23.3205);
		trans2.setLongitude(-6.7297);
		String cardNo = "2703186189652095";
		when(transactionRepository.findLongitudeLatitudeOfTodaysLastTransaction(cardNo)).thenReturn(trans2);
		
		assertFalse(validationService.isTransactionValid(trans1));
		
	}
	//https://www.geeksforgeeks.org/program-distance-two-points-earth/#:~:text=For%20this%20divide%20the%20values,is%20the%20radius%20of%20Earth.
}
