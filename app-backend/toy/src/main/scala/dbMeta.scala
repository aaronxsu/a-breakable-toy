package com.github.aaronxsu.db.meta

import java.util.UUID

import doobie._
import doobie.implicits._

trait ToyMeta {
  implicit val UUIDMeta: Meta[UUID] =
    Meta[String].nxmap(UUID.fromString, _.toString)
}
