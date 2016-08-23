# scalacourses-play-utils
Utilities for Slick Referenced from ScalaCourses.com

See the unit tests for usage examples.

This is the branch for Slick 3.1.x, Java 8 and Scala 2.11.

This version has a dependency on the Postgres driver.

## Installing ##

Add two lines to `build.sbt`.

 * Add the `scalacourses-slick-utils` dependency:
````
"com.micronautics" %% "scalacourses-slick-utils" % "3.1.3" withSources()
````

 * Add this to the `resolvers`:
````
"micronautics/play on bintray" at "http://dl.bintray.com/micronautics/play"
````

This library has been built against Scala Scala 2.11.8 and Play 2.5.x.
