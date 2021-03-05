addSbtPlugin("com.eed3si9n" % "sbt-projectmatrix" % "0.7.0")

addSbtPlugin("com.thesamet" % "sbt-protoc" % "$sbt_protoc_version$")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "$scalapb_version$"

addSbtPlugin("com.thesamet" % "sbt-protoc-gen-project" % "0.1.6")

// Build info is used to make the version number of the core library available
// to the code generator so it can automatically add the correct version of
// the core library to the build.
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.10.0")
