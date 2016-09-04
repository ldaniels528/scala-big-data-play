package com.github.ldaniels528.demo

import java.util

import org.apache.kafka.clients.producer.ProducerConfig

/**
  * Kafka Properties
  * @author lawrence.daniels@gmail.com
  */
object KafkaProperties {

  /**
    * Returns the Kafka producer configuration
    * @param brokers the given Kafka brokers
    * @return the Kafka producer configuration
    */
  def getKafkaProducerConfig(brokers: String) = {
    val props = new util.Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put("acks", "all")
    props.put("retries", 0: Integer)
    props.put("batch.size", 16384: Integer)
    props.put("linger.ms", 1: Integer)
    props.put("buffer.memory", 33554432: Integer)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props
  }

}
