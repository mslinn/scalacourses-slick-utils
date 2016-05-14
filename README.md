# scalacourses-play-utils
Utilities for Slick Referenced from ScalaCourses.com

See the unit tests for usage examples.

This is the branch for Slick 2.1.0.

This version has a dependency on the Postgres driver implementation

## Installing ##

Add two lines to `build.sbt`.

 * Add the `scalacourses-slick-utils` dependency:
````
"com.micronautics" %% "scalacourses-slick-utils" % "2.1.1" withSources()
````

 * Add this to the `resolvers`:
````
"micronautics/play on bintray" at "http://dl.bintray.com/micronautics/play"
````

This library has been built against Scala 2.10.6 / Play 2.2.6 / Slick 1.0.1 and Scala 2.11.8 / Play 2.5.3.
