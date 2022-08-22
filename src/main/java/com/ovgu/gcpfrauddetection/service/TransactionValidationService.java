package com.ovgu.gcpfrauddetection.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ovgu.gcpfrauddetection.dto.TransactionDto;
import com.ovgu.gcpfrauddetection.entity.Transaction;
import com.ovgu.gcpfrauddetection.repository.TransactionRepository;

@Service
public class TransactionValidationService {
	
	@Autowired
	private TransactionRepository transRepo;
	
	/*
	 * Check if the incoming transaction is valid.
	 */
	public boolean isTransactionValid(Transaction trans) {
		if(isTransactionAmountValid(trans.getAmount()) &&
				isDailyTransactionLimitValid(trans) &&
				isDailyTransactionCountValid(trans) &&
				isTransactionTimeValid(trans) &&
				isTransactionDistanceValid(trans)) {
			transRepo.save(trans);
			return true;
		}
		return false;
	}

	/*
	 * Check if the transaction amount exceeds the daily transaction amount threshold.
	 * valid if amount <= 500
	 */
	public boolean isTransactionAmountValid(double amount) {
		if(amount > 500) {
			return false;
		}
		return true;
	}
	
	/*
	 * Check if the daily transaction total exceeds the daily transaction total threshold.
	 * valid if total amount <= 1000
	 */
	public boolean isDailyTransactionLimitValid(Transaction transaction) {
		Double totalAmountInDb = transRepo.findTodaysTransactionAmountTotal(transaction.getCardNo());
		double total;
		if(totalAmountInDb != null) {
			total = totalAmountInDb + transaction.getAmount();			
		}
		else {
			total = transaction.getAmount();
		}
		if(total > 1000) {
			return false;
		}
		return true;
	}

	/*
	 * Check if the daily transaction count exceeds the daily transaction count threshold.
	 * valid if count <= 5
	 */
	public boolean isDailyTransactionCountValid(Transaction transaction) {
		int transCount = transRepo.findTodaysTransactionCount(transaction.getCardNo());
		if(++transCount > 5) {
			return false;
		}
		return true;
	}

	/*
	 * Check if the subsequent transaction times exceeds 5 seconds.
	 * valid if the time difference of the subsequent daily transactions are greater than 5 seconds.
	 */
	public boolean isTransactionTimeValid(Transaction trans) {
		String time = transRepo.findTimeOfTodaysLastTransaction(trans.getCardNo());
		if(time !=null) {
			Calendar cal = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				cal.setTime(sdf.parse(time));
				cal1.setTime(sdf.parse(trans.getDateTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if((cal1.getTimeInMillis() - cal.getTimeInMillis())/1000 < 5 ) {
				return false;
			}
			return true;
		}
		return true;
	}

	/*
	 * Calculate distance in km for two longitude latitude points.
	 */
	public int calculateDistance(double lat1, double longt1, double lat2, double longt2) {
		//convert degrees to radians
		longt1 = Math.toRadians(longt1);
		longt2 = Math.toRadians(longt2);
		lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        
        // Haversine formula
        double dlon = longt2 - longt1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.pow(Math.sin(dlon / 2),2);
             
        double c = 2 * Math.asin(Math.sqrt(a));
 
        // Radius of earth in kilometers.
        double r = 6371;
 
        // calculate the result
        return (int) (c * r);
	}

	
	/*
	 * Check if the subsequent transaction distance exceeds 1000km.
	 * valid if the distance of the subsequent daily transactions are lesser than 1000km.
	 */
	public boolean isTransactionDistanceValid(Transaction trans1) {
		Transaction trans2 = transRepo.findLongitudeLatitudeOfTodaysLastTransaction(trans1.getCardNo());
		if(trans2 != null) {
			int distance = calculateDistance(trans1.getLatitude(), trans1.getLongitude(), trans2.getLatitude(), trans2.getLongitude());
			if(distance > 1000) {
				return false;
			}
			return true;
		}
		return true;
	}

	/*
	 * Transform TransactionDto to Transaction object.
	 */
	public Transaction transform(TransactionDto transactionDto) {
		Transaction trans = new Transaction();
		trans.setDateTime(transactionDto.getDateTime());
		trans.setAmount(transactionDto.getAmount());
		trans.setCardNo(transactionDto.getCardNo());
		trans.setLatitude(transactionDto.getLatitude());
		trans.setLongitude(transactionDto.getLongitude());
		return trans;
	}		

}
