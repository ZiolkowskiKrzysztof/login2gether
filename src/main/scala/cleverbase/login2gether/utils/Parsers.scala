package cleverbase.login2gether.utils

import cleverbase.login2gether.domain.Login
import io.circe.{Decoder, Encoder}
import io.circe._, io.circe.generic.semiauto._

object Parsers {
  implicit val fooDecoder: Decoder[Login] = deriveDecoder[Login]
  implicit val fooEncoder: Encoder[Login] = deriveEncoder[Login]
}
