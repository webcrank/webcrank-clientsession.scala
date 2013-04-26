package webcrank.clientsession
package internal

import java.security.SecureRandom
import javax.crypto.spec.IvParameterSpec

object Iv {
  def spec(bytes: Array[Byte]) =
    new IvParameterSpec(bytes)

  def generate(rng: SecureRandom, bytes: Int) = {
    val iv = new Array[Byte](bytes)
    rng.nextBytes(iv)
    iv
  }
}
