import sbt._
import Keys._
import sbt.Tests.Setup

object Build extends sbt.Build {

  lazy val root = Project(id = "buttercoin-java", base = file("."))
    .settings(
      organization := "com.buttercoin",
      organizationName := "Buttercoin",
      organizationHomepage := Some(new URL("https://buttercoin.com")),
      description := "Official Buttercoin API Java Client",
      startYear := Some(2015),
      resolvers += Resolver.typesafeRepo("releases"),
      concurrentRestrictions in Global += Tags.limit(Tags.Test, 1),
      testOptions += Tests.Argument(TestFrameworks.JUnit, "-q"),
      testOptions += Setup(cl =>
        cl.loadClass("org.slf4j.LoggerFactory").
          getMethod("getLogger", cl.loadClass("java.lang.String")).
          invoke(null, "ROOT")
      ),

      libraryDependencies += "com.google.guava" % "guava" % "18.0",
      libraryDependencies += "com.ning" % "async-http-client" % "1.9.9",
      libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.1",

      // testing
      libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.10" % "test",
      libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2" % "test",
      libraryDependencies += "org.easytesting" % "fest-assert" % "1.4" % "test",
      libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",

      crossPaths := false,
      autoScalaLibrary := false,
      publishMavenStyle := true,
      publishTo <<= version {
        v: String =>
          val nexus = "https://oss.sonatype.org/"
          if (v.trim.endsWith("SNAPSHOT"))
            Some("snapshots" at nexus + "content/repositories/snapshots")
          else
            Some("releases" at nexus + "service/local/staging/deploy/maven2")
      },
      pomExtra :=
        <url>https://github.com/buttercoin/buttercoin-java</url>
          <issueManagement>
            <system>GitHub Issues</system>
            <url>https://github.com/buttercoin/buttercoin-java/issues</url>
          </issueManagement>
          <ciManagement>
            <system>Travis CI</system>
            <url>https://travis-ci.org/buttercoin/buttercoin-java</url>
          </ciManagement>
          <licenses>
            <license>
              <name>MIT License</name>
              <url>http://opensource.org/licenses/MIT</url>
              <distribution>repo</distribution>
            </license>
          </licenses>
          <scm>
            <url>https://github.com/buttercoin/buttercoin-java</url>
            <connection>scm:git:git@github.com:buttercoin/buttercoin-java.git</connection>
            <developerConnection>scm:git:git@github.com:buttercoin/buttercoin-java.git</developerConnection>
          </scm>
          <developers>
            <developer>
              <id>analytically</id>
              <name>Mathias Bogaert</name>
              <url>https://twitter.com/buttercoin</url>
              <organization>Buttercoin</organization>
              <organizationUrl>https://buttercoin.com</organizationUrl>
            </developer>
          </developers>
    )
}
