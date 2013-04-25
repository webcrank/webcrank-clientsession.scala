package webcrank

package object clientsession {
  val CipherBlockSize = 16 // bytes
  val CipherKeyLength = 32 // bytes
  val AuthKeyLength = 64   // bytes
  val IvBlockSize = 16 // bytes (cipher block size)

  val AuthAlgorithm = "HmacSHA512"
  val CipherType = "AES"
  val CipherAlgorithm = "AES/CTR/NOPADDING"
  val PrngAlgorithm = "SHA1PRNG"

}
