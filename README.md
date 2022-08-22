
# Rule Engine
Rule engine is a RESTful web service hosted in compute engine that validates online transaction against the below conditions.

 1. Check if the transaction amount exceeds the daily transaction amount (threshold = 500)
 2. Check if the daily transaction total exceeds the daily transaction total (threshold = 1000)
 3. Check if the daily transaction count exceeds the daily transaction count (threshold = 5)
 4. Check if the time difference of the subsequent daily transactions is greater than the minimum time difference(threshold =5 seconds)
 5. Check if the distance of the subsequent daily transactions is lesser than the maximum distance(threashold=1000km)

## Deployment
To deploy the rule engine, first Tomcat server and MYSQL should be installed in the virtual machine using below steps and then the WAR files should be deployed in the server installed.
### Install Tomcat
https://bytesofgigabytes.com/googlecloud/installing-apache-tomcat-on-google-cloud/
### Install MYSQL
https://cloud.google.com/architecture/setup-mysql

## Usage
To access the rule engine, send the request as below.
### URL
http://34.89.222.32:8080/gcp-fraud-detection/api/isTransactionValid
### JSON Request
{
"amount":21.69,
"dateTime":"2022-04-11 04:00:08",
"cardNo":"3560725013359370",
"longitude":-103.484949,
"latitude":32.675272,
"category":"gas_transport"
}
### Response
The response will be a string either true or false. If the transaction is valid the response will be true and otherwise false.
