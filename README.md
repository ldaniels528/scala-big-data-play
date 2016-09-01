# Scala Storm-Kafka Demo
A simple Storm-Kafka demo written in Scala.

The purpose of the sample is to demonstrate a simple Storm spout that produces random (fake)
stock quotes, which are then persisted to Kafka via the native `KafkaBolt` class.

### Build Requirements

* [SBT 0.13.x] (http://www.scala-sbt.org/download.html)

### Running the demo

To run this demo, simple execute the following command:

```
    sbt clean run
```

**NOTE:** The Kafka `bootstrap.servers` property is currently hard-coded in 
`com.github.ldaniels528.demo.KafkaStormDemoApp`. You'll have to change this in order
for the demo to work properly.