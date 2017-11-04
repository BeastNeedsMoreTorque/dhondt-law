enablePlugins(ScalaJSPlugin)

name := "D'Hont Method simulator"
scalaVersion := "2.12.2" 

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.3"
