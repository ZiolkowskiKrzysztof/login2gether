package cleverbase.login2gether.utils

import cats.effect.unsafe.implicits.global
import cleverbase.login2gether.domain.User
import org.http4s.Status
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AuthenticatorSpec extends AnyFlatSpec with Matchers {

  val superMateLogin    = "admin"
  val superMatePassword = "password"
  val user              = User(username = "user", password = "password")

  "Authenticator" should "login superMate" in {
    val db            = new UserDB
    val authenticator = new Authenticator(db)

    authenticator.login(superMateLogin, superMatePassword).unsafeRunSync() shouldBe Status.Ok
  }

  "Authenticator" should "return BadRequest for totally wrong credentials" in {
    val db            = new UserDB
    val authenticator = new Authenticator(db)

    authenticator.login("blah blah", "blah blah").unsafeRunSync() shouldBe Status.BadRequest
  }

  "Authenticator" should "return Ok for positive user scenario" in {
    val db            = new UserDB
    val superMateUUID = db.getUsers.head.uuid
    val updatedUser   = user.copy(isAllowedToLogin = true)
    val authenticator = new Authenticator(db)
    db.addUser(user)

    authenticator.login(user.username, user.password).unsafeRunSync() shouldBe Status.BadRequest

    // ayay

    db.getUserByUUID(superMateUUID).get.askedForPermission.size shouldBe 1
    db.updateUser(updatedUser)

  }
}
