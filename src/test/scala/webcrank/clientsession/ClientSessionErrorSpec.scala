package webcrank.clientsession

import scalaz.scalacheck.ScalazProperties._

object ClientSessionErrorSpec extends test.Spec {
 "ClientSessionError" should {
   "satisfy equals laws" ! equal.laws[ClientSessionError]
 }
}
