name := "big-data-types"

version := "0.0.3"

scalaVersion := "2.13.3"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

//Sonatype
publishTo := sonatypePublishToBundle.value

// groupId, SCM, license information
organization := "io.github.data-tools"
homepage := Some(url("https://github.com/data-tools/big-data-types"))
scmInfo := Some(ScmInfo(url("https://github.com/data-tools/big-data-types"), "git@github.com:data-tools/big-data-types.git"))
developers := List(Developer("JavierMonton", "Javier Monton", "", url("https://github.com/JavierMonton")))
licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
publishMavenStyle := true

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.clapper" %% "grizzled-slf4j" % "1.3.4",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "com.google.cloud" % "google-cloud-bigquery" % "1.124.2"
)

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.2"

libraryDependencies ++= Seq(
  scalatest % Test,
)

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies += scalatest % "it,test"
  )