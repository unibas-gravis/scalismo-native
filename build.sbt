// this is the root build.sbt

// these settings apply to all subprojects

organization in ThisBuild := productPackage.mkString(".")

version in ThisBuild := productVersion

javacOptions in ThisBuild ++= Seq("-source", "1.8", "-target", "1.8")

javacOptions in (ThisBuild, doc) := Seq("-source", "1.8")

scalacOptions in ThisBuild ++= Seq("-encoding", "UTF-8", "-Xlint", "-deprecation", "-unchecked", "-feature", "-target:jvm-1.8")

publishTo in ThisBuild := Some(Resolver.file("file",  new File( publishPrefix )) )

crossPaths in ThisBuild := false


homepage in ThisBuild := Some(url("https://scalismo.org"))
licenses in ThisBuild += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
scmInfo in ThisBuild := Some(ScmInfo(url("https://github.com/unibas-gravis/scalismo-native"), "git@github.com:unibas-gravis/scalismo-native.git"))
developers in ThisBuild := List(Developer("marcelluethi", "marcelluethi", "marcel.luethi@unibas.ch", url("https://github.com/marcelluethi")))
publishMavenStyle := true
publishTo in ThisBuild := Some(
      if (isSnapshot.value)
        Opts.resolver.sonatypeSnapshots
      else
        Opts.resolver.sonatypeStaging
    )


// the root project itself does not publish anything, but depends on the publish[-local] tasks.
// Well, except that for the implementation, it also needs to be "fixed up".
// So first, set the task to do nothing, then add the dependency. The other dependencies are found transitively.

publishLocal := {}

publishLocal <<= publishLocal dependsOn (publishLocalFixup in impl_all, publishLocalFixup in impl_linux64, publishLocalFixup in impl_mac64, publishLocalFixup in impl_win64, publishLocalFixup in impl_win32, publishLocalFixup in impl_win)

publish := {}

publish <<= publish dependsOn (publishFixup in impl_all, publishFixup in impl_linux64, publishFixup in impl_mac64, publishFixup in impl_win64, publishFixup in impl_win32, publishFixup in impl_win)

