package com.github.aaronxsu

import akka.http.scaladsl.unmarshalling._

import io.circe._
import io.circe.generic.JsonCodec
import geotrellis.raster.summary.Statistics
import java.util.UUID

@JsonCodec
case class ImageryStats[T](
  dataCells: Long,
  mean: Double,
  median: T,
  mode: T,
  stddev: Double,
  zmax: T,
  zmin: T
)

@JsonCodec
case class TiffPointer(
  id: UUID,
  local_file_path: String,
  s3_file_path: String
)


object ImageryStats {
  def apply(stats: Statistics[Int]): ImageryStats[Int] =
    new ImageryStats[Int](stats.dataCells, stats.mean, stats.median, stats.mode, stats.stddev, stats.zmax, stats.zmin)
}
