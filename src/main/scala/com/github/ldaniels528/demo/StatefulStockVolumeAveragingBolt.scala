package com.github.ldaniels528.demo

import java.util

import com.github.ldaniels528.demo.StatefulStockVolumeAveragingBolt._
import org.apache.storm.state.KeyValueState
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseStatefulBolt
import org.apache.storm.tuple.{Fields, Tuple, Values}
import org.slf4j.LoggerFactory
import java.lang.{ Long => JLong }

/**
  * Stateful Stock Volume Averaging Bolt
  * @author lawrence.daniels@gmail.com
  */
class StatefulStockVolumeAveragingBolt(name: String) extends BaseStatefulBolt[KeyValueState[String, Long]] {
  private var collector: OutputCollector = _
  private var kvState: KeyValueState[String, Long] = _
  private var sum: JLong = 0L

  override def declareOutputFields(declarer: OutputFieldsDeclarer) = declarer.declare(new Fields("value"))

  override def execute(input: Tuple): Unit = {
    sum += input.getValueByField("value").asInstanceOf[Number].longValue()
    logger.debug(s"$name sum = $sum")
    kvState.put("sum", sum)
    collector.emit(input, new Values(sum))
    collector.ack(input)
  }

  override def initState(state: KeyValueState[String, Long]): Unit = {
    kvState = state
    sum = kvState.get("sum", 0L)
    logger.debug("Initstate, sum from saved state = {} ", sum)
  }

  override def prepare(stormConf: util.Map[_, _], context: TopologyContext, collector: OutputCollector) {
    this.collector = collector
  }

}

/**
  * Stateful Stock Volume Averaging Bolt Companion
  * @author lawrence.daniels@gmail.com
  */
object StatefulStockVolumeAveragingBolt {
  private val logger = LoggerFactory.getLogger(classOf[StatefulStockVolumeAveragingBolt])

}