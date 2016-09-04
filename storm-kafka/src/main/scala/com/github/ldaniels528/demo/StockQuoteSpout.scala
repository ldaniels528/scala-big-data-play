package com.github.ldaniels528.demo

import java.util

import org.apache.storm.spout.SpoutOutputCollector
import org.apache.storm.task.TopologyContext
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichSpout
import org.apache.storm.tuple.{Fields, Values}

/**
  * Stock Quote Spout
  * @author lawrence.daniels@gmail.com
  */
class StockQuoteSpout() extends BaseRichSpout {
  private var collector: SpoutOutputCollector = _

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare(new Fields("quote"))
  }

  override def nextTuple(): Unit = {
    val msgId = util.UUID.randomUUID().toString
    collector.emit(new Values(RandomStockQuoteService.getQuote), msgId)
    ()
  }

  override def open(conf: util.Map[_, _], context: TopologyContext, collector: SpoutOutputCollector): Unit = {
    this.collector = collector
  }

}
