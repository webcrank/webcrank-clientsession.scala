name := "webcrank-clientsession"

scalaVersion := "2.10.1"

crossScalaVersions := Seq("2.9.2", "2.9.3", "2.10.1")

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.0",
  "com.owtelse.codec" % "base64" % "1.0.6",
  "org.scalaz" %% "scalaz-scalacheck-binding" % "7.0.0" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
)

libraryDependencies <+= scalaVersion.apply(ver => {
  val specs = if (ver startsWith "2.9") "1.12.4.1" else "1.14"
  "org.specs2" %% "specs2" % specs % "test"
})

webcrank.standard(
  "webcrank.password",
  "http://webcrank.io",
  "git://github.com/webcrank/webcrank-clientsession.scala.git",
  webcrank.licenses.BSD3,
  Seq(
    webcrank.developer("mth", "Mark Hibberd", Some("http://mth.io"))
  )
)
