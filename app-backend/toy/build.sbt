import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.aaronxsu",
      scalaVersion in ThisBuild := "2.11.11",
      version      := "0.1.0-SNAPSHOT",
      scapegoatVersion in ThisBuild := "1.1.0"
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
      "locationtech-snapshots" at "https://repo.locationtech.org/content/groups/snapshots",
      Resolver.bintrayRepo("hseeberger", "maven")
    ),
    libraryDependencies ++= Seq(
      scalaTest % Test,
      akkaHttp,
      akkaStream,
      geotrellisSpark,
      geotrellisS3,
      geotrellisRaster,
      apacheSpark,
      awsSdk,
      apacheCommonIO,
      circeCore,
      circeGeneric,
      circeGenericExtras,
      circeParser,
      circeOptics,
      akkaCirceJson,
      doobieCore,
      doobieHikari,
      doobiePostgres,
      doobieSpecs,
      doobieScalatest
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )
