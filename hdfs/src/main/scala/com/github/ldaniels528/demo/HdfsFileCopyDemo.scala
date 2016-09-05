package com.github.ldaniels528.demo

import com.github.ldaniels528.demo.IOUtilities._
import com.github.ldaniels528.demo.RandomStockQuoteService._
import org.apache.hadoop.conf._
import org.apache.hadoop.fs._

import scala.util.Properties

/**
  * HDFS File Copy Demo
  * @author lawrence.daniels@gmail.com
  */
object HdfsFileCopyDemo {

  /**
    * For standalone operation
    * @param args the given commandline arguments
    */
  def main(args: Array[String]): Unit = fileCopyDemo(args)

  /**
    * Generate the quotes file
    * @param args the given commandline arguments
    * @example com.github.ldaniels528.demo.HdfsFileCopyDemo hdfs://host:port stockQuotes.js 100000
    */
  def fileCopyDemo(args: Array[String]) = {
    args match {
      case Array(uri, fileName, count, _*) =>
        generateQuotes(uri, fileName, count.toInt)
      case _ =>
        throw new IllegalArgumentException(s"${getClass.getName} <hadoop-uri> <fileName> <count>")
    }
  }

  private def generateQuotes(uri: String, filePath: String, count: Int) = {
    // setup the configuration
    System.setProperty("HADOOP_USER_NAME", Properties.userName)
    val conf = {
      val conf = new Configuration()
      conf.set("fs.defaultFS", uri)
      conf
    }

    // persist the data
    val fs = FileSystem.get(conf)
    fs.create(new Path(filePath)) use { out =>
      //(1 to 100000) map (_ => getQuote) foreach out.writeUTF
      out.write(getQuote.getBytes)
    }
  }

}
