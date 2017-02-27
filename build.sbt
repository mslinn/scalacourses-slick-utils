import sbt.Keys._

version := "3.1.5"
name := "scalacourses-slick-utils"
organization := "com.micronautics"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

crossScalaVersions := Seq("2.11.8", "2.12.1")

scalaVersion := "2.11.8"
scalacOptions ++= (
  scalaVersion {
    case sv if sv.startsWith("2.10") => Nil
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
  case sv if sv.startsWith("2.12") => // Play 2.6.x, Scala 2.12.x, Slick 3.2.x, Java 8
    javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-g:vars")
    Seq(
      "com.zaxxer"             %  "HikariCP"           % "2.5.1"     withSources(),
      "com.github.tototoshi"   %% "slick-joda-mapper"  % "2.4.0"     withSources(),
      "com.typesafe.slick"     %% "slick"              % "3.2.0-RC1" withSources(),
      "com.typesafe.slick"     %% "slick-hikaricp"     % "3.2.0-RC1" withSources(),
      "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0-M2"  % Test withSources(),
      "com.typesafe.play"      %% "play-json"          % "2.6.0-M3"  % Provided,
      "org.clapper"            %% "grizzled-scala"     % "4.2.0"     withSources()
    )

  case sv if sv.startsWith("2.11") => // Play 2.5.x, Scala 2.11.x, Slick 3.1.x, Java 8
    javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.8", "-target", "1.8", "-g:vars")
    Seq(
      "com.zaxxer"             %  "HikariCP"           % "2.5.1"  withSources(),
      "com.github.tototoshi"   %% "slick-joda-mapper"  % "2.2.0"  withSources(),
      "com.typesafe.slick"     %% "slick"              % "3.1.1"  withSources(),
      "com.typesafe.slick"     %% "slick-hikaricp"     % "3.1.1"  exclude("com.zaxxer", "HikariCP-java6") withSources(),
      "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1"  % Test withSources(),
      "com.typesafe.play"      %% "play-json"          % "2.5.12"  % Provided,
      "org.clapper"            %% "grizzled-scala"     % "4.2.0"  withSources()
    )

  case sv if sv.startsWith("2.10") =>  // Play 2.2.6, Scala 2.10.x, Slick 2.1.x, Java 7
    javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked", "-source", "1.7", "-target", "1.7", "-g:vars")
    Seq(
      "com.github.tototoshi" %% "slick-joda-mapper"  % "1.2.0" withSources(),
      "com.typesafe.slick"   %% "slick"              % "2.1.0" withSources(),
      "com.typesafe.play"    %% "play-json"          % "2.2.6" % Provided withSources(),
      "org.scalatestplus"    %% "play"               % "1.0.0" % Test,
      "org.clapper"          %% "grizzled-scala"     % "1.3"   withSources()
    )
}.value

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "2.16.0" withSources()
)

resolvers ++= Seq(
  "Lightbend Releases"           at "http://repo.typesafe.com/typesafe/releases",
  "micronautics/slick on bintray" at "http://dl.bintray.com/micronautics/slick",
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
