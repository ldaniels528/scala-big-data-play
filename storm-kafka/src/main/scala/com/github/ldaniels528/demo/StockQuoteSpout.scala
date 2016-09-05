package com.github.ldaniels528.demo

import java.util

import com.github.ldaniels528.demo.StockQuoteSpout._
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

  override def declareOutputFields(declarer: OutputFieldsDeclarer) {
    declarer.declare(new Fields(MessageDataName))
  }

  override def nextTuple() {
    collector.emit(new Values(RandomStockQuoteService.getQuote), msgId)
    ()
  }

  override def open(conf: Conf, context: TopologyContext, collector: SpoutOutputCollector) {
    this.collector = collector
  }

  @inline
  private def msgId = util.UUID.randomUUID().toString

}

/**
  * Stock Quote Spout Companion
  * @author lawrence.daniels@gmail.com
  */
object StockQuoteSpout {
  type Conf = util.Map[_, _]

  val MessageDataName = "quote"

}