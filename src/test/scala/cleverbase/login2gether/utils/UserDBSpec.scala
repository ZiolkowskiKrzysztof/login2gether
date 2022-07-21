package cleverbase.login2gether.utils

import cleverbase.login2gether.domain.User
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class UserDBSpec extends AnyFlatSpec with Matchers {

  "UserDB" should "be able to add user" in {
    val user = User(username = "username", password = "password")
    val db   = new UserDB

    db.addUser(user)

    db.getUsers.size shouldBe 2 // user + superMate
  }

  "UserDB" should "be able to update user" in {
    val user1        = User(username = "username", password = "password")
    val user2        = User(username = "admin", password = "admin")
    val updatedUser1 = user1.copy(isActive = true)
    val db           = new UserDB

    db.addUser(user1)
    db.addUser(user2)
    db.getUserByUUID(user1.uuid).get.isActive shouldBe false

    db.updateUser(updatedUser1)
    db.getUsers.size shouldBe 3 // user1 + user2 + superMate
    db.getUserByUUID(user1.uuid).get.isActive shouldBe true
  }

  "UserDB" should "be able to update multiple users" in {
    val user1        = User(username = "username", password = "password")
    val user2        = User(username = "admin", password = "admin")
    val updatedUser1 = user1.copy(isActive = true)
    val updatedUser2 = user2.copy(isActive = true)
    val db           = new UserDB

    db.addUser(user1)
    db.addUser(user2)
    db.getUserByUUID(user1.uuid).get.isActive shouldBe false
    db.getUserByUUID(user2.uuid).get.isActive shouldBe false

    db.updateUsers(List(updatedUser1, updatedUser2))

    db.getUserByUUID(user1.uuid).get.isActive shouldBe true
    db.getUserByUUID(user2.uuid).get.isActive shouldBe true
  }

}
