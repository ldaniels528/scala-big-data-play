# Scala Big Data Demos
Simple demos written in Scala for Spark, Storm, Flink (and soon others).

### Build Requirements

* [SBT 0.13.x] (http://www.scala-sbt.org/download.html)

### Running the Hadoop-Copy Demo

```bash
    sbt clean "project hdfs_copy" "run hdfs://localhost:19000 stockQuotes.js 100000"
```

### Running the Spark-Kafka Consumer-Producer Demo

To run the Spark-Kafka streaming consumer-producer demo, simple execute the following command:

```bash
    sbt clean "project spark_kafka_consumer" run
```

If though, your Kafka brokers are not running locally (e.g. localhost), do the following instead.
In the example, below replace "dev111" with the hostname and port of your Kafka broker.

```bash
    sbt clean "project spark_kafka_consumer" "run dev111:9091"
```

### Running the Spark-Kafka Producer Demo

To run the Spark-Kafka streaming producer demo, simple execute the following command:

```bash
    sbt clean "project spark_kafka_producer" run
```

If though, your Kafka brokers are not running locally (e.g. localhost), do the following instead.
In the example, below replace "dev111" with the hostname and port of your Kafka broker.

```bash
    sbt clean "project spark_kafka_producer" "run dev111:9091"
```

### Running the Storm-Kafka Producer Demo

To run the Storm-Kafka streaming producer, simple execute the following command:

```bash
    sbt clean "project storm_kafka_producer" run
```

If though, your Kafka brokers are not running locally (e.g. localhost), do the following instead.
In the example, below replace "dev111" with the hostname and port of your Kafka broker.

```bash
    sbt clean "project storm_kafka_producer" "run dev111:9091"
```

### Running the Flink-Kafka Producer Demo

To run the Flink-Kafka streaming consumer-producer, simple execute the following command:

```bash
    sbt clean "project flink_kafka_producer" run
```

If though, your Kafka brokers are not running locally (e.g. localhost), do the following instead.
In the example, below replace "dev111" with the hostname and port of your Kafka broker.

```bash
    sbt clean "project flink_kafka_producer" "run dev111:9091"
```

