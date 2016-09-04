package com.github.ldaniels528.demo

import java.util

/**
  * Random Stock Quote Service
  * @author lawrence.daniels@gmail.com
  */
object RandomStockQuoteService {
  private val random = new util.Random()

  def getQuote = f"""{ "symbol":$nextSymbol, "price":$nextPrice%.04f, "tradeTime":$nextTime }"""

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
