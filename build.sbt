name := """Beads_Web-Application"""
organization := "htwg-konstanz.de"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "htwg-konstanz.de.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "htwg-konstanz.de.binders._"
