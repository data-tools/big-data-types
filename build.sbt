name := "big-data-types"

//used to build Sonatype releases
version := "0.1.1"

lazy val scala213 = "2.13.3"
lazy val scala212 = "2.12.12"
lazy val scala211 = "2.11.12"
lazy val supportedScalaVersions = List(scala213, scala212)
scalaVersion := scala212

crossVersionSharedSources

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

//Dependencies
lazy val commonDependencies = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.clapper" %% "grizzled-slf4j" % "1.3.4",
  "com.chuusai" %% "shapeless" % "2.3.3")

lazy val bigqueryDependencies = Seq(
  "com.google.cloud" % "google-cloud-bigquery" % "1.124.2"
)

lazy val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % "3.0.1" % Provided,
  "org.apache.spark" %% "spark-sql" % "3.0.1" % Provided
)
lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.2"

libraryDependencies ++= Seq(
  scalatest % Test,
)

//Project settings
lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    crossScalaVersions := supportedScalaVersions,
    crossVersionSharedSources
    //libraryDependencies ++= commonDependencies
  ).aggregate(
      core,
      bigquery,
      spark
    )

lazy val core = (project in file("core")).settings(
  crossScalaVersions := supportedScalaVersions,
  crossVersionSharedSources,
  libraryDependencies ++= commonDependencies ++ Seq(scalatest % Test)
)

lazy val bigquery = (project in file("bigquery")).configs(IntegrationTest).settings(
  Defaults.itSettings,
  crossScalaVersions := supportedScalaVersions,
  crossVersionSharedSources,
  libraryDependencies ++= bigqueryDependencies ++ Seq(scalatest % "it,test")

).dependsOn(core % "it->test;test->test;compile->compile")

lazy val spark = (project in file("spark")).settings(
  crossScalaVersions := List(scala212),
  crossVersionSharedSources,
  libraryDependencies ++= sparkDependencies ++ Seq(scalatest % Test)
).dependsOn(core % "test->test;compile->compile")


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