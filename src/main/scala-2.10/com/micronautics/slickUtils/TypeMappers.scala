package com.micronautics.slickUtils

import java.sql.Timestamp
import com.micronautics.playUtils.JsonFormats
import grizzled.net.IPAddress
import org.joda.time.DateTime
import play.api.libs.json._
import scala.collection.immutable
import slick.driver.PostgresDriver.simple._

/** TODO remove dependency on the Postgres driver implementation */
trait TypeMappers extends JsonFormats {
  def Logger: org.slf4j.Logger

  private val comma = ","

  implicit val dateTimeMapper = MappedColumnType.base[DateTime, Timestamp](
    dateTime  => new Timestamp(dateTime.getMillis),
    timeStamp => new DateTime(timeStamp.getTime)
  )

  implicit val ipAddressMapper = MappedColumnType.base[IPAddress, String](
    ipAddress => ipAddress.toString,
    ipStr     => new IPAddress(ipStr.getBytes)
  )

  implicit def listIntMapper = MappedColumnType.base[List[Int], String](
    list =>
      list.mkString(comma),
    str => {
      try {
        val r = str.trim.split(comma).filter(_.nonEmpty).map(_.toInt).toList
        r
      } catch {
        case e: Throwable =>
          Logger.error(e.getMessage)
          Nil
      }
    }
  )

  /** Maps are persisted as {"key1":1,"key2":2} */
  implicit def mapStringIntMapper = MappedColumnType.base[immutable.Map[String, Int], String](
    map =>
      map.toList.mkString(comma),
    str =>
      Json.fromJson[Map[String, Int]](Json.parse(str)).get
  )

  /** Maps are persisted as {1:2,2:3} */
  implicit def mapLongListIntMapper = MappedColumnType.base[immutable.Map[Long, List[Int]], String](
    mapLL => {
      val json = mapLL.toList.map { case (k, v) => Json.toJson((Json.toJson(k), Json.toJson(v))) }
      Json.toJson(json).toString
    },
    str =>
      Json.fromJson[Map[Long, List[Int]]](Json.parse(str)).get
  )

  /** Lists are persisted as 1:"value1",2:"value2" */
  implicit def listLongMapper = MappedColumnType.base[List[Long], String](
    list =>
      list.mkString(comma),
    str => {
      try {
        str.trim.split(comma).filter(_.nonEmpty).map(_.toLong).toList
      } catch {
        case e: Throwable =>
          Logger.error(e.getMessage)
          List.empty[Long]
      }
    }
  )

  implicit val uuidMapper = MappedColumnType.base[java.util.UUID, String](
    uuid   => uuid.toString,
    string => java.util.UUID.fromString(string)
  )

  /** Vectors are persisted as 1:"value1",2:"value2" */
  implicit def vectorLongMapper = MappedColumnType.base[Vector[Long], String](
    list =>
      list.mkString(comma),
    str => {
      try {
        str.trim.split(comma).filter(_.nonEmpty).map(_.toLong).toVector
      } catch {
        case e: Throwable =>
          Logger.error(e.getMessage)
          Vector.empty[Long]
      }
    }
  )

  /** Lists are persisted as "key1":"value1","key2":"value2" */
  implicit def listStringMapper = MappedColumnType.base[List[String], String](
    list =>
      try {
        import play.api.libs.json.Json
        val r: String = Json.stringify(Json.toJson(list))
        r
      } catch {
        case e: Throwable =>
          Logger.error(e.getMessage)
          ""
      },
    str => {
      try {
        val jsValue = Json.parse(str)
        val result = Json.fromJson[Array[String]](jsValue).get.toList
        result
      } catch {
        case e: Throwable =>
          Logger.error(e.getMessage)
          Nil
      }
    }
  )
}
