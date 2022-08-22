Feature: Detecting Frauds.
  Scenario Outline: Verify fraud trasaction detection using rule engine and machine learning model.
    Given Publish transaction details to Pub Sub topic "<Message>"
    When Dataflow job runs
    Then Assert the big query record created "<MLStatus>", "<RuleEngineStatus>"

Examples:    
  |Description 												|Message																																																																								|MLStatus|RuleEngineStatus|
  |RuleEngine=fraud,MLModel=fraud 		|""{"amount":977.01,"dateTime":"2020-06-21 01:00:08","cardNo":"3524574586339330","longitude":-80.834389,"latitude":26.888686,"category":"shopping_net"}""	|1       |0               |
  #|RuleEngine=Genuine,MLModel=fraud 	|{"amount":21.69,"dateTime":"2020-06-21 03:59:46","cardNo":"3560725013359370","longitude":-103.484949,"latitude":32.675272,"category":"gas_transport"}											|1       |1               |
  #|RuleEngine=Genuine,MLModel=genuine |{"amount":9.16,"dateTime":"2020-06-21 01:00:08","cardNo":"3524574586339330","longitude":-80.834389,"latitude":26.888686,"category":"shopping_net"}			|0       |1               |
  #|RuleEngine=fraud,MLModel=genuine  	|{"amount":501,"dateTime":"2020-06-21 01:00:08","cardNo":"3524574586339330","longitude":-80.834389,"latitude":26.888686,"category":"shopping_net"}			|0       |0               |

    
    
    
    