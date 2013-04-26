package webcrank.clientsession

import java.security.SecureRandom
import scalaz.scalacheck.ScalazProperties._
import com.owtelse.codec.Base64

object ClientSessionSpec extends test.Spec {

  "ClientSession" should {
    "symmetric encrypt and decrypt" ! prop((key: ClientSessionKey, s: UnicodeString) =>
      !s.value.isEmpty ==> {
        val sessions = ClientSession(SecureRandom.getInstance("SHA1PRNG"), key)
        sessions.verify(sessions.secure(s.value)).exists(_ == s.value)
      })
  }
}
