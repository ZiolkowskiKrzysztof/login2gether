package cleverbase.login2gether.utils

import cats.effect.IO
import cleverbase.login2gether.domain.User
import com.typesafe.scalalogging.LazyLogging
import org.http4s.Status

import java.util.UUID
import scala.concurrent.duration._

class Authenticator(userDB: UserDB) extends LazyLogging {

  def login(username: String, password: String): IO[Status] = {
    logger.info(s"login($username, $password)")
    identify(username, password).flatMap {
      case Some(user) if user.isSuperMate =>
        // superMate doesn't have to ask anyone for permission
        changeActivityStatus(user)
        IO.pure(Status.Ok)
      case Some(user) =>
        // ask for permission
        for {
          _ <- askForPermission(user)
          _ = logger.info(s"$User has asked for permission, 30 sec left")
          _ <- IO.sleep(30.seconds)
          _ = logger.info(s"time's up")
          hasPermission <- checkPermission(user)
        } yield
          if (hasPermission) {
            changeActivityStatus(user)
            Status.Ok
          } else Status.Forbidden

      case _ => IO.pure(Status.BadRequest)
    }
  }

  def givePermission(fromUserUUID: UUID, toUserUUID: UUID): IO[Status] = {
    logger.info(s"givePermission($fromUserUUID, $toUserUUID)")
    IO.delay {
      userDB.getUserByUUID(fromUserUUID) match {
        case Some(fromUser) =>
          userDB.getUserByUUID(toUserUUID) match {
            case Some(toUser) =>
              if (fromUser.askedForPermission.contains(toUser) && fromUser.isActive) {
                logger.info(s"givePermission: mate is active and has requested user on a list")
                userDB.updateUser(toUser.copy(isAllowedToLogin = true))
                Status.Ok
              } else
                logger.info(s"givePermission: mate is not active or hasn't got requested user on a list")
              Status.Forbidden
            case None => Status.Forbidden
          }
        case _ => Status.Forbidden
      }
    }
  }

  // checks only if username & password combination is in DB
  private def identify(username: String, password: String): IO[Option[User]] =
    IO.delay(userDB.getUsers.find(_.username == username) match {
      case Some(user) if user.password == password => Some(user)
      case _                                       => None
    })

  private def changeActivityStatus(user: User): Unit = userDB.updateUser(user.copy(isActive = true))

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
