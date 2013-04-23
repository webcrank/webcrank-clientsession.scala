name := "webcrank-clientsession"

scalaVersion := "2.10.1"

crossScalaVersions := Seq("2.9.2", "2.9.3", "2.10.1")

releaseSettings

useGpg := true

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.0",
  "org.scalaz" %% "scalaz-scalacheck-binding" % "7.0.0" % "test",
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
)

libraryDependencies <+= scalaVersion.apply(ver => {
  val specs = if (ver startsWith "2.9") "1.12.4.1" else "1.14"
  "org.specs2" %% "specs2" % specs % "test"
})

resolvers ++= Seq(
  "oss snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "oss releases" at "http://oss.sonatype.org/content/repositories/releases"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-optimise",
  "-Yinline-warnings",
  "-feature",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:postfixOps"
)
