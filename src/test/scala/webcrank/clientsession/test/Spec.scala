package webcrank.clientsession
package test

import org.specs2.{mutable, ScalaCheck}, mutable._

abstract class Spec
  extends Specification
  with ScalaCheck
  with ClientSessionArbitraries
  with ClientSessionProperties
