package webcrank.clientsession
package internal

import java.util.Arrays
import java.security._
import javax.crypto._
import javax.crypto.spec._

object Authentication {
  val AuthAlgorithm = "HmacSHA512"

  def key(data: Array[Byte]) =
    new SecretKeySpec(data, AuthAlgorithm)

  def authenticator(key: ClientSessionKey) = {
    val auth = Mac.getInstance(AuthAlgorithm)
    auth.init(key.hmacKey)
    auth
  }

  def authenticate(key: ClientSessionKey, data: Array[Byte]) = {
    val auth = authenticator(key)
    auth.update(data)
    auth.doFinal
  }

  def isAuthentic(key: ClientSessionKey, data: Array[Byte], mac: Array[Byte]) = {
    val authed = authenticate(key, data)
    Arrays.equals(authed, mac)
  }
}
