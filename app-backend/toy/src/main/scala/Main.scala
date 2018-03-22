package com.github.aaronxsu

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import com.typesafe.config.ConfigFactory

import com.amazonaws.auth._
import com.amazonaws.regions._
import com.amazonaws.services.s3.AmazonS3ClientBuilder

import geotrellis.spark._
import geotrellis.spark.io._
import geotrellis.spark.io.s3._
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.raster.io.geotiff._

object WebServer {

  def readRasterData(path: String): SinglebandGeoTiff = {
    GeoTiffReader.readSingleband(path)
  }

  def readLocalFile(fileName: String) = complete {
    val path: String = s"${System.getProperty("user.dir")}/data/raster/${fileName}"
    val tiffData: SinglebandGeoTiff = readRasterData(path)
    HttpEntity(ContentTypes.`text/html(UTF-8)`, s"""<h1>${fileName}</h1>
      <h2>CRS: ${tiffData.crs}</h2>
      <h2>Extent: ${tiffData.extent}</h2>""")
  }

  def readS3File(bucket: String, key: String) = complete {
    val client = AmazonS3ClientBuilder.standard()
      .withCredentials(new DefaultAWSCredentialsProviderChain())
      .withRegion(Regions.US_EAST_1)
      .build()
    // val s3Client = new AmazonS3Client(client)
    val s3Object: S3Object = client.getObject(bucketName=bucket, key=key)
    // HttpEntity(ContentTypes.`text/html(UTF-8)`, s"""<h1>${bucket}</h1>
    //   <h2>${key}</h2>""")
    s3Object
  }

  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    val config = ConfigFactory.load()

    val routes =
      pathPrefix("api") {
        pathPrefix("imagery") {
          pathPrefix("local") {
            parameter("name") { name =>
              get { readLocalFile(name) }
            }
          } ~
          {
            pathPrefix("s3") {
              parameter("bucket", "key") { (bucket, key) =>
                get { readS3File(bucket, key) }
              }
            }
          }
        }
      }

    Http().bindAndHandle(routes, config.getString("http.address"), config.getInt("http.port"))
  }

}
