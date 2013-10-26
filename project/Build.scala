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
          "com.typesafe.akka" %% "akka-actor" % "2.2.3"
      )
    )
  )
  
}

