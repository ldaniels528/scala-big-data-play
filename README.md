# Scala Big Data Demo
Simple demos written in Scala for Spark, Storm (and soon others).

### Build Requirements

* [SBT 0.13.x] (http://www.scala-sbt.org/download.html)

### Running the Spark Demo

To run the Spark Streaming demo, simple execute the following command:

```bash
    sbt clean "project spark_kafka" run
```

If though, your Kafka brokers are not running locally (e.g. localhost), do the following instead.
In the example, below replace "dev111" with the hostname and port of your Kafka broker.

```bash
    sbt clean "project spark_kafka" "run dev111:9091"
```

### Running the Storm Demo

To run the Storm demo, simple execute the following command:

```bash
    sbt clean "project storm_kafka" run
```

If though, your Kafka brokers are not running locally (e.g. localhost), do the following instead.
In the example, below replace "dev111" with the hostname and port of your Kafka broker.

```bash
    sbt clean "project storm_kafka" "run dev111:9091"
```

