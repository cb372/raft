import sbt._
import Keys._

object RaftBuild extends Build {

  lazy val pakkas = Project(
    id = "raft",
    base = file("."),
    settings = Defaults.defaultSettings ++ Seq(
      organization := "com.github.cb372",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.3",
      libraryDependencies ++= Seq(
          "com.typesafe.akka" %% "akka-actor" % "2.2.3",

          "com.typesafe.akka" %% "akka-testkit" % "2.2.3" % "test",
          "org.scalatest" %% "scalatest" % "2.0.RC2" % "test"
      )
    )
  )
  
}

