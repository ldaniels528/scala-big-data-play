package com.github.ldaniels528.demo

import java.text.DecimalFormat

import com.github.ldaniels528.demo.DataUtilities._
import com.github.ldaniels528.demo.StatisticsGenerator.Statistics
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Spark-Kafka Consumer-Producer Demo Application
  * @author lawrence.daniels@gmail.com
  */
object SparkKafkaConsumerDemoApp {
  private val logger = LoggerFactory.getLogger(getClass)
  private val defaultInputTopic = "stockQuotes.spark"
  private val defaultOutputTopic = "stockQuotes.spark2"
  private val defaultInputBrokers = "localhost:9092"
  private val defaultOutputBrokers = "localhost:9092"
  private val defaultArgs = Seq(defaultInputTopic, defaultInputBrokers, defaultOutputTopic, defaultOutputBrokers)

  /**
    * For standalone operation
    * @param args the given commandline arguments
    */
  def main(args: Array[String]): Unit = consumerDemo(args)

  /**
    * Starts the Spark-Kafka consumer demo
    * @param args the given commandline arguments
    */
  def consumerDemo(args: Array[String]) = {
    // get the input parameters
    val Seq(inputTopic, inputBrokers, outputTopic, outputBrokers, _*) = defaultArgs.zipWithIndex map { case (default, n) =>
      args.maybe(n) getOrElse default
    }

    // get the Kafka consumer & producer configurations
    val consumerProps = KafkaProperties.getKafkaConsumerConfig(inputBrokers)
    val producerProps = KafkaProperties.getKafkaProducerConfig(outputBrokers)

    // create the consumer & producer instances
    val consumer = new KafkaConsumer[String, String](consumerProps)
    val producer = new KafkaProducer[String, String](producerProps)

    // create service objects
    val statsGen = new StatisticsGenerator()
    val numberFormat = new DecimalFormat("#,###")

    // allow the process to run for 5 minutes
    consumer.subscribe(Seq(inputTopic))

    val cutOffTime = System.currentTimeMillis() + 5.minutes
    while (System.currentTimeMillis() < cutOffTime) {

      Option(consumer.poll(1.second)) foreach { records =>
        records foreach { record =>
          producer.send(new ProducerRecord(outputTopic, record.key(), record.value()))

          (statsGen ++) foreach { case Statistics(total, delta, rps) =>
            logger.info(f"$outputTopic : + ${numberFormat.format(delta)} => ${numberFormat.format(total)} records : ${rps / 1000}%.01fK recs/sec")
          }
        }
      }
    }
  }

}
