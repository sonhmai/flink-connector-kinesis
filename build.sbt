import Dependencies._

ThisBuild / scalaVersion     := "2.12.10"
ThisBuild / version          := "0.1.1"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val common = (project in file("modules/common"))
  .settings(
    name := "common",
    libraryDependencies ++= CommonDeps
  )

lazy val flinkConnectorKinesis = (project in file("modules/flinkConnectorKinesis"))
  .settings(
    name := "flinkConnectorKinesis",
    libraryDependencies ++= flinkConnectorKinesisDeps
  )

lazy val table = (project in file("modules/table"))
  .settings(
    name := "table",
    libraryDependencies ++= tableModuleDeps
  )
  .settings(
    assemblySettings,
    assembly / mainClass := Some("example.StreamingAppDataStream")
  )
  .enablePlugins(AssemblyPlugin)
  .dependsOn(common)

lazy val root = (project in file("."))
  .settings(
    name := "flink-sql-kinesis-example"
  )
  .aggregate(
    flinkConnectorKinesis,
    table,
    common
  )

lazy val assemblySettings = Seq(
  assembly / test := {},
  assembly / assemblyMergeStrategy := {
    case x if Assembly.isConfigFile(x) =>
      MergeStrategy.concat
    case PathList(ps @ _*)
      if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
      MergeStrategy.rename
    case PathList("META-INF", xs @ _*) =>
      (xs map { _.toLowerCase }) match {
        case ("manifest.mf" :: Nil) | ("index.list" :: Nil) |
             ("dependencies" :: Nil) =>
          MergeStrategy.discard
        case ps @ (x :: xs)
          if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
          MergeStrategy.discard
        case "plexus" :: xs =>
          MergeStrategy.discard
        case "services" :: xs =>
          MergeStrategy.filterDistinctLines
        case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
          MergeStrategy.filterDistinctLines
        case _ => MergeStrategy.first
      }
    case _ => MergeStrategy.first
  },
  assembly / artifact := {
    val art = (assembly / artifact).value
    art.withClassifier(Some("assembly"))
  }
)

