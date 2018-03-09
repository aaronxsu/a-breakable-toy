package com.github.aaronxsu

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import com.typesafe.config.{ConfigFactory, Config}

object WebServer {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    val config = ConfigFactory.load()

    val routes =
      get {
        pathSingleSlash {
          complete {
            HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>")
          }
        }
      }

    println(config.getString("http.address"))
    println(config.getInt("http.port"))

    Http().bindAndHandle(routes, config.getString("http.address"), config.getInt("http.port"))
  }
}
