# scalacourses-play-utils
Utilities for Slick Referenced from ScalaCourses.com

See the unit tests for usage examples.

This is the branch for Slick 3.2.x, Java 8, Play 2.6.x and Scala 2.12.x.

This version has a dependency on the Postgres driver.

## Installing ##

Add two lines to `build.sbt`.

 * Add the `scalacourses-slick-utils` dependency:
````
"com.micronautics" %% "scalacourses-slick-utils" % "3.2.0" withSources()
````

 * Add this to the `resolvers`:
````
"micronautics/play on bintray" at "http://dl.bintray.com/micronautics/play"
````
