libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value,

  $package$.Generator -> (sourceManaged in Compile).value
)
