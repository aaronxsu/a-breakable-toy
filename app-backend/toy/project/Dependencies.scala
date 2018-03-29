import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4"
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http"   % "10.1.0"
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.5.11"
  lazy val geotrellisSpark = "org.locationtech.geotrellis" %% "geotrellis-spark" % "1.2.0-RC2"
  lazy val geotrellisS3 = "org.locationtech.geotrellis" %% "geotrellis-s3" % "1.2.0-RC2"
  lazy val geotrellisRaster = "org.locationtech.geotrellis" %% "geotrellis-raster" % "1.2.0-RC2"
  lazy val apacheSpark = "org.apache.spark" %% "spark-core" % "2.2.0" % Provided
  lazy val awsSdk = "com.amazonaws" % "aws-java-sdk" % "1.11.92"
  lazy val apacheCommonIO = "commons-io" % "commons-io" % "2.4"
  lazy val circeCore = "io.circe" %% "circe-core" % "0.9.1"
  lazy val circeGeneric = "io.circe" %% "circe-generic" % "0.9.1"
  lazy val circeGenericExtras = "io.circe" %% "circe-generic-extras" % "0.9.1"
  lazy val circeParser = "io.circe" %% "circe-parser" % "0.9.1"
  lazy val circeOptics = "io.circe" %% "circe-optics" % "0.9.1"
  lazy val akkaCirceJson = "de.heikoseeberger" %% "akka-http-circe" % "1.20.0-RC1"
}
