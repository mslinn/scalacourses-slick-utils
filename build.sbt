import sbt.Keys._

version := "3.2.1"
name := "scalacourses-slick-utils"
organization := "com.micronautics"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.12.2"

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.2")

scalacOptions ++=
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
scalacOptions in (Compile, doc) ++= baseDirectory.map {
  (bd: File) => Seq[String](
     "-sourcepath", bd.getAbsolutePath,
     "-doc-source-url", "https://github.com/mslinn/scalacourses-slick-utils/tree/master{FILE_PATH}.scala"
  )
}.value
scalacOptions in Test ++= Seq("-Yrangepos")

libraryDependencies ++= scalaVersion {
  case sv if sv.startsWith("2.12") => // Play 2.6.x, Scala 2.12.x, Slick 3.2.x, Java 8
    Seq(
      "com.zaxxer"             %  "HikariCP"           % "2.6.1"     withSources(),
      "com.github.tototoshi"   %% "slick-joda-mapper"  % "2.3.0"     withSources(),
      "com.typesafe.play"      %% "play"               % "2.6.0-M4"  % Provided,
      "com.typesafe.slick"     %% "slick"              % "3.2.0"     withSources(),
      "com.typesafe.slick"     %% "slick-hikaricp"     % "3.2.0"     withSources(),
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0-M2"  % Test withSources(),
      "com.typesafe.play"      %% "play-json"          % "2.6.0-M3"  % Provided,
      "org.clapper"            %% "grizzled-scala"     % "4.2.0"     withSources()
    )

  case sv if sv.startsWith("2.11") => // Play 2.5.x, Scala 2.11.x, Slick 3.2.x, Java 8
    Seq(
      "com.zaxxer"             %  "HikariCP"           % "2.6.1"  withSources(),
      "com.github.tototoshi"   %% "slick-joda-mapper"  % "2.3.0"  withSources(), // Not available Apr 9/17
      "com.typesafe.slick"     %% "slick"              % "3.2.0"  withSources(),
      "com.typesafe.slick"     %% "slick-hikaricp"     % "3.2.0"  exclude("com.zaxxer", "HikariCP-java6") withSources(),
      "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0"  % Test withSources(),
      "com.typesafe.play"      %% "play-json"          % "2.5.13" % Provided,
      "org.clapper"            %% "grizzled-scala"     % "4.2.0"  withSources()
    )

  case sv if sv.startsWith("2.10") =>  // Play 2.2.6, Scala 2.10.x, Slick 2.1.x, Java 7
    Seq(
      "com.github.tototoshi" %% "slick-joda-mapper"       % "1.2.0" withSources(),
      "com.micronautics"     %% "scalacourses-play-utils" % "0.1.1" withSources(),
      "com.typesafe.slick"   %% "slick"                   % "2.1.0" withSources(),
      "com.typesafe.play"    %% "play"                    % "2.2.6" % Provided withSources(),
      "com.typesafe.play"    %% "play-json"               % "2.2.6" % Provided withSources(),
      "org.scalatestplus"    %% "play"                    % "1.0.0" % Test,
      "org.clapper"          %% "grizzled-scala"          % "1.3"   withSources()
    )
}.value

libraryDependencies ++= Seq(
  "com.github.nscala-time" %% "nscala-time" % "2.16.0" withSources()
)

resolvers ++= Seq(
  "Lightbend Releases"            at "http://repo.typesafe.com/typesafe/releases",
  "micronautics/slick on bintray" at "http://dl.bintray.com/micronautics/slick",
  "micronautics/play on bintray"  at "http://dl.bintray.com/micronautics/play"
)

cancelable := true

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

// sbt-site settings
enablePlugins(SiteScaladocPlugin)
siteSourceDirectory := target.value / "api"
publishSite

// sbt-ghpages settings
enablePlugins(GhpagesPlugin)
git.remoteRepo := "git@github.com:mslinn/scalacourses-slick-utils.git"
