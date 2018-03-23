package com.github.aaronxsu

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import com.typesafe.config.ConfigFactory

import geotrellis.spark._
import geotrellis.spark.io._
import geotrellis.spark.io.s3._
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.raster.io.geotiff._

object WebServer {

  def readRasterData(fileName: String): SinglebandGeoTiff = GeoTiffReader.readSingleband(
    s"${System.getProperty("user.dir")}/data/raster/${fileName}")

  def readRasterData(bytes: Array[Byte]): SinglebandGeoTiff = GeoTiffReader.readSingleband(bytes)

  def getBytes(fileName: String): Array[Byte] = readRasterData(fileName).toByteArray

  def getBytes(bucket: String, key: String): Array[Byte] = S3.s3Client.readBytes(bucket, key)

  def readLocalFile(fileName: String, asBytes: Option[Boolean]) = complete {
    val tiffData: SinglebandGeoTiff = readRasterData(fileName)
    lazy val fileAsBytes: Array[Byte] = getBytes(fileName)

    asBytes match {
      case Some(true) => HttpEntity(ContentTypes.`text/html(UTF-8)`, fileAsBytes)
      case _ => {
        HttpEntity(ContentTypes.`text/html(UTF-8)`, s"""<h1>${fileName}</h1>
          <h2>CRS: ${tiffData.crs}</h2>
          <h2>Extent: ${tiffData.extent}</h2>""")
      }
    }
  }

  def readS3File(bucket: String, key: String, asBytes: Option[Boolean]) = complete {
    val fileAsBytes: Array[Byte] = getBytes(bucket, key)

    asBytes match {
      case Some(true) => HttpEntity(ContentTypes.`text/html(UTF-8)`, fileAsBytes)
      case _ => {
        val tiffData: SinglebandGeoTiff = readRasterData(fileAsBytes)
        HttpEntity(ContentTypes.`text/html(UTF-8)`, s"""<h1>Bucket: ${bucket}</h1>
          <h2>Key: ${key}</h2>
          <h2>Extent: ${tiffData.crs}</h2>
          <h2>Extent: ${tiffData.extent}</h2>""")
      }
    }
  }

  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    val config = ConfigFactory.load()

    val routes =
      pathPrefix("api") {
        pathPrefix("imagery") {
          pathPrefix("local") {
            parameter("name".as[String], "asBytes".as[Boolean].?) { (name, asBytes) =>
              get { readLocalFile(name, asBytes) }
            }
          } ~
          {
            pathPrefix("s3") {
              parameter("bucket".as[String], "key".as[String], "asBytes".as[Boolean].?) { (bucket, key, asBytes) =>
                get { readS3File(bucket, key, asBytes) }
              }
            }
          }
        }
      }

    Http().bindAndHandle(routes, config.getString("http.address"), config.getInt("http.port"))
  }

}
