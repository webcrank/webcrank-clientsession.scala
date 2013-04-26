package webcrank.clientsession
package internal

import java.security._
import javax.crypto._
import javax.crypto.spec._

object Encryption {
  val CipherType = "AES"
  val CipherAlgorithm = "AES/CBC/PKCS5Padding"

  def key(data: Array[Byte]) =
    new SecretKeySpec(data, CipherType)

  def encrypt(key: ClientSessionKey, iv: Array[Byte], data: Array[Byte], rng: SecureRandom) =
    cipher(key, iv, Cipher.ENCRYPT_MODE, rng).doFinal(data)

  def decrypt(key: ClientSessionKey, iv: Array[Byte], data: Array[Byte], rng: SecureRandom) =
    cipher(key, iv, Cipher.DECRYPT_MODE, rng).doFinal(data)

  def cipher(key: ClientSessionKey, iv: Array[Byte], mode: Int, rng: SecureRandom) = {
    var parameters = Iv.spec(iv)
    val cipher = Cipher.getInstance(CipherAlgorithm)
    cipher.init(mode, key.aesKey, parameters, rng)
    cipher
  }
}
