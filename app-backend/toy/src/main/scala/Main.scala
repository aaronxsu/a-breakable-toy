package com.github.aaronxsu

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import com.typesafe.config.ConfigFactory
import io.circe._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._

import geotrellis.spark._
import geotrellis.spark.io._
import geotrellis.spark.io.s3._
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.raster.io.geotiff._
import geotrellis.raster.render.ColorMap

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

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
        Future{ImageryStats(tiffData.tile.statistics.get)}
      }
    }
  }

  def readS3File(bucket: String, key: String, asBytes: Option[Boolean]) = complete {
    val fileAsBytes: Array[Byte] = getBytes(bucket, key)

    asBytes match {
      case Some(true) => HttpEntity(ContentTypes.`text/html(UTF-8)`, fileAsBytes)
      case _ => {
        val tiffData: SinglebandGeoTiff = readRasterData(fileAsBytes)
        Future{ImageryStats(tiffData.tile.statistics.get)}
      }
    }
  }

  def singleBandTifToPngBytes(tiffData: SinglebandGeoTiff): Array[Byte] = {
    val s = "viridis"
    val pixels = 512
    val zmin = 0
    val zmax = 255
    val interval = 20
    val colorMap = ColorMap(
      (zmin to zmax by interval).toArray,
      ColorOptions.fromString(s).getOrElse(ColorOptions.default)
    )
    tiffData.tile.resample(pixels, pixels).renderPng(colorMap).bytes
  }

  def readLocalFileAsPng(fileName: String) = complete {
    HttpEntity(MediaTypes.`image/png`, singleBandTifToPngBytes(readRasterData(fileName)))
  }

  def readS3FileAsPng(bucket: String, key: String) = complete {
    HttpEntity(MediaTypes.`image/png`, singleBandTifToPngBytes(readRasterData(getBytes(bucket, key))))
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
        } ~
        pathPrefix("png") {
          pathPrefix("local") {
            parameter("name".as[String]) { (name) =>
              get { readLocalFileAsPng(name) }
            }
          } ~
          pathPrefix("s3") {
            parameter("bucket".as[String], "key".as[String]) { (bucket, key) =>
              get { readS3FileAsPng(bucket, key) }
            }
          }
        }
      }

    Http().bindAndHandle(routes, config.getString("http.address"), config.getInt("http.port"))
  }

}
