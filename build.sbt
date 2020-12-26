name := "big-data-types"

//used to build Sonatype releases
version := "0.0.4"

lazy val scala213 = "2.13.3"
lazy val scala212 = "2.12.12"
lazy val scala211 = "2.11.12"
lazy val supportedScalaVersions = List(scala213, scala212, scala211)


//scalaVersion := scala213
commonSettings
//crossScalaVersions := Nil

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

inThisBuild(Seq(
  organization := "io.github.data-tools",
  scalaVersion := scala213,
  crossScalaVersions :=List(scala213),
))

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    //crossScalaVersions := supportedScalaVersions,
    Defaults.itSettings,
    libraryDependencies += scalatest % "it,test"
  )

lazy val commonSettings = crossVersionSharedSources


lazy val crossVersionSharedSources: Seq[Setting[_]] =
  Seq(Compile, Test).map { sc =>
    (unmanagedSourceDirectories in sc) ++= {
      (unmanagedSourceDirectories in sc ).value.flatMap { dir: File =>
        if(dir.getName != "scala") Seq(dir)
        else
          CrossVersion.partialVersion(scalaVersion.value) match {
            case Some((2, y)) if y >= 13 => Seq(new File(dir.getPath + "_2.13+"))
            case Some((2, y)) if y >= 11 => Seq(new File(dir.getPath + "_2.13-"))
          }
      }
    }
  }