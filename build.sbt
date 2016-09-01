import sbt._

val apacheCurator = "3.1.0"
val avroVersion = "1.8.1"
val kafkaVersion = "0.10.0.1"
val slf4jVersion = "1.7.21"
val stormVersion = "1.0.2"
val twitterBijection = "0.9.2"

name := "scala-kafka-storm-demo"

organization := "com.github.ldaniels528"

version := "0.0.1"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.8", "-unchecked",
  "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-g:vars")

// Kafka-Storm Dependencies
libraryDependencies ++= Seq(
  //
  // Avro Dependencies
  "com.twitter" %% "bijection-core" % twitterBijection,
  "com.twitter" %% "bijection-avro" % twitterBijection,
  "org.apache.avro" % "avro" % avroVersion,
  //
  // Zookeeper Dependencies
  "org.apache.curator" % "curator-framework" % apacheCurator exclude("org.slf4j", "slf4j-log4j12"),
  "org.apache.curator" % "curator-test" % apacheCurator exclude("org.slf4j", "slf4j-log4j12"),
  //
  // Kafka Dependencies
  "org.apache.kafka" %% "kafka" % kafkaVersion exclude("org.slf4j", "slf4j-log4j12"),
  "org.apache.kafka" % "kafka-clients" % kafkaVersion,
  //
  // Storm Dependencies
  "org.apache.storm" % "storm-core" % stormVersion,
  "org.apache.storm" % "storm-kafka" % stormVersion
)

// General Dependencies
libraryDependencies ++= Seq(
  "log4j" % "log4j" % "1.2.17" % "test",
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "org.slf4j" % "slf4j-log4j12" % slf4jVersion % "test"
)

// Testing Dependencies
libraryDependencies ++= Seq(
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "org.scalatest" %% "scalatest" % "2.2.2" % "test"
)
