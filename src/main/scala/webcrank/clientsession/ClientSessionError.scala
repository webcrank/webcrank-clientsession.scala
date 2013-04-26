package webcrank.clientsession

import scalaz._, Scalaz._

sealed trait ClientSessionError
private object ClientSessionHmacError extends ClientSessionError
private object ClientSessionIncompleteDataError extends ClientSessionError

object ClientSessionError {
  implicit val ClientSessionErrorShow  =
    Show.showFromToString[ClientSessionError]

  implicit val ClientSessionErrorEqual =
    Equal.equalA[ClientSessionError]
}
