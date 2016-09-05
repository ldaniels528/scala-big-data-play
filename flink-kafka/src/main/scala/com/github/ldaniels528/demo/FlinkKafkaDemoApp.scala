package com.github.ldaniels528.demo

import com.github.ldaniels528.demo.DataUtilities._
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer09, FlinkKafkaProducer09}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

import scala.language.postfixOps

/**
  * Flink-Kafka Demo Application
  * @author lawrence.daniels@gmail.com
  */
object FlinkKafkaDemoApp {
  private val defaultTopic = "stockQuotes.flink"

  /**
    * For standalone operation
    * @param args the given commandline arguments
    */
  def main(args: Array[String]): Unit = producerDemo(args)

  /**
    * Flink-Kafka producer demo
    * @param args the given commandline arguments
    */
  def producerKafka2KafkaDemo(args: Array[String]) = {
    // get the input parameters
    val (brokers, topic, appArgs) = (
      args.maybe(0) getOrElse "localhost:9092",
      args.maybe(1) getOrElse defaultTopic,
      args.drop(2))

    // get the consumer and producer configurations
    val consumerProps = KafkaProperties.getKafkaConsumerConfig(brokers)
    val producerProps = KafkaProperties.getKafkaProducerConfig(brokers)

    // start the streaming process
    val streamExecEnv = StreamExecutionEnvironment.getExecutionEnvironment
    val stream = streamExecEnv
      .addSource(new FlinkKafkaConsumer09[String]("stockQuotes.spark", new SimpleStringSchema(), consumerProps))
      .addSink(new FlinkKafkaProducer09[String](topic, new SimpleStringSchema(), producerProps))

    val job = streamExecEnv.execute("Flink-Kafka Demo")
    ()
  }

  /**
    * Flink-Kafka producer demo
    * @param args the given commandline arguments
    */
  def producerDemo(args: Array[String]) = {
    // get the input parameters
    val (brokers, topic, appArgs) = (
      args.maybe(0) getOrElse "localhost:9092",
      args.maybe(1) getOrElse defaultTopic,
      args.drop(2))

    // get the consumer and producer configurations
    val consumerProps = KafkaProperties.getKafkaConsumerConfig(brokers)
    val producerProps = KafkaProperties.getKafkaProducerConfig(brokers)

    // start the streaming process
    val streamExecEnv = StreamExecutionEnvironment.getExecutionEnvironment
    val stream = streamExecEnv
      .addSource(new FlinkKafkaConsumer09[String]("stockQuotes.spark", new SimpleStringSchema(), consumerProps))
      .addSink(new FlinkKafkaProducer09[String](topic, new SimpleStringSchema(), producerProps))

    val job = streamExecEnv.execute("Flink-Kafka Demo")
    ()
  }

}
