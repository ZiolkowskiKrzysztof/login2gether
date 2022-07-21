package cleverbase.login2gether.domain

import java.util.UUID

case class User(uuid: UUID = UUID.randomUUID(),
                username: String,
                password: String,
                isActive: Boolean = false,
                isSuperMate: Boolean = false,
                mates: List[UUID] = List.empty,
                secrets: List[Secret] = List.empty,
                askedForPermission: List[User] = List.empty,
                isAllowedToLogin: Boolean = false)
