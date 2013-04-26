package webcrank.clientsession

import internal._

import com.owtelse.codec.Base64

import java.security.SecureRandom
import javax.crypto.SecretKey
import java.util.Arrays

import scalaz._, Scalaz._

// FIX Swap to using ByteString implementation. No release at the moment.
sealed trait ClientSessionKey {
  val aes: Array[Byte]

  val hmac: Array[Byte]

  lazy val token =
    Base64.encode(aes ++ hmac)

  lazy val aesKey =
    Encryption.key(aes)

  lazy val hmacKey =
    Authentication.key(hmac)

  lazy val aesEncoded =
    Base64.encode(aes)

  lazy val hmacEncoded =
    Base64.encode(hmac)

  override def hashCode =
    Arrays.hashCode(aes) * 31 +
    Arrays.hashCode(hmac)

  override def equals(o: Any) =
    o.isInstanceOf[ClientSessionKey] && {
      val k = o.asInstanceOf[ClientSessionKey]
      Arrays.equals(aes, k.aes) &&
      Arrays.equals(hmac, k.hmac)
    }
}

object ClientSessionKey {
  val CipherKeyLength = 32 // bytes
  val AuthKeyLength = 64   // bytes

  private[webcrank] def build(
    aesData: Array[Byte],
    hmacData: Array[Byte]
  ): ClientSessionKey =
    new ClientSessionKey {
      val aes = aesData
      val hmac = hmacData
    }

  def fromToken(token: String): Option[ClientSessionKey] =
    Base64.decode(token).splitAt(CipherKeyLength) match {
      case (aes, hmac) => fromBytes(aes, hmac)
    }

  def fromEncoded(aes: String, hmac: String): Option[ClientSessionKey] =
    fromBytes(Base64.decode(aes), Base64.decode(hmac))

  def fromKey(aes: SecretKey, hmac: SecretKey): Option[ClientSessionKey] =
    fromBytes(aes.getEncoded, hmac.getEncoded)

  def fromBytes(aes: Array[Byte], hmac: Array[Byte]): Option[ClientSessionKey] =
    (aes.length == CipherKeyLength && hmac.length == AuthKeyLength).option(build(aes, hmac))

  def generate: ClientSessionKey =
    generateFrom(Random.create)

  def generateFrom(random: SecureRandom): ClientSessionKey = {
    val keydata = new Array[Byte](CipherKeyLength + AuthKeyLength)
    random.nextBytes(keydata)
    val (cipherdata, authdata) = keydata.splitAt(CipherKeyLength)
    build(cipherdata, authdata)
  }

  implicit val ClientSessionKeyShow  =
    Show.showFromToString[ClientSessionKey]

  implicit val ClientSessionKeyEqual =
    Equal.equalA[ClientSessionKey]
}
