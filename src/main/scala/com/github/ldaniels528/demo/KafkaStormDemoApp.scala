package com.github.ldaniels528.demo

import java.util

import org.apache.storm.kafka.bolt.KafkaBolt
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector
import org.apache.storm.topology.TopologyBuilder
import org.apache.storm.{Config, LocalCluster, StormSubmitter}

/**
  * Kafka Storm Demo Application
  * @author lawrence.daniels@gmail.com
  */
object KafkaStormDemoApp {

  def main(args: Array[String]): Unit = {
    // define the Kafka configuration
    val props = new util.Properties()
    props.put("bootstrap.servers", "dev111:9091")
    props.put("acks", "all")
    props.put("retries", 0: Integer)
    props.put("batch.size", 16384: Integer)
    props.put("linger.ms", 1: Integer)
    props.put("buffer.memory", 33554432: Integer)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    // create the topology builder
    val builder = new TopologyBuilder()
    builder.setSpout("stockSpout", new RandomStockQuoteSpout())
    builder.setBolt("kafkaBolt", new KafkaBolt[String, String]()
      .withProducerProperties(props)
      .withTopicSelector(new DefaultTopicSelector("stocks"))
      .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper("msgId", "quote"))).shuffleGrouping("stockSpout")

    // define the Storm configuration
    val config = new Config()
    config.setDebug(true)
    config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1: Integer)

    // start the topology
    args match {
      case Array(name, _*) =>
        config.setNumWorkers(1)
        StormSubmitter.submitTopologyWithProgressBar(name, config, builder.createTopology())

      case _ =>
        val cluster = new LocalCluster()
        val topology = builder.createTopology()
        cluster.submitTopology("stockTest", config, topology)

      /*
      Utils.sleep(40000)
      cluster.killTopology("stockTest")
      cluster.shutdown()*/
    }
  }

}
