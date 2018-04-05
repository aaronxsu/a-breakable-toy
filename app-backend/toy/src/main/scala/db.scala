package com.github.aaronxsu

import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._
import java.util.UUID
import com.github.aaronxsu.db.meta.ToyMeta

object ToyDb extends ToyMeta {
  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://database.service.aaronxsu.toy/abreakabletoy",
    "abreakabletoy",
    "abreakabletoy"
  )

  val tableName = fr"tiffs"

  val select = fr"""
    SELECT id, local_file_path, s3_file_path
    FROM
  """ ++ tableName

  def get(id: UUID): ConnectionIO[TiffPointer] = {
    (select ++ fr"WHERE id=$id").query[TiffPointer].unique
  }
}
