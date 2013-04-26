package webcrank.clientsession
package test

import java.security.SecureRandom
import javax.crypto._, spec._
import scalaz.Show, scalaz.syntax.show._
import org.scalacheck.{Pretty, Gen, Arbitrary}, Gen.oneOf

trait ClientSessionArbitraries {
  implicit def ShowPretty[A: Show](a: A): Pretty =
    Pretty(_ => a.shows)

  implicit val ClientSessionKeyArbitrary: Arbitrary[ClientSessionKey] =
    Arbitrary(Gen(params => {
      val seed = Array[Byte](20)
      params.rng.nextBytes(seed)
      val secure = new SecureRandom(seed)
      Some(ClientSessionKey.generateFrom(secure))
    }))

  implicit val ClientSessionErrorArbitrary: Arbitrary[ClientSessionError] =
    Arbitrary(Gen.oneOf(ClientSessionHmacError, ClientSessionIncompleteDataError))
}

object ClientSessionArbitrary extends ClientSessionArbitraries
