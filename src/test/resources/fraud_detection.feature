Feature: Detecting Frauds.

  Scenario Outline: Verify fraud trasaction detection using rule engine and machine learning model.
    Given Publish transaction details to Pub Sub topic "<Amount>", "<DateTime>", "<CardNo>", "<Longitude>", "<Latitude>", "<Category>"
    When Dataflow job runs
    Then Assert the big query record created "<DateTime>", "<MLStatus>", "<RuleEngineStatus>"

    Examples: 
      | Description                        | Amount | DateTime            | CardNo           | Longitude   | Latitude  | Category      | MLStatus | RuleEngineStatus |
      | RuleEngine=fraud,MLModel=fraud     | 977.01 | 2022-04-11 01:00:08 | 3524574586339330 |  -80.834389 | 26.888686 | shopping_net  |        1 |                0 |
      | RuleEngine=Genuine,MLModel=fraud   |  21.69 | 2022-04-11 05:00:08 | 3560725013359370 | -103.484949 | 32.675272 | gas_transport |        1 |                1 |
      | RuleEngine=Genuine,MLModel=genuine |   9.16 | 2020-06-22 01:00:08 | 3524574586339330 |  -80.834389 | 26.888686 | shopping_net  |        0 |                1 |
      | RuleEngine=fraud,MLModel=genuine   |    501 | 2020-06-22 02:00:0  | 3524574586339330 |  -80.834389 | 26.888686 | shopping_net  |        0 |                0 |