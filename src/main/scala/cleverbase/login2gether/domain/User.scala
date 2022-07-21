package cleverbase.login2gether.domain

import java.util.UUID

case class User(uuid: UUID,
                username: String,
                password: String,
                isActive: Boolean,
                isSuperMate: Boolean,
                mates: List[User],
                secrets: List[Secret])
