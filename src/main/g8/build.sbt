val Scala210 = "2.10.7"

val Scala212 = "2.12.10"

ThisBuild / scalaVersion := Scala212

lazy val generator = (project in file("generator"))
  .enablePlugins(AssemblyPlugin)
  .settings(
      crossScalaVersions in ThisBuild := Seq(Scala212, Scala210),

      organization := "$organization$",

      name := "$name$",

      libraryDependencies ++= Seq(
          "com.thesamet.scalapb" %% "compilerplugin" % scalapb.compiler.Version.scalapbVersion
      ),

      assemblyOption in assembly := (assemblyOption in assembly).value.copy(
        prependShellScript = Some(sbtassembly.AssemblyPlugin.defaultUniversalScript(shebang = !isWindows))
      ),

      Compile / mainClass := Some("$package$.Main")
  )

def isWindows: Boolean = sys.props("os.name").startsWith("Windows")

// The e2e project exercises the generator. We need to use the generator project above to generate
// code for this project. To accomplish that, we use the assembly task to create a fat jar of the
// generator, and provide this to sbt-protoc as a plugin.
lazy val e2e = (project in file("e2e"))
.settings(
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test",

    // Makes the e2e project depends on assembling the generator
    Compile / PB.generate := ((Compile / PB.generate) dependsOn (generator / Compile / assembly)).value,

    // Regenerates protos on each compile even if they have not changed. This is so changes in the plugin
    // are picked up without having to manually clean.
    Compile / PB.recompile := true,

    Compile / PB.targets := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value,

      // Creates a target using the assembled
      protocbridge.Target(
        generator=PB.gens.plugin(
          "mygen",
          (generator / assembly / target).value / "$name$-assembly-" + version.value + ".jar"
        ),
        outputPath=(Compile / sourceManaged).value,
        options=Seq("grpc", "java_conversions")
      )
    ),

)
