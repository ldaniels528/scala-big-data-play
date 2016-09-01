package com.github.ldaniels528.demo

import java.util

import org.apache.storm.spout.SpoutOutputCollector
import org.apache.storm.task.TopologyContext
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.BaseRichSpout
import org.apache.storm.tuple.{Fields, Values}
import org.slf4j.LoggerFactory

/**
  * Random Stock Quote Spout
  * @author lawrence.daniels@gmail.com
  */
class RandomStockQuoteSpout() extends BaseRichSpout {
  private var collector: SpoutOutputCollector = _
  private val random = new util.Random()

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare(new Fields("quote"))
  }

  override def nextTuple(): Unit = {
    val msgId = util.UUID.randomUUID().toString
    collector.emit(new Values(f"""{ "symbol":$nextSymbol, "price":$nextPrice%.04f, "tradeTime":$nextTime }"""), msgId)
    ()
  }

  override def open(conf: util.Map[_, _], context: TopologyContext, collector: SpoutOutputCollector): Unit = {
    this.collector = collector
  }

  @inline
  private def nextSymbol =
    s""""${symbols(random.nextInt(symbols.length))}""""

  @inline
  private def nextPrice = 100d * random.nextDouble()

  @inline
  private def nextTime = {
    val hh = random.nextInt(15) + 1
    val mm = random.nextInt(60)
    val ss = random.nextInt(60)
    s""""$ss:$mm:$ss""""
  }

  private val symbols = Seq("AAPL", "AMD", "AMZN", "FB", "GE", "GOOG", "INTC", "MSFT", "RHAT", "TWTR")

}

/**
  * Random Stock Quote Spout Companion
  * @author lawrence.daniels@gmail.com
  */
object RandomStockQuoteSpout {
  private val logger = LoggerFactory.getLogger(classOf[RandomStockQuoteSpout])

}