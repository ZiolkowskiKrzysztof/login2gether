package cleverbase.login2gether.route

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.Http4sDsl

import java.util.UUID

class AuthRoute[F[_]: ConcurrentEffect]() extends Http4sDsl[F] {

  val user1 = UUID.fromString("cb401646-d08f-4f85-baa1-d2ea71737c6a")

  def validateLogin(uuid: UUID): F[UUID] =
    if (uuid == user1) Sync[F].delay(UUID.randomUUID())
    else Sync[F].raiseError(new Exception("Unknown"))

  val loginRoute: HttpRoutes[F] = {
    import org.http4s.circe.CirceEntityDecoder._
    import org.http4s.circe.CirceEntityEncoder._
    HttpRoutes.of { case post @ POST -> Root / "login" =>
      for {
        user  <- post.as[UUID]
        token <- validateLogin(user)
        resp  <- Ok(token)
      } yield resp
    }
  }
}
