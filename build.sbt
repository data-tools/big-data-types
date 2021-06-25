//used to build Sonatype releases
lazy val versionNumber = "0.3.5"
lazy val projectName = "big-data-types"
version := versionNumber
name := projectName

lazy val scala213 = "2.13.6"
lazy val scala212 = "2.12.14"
lazy val scala3 = "3.0.0"
lazy val supportedScalaVersions = List(scala3, scala213, scala212)
scalaVersion := scala213

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x                             => MergeStrategy.first
}

//Sonatype
// groupId, SCM, license information
lazy val publishSettings = Seq(
  version := versionNumber,
  publishTo := sonatypePublishToBundle.value,
  organization := "io.github.data-tools",
  homepage := Some(url("https://github.com/data-tools/big-data-types")),
  scmInfo := Some(
    ScmInfo(url("https://github.com/data-tools/big-data-types"), "git@github.com:data-tools/big-data-types.git")
  ),
  developers := List(Developer("JavierMonton", "Javier Monton", "", url("https://github.com/JavierMonton"))),
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  publishMavenStyle := true
)

lazy val noPublishSettings = {
  publish / skip := true
}

publishSettings

//Dependencies
lazy val coreDependencies2 = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.clapper" %% "grizzled-slf4j" % "1.3.4",
  "com.chuusai" %% "shapeless" % "2.3.7",
  scalatest % Test
)

lazy val coreDependencies3 = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.clapper" % "grizzled-slf4j_2.13" % "1.3.4",
  scalatest % Test
)

lazy val bigqueryDependencies = Seq(
  "com.google.auto.value" % "auto-value-annotations" % "1.8.1", //needed for an incompatibility between BQ & Scala3
  "com.google.cloud" % "google-cloud-bigquery" % "1.134.0",
  scalatest % "it,test"
)

lazy val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % "3.1.2" % Provided,
  "org.apache.spark" %% "spark-sql" % "3.1.2" % Provided,
  scalatest % Test
)
lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.9"

//Project settings
lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(noPublishSettings,
    scalacOptions += "-Ytasty-reader",
    crossScalaVersions := Nil)
  .aggregate(
    core,
    bigquery,
    spark,
    examples
  )

lazy val core = (project in file("core")).settings(
  name := projectName + "-core",
  publishSettings,
  crossScalaVersions := supportedScalaVersions,
  crossVersionSharedSourcesScala3, //different one for Scala 2 or 3
  //for Scala 2 or 3
  libraryDependencies ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, _)) => coreDependencies2
      case Some((3, _)) => coreDependencies3
      case _            => Nil
    }
  }
)

lazy val bigquery = (project in file("bigquery"))
  .configs(IntegrationTest)
  .settings(
    name := projectName + "-bigquery",
    publishSettings,
    Defaults.itSettings,
    crossScalaVersions := supportedScalaVersions,
    crossVersionSharedSources,
    libraryDependencies ++= bigqueryDependencies
  )
  .dependsOn(core % "test->test;compile->compile")

lazy val spark = (project in file("spark"))
  .settings(
    name := projectName + "-spark",
    publishSettings,
    crossScalaVersions := List(scala212),
    crossVersionSharedSources,
    libraryDependencies ++= sparkDependencies
  )
  .dependsOn(core % "test->test;compile->compile")

// Examples module for testing, with all modules included, not built
lazy val examples = (project in file("examples"))
  .settings(
    name := projectName + "-examples",
    noPublishSettings,
    crossScalaVersions := List(scala212, scala213),
    crossVersionSharedSources,
    libraryDependencies ++= sparkDependencies //due to Spark provided dependencies
  )
  .dependsOn(core % "test->test;compile->compile")
  .dependsOn(bigquery % "test->test;compile->compile")
  .settings(
    noPublishSettings,
    crossScalaVersions := List(scala212)
  )
  .dependsOn(spark % "test->test;compile->compile")

lazy val crossVersionSharedSources: Seq[Setting[_]] =
  Seq(Compile, Test).map { sc =>
    (sc / unmanagedSourceDirectories) ++= {
      (sc / unmanagedSourceDirectories).value.flatMap { dir: File =>
        if (dir.getName != "scala") Seq(dir)
        else
          CrossVersion.partialVersion(scalaVersion.value) match {
            case Some((3, _))            => Seq(new File(dir.getPath + "_2.13+"))
            case Some((2, y)) if y >= 13 => Seq(new File(dir.getPath + "_2.13+"))
            case Some((2, y)) if y >= 11 => Seq(new File(dir.getPath + "_2.13-"))
          }
      }
    }
  }

lazy val crossVersionSharedSourcesScala3: Seq[Setting[_]] =
  Seq(Compile, Test).map { sc =>
    (sc / unmanagedSourceDirectories) ++= {
      (sc / unmanagedSourceDirectories).value.flatMap { dir: File =>
        if (dir.getName != "scala") Seq(dir)
        else
          CrossVersion.partialVersion(scalaVersion.value) match {
            case Some((3, _)) => Seq(new File(dir.getPath + "_3"))
            case Some((2, _)) => Seq(new File(dir.getPath + "_2"))
          }
      }
    }
  }
