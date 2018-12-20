val Scala210 = "2.10.7"

val Scala212 = "2.12.7"

lazy val root = (project in file("."))
    .enablePlugins(ScriptedPlugin)
    .settings(
        crossScalaVersions in ThisBuild := Seq(Scala210, Scala212),
        resolvers += Resolver.typesafeIvyRepo("releases"),
        organization := "$organization$",

        name := "$name$",

        scriptedSbt := {
          scalaBinaryVersion.value match {
                  case "2.10" => "0.13.7"
                  case "2.12" => "1.2.7"
          }
        },
        libraryDependencies ++= Seq(
            "com.thesamet.scalapb" %% "compilerplugin" % "$scalapb_version$"
        ),

        scriptedLaunchOpts := { scriptedLaunchOpts.value ++
          Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
        },
        scriptedBufferLog := false
    )
