package cleverbase.login2gether

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

object Routes {

  def healthCheckRoute: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root / "healthCheck" =>
        for {
          resp <- Ok("works")
        } yield resp
    }
  }

  def loginRoutes(): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      // todo: authentication
      case login @ POST -> Root / "login" =>
        for {
          resp <- Ok("works")
        } yield resp
    }

  def secretsRoutes(): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      // todo: see secret after auth (permitted user/owner)
      case GET -> Root / "secrets" / uuid =>
        for {
          resp <- Ok("works")
        } yield resp

      // todo: create Secret
      case secret @ POST -> Root / "secrets" =>
        for {
          resp <- Ok("works")
        } yield resp
    }

  def permissionRoutes(): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      // todo: create Permission
      case permission @ POST -> Root / "permissions" =>
        for {
          resp <- Ok("works")
        } yield resp
    }

}
