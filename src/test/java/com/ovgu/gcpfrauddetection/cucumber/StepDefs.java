package com.ovgu.gcpfrauddetection.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

public class StepDefs {

	@Given("Publish transaction details to Pub Sub topic {string}")
	public void publish_transaction_details_to_pub_sub_topic(String message) throws IOException, InterruptedException {
		ConnectToPubSubMessage pubSubObj = new ConnectToPubSubMessage();
		pubSubObj.publishMessageToPubSub(message);
	}

	@Given("Publish transaction details to Pub Sub topic {string}, {string}, {string}, {string}, {string}, {string}")
	public void publish_transaction_details_to_pub_sub_topic(String amount, String dateTime, String cardNo,
			String longitude, String latitude, String category) throws IOException, InterruptedException {
		String message = convertToJsonTransaction(Double.valueOf(amount), dateTime, cardNo, Double.valueOf(longitude),
				Double.valueOf(latitude), category);
		ConnectToPubSubMessage pubSubObj = new ConnectToPubSubMessage();
		pubSubObj.publishMessageToPubSub(message);

	}

	@When("Dataflow job runs")
	public void dataflow_job_runs() throws InterruptedException {
		Thread.sleep(60000);
	}

	@Then("Assert the big query record created {string}, {string}, {string}")
	public void assert_the_big_query_record_created(String dateTime, String mlStatus, String ruleStatus) throws InterruptedException {
		ConnectToBigQueryTable bqObj = new ConnectToBigQueryTable();
		TableResult result = bqObj.connectToBigQuery(dateTime);
		String mlFraudstatus = "0";
		String ruleFraudStatus = "0";
		for (FieldValueList row : result.iterateAll()) {
			mlFraudstatus =  row.get("ml_fraud_status").getStringValue();
			ruleFraudStatus = row.get("rule_fraud_status").getStringValue();
			break;
		}
		assertEquals(mlStatus, mlFraudstatus);
		assertEquals(ruleStatus, ruleFraudStatus);
	}

	private String convertToJsonTransaction(double amount, String dateTime, String cardNo, double longitude,
			double latitude, String category) {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;

		Transaction trans = new Transaction();
		trans.setLatitude(latitude);
		trans.setLongitude(longitude);
		trans.setAmount(amount);
		trans.setDateTime(dateTime);
		trans.setCardNo(cardNo);
		trans.setCategory(category);

		try {
			jsonString = objectMapper.writeValueAsString(trans);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("Converted JSON string  : " + jsonString);
		return jsonString;
	}

}
