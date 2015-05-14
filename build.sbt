name := """SRTaller2"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies +="org.xerial" % "sqlite-jdbc" % "3.7.2"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.21",
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)
