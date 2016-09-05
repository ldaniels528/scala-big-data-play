package com.github.ldaniels528.demo

import com.github.ldaniels528.demo.DataUtilities._
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer09, FlinkKafkaProducer09}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.slf4j.LoggerFactory

import scala.language.postfixOps

/**
  * Flink-Kafka Consumer-Producer Demo Application
  * @author lawrence.daniels@gmail.com
  */
object FlinkKafkaConsumerDemoApp {
  private val logger = LoggerFactory.getLogger(getClass)
  private val defaultInputTopic = "stockQuotes.spark"
  private val defaultOutputTopic = "stockQuotes.flink"
  private val defaultInputBrokers = "localhost:9092"
  private val defaultOutputBrokers = "localhost:9092"
  private val defaultArgs = Seq(defaultInputTopic, defaultInputBrokers, defaultOutputTopic, defaultOutputBrokers)

  /**
    * For standalone operation
    * @param args the given commandline arguments
    */
  def main(args: Array[String]): Unit = consumerProducerDemo(args)

  /**
    * Flink-Kafka producer demo
    * @param args the given commandline arguments
    */
  def consumerProducerDemo(args: Array[String]) = {
    // get the input parameters
    val Seq(inputTopic, inputBrokers, outputTopic, outputBrokers, _*) = defaultArgs.zipWithIndex map { case (default, n) =>
      args.maybe(n) getOrElse default
    }

    // get the consumer and producer configurations
    val consumerProps = KafkaProperties.getKafkaConsumerConfig(inputBrokers)
    val producerProps = KafkaProperties.getKafkaProducerConfig(outputBrokers)

    // start the streaming process
    val streamExecEnv = StreamExecutionEnvironment.getExecutionEnvironment
    val stream = streamExecEnv
      .addSource(new FlinkKafkaConsumer09[String](inputTopic, new SimpleStringSchema(), consumerProps))
      .addSink(new FlinkKafkaProducer09[String](outputTopic, new SimpleStringSchema(), producerProps))

    val job = streamExecEnv.execute("Flink-Kafka Consumer-Producer Demo")
    ()
  }

}
