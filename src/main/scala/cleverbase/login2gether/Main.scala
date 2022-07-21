package cleverbase.login2gether

import cats.effect.{IO, IOApp}
import com.typesafe.scalalogging.Logger

object Main extends IOApp.Simple {
  val logger        = Logger("AppLogger")
  def run: IO[Unit] = Server.run
}
