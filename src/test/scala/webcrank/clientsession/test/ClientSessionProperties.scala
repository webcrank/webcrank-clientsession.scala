package webcrank.clientsession
package test

import argonaut._, Argonaut._
import org.scalacheck.{Prop, Arbitrary}, Prop.forAll

trait ClientSessionProperties {
  // FIX Pull out to either argounaut or into at least a test library for webcrank.
  object json {
    def laws[A: EncodeJson: DecodeJson: Arbitrary] =
      forAll((a: A) => a.jencode.jdecode[A].value exists (_ == a))
  }
}

object ClientSessionProperty extends ClientSessionProperties
