# Kinesis Test

Java 8 lambda example for processing an AWS Kinesis stream

### Prerequisites

Java 8

## Building
Use `./gradlew build` to build the project. This will create a zip file in `build/distributions/` that can be uploaded 
to AWS as a lambda.

## Deploy to AWS
The following command can upload the lambda to AWS:
```
aws lambda create-function --function-name ProcessKinesisRecords --runtime java8 \
--role arn:aws:iam::130601813503:role/lambda-kinesis-role --handler com.halosight.ProcessKinesisRecords \
--description 'Processes Kinesis Records and prints out their data' \
--zip-file fileb://build/distributions/kinesis-test-1.0-SNAPSHOT.zip
```

Update the function:
```
aws lambda update-function-code --function-name ProcessKinesisRecords \
--zip-file fileb://build/distributions/kinesis-test-1.0-SNAPSHOT.zip
```

You can use this to delete the function:
```
aws lambda delete-function --function-name ProcessKinesisRecords
```

Use this to test the function:
```
aws lambda invoke --function-name ProcessKinesisRecords --payload file://src/test/resources/input.txt out.txt
```

## Test with Kinesis
Create a Kinesis stream:
```
aws kinesis create-stream --stream-name lambda-stream --shard-count 1
```

Show stream details (to get the arn):
```
aws kinesis describe-stream --stream-name lambda-stream
```

Map stream as event source for lambda function:
```
aws lambda create-event-source-mapping --function-name ProcessKinesisRecords \
--event-source arn:aws:kinesis:us-west-2:130601813503:stream/lambda-stream --batch-size 100 --starting-position LATEST
```

Put something in the stream:
```
aws kinesis put-record --stream-name lambda-stream --partition-key 1 --data "Test from Dave"
```

## Verify in Cloudwatch
 1. Sign into the AWS management console
 2. Find CloudWatch in services
 3. Click on Logs on the left hand side
 4. Click on the `/aws/lambda/ProcessKinesisRecords` log group
 5. Click into the logs to see messages that you push to the stream get logged by the lambda