package com.github.ldaniels528.demo

import com.github.ldaniels528.demo.DataUtilities._
import org.apache.storm.kafka.bolt.KafkaBolt
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.{Config, LocalCluster, StormSubmitter}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
  * Storm-Kafka Demo Application
  * @author lawrence.daniels@gmail.com
  */
object StormKafkaDemoApp {
  private val logger = LoggerFactory.getLogger(getClass)
  private val topologyName = "StormKafkaDemo"
  private val spoutName = "stockSpout"
  private val boltName = "kafkaBolt"
  private val defaultTopic = "stockQuotes.storm"

  /**
    * For standalone operation
    * @param args the given commandline arguments
    */
  def main(args: Array[String]): Unit = producerDemo(args)

  /**
    * Storm-Kafka producer demo
    * @param args the given commandline arguments
    */
  def producerDemo(args: Array[String]) = {
    // get the input parameters
    val (brokers, topic, appArgs) = (
      args.maybe(0) getOrElse "localhost:9092",
      args.maybe(1) getOrElse defaultTopic, args.drop(2))

    // get the Kafka producer configuration
    val kafkaProps = KafkaProperties.getKafkaProducerConfig(brokers)

    // create the topology builder
    val builder = new TopologyBuilder()
    builder.setSpout(spoutName, new StockQuoteSpout())
    builder.setBolt(boltName, new KafkaBolt[String, String]()
      .withProducerProperties(kafkaProps)
      .withTopicSelector(new DefaultTopicSelector(topic))
      .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper("msgId", "quote"))).shuffleGrouping(spoutName)

    // define the Storm configuration
    val config = new Config()
    config.setDebug(true)
    config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1: Integer)

    // start the topology
    appArgs.toList match {
      case name :: params =>
        config.setNumWorkers(1)
        StormSubmitter.submitTopologyWithProgressBar(name, config, builder.createTopology())

      case _ =>
        val cluster = new LocalCluster()
        val topology = builder.createTopology()
        cluster.submitTopology(topologyName, config, topology)

        import java.util.{Timer, TimerTask}

        // allow the process to run for 1 minute
        val timer = new Timer()
        timer.schedule(new TimerTask {
          override def run() = {
            logger.info(s"Shutting down topology '$topologyName'...")
            cluster.killTopology(topologyName)
            cluster.shutdown()
          }
        }, 90.seconds)
    }
  }

}
