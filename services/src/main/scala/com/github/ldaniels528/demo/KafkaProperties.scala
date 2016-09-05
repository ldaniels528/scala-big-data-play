package com.github.ldaniels528.demo

import java.util

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig

/**
  * Kafka Properties
  * @author lawrence.daniels@gmail.com
  */
object KafkaProperties {

  /**
    * Returns the Kafka consumer configuration
    * @param brokers the given Kafka brokers
    * @return the Kafka consumer configuration
    */
  def getKafkaConsumerConfig(brokers: String, groupID: String = "dev") = {
    val props = new util.Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.ACKS_CONFIG, "all")
    props.put(ProducerConfig.RETRIES_CONFIG, 0: Integer)
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384: Integer)
    props.put(ProducerConfig.LINGER_MS_CONFIG, 1: Integer)
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432: Integer)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000: Integer)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupID)
    props
  }

  /**
    * Returns the Kafka producer configuration
    * @param brokers the given Kafka brokers
    * @return the Kafka producer configuration
    */
  def getKafkaProducerConfig(brokers: String) = {
    val props = new util.Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ProducerConfig.ACKS_CONFIG, "all")
    props.put(ProducerConfig.RETRIES_CONFIG, 0: Integer)
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384: Integer)
    props.put(ProducerConfig.LINGER_MS_CONFIG, 1: Integer)
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432: Integer)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props
  }

}
