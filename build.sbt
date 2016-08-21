import bintray.Keys._

version := "3.1.2"
name := "scalacourses-slick-utils"
organization := "com.micronautics"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

crossScalaVersions := Seq("2.11.8"/*, "2.12.0"*/)

scalaVersion := "2.11.8"
scalacOptions ++= Seq("-deprecation", "-encoding", "UTF-8", "-feature", "-target:jvm-1.7", "-unchecked",
    "-Ywarn-adapted-args", "-Ywarn-value-discard", "-Xlint")
scalacOptions in (Compile, doc) <++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/scalacourses-slick-utils/tree/master€{FILE_PATH}.scala"
  )
}
scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies <++= scalaVersion {
  case sv if sv.startsWith("2.11") | sv.startsWith("2.12") =>
    javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-g:vars")
    Seq(
      "com.github.tototoshi"   %% "slick-joda-mapper"  % "2.2.0" withSources(),
      "com.typesafe.slick"     %% "slick"              % "3.1.1" withSources(),
      "com.typesafe.slick"     %% "slick-hikaricp"     % "3.1.1" exclude("com.zaxxer", "HikariCP-java6") withSources(),
      "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % "test" withSources(),
      "com.typesafe.play"      %% "play-json"          % "2.5.4" % "provided",
      "org.clapper"            %% "grizzled-scala"     % "2.6.0" withSources()
    )

  case sv if sv.startsWith("2.10") =>
    javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.7", "-target", "1.7", "-g:vars")
    Seq(
      "com.github.tototoshi" %% "slick-joda-mapper"  % "1.2.0" withSources(),
      "com.typesafe.slick"   %% "slick"              % "2.1.0" withSources(),
      "com.typesafe.play"    %% "play-json"          % "2.2.6" % "provided" withSources(),
      "org.scalatestplus"    %% "play"               % "1.0.0" % "test",
      "org.clapper"          %% "grizzled-scala"     % "1.3"   withSources()
    )
}

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time"  % "2.12.0" withSources()
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

bintrayPublishSettings
bintrayOrganization in bintray := Some("micronautics")
repository in bintray := "play"
publishArtifact in Test := false

cancelable := true
