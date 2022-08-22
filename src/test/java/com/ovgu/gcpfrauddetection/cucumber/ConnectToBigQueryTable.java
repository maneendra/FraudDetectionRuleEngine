package com.ovgu.gcpfrauddetection.cucumber;

import java.util.UUID;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;

public class ConnectToBigQueryTable {

	//https://cloud.google.com/bigquery/docs/quickstarts/quickstart-client-libraries
	public TableResult connectToBigQuery(String dateTime) throws InterruptedException {
		BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
		QueryJobConfiguration queryConfig = QueryJobConfiguration
				//.newBuilder("SELECT *" + "FROM `ultra-palisade-339421.fraud_detection_dataset.transaction_fraud_status_data_table` "
					//	+ "ORDER BY trans_date_and_time DESC LIMIT 1")
				.newBuilder("SELECT *" + "FROM `ultra-palisade-339421.fraud_detection_dataset.transaction_fraud_status_data_table` WHERE trans_date_and_time='" + dateTime + "'")
				.setUseLegacySql(false).build();

		// Create a job ID so that we can safely retry.
		JobId jobId = JobId.of(UUID.randomUUID().toString());
		Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

		// Wait for the query to complete.
		queryJob = queryJob.waitFor();

		// Check for errors
		if (queryJob == null) {
			throw new RuntimeException("Job no longer exists");
		} else if (queryJob.getStatus().getError() != null) {
			throw new RuntimeException(queryJob.getStatus().getError().toString());
		}

		// Get the results.
		TableResult result = queryJob.getQueryResults();
		return result;
	}
}
