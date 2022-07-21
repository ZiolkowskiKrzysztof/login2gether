package cleverbase.login2gether.utils

import cats.effect.IO
import cleverbase.login2gether.domain.User

import scala.concurrent.duration._

class Authenticator(userDB: UserDB) {

  def login(username: String, password: String) =
    identify(username, password).map {
      case None => "login failed, wrong username or password"
      case Some(user) =>
        if (user.isSuperMate) {
          changeActiveStatus(user)
          "admin logged in"
        } else {
          for {
            _             <- askForPermission(user)
            _             <- IO.sleep(60.seconds)
            hasPermission <- checkPermission(user)
          } yield
            if (hasPermission) {
              changeActiveStatus(user)
              "user logged in"
            } else "user didn't get permission"
        }
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
