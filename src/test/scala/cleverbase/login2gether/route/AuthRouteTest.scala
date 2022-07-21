package cleverbase.login2gether.route

import cats.effect.IO
import org.scalatest.funsuite.AnyFunSuite
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._

import java.util.UUID
import scala.concurrent.ExecutionContext
import cats.syntax.all._
import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._
import cats.effect._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._

class AuthRouteTest extends AnyFunSuite {
  implicit val contextShift     = IO.contextShift(ExecutionContext.global)
  implicit val timer            = IO.timer(ExecutionContext.global)
  implicit val concurrentEffect = IO.ioConcurrentEffect(contextShift)

  val route = new AuthRoute[IO]()

  test("logging in for user1") {
    val req    = Request[IO](method = POST, uri"/login").withEntity(route.user1)
    val result = route.loginRoute.run(req)
    val x      = result.value.unsafeRunSync().get
    assert(x.status == Status.Ok)
    x.as[UUID].unsafeRunSync() // Is a valid UUID
  }

}
