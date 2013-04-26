package webcrank.clientsession
package internal

import java.security.SecureRandom
import javax.crypto.spec.IvParameterSpec

object Random {
  val RandomAlgorithm = "SHA1PRNG"

  def create =
    SecureRandom.getInstance(RandomAlgorithm)
}
