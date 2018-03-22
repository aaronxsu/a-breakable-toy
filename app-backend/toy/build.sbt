import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.aaronxsu",
      scalaVersion := "2.11.11",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "toy",
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-Yinline-warnings",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-language:higherKinds",
      "-language:postfixOps",
      "-language:existentials"),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    resolvers ++= Seq(
      "locationtech-releases" at "https://repo.locationtech.org/content/groups/releases",
      "locationtech-snapshots" at "https://repo.locationtech.org/content/groups/snapshots"
    ),
    libraryDependencies ++= Seq(
      scalaTest % Test,
      akkaHttp,
      akkaStream,
      geotrellisSpark,
      geotrellisS3,
      apacheSpark,
      awsSdk
    ),
    initialCommands in console := """
      |import geotrellis.raster._
      |import geotrellis.vector._
      |import geotrellis.proj4._
      |import geotrellis.spark._
      |import geotrellis.spark.io._
      |import geotrellis.spark.io.hadoop._
      |import geotrellis.spark.tiling._
      |import geotrellis.spark.util._
      """.stripMargin
    )
