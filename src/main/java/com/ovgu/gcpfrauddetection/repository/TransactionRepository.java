package com.ovgu.gcpfrauddetection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ovgu.gcpfrauddetection.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
	
	 @Query(value = "SELECT sum(amt) FROM trans where cc_num = ?1 AND CAST(trans_date_trans_time AS DATE) = CURDATE()", nativeQuery = true)
	 Double findTodaysTransactionAmountTotal(String cardNo);

	 @Query(value = "SELECT count(cc_num) FROM trans where cc_num = ?1 AND CAST(trans_date_trans_time AS DATE) = CURDATE()", nativeQuery = true)
	 int findTodaysTransactionCount(String cardNo);

	 @Query(value = "SELECT trans_date_trans_time FROM trans where cc_num = ?1 AND CAST(trans_date_trans_time AS DATE) = CURDATE() ORDER BY trans_date_trans_time desc limit 1", nativeQuery = true)
	 String findTimeOfTodaysLastTransaction(String cardNo);

	 @Query(value = "SELECT transaction_id, lat, longtitude FROM trans where cc_num = ?1 AND CAST(trans_date_trans_time AS DATE) = CURDATE() ORDER BY trans_date_trans_time desc limit 1", nativeQuery = true)
	 Transaction findLongitudeLatitudeOfTodaysLastTransaction(String cardNo);
}
