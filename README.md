# scalacourses-play-utils
Utilities for Slick Referenced from ScalaCourses.com

See the unit tests for usage examples.

There are branches for Slick 1.0.1 and Slick 2.1.0.

## Installing ##

Add two lines to `build.sbt`.

 * Add the `scalacourses-slick-utils` dependency:
````
"com.micronautics" %% "scalacourses-slick-utils" % "0.1.1" withSources()
````

 * Add this to the `resolvers`:
````
"micronautics/play on bintray" at "http://dl.bintray.com/micronautics/play"
````

This library has been built against Scala 2.10.5 / Play 2.2.6 / Slick 1.0.1 but not Scala 2.11.7 / Play 2.4.2.
