addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.27")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "$scalapb_version$"
