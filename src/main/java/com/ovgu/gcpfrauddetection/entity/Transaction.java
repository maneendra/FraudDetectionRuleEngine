package com.ovgu.gcpfrauddetection.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="transaction")
@Table(name="trans")
public class Transaction {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "transaction_id", updatable = false, nullable = false)
	private int transaction_id;
	
	@Column(name = "trans_date_trans_time")
	private String dateTime;
	
	@Column(name = "cc_num")
	private String cardNo;
	
	@Column(name = "amt")
	private double amount;
	
	@Column(name = "lat")
	private double latitude;
	
	@Column(name = "longtitude")
	private double longitude;
	
	public Transaction() {
		
	}
	
	public Transaction(double amount, String cardNo, String dateTime,double latitude, double longtitude) {
		this.amount = amount;
		this.cardNo = cardNo;
		this.dateTime = dateTime;
		this.latitude = latitude;
		this.longitude = longtitude;
	}
	
	public int getId() {
		return transaction_id;
	}

	public void setId(int transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, cardNo, dateTime, latitude, longitude);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(cardNo, other.cardNo) && Objects.equals(dateTime, other.dateTime)
				&& Double.doubleToLongBits(latitude) == Double.doubleToLongBits(other.latitude)
				&& Double.doubleToLongBits(longitude) == Double.doubleToLongBits(other.longitude);
	}


	

	
	

}
