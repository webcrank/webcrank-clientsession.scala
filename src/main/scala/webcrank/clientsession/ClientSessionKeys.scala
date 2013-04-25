package webcrank.clientsession

import argonaut._, Argonaut._

import com.owtelse.codec.Base64

import java.security.SecureRandom
import javax.crypto._, spec._
import java.util.Arrays

import scalaz._, Scalaz._

case class ClientSessionKeys(aes: SecretKey, hmac: SecretKey) {
  lazy val aesBytes =
    aes.getEncoded

  lazy val hmacBytes =
    hmac.getEncoded

  lazy val aesEncoded =
    Base64.encode(aesBytes)

  lazy val hmacEncoded =
    Base64.encode(hmacBytes)

  override def hashCode = {
    val h1 = aes.getFormat.hashCode
    val h2 = h1 * 31 + aes.getAlgorithm.hashCode
    val h3 = h2 * 31 + Arrays.hashCode(aes.getEncoded)
    val h4 = h3 * 31 + hmac.getFormat.hashCode
    val h5 = h4 * 31 + hmac.getAlgorithm.hashCode
    h5 * 31 + Arrays.hashCode(hmac.getEncoded)
  }

  override def equals(o: Any) =
    o.isInstanceOf[ClientSessionKeys] && {
      val k = o.asInstanceOf[ClientSessionKeys]
      aes.getFormat == k.aes.getFormat &&
      aes.getAlgorithm == k.aes.getAlgorithm &&
      Arrays.equals(aesBytes, k.aesBytes) &&
      hmac.getFormat == k.hmac.getFormat &&
      hmac.getAlgorithm == k.hmac.getAlgorithm &&
      Arrays.equals(hmacBytes, k.hmacBytes)
    }
}

object ClientSessionKeys {
  def fromEncoded(aes: String, hmac: String) =
    fromBytes(Base64.decode(aes), Base64.decode(hmac))

  def fromBytes(aes: Array[Byte], hmac: Array[Byte]) =
    fromKeys(new SecretKeySpec(aes, CipherType), new SecretKeySpec(hmac, AuthAlgorithm))

  def fromKeys(aes: SecretKey, hmac: SecretKey) =
    ClientSessionKeys(aes, hmac)

  def generate: ClientSessionKeys =
    generateFrom(SecureRandom.getInstance(PrngAlgorithm))

  def generateFrom(random: SecureRandom): ClientSessionKeys = {
    val keydata = new Array[Byte](CipherKeyLength + AuthKeyLength)
    random.nextBytes(keydata)
    val (cipherdata, authdata) = keydata.splitAt(CipherKeyLength)
    fromBytes(cipherdata, authdata)
  }

  implicit def ClientSessionKeysEncodeJson: EncodeJson[ClientSessionKeys] =
    jencode2L((c: ClientSessionKeys) => (c.aesEncoded, c.hmacEncoded))("aes", "hmac")

  implicit def ClientSessionKeysDecodeJson: DecodeJson[ClientSessionKeys] =
    jdecode2L((aes: String, hmac: String) => (aes, hmac))("aes", "hmac") map ({
      case (aes, hmac) => fromEncoded(aes, hmac)
    })

  implicit val ClientSessionKeysShow  =
    Show.showFromToString[ClientSessionKeys]

  implicit val ClientSessionKeysEqual =
    Equal.equalA[ClientSessionKeys]
}
