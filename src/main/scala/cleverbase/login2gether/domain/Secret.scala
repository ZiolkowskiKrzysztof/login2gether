package cleverbase.login2gether.domain

import java.util.UUID

case class Secret(uuid: UUID = UUID.randomUUID(), text: String)
