package com.micronautics.slickUtils

import java.sql.Timestamp
import grizzled.net.IPAddress
import org.joda.time.DateTime
import play.api.data.validation.ValidationError
import play.api.libs.json._
import scala.collection.immutable
import slick.driver.PostgresDriver.api._

/** TODO remove dependency on the Postgres driver implementation */
trait TypeMappers {
  val Logger = org.slf4j.LoggerFactory.getLogger("slickUtils")
  private val comma = ","

  implicit val dateTimeMapper = MappedColumnType.base[DateTime, Timestamp](
    dateTime  => new Timestamp(dateTime.getMillis),
    timeStamp => new DateTime(timeStamp.getTime)
  )

  implicit val ipAddressMapper = MappedColumnType.base[IPAddress, String](
    ipAddress => ipAddress.toString,
    ipStr     => new IPAddress(ipStr.getBytes)
  )

  /** Persists to "1.0,2.0,3.0,4.0" */
  implicit def listDoubleMapper = MappedColumnType.base[List[Double], String](
    list =>
      list.mkString(comma),
    str => {
      try {
        val r = str.trim.split(comma).filter(_.nonEmpty).map(_.toDouble).toList
        r
      } catch {
        case e: Throwable =>
          Logger.error(e.getMessage)
          Nil
      }
    }
  )

  /** Persists to "1,2,3,4" */
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

  implicit def tuple2Reads[A, B](implicit aReads: Reads[A], bReads: Reads[B]): Reads[(A, B)] = Reads[(A, B)] {
    case JsArray(arr) if arr.size == 2 => for {
      a <- aReads.reads(arr.head)
      b <- bReads.reads(arr(1))
    } yield (a, b)
    case _ => JsError(Seq(JsPath() -> Seq(ValidationError("Expected array of two elements"))))
  }

  implicit def tuple2Writes[A, B](implicit aWrites: Writes[A], bWrites: Writes[B]): Writes[(A, B)] = new Writes[(A, B)] {
    def writes(tuple: (A, B)) = JsArray(Seq(aWrites.writes(tuple._1), bWrites.writes(tuple._2)))
  }

  implicit val mapLongIntReads: Reads[Map[Long, Int]] =
    new Reads[Map[Long, Int]] {
      def reads(json: JsValue) =
        json.validate[Map[String, Int]].map(_.map {
          case (key, value) => key.toLong -> value
        })
    }

  implicit val mapLongListIntReads: Reads[Map[Long, List[Int]]] = {
    new Reads[Map[Long, List[Int]]] {
      def reads(json: JsValue) =
        json.validate[Map[String, List[Int]]].map(_.map {
          case (key, value) => key.toLong -> value
        })
    }
  }

  implicit val mapStringIntWrites = new Writes[Map[String, Int]] {
    def writes(mapStringInt: Map[String, Int]) = {
      Json.arr(mapStringInt.map {
        case (key, value) => Json.obj(key -> value)
      }.toSeq)
    }
  }

  implicit val mapStringLongWrites = new Writes[Map[String, Long]] {
    def writes(mapStringLong: Map[String, Long]) = {
      Json.arr(mapStringLong.map {
        case (key, value) => Json.obj(key -> value)
      }.toSeq)
    }
  }
}
