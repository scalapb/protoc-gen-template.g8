val Scala210 = "2.10.7"

val Scala212 = "2.12.7"

lazy val root = (project in file("."))
    .enablePlugins(ScriptedPlugin)
    .settings(
        crossScalaVersions in ThisBuild := Seq(Scala210, Scala212),

        organization := "$organization$",

        name := "$name$",

        libraryDependencies ++= Seq(
            "com.thesamet.scalapb" %% "compilerplugin" % "$scalapb_version$"
        ),

        scriptedLaunchOpts := { scriptedLaunchOpts.value ++
          Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
        },
        scriptedBufferLog := false
    )
