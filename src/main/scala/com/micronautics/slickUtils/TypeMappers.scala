package com.micronautics.slickUtils

import java.sql.Timestamp
import com.micronautics.playUtils.JsonFormats
import grizzled.net.IPAddress
import org.joda.time.DateTime
import play.api.libs.json._
import scala.collection.immutable
import scala.slick.lifted.{BaseTypeMapper, MappedTypeMapper, TypeMapper}

// TODO add typemapper for UUID/String
trait TypeMappers extends JsonFormats {
  val Logger = org.slf4j.LoggerFactory.getLogger("slickUtils")
  private val comma = ","

  implicit val dateTimeMapper: TypeMapper[DateTime] = MappedTypeMapper.base[DateTime, Timestamp](
    dt => new Timestamp(dt.getMillis),
    ts => new DateTime(ts.getTime)
  )

  implicit val ipAddressMapper: TypeMapper[IPAddress] = MappedTypeMapper.base[IPAddress, String](
    ipAddress => ipAddress.toString,
    ipStr => new IPAddress(ipStr.getBytes)
  )

  implicit def listIntMapper: BaseTypeMapper[List[Int]] = MappedTypeMapper.base[List[Int], String](
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
  implicit def mapStringIntMapper: BaseTypeMapper[immutable.Map[String, Int]] = MappedTypeMapper.base[immutable.Map[String, Int], String](
    map =>
      map.toList.mkString(comma),
    str =>
      Json.fromJson[Map[String, Int]](Json.parse(str)).get
  )

  /** Maps are persisted as {1:2,2:3} */
  implicit def mapLongListIntMapper: BaseTypeMapper[immutable.Map[Long, List[Int]]] = MappedTypeMapper.base[immutable.Map[Long, List[Int]], String](
    mapLL => {
      val json = mapLL.toList.map { case (k, v) => Json.toJson((Json.toJson(k), Json.toJson(v))) }
      Json.toJson(json).toString()
    },
    str =>
      Json.fromJson[Map[Long, List[Int]]](Json.parse(str)).get
  )

  /** Lists are persisted as 1:"value1",2:"value2" */
  implicit def listLongMapper: BaseTypeMapper[List[Long]] = MappedTypeMapper.base[List[Long], String](
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

  /** Vectors are persisted as 1:"value1",2:"value2" */
  implicit def vectorLongMapper: BaseTypeMapper[Vector[Long]] = MappedTypeMapper.base[Vector[Long], String](
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
  implicit def listStringMapper: BaseTypeMapper[List[String]] = MappedTypeMapper.base[List[String], String](
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
