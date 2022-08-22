package com.ovgu.gcpfrauddetection.repository;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ovgu.gcpfrauddetection.entity.Transaction;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {
	
	@Autowired
	private TransactionRepository transRepository;
	
	private static String dateAndTimeNow;
	
	private static String dateAndTimeBefore5Seconds;
	
	private static String yesterdayDateAndTime;
	
	/*
	 * Set up test data.
	 */
	@BeforeAll
	static void initAll() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateAndTimeNow = formatter.format(date);
		System.out.println("date and time now : " + dateAndTimeNow);
		
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		dateAndTimeBefore5Seconds = (now.minusSeconds(5)).format(dateTimeformatter);
		System.out.println("date and time before 5 seconds : " + dateAndTimeBefore5Seconds);
		
		yesterdayDateAndTime = (now.minusDays(1)).format(dateTimeformatter);
		System.out.println("date and time before 1 day : " + yesterdayDateAndTime);
	} 
	
	/*
	 * Test the injected components are not null.
	 */
	@Test
	void testInjectedComponents() {
		assertTrue(transRepository != null);
	}
	
	/*
	 * Test the empty repository. 
	 */
	@Test
	void testEmptyRepository() {
		List<Transaction> transactions = transRepository.findAll();
		assertTrue(transactions.isEmpty());
	}
	
	/*
	 * Test saving the transaction to the database.
	 */
	@Test
	void testSavingTransaction() {
		Transaction trans1 = new Transaction();
		trans1.setAmount(99.99);
		trans1.setCardNo("2703186189652095");
		trans1.setDateTime("2019-01-01 00:00:18");
		trans1.setLatitude(36);
		trans1.setLongitude(36);
		Transaction savedTrans = transRepository.save(trans1);
		System.out.println(savedTrans);
		assertTrue(savedTrans !=null);
		assertTrue(savedTrans.getId() > 0);
	}
	
	/*
	 * Find the today's transactions total amount when there are no todays transactions.
	 */
	@Test
	void testFindingTodaysTransactionAmountTotalWhenThereAreNoTransactions() {
		String cardNo = "2703186189652095";
		assertNull(transRepository.findTodaysTransactionAmountTotal(cardNo));
	}
	
	/*
	 * Find the today's transactions total amount when there are only todays transactions.
	 */
	@Test
	void testFindingTodaysTransactionAmountTotalWhenThereAreTransactions() {
		Transaction trans1 = new Transaction(101.25,"2703186189652095",dateAndTimeNow, 36.0788, 81.1781);
		Transaction trans2 = new Transaction(85.25,"2703186189652095",dateAndTimeNow, 36.0788, 81.1781);
		transRepository.save(trans1);
		transRepository.save(trans2);
		String cardNo = "2703186189652095";
		assertTrue(transRepository.findTodaysTransactionAmountTotal(cardNo) == 186.50);
	}
	
	
	/*
	 * Find the today's transactions total amount when there are multiple days transactions.
	 */
	@Test
	void testFindingTodaysTransactionAmountTotalWhenThereAreMultipleDaysTransactions() {
		Transaction trans1 = new Transaction(101.25,"2703186189652095",yesterdayDateAndTime, 36.0788, 81.1781);
		Transaction trans2 = new Transaction(85.25,"2703186189652095",dateAndTimeNow, 36.0788, 81.1781);
		transRepository.save(trans1);
		transRepository.save(trans2);
		String cardNo = "2703186189652095";
		assertTrue(transRepository.findTodaysTransactionAmountTotal(cardNo) == 85.25);
	}
	
	
	/*
	 * Find no of transactions that happened today when there are no todays transactions.
	 */
	@Test
	void testFindingTodaysTransactionCountWhenThereAreNoTransactions() {
		String cardNo = "2703186189652095";
		assertEquals(0, transRepository.findTodaysTransactionCount(cardNo));
	}
	
	
	/*
	 * Find no of transactions that happened today when there are only todays transactions.
	 */
	@Test
	void testFindingTodaysTransactionCountWhenThereAreTransactions() {
		Transaction trans1 = new Transaction(101.25,"2703186189652095",dateAndTimeNow, 36.0788, 81.1781);
		Transaction trans2 = new Transaction(85.25,"2703186189652095",dateAndTimeNow, 36.0788, 81.1781);
		transRepository.save(trans1);
		transRepository.save(trans2);
		String cardNo = "2703186189652095";
		assertEquals(2, transRepository.findTodaysTransactionCount(cardNo));
	}
	
	
	/*
	 * Find no of transactions that happened today when there are multiple days transactions.
	 */
	@Test
	void testFindingTodaysTransactionCountWhenThereAreMultipleDaysTransactions() {
		Transaction trans1 = new Transaction(101.25,"2703186189652095",yesterdayDateAndTime, 36.0788, 81.1781);
		Transaction trans2 = new Transaction(85.25,"2703186189652095",dateAndTimeNow, 36.0788, 81.1781);
		transRepository.save(trans1);
		transRepository.save(trans2);
		String cardNo = "2703186189652095";
		assertEquals(1, transRepository.findTodaysTransactionCount(cardNo));
	}
	
	
	/*
	 * Find the transaction time of todays last transaction when there are no transactions in the database
	 */
	@Test
	void testFindingLastTransactionTimeOfTodaysTransactionsWhenThereAreNoTransactions() {
		String cardNo = "2703186189652095";
		assertNull(transRepository.findTimeOfTodaysLastTransaction(cardNo));
	}
	
	
	/*
	 * Find the transaction time of todays last transaction when there are only todays transactions in the database
	 */
	@Test
	void testFindingLastTransactionTimeOfTodaysTransactionsWhenThereAreOnlyTodaysTransactions() {
		Transaction trans1 = new Transaction(101.25,"2703186189652095",dateAndTimeBefore5Seconds, 36.0788, 81.1781);
		Transaction trans2 = new Transaction(85.25,"2703186189652095",dateAndTimeNow, 36.0788, 81.1781);
		transRepository.save(trans1);
		transRepository.save(trans2);
		String cardNo = "2703186189652095";
		assertEquals(dateAndTimeNow, transRepository.findTimeOfTodaysLastTransaction(cardNo));
	}
	
	
	
	/*
	 * Find the transaction time of todays last transaction when there are multiple transactions in the database
	 */
	@Test
	void testFindingLastTransactionTimeOfTodaysTransactionsWhenThereAreMultipleDaysTransactions() {
		Transaction trans1 = new Transaction(101.25,"2703186189652095","2022-02-26 00:00:18", 36.0788, 81.1781);
		Transaction trans2 = new Transaction(101.25,"2703186189652095",dateAndTimeBefore5Seconds, 36.0788, 81.1781);
		Transaction trans3 = new Transaction(85.25,"2703186189652095",dateAndTimeNow, 36.0788, 81.1781);
		transRepository.save(trans1);
		transRepository.save(trans2);
		transRepository.save(trans3);
		String cardNo = "2703186189652095";
		System.out.println(transRepository.findTimeOfTodaysLastTransaction(cardNo));
		assertEquals(dateAndTimeNow, transRepository.findTimeOfTodaysLastTransaction(cardNo));
	}
	
	
	/*
	 * Find the longitude and latitude of todays last transaction when there are no transactions in the database.
	 */
	@Test
	void testFindingLongitudeLatitudeofTodaysLastTransactionWhenThereAreNoTransactions() {
		String cardNo = "2703186189652095";
		assertNull(transRepository.findLongitudeLatitudeOfTodaysLastTransaction(cardNo));
	}
	
	
	/*
	 * Find the longitude and latitude of todays last transaction when there are only todays transactions in the database.
	 */
	@Test
	void testFindingLongitudeLatitudeofTodaysLastTransactionWhenThereAreOnlyTodaysTransactions() {
		Transaction trans1 = new Transaction(101.25,"2703186189652095",dateAndTimeBefore5Seconds, 35.0788, 80.1781);
		Transaction trans2 = new Transaction(85.25,"2703186189652095",dateAndTimeNow, 36.0788, 81.1781);
		transRepository.save(trans1);
		transRepository.save(trans2);
		String cardNo = "2703186189652095";
		Transaction resultTrans = transRepository.findLongitudeLatitudeOfTodaysLastTransaction(cardNo);
		assertEquals(36.0788, resultTrans.getLatitude());
		assertEquals(81.1781, resultTrans.getLongitude());
	}
	
	
	/*
	 * Find the longitude and latitude of todays last transaction when there are multiple days transactions in the database.
	 */
	@Test
	void testFindingLongitudeLatitudeofTodaysLastTransactionWhenThereAreMultipleDaysTransactions() {
		Transaction trans1 = new Transaction(101.25,"2703186189652095",dateAndTimeBefore5Seconds, 35.0788, 80.1781);
		Transaction trans2 = new Transaction(85.25,"2703186189652095",dateAndTimeNow, 36.0788, 81.1781);
		Transaction trans3 = new Transaction(101.25,"2703186189652095",yesterdayDateAndTime, 36.0788, 81.1781);
		transRepository.save(trans1);
		transRepository.save(trans2);
		transRepository.save(trans3);
		String cardNo = "2703186189652095";
		Transaction resultTrans = transRepository.findLongitudeLatitudeOfTodaysLastTransaction(cardNo);
		assertEquals(36.0788, resultTrans.getLatitude());
		assertEquals(81.1781, resultTrans.getLongitude());
	}
	

}
