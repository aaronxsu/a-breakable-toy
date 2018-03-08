import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4"

  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http"   % "10.1.0"
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.5.11"
}
