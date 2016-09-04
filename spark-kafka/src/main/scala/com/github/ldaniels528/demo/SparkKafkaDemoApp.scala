package com.github.ldaniels528.demo

import java.text.DecimalFormat
import java.util.UUID

import com.github.ldaniels528.demo.DataUtilities._
import com.github.ldaniels528.demo.StatisticsGenerator.Statistics
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Spark-Kafka Demo Application
  * @author lawrence.daniels@gmail.com
  */
object SparkKafkaDemoApp {
  private val logger = LoggerFactory.getLogger(getClass)
  private val defaultTopic = "stockQuotes.spark"
  private val numberFormat = new DecimalFormat("#,###")

  /**
    * For standalone operation
    * @param args the given commandline arguments
    */
  def main(args: Array[String]): Unit = producerDemo(args)

  /**
    * Spark-Kafka producer demo
    * @param args the given commandline arguments
    */
  def producerDemo(args: Array[String]) = {
    // get the input parameters
    val (brokers, topic) = (
      args.maybe(0) getOrElse "localhost:9092",
      args.maybe(1) getOrElse defaultTopic)

    // get the Kafka producer configuration
    val kafkaProps = KafkaProperties.getKafkaProducerConfig(brokers)

    // create the producer
    val producer = new KafkaProducer[String, String](kafkaProps)
    val statsGen = new StatisticsGenerator()

    // allow the process to run for 1 minute
    val cutOffTime = System.currentTimeMillis() + 1.minute
    while (System.currentTimeMillis() < cutOffTime) {
      val key = UUID.randomUUID().toString
      val message = RandomStockQuoteService.getQuote
      producer.send(new ProducerRecord(topic, key, message))

      (statsGen ++) foreach { case Statistics(total, delta, rps) =>
        logger.info(f"$topic : + ${numberFormat.format(delta)} => ${numberFormat.format(total)} records : ${rps / 1000}%.01fK recs/sec")
      }
    }
  }

}
