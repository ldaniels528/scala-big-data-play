import sbt._

val apacheCurator = "3.2.0"
val avroVersion = "1.8.1"
val kafkaVersion = "0.10.0.1"
val slf4jVersion = "1.7.21"
val sparkVersion = "2.0.0"
val sparkKafkaVersion = "1.6.2"
val stormVersion = "1.0.2"
val twitterBijection = "0.9.2"

val myScalaVersion = "2.11.8"

lazy val avro_dependencies = Seq(
  libraryDependencies ++= Seq(
    "com.twitter" %% "bijection-core" % twitterBijection,
    "com.twitter" %% "bijection-avro" % twitterBijection,
    "org.apache.avro" % "avro" % avroVersion
  ))

lazy val logging_dependencies = Seq(
  libraryDependencies ++= Seq(
    //
    // General Dependencies
    "log4j" % "log4j" % "1.2.17" % "test",
    "org.slf4j" % "slf4j-api" % slf4jVersion,
    "org.slf4j" % "slf4j-log4j12" % slf4jVersion % "test"
  ))

lazy val kafka_dependencies = Seq(
  libraryDependencies ++= Seq(
    //
    // Zookeeper Dependencies
    "org.apache.curator" % "curator-framework" % apacheCurator exclude("org.slf4j", "slf4j-log4j12"),
    "org.apache.curator" % "curator-test" % apacheCurator exclude("org.slf4j", "slf4j-log4j12"),
    //
    // Kafka Dependencies
    "org.apache.kafka" %% "kafka" % kafkaVersion exclude("org.slf4j", "slf4j-log4j12"),
    "org.apache.kafka" % "kafka-clients" % kafkaVersion
  ))

lazy val root = (project in file("."))
  .aggregate(spark_kafka)
  .aggregate(storm_kafka)
  .settings(
    name := "big-data-demoes",
    organization := "com.github.ldaniels528",
    version := "0.0.1"
  )

lazy val services = (project in file("./services"))
  .settings(logging_dependencies)
  .settings(kafka_dependencies)
  .settings(
    name := "data-services",
    organization := "com.github.ldaniels528",
    version := "0.0.1",
    scalaVersion := myScalaVersion,
    scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.8", "-unchecked", "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")
  )

lazy val storm_kafka = (project in file("./storm-kafka"))
  .aggregate(services)
  .dependsOn(services)
  .settings(logging_dependencies)
  .settings(kafka_dependencies)
  .settings(
    name := "storm-kafka-demo",
    organization := "com.github.ldaniels528",
    version := "0.0.1",
    scalaVersion := myScalaVersion,
    scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.8", "-unchecked", "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint"),
    javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-g:vars"),
    // Storm-Kafka Dependencies
    libraryDependencies ++= Seq(
      //
      // Storm Dependencies
      "org.apache.storm" % "storm-core" % stormVersion,
      "org.apache.storm" % "storm-kafka" % stormVersion
    ))

lazy val spark_kafka = (project in file("./spark-kafka"))
  .aggregate(services)
  .dependsOn(services)
  .settings(logging_dependencies)
  .settings(kafka_dependencies)
  .settings(
    name := "spark-kafka-demo",
    organization := "com.github.ldaniels528",
    version := "0.0.1",
    scalaVersion := myScalaVersion,
    scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.8", "-unchecked", "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint"),
    javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-g:vars"),
    // Spark-Kafka Dependencies
    libraryDependencies ++= Seq(
      //
      // Spark Dependencies
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-streaming" % sparkVersion,
      "org.apache.spark" %% "spark-streaming-kafka" % sparkKafkaVersion
    ))

// loads the jvm project at sbt startup
onLoad in Global := (Command.process("project spark_kafka", _: State)) compose (onLoad in Global).value
