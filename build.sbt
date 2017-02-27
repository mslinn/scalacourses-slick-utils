import sbt.Keys._

version := "0.1.1"
name := "scalacourses-slick-utils"
organization := "com.micronautics"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

crossScalaVersions := Seq("2.10.6", "2.11.7", "2.12.1")
scalaVersion := "2.10.6"

scalacOptions ++= (
  scalaVersion {
    case sv if sv.startsWith("2.10") => List(
      "-target:jvm-1.7"
    )
    case _ => List(
      "-target:jvm-1.8",
      "-Ywarn-unused"
    )
  }.value ++ Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Ywarn-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture",
    "-Xlint"
  )
)

scalacOptions in (Compile, doc) <++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/scalacourses-slick-utils/tree/master€{FILE_PATH}.scala"
  )
}
scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= scalaVersion {
  case sv if sv.startsWith("2.12") =>
    Seq(
      "com.typesafe.play"      %% "play"               % "2.6.0-M1" % Provided,
      "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0-M2" % Test
    )

  case sv if sv.startsWith("2.11") =>
    Seq(
      "com.typesafe.play" %% "play-json" % "2.4.2"    % "provided",
      "org.scalatestplus" %% "play"      % "1.4.0-M3" % "test"
    )

  case sv if sv.startsWith("2.10") =>
    Seq(
      "com.typesafe.play" %% "play"      % "2.2.6" % "provided",
      "org.scalatestplus" %% "play"      % "1.0.0" % "test"
    )
}.value

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time"             % "2.16.0" withSources(),
  "com.github.tototoshi"   %% "slick-joda-mapper"       % "0.4.1"  withSources(),
  "com.micronautics"       %% "scalacourses-play-utils" % "0.1.12" withSources(),
  "com.typesafe.slick"     %% "slick"                   % "3.1.1"  withSources(),
  "org.clapper"            %% "grizzled-scala"          % "1.3"
)

javacOptions ++= Seq(
  "-Xlint:deprecation",
  "-Xlint:unchecked",
  "-source", "1.8",
  "-target", "1.8",
  "-g:vars"
)

resolvers ++= Seq(
  "Lightbend Releases"           at "http://repo.typesafe.com/typesafe/releases",
  "micronautics/play on bintray" at "http://dl.bintray.com/micronautics/play"
)

logLevel := Level.Warn

// Only show warnings and errors on the screen for compilations.
// This applies to both test:compile and compile and is Info by default
logLevel in compile := Level.Warn

// Level.INFO is needed to see detailed output when running tests
logLevel in test := Level.Info

// define the statements initially evaluated when entering 'console', 'console-quick', but not 'console-project'
initialCommands in console := """
                                |""".stripMargin

bintrayOrganization := Some("micronautics")
bintrayRepository := "play"
publishArtifact in Test := false

cancelable := true
