name := "case-class-to-database-types"

version := "0.1"

scalaVersion := "2.13.3"

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