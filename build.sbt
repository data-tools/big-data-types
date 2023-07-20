//used to build Sonatype releases
//lazy val versionNumber = "1.3.1"
lazy val projectName = "big-data-types"
//version := versionNumber
name := projectName

lazy val scala213 = "2.13.7"
lazy val scala212 = "2.12.15"
lazy val scala3 = "3.1.0"
lazy val supportedScalaVersions = List(scala3, scala213, scala212)
scalaVersion := scala213

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x                             => MergeStrategy.first
}

//Sonatype
// groupId, SCM, license information
lazy val publishSettings = Seq(
  // version := versionNumber,
  // publishTo := sonatypePublishToBundle.value,
  organization := "io.github.data-tools",
  homepage := Some(url("https://github.com/data-tools/big-data-types")),
  scmInfo := Some(
    ScmInfo(url("https://github.com/data-tools/big-data-types"), "git@github.com:data-tools/big-data-types.git")
  ),
  developers := List(Developer("JavierMonton", "Javier Monton", "", url("https://github.com/JavierMonton"))),
  licenses := Seq("APL2" -> url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
  // publishMavenStyle := true
)

lazy val noPublishSettings =
  publish / skip := true

publishSettings

lazy val scalacCommon = Seq("-Xsource:3")

//Dependencies
lazy val coreDependencies2 = Seq(
  "ch.qos.logback" % "logback-classic" % "1.4.8",
  "org.clapper" %% "grizzled-slf4j" % "1.3.4",
  "com.chuusai" %% "shapeless" % "2.3.10",
  scalatest % Test
)

lazy val coreDependencies3 = Seq(
  "ch.qos.logback" % "logback-classic" % "1.4.8",
  "org.clapper" % "grizzled-slf4j_2.13" % "1.3.4",
  scalatest % Test
)

lazy val bigqueryDependencies = Seq(
  "com.google.auto.value" % "auto-value-annotations" % "1.10.1", // needed for an incompatibility between BQ & Scala3
  "com.google.cloud" % "google-cloud-bigquery" % "2.30.1",
  scalatest % "it,test"
)

lazy val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % "3.4.1" % Provided,
  "org.apache.spark" %% "spark-sql" % "3.4.1" % Provided,
  scalatest % Test
)

lazy val cassandraDependencies = Seq(
  "com.datastax.oss" % "java-driver-core" % "4.15.0",
  "com.datastax.oss" % "java-driver-query-builder" % "4.15.0",
  scalatest % Test
)

val circeVersion = "0.14.3"

lazy val jsonCirceDependencies = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val scalatest = "org.scalatest" %% "scalatest" % "3.2.16"

//Project settings
lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(noPublishSettings, scalacOptions ++= scalacCommon, crossScalaVersions := Nil)
  .aggregate(
    core,
    bigquery,
    spark,
    cassandra,
    jsonCirce,
    examples
  )

lazy val core = (project in file("core")).settings(
  name := projectName + "-core",
  publishSettings,
  scalacOptions ++= scalacCommon,
  crossScalaVersions := supportedScalaVersions,
  crossVersionSharedSourcesScala3, // different one for Scala 2 or 3
  // for Scala 2 or 3
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
    scalacOptions ++= scalacCommon,
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
    scalacOptions ++= scalacCommon,
    crossScalaVersions := List(scala212, scala213),
    crossVersionSharedSources,
    libraryDependencies ++= sparkDependencies
  )
  .dependsOn(core % "test->test;compile->compile")

lazy val cassandra = (project in file("cassandra"))
  .configs(IntegrationTest)
  .settings(
    name := projectName + "-cassandra",
    publishSettings,
    scalacOptions ++= scalacCommon,
    crossScalaVersions := supportedScalaVersions,
    crossVersionSharedSources,
    libraryDependencies ++= cassandraDependencies
  )
  .dependsOn(core % "test->test;compile->compile")

lazy val jsonCirce = (project in file("jsoncirce"))
  .configs(IntegrationTest)
  .settings(
    name := projectName + "-circe",
    publishSettings,
    scalacOptions ++= scalacCommon,
    crossScalaVersions := supportedScalaVersions,
    crossVersionSharedSources,
    libraryDependencies ++= jsonCirceDependencies
  )
  .dependsOn(core % "test->test;compile->compile")

// Examples module for testing, with all modules included, not built
lazy val examples = (project in file("examples"))
  .settings(
    name := projectName + "-examples",
    noPublishSettings,
    scalacOptions ++= scalacCommon,
    crossScalaVersions := supportedScalaVersions,
    crossVersionSharedSourcesScala3
  )
  .dependsOn(core % "test->test;compile->compile")
  .dependsOn(bigquery % "test->test;compile->compile")
  .dependsOn(cassandra % "test->test;compile->compile")
  .dependsOn(jsonCirce % "test->test;compile->compile")
  .settings(
    noPublishSettings,
    crossScalaVersions := List(scala212, scala213),
    libraryDependencies ++= sparkDependencies // due to Spark provided dependencies
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
