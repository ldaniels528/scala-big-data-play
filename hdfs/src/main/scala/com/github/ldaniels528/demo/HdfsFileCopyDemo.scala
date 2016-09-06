package com.github.ldaniels528.demo

import java.io.{BufferedOutputStream, File, FileInputStream, PrintWriter}

import com.github.ldaniels528.demo.DataUtilities._
import com.github.ldaniels528.demo.IOUtilities._
import com.github.ldaniels528.demo.RandomStockQuoteService._
import org.apache.hadoop.conf._
import org.apache.hadoop.fs._
import org.apache.hadoop.io.IOUtils
import org.slf4j.LoggerFactory

import scala.util.Properties

/**
  * HDFS File Copy Demo
  * @author lawrence.daniels@gmail.com
  */
object HdfsFileCopyDemo {
  private val logger = LoggerFactory.getLogger(getClass)
  private val defaultArgs = Seq("hdfs://localhost:9000/", "stockQuotes.js", "100000")

  /**
    * For standalone operation
    * @param args the given commandline arguments
    */
  def main(args: Array[String]): Unit = fileCopyDemo(args)

  /**
    * Generate the quotes file
    * @param args the given commandline arguments
    * @example com.github.ldaniels528.demo.HdfsFileCopyDemo hdfs://localhost:9000/ stockQuotes.js 100000
    */
  def fileCopyDemo(args: Array[String]) = {
    val myArgs = args.takeRight(3)
    val Seq(uri, fileName, count, _*) = defaultArgs.zipWithIndex map { case (default, n) =>
      myArgs.maybe(n) getOrElse default
    }

    // generate the quotes
    generateQuotes(uri, fileName, count.toInt)
  }

  /**
    * Generate the quotes file and copies it to HDFS
    * @param uri      the given HDFS URI
    * @param fileName the name of the destination file
    * @param count    the number of records to generate
    */
  private def generateQuotes(uri: String, fileName: String, count: Int) = {
    // setup the configuration
    System.setProperty("HADOOP_USER_NAME", Properties.userName)
    val conf = {
      val conf = new Configuration()
      conf.set("fs.defaultFS", uri)
      conf
    }

    // generate the file
    val stockQuotesFile = File.createTempFile("hdfs", "quotes")
    logger.info(s"Generating temporarily stock quote file - ${stockQuotesFile.getAbsolutePath}")
    new PrintWriter(stockQuotesFile) use { out =>
      (1 to count) map (_ => getQuote) foreach out.println
    }

    try {
      // copy the file to HDFS
      val fs = FileSystem.get(conf)
      new BufferedOutputStream(fs.create(new Path(fileName))) use { out =>
        new FileInputStream(stockQuotesFile) use { in =>
          IOUtils.copyBytes(in, out, conf)
        }
      }

    } finally {
      // delete the file on exit
      stockQuotesFile.deleteOnExit()
    }
  }

}
