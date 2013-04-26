package webcrank.clientsession

import scalaz.scalacheck.ScalazProperties._

object ClientSessionKeySpec extends test.Spec {
 "ClientSessionKey" should {
   "satisfy equals laws" ! equal.laws[ClientSessionKey]

   "recreate from bytes" ! prop((c: ClientSessionKey) =>
     ClientSessionKey.fromBytes(c.aes, c.hmac).exists(_ == c))

   "recreate from encoded" ! prop((c: ClientSessionKey) =>
     ClientSessionKey.fromEncoded(c.aesEncoded, c.hmacEncoded).exists(_ == c))

   "recreate from keys" ! prop((c: ClientSessionKey) =>
     ClientSessionKey.fromKey(c.aesKey, c.hmacKey).exists(_ == c))

   "recreate from token" ! prop((c: ClientSessionKey) =>
     ClientSessionKey.fromToken(c.token).exists(_ == c))

   "random" ! prop((ca: ClientSessionKey, cb: ClientSessionKey) =>
     ca != cb)

   "hash recreate" ! prop((c: ClientSessionKey) =>
     ClientSessionKey.fromEncoded(c.aesEncoded, c.hmacEncoded).exists(_.hashCode == c.hashCode))

   "hash" ! prop((ca: ClientSessionKey, cb: ClientSessionKey) =>
     ca != cb || (ca == cb && ca.hashCode == cb.hashCode))

   "aes length" ! prop((c: ClientSessionKey) =>
     c.aes.length == ClientSessionKey.CipherKeyLength)

   "hmac length" ! prop((c: ClientSessionKey) =>
     c.hmac.length == ClientSessionKey.AuthKeyLength)
 }
}
