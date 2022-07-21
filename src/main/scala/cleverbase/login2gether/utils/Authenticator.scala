package cleverbase.login2gether.utils

import cats.effect.IO
import cleverbase.login2gether.domain.User
import org.http4s.Status

import scala.concurrent.duration._

class Authenticator(userDB: UserDB) {

  def login(username: String, password: String): IO[Status] =
    identify(username, password).flatMap {
      case Some(user) if user.isSuperMate =>
        changeActiveStatus(user)
        IO.pure(Status.Ok)
      case Some(user) =>
        for {
          _             <- askForPermission(user)
          _             <- IO.sleep(60.seconds)
          hasPermission <- checkPermission(user)
        } yield
          if (hasPermission) {
            changeActiveStatus(user)
            Status.Ok
          } else Status.Forbidden

      case _ => IO.pure(Status.BadRequest)
    }

  private def identify(username: String, password: String): IO[Option[User]] =
    IO.delay(userDB.getUsers.find(_.username == username) match {
      case Some(user) if user.password == password => Some(user)
      case _                                       => None
    })

  private def changeActiveStatus(user: User): IO[Unit] = IO.delay(userDB.updateUser(user.copy(isActive = true)))

  private def askForPermission(askingUser: User): IO[Unit] =
    IO.delay(userDB.updateUsers(askingUser.mates))

  private def checkPermission(user: User): IO[Boolean] =
    IO.delay {
      userDB.getUserByUUID(user.uuid) match {
        case None       => false
        case Some(user) => user.isAllowedToLogin
      }
    }

}
