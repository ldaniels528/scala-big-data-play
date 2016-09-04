package com.github.ldaniels528.demo

import com.github.ldaniels528.demo.StatisticsGenerator.Statistics

/**
  * Statistics Generator
  * @author lawrence.daniels@gmail.com
  */
class StatisticsGenerator() {
  private var lastUpdate: Long = System.currentTimeMillis()
  private var prevTotal: Long = _
  private var total: Long = _

  def ++ = update(1)

  def +=(delta: Long) = update(delta)

  def update(count: Long) = {
    total += count

    val diff = (System.currentTimeMillis() - lastUpdate) / 1000d
    if (diff >= 1.0d) {
      val delta = total - prevTotal
      val rps = delta / diff

      lastUpdate = System.currentTimeMillis()
      prevTotal = total
      Some(Statistics(total, delta, rps))
    }
    else None
  }

}

/**
  * Statistics Generator Companion
  * @author lawrence.daniels@gmail.com
  */
object StatisticsGenerator {

  /**
    * Represents the generated statistics
    * @author lawrence.daniels@gmail.com
    */
  case class Statistics(records: Long, delta: Long, recordsPerSecond: Double)

}