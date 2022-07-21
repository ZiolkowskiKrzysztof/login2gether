package cleverbase.login2gether

import cats.effect.IO
import cleverbase.login2gether.domain.{Login, Permission}
import cleverbase.login2gether.utils.{Authenticator, UserDB}
import cleverbase.login2gether.utils.Parsers._
import io.circe.syntax.EncoderOps
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.circe._
import io.circe.generic.auto._

import java.util.UUID

object Routes {

  def healthCheckRoute: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root / "healthCheck" =>
        for {
          resp <- Ok("works")
        } yield resp
    }
  }

  def loginRoutes(authenticator: Authenticator): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case req @ POST -> Root / "login" =>
        for {
          login <- req.as[Login]
          auth  <- authenticator.login(login.username, login.password)
          resp  <- Ok(auth.toString())
        } yield resp

      case req @ POST -> Root / "permissions" / fromUserUUID =>
        for {
          forUserUUID <- req.as[Permission]
          response    <- authenticator.givePermission(UUID.fromString(fromUserUUID), forUserUUID.forUser)
          resp        <- Ok(response.toString)
        } yield resp
    }

  // easy way to check existing users and adding new ones
  def userRoutes(userDB: UserDB): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root / "users" =>
        for {
          resp <- Ok(userDB.getUsers.asJson)
        } yield resp

      case req @ POST -> Root / "users" =>
        for {
          login <- req.as[Login]
          _ = userDB.addNewUser(login.username, login.password)
          resp <- Ok("Added new user")
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

}
