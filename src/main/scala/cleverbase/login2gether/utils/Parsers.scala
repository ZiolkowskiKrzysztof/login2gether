package cleverbase.login2gether.utils

import cats.effect.IO
import cleverbase.login2gether.domain.{Login, Permission, User}
import io.circe.generic.auto._
import org.http4s.circe.{jsonEncoderOf, jsonOf}

object Parsers {
  implicit val loginDecoder      = jsonOf[IO, Login]
  implicit val permissionDecoder = jsonOf[IO, Permission]
  implicit val userEncoder       = jsonEncoderOf[IO, User]
}
