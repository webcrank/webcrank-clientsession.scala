package webcrank.clientsession

import internal._
import com.owtelse.codec.Base64
import java.security.SecureRandom
import scalaz._, Scalaz._

/**
 * This is a secure client side session implementation, providing encryption,
 * anti-tampering and anti-forgery.
 *
 * It uses an AES/CBC/PKCS5Padding cipher for encryption to ensure nobody can READ the session.
 * It uses a HMAC/SHA512 for authentication to ensure nobody can MODIFY the session.
 *
 * The encrypted session is represented as a byte string as follows:
 * {{{
 *    hmac(iv + encrypted) ++ iv ++ encrypted
 * }}}
 *
 * Where:
 *  - hmac(iv + encrypted) is exactly 64 bytes
 *  - iv is exactly 16 bytes
 *  - encrypted is arbitrary length data
 *
 * '''Note''' you should _not_ store sensitive data in this store even though
 * it is encrypted. The general approach is that the store would encrypt only
 * enough information to confirm a users identity.
 */
case class ClientSession(rng: SecureRandom, key: ClientSessionKey) {
  import ClientSessionKey.AuthKeyLength

  val IvSize = 16     // bytes (based cipher block size)

  val AuthAlgorithm = "HmacSHA512"
  val CipherType = "AES"
  val CipherAlgorithm = "AES/CBC/PKCS5Padding"
  val PrngAlgorithm = "SHA1PRNG"

  def secure[A](plaintext: String): String = {
    val encoded = Base64._encode(plaintext.getBytes("UTF-8"))
    val iv = Iv.generate(rng, IvSize)
    val encrypted = Encryption.encrypt(key, iv, encoded, rng)
    val unauthenticated = iv ++ encrypted
    val authenticated = Authentication.authenticate(key, unauthenticated)
    Base64.encode(authenticated ++ unauthenticated)
  }

  def verify(ciphertext: String): ClientSessionError \/ String = {
    val cipherbytes = Base64.decode(ciphertext)
    if (cipherbytes.length < IvSize + AuthKeyLength)
      ClientSessionIncompleteDataError.left
    else {
      val (authenticated, unauthenticated) = cipherbytes.splitAt(AuthKeyLength)
      if (!Authentication.isAuthentic(key, unauthenticated, authenticated))
        ClientSessionHmacError.left
      else {
        val (iv, encrypted) = unauthenticated.splitAt(IvSize)
        val decrypted = Encryption.decrypt(key, iv, encrypted, rng)
        new String(Base64._decode(decrypted), "UTF-8").right
      }
    }
  }
}


object ClientSession {
  def create(key: ClientSessionKey) =
    ClientSession(Random.create, key)

  // FIX IO me up!
  def manage(store: String => Unit, restore: => Option[String]) = {
    val current = restore.flatMap(ClientSessionKey.fromToken)
    val key = current.getOrElse(ClientSessionKey.generate)
    current.isEmpty.when(store(key.token))
    create(key)
  }

  def ephemeral =
    manage(_ => (), None)
}
