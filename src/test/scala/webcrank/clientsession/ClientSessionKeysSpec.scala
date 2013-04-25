package webcrank.clientsession

import scalaz.scalacheck.ScalazProperties._

object ClientSessionKeysSpec extends test.Spec {
 "ClientSessionKeys" should {
   "satisfy equals laws" ! equal.laws[ClientSessionKeys]

   "satisfy json laws" ! json.laws[ClientSessionKeys]

   "symmetric bytes" ! prop((c: ClientSessionKeys) =>
     ClientSessionKeys.fromBytes(c.aesBytes, c.hmacBytes) == c)

   "symmetric encoded" ! prop((c: ClientSessionKeys) =>
     ClientSessionKeys.fromEncoded(c.aesEncoded, c.hmacEncoded) == c)

   "symmetric keys" ! prop((c: ClientSessionKeys) =>
     ClientSessionKeys.fromKeys(c.aes, c.hmac) == c)

   "random" ! prop((ca: ClientSessionKeys, cb: ClientSessionKeys) =>
     ca != cb)

   "hash recreate" ! prop((c: ClientSessionKeys) =>
     ClientSessionKeys.fromEncoded(c.aesEncoded, c.hmacEncoded).hashCode == c.hashCode)

   "hash mod" ! prop((ca: ClientSessionKeys, cb: ClientSessionKeys) =>
     ca.copy(aes = cb.aes).hashCode == cb.copy(hmac = ca.hmac).hashCode)

   "hash" ! prop((ca: ClientSessionKeys, cb: ClientSessionKeys) =>
     ca != cb || (ca == cb && ca.hashCode == cb.hashCode))

   "aes length" ! prop((c: ClientSessionKeys) =>
     c.aesBytes.length == 32)

   "hmac length" ! prop((c: ClientSessionKeys) =>
     c.hmacBytes.length == 64)
 }
}
