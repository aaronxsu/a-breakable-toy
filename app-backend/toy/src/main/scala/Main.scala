package com.github.aaronxsu

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import com.typesafe.config.{ConfigFactory, Config}
import geotrellis.raster.io.geotiff.reader.GeoTiffReader
import geotrellis.raster.io.geotiff._

object WebServer {

  def readRasterData(path: String): SinglebandGeoTiff = {
    GeoTiffReader.readSingleband(path)
  }

  def listLocalFile(fileName: String) = complete {
    val path = s"${System.getProperty("user.dir")}/data/raster/${fileName}"
    val tiffData = readRasterData(path)
    HttpEntity(ContentTypes.`text/html(UTF-8)`, s"""<h1>${fileName}</h1>
      <h2>CRS: ${tiffData.crs}</h2>
      <h2>Extent: ${tiffData.extent}</h2>""")
  }

  def main(args: Array[String]) {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    val config = ConfigFactory.load()

    val routes =
      pathPrefix("api") {
        pathPrefix("localfile") {
          pathPrefix(Segment) {fileName =>
            get { listLocalFile(fileName) }
          }
        }
      }

    Http().bindAndHandle(routes, config.getString("http.address"), config.getInt("http.port"))
  }

}
