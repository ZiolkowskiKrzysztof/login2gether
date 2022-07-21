package cleverbase.login2gether.utils

import cleverbase.login2gether.domain.User

import java.util.UUID
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class UserDB {
  private var users     = scala.collection.mutable.ArrayBuffer.empty[User]
  private val superMate = User(username = "admin", password = "password", isSuperMate = true)

  def getUsers: Seq[User]                            = users.toList
  def getUserByUUID(uuid: UUID): Option[User]        = users.find(_.uuid == uuid)
  def addNewUser(username: String, password: String) = addUser(User(username = username, password = password))
  def addUser(user: User): mutable.Seq[User]         = users += user.copy(mates = superMate :: user.mates)

  def updateUser(user: User): Unit = {
    val updatedUsers = (user :: users.toList.filterNot(_.uuid == user.uuid)).to(ArrayBuffer) // super strange, I know.
    users = updatedUsers
  }

  def updateUsers(users: List[User]): Seq[Unit] =
    users.map(updateUser)

  addUser(superMate)
}
