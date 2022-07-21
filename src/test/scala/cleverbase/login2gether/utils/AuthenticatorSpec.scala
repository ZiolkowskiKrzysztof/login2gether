package cleverbase.login2gether.utils

import cleverbase.login2gether.domain.User
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AuthenticatorSpec extends AnyFlatSpec with Matchers {

  val superMateLogin    = "admin"
  val superMatePassword = "password"
  val user1             = User(username = "user1", password = "password1")
  val user2             = User(username = "user2", password = "password2")

  "Authenticator" should "login superMate" in {
    val db            = new UserDB
    val authenticator = new Authenticator(db)

    authenticator.login(superMateLogin, superMatePassword).unsafeRunSync()
  }
}
