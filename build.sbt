import AssemblyKeys._

// this must appear as a standalone line before any other instructions related
// to https://github.com/sbt/sbt-assembly
assemblySettings

organization := "org.statismo"

name := "nativelibs"

version := "develop-SNAPSHOT"

scalaVersion := "2.11.2"

EclipseKeys.withSource := true

publishTo := Some(Resolver.file("file",  new File( "/export/contrib/statismo/repo/public" )) )

// it's too freaking complicated to figure out the path from the configuration, so here it is, duplicated:

TaskKey[Unit]("publish-fixup") <<= (unmanagedBase in Compile) map {
	(lib) => """ant -Djarfile=%s -Dsrcfile=%s -Dlibdir=%s""".format(
		"/export/contrib/statismo/repo/public/org/statismo/nativelibs_2.11/develop-SNAPSHOT/nativelibs_2.11-develop-SNAPSHOT.jar",
		"/export/contrib/statismo/repo/public/org/statismo/nativelibs_2.11/develop-SNAPSHOT/nativelibs_2.11-develop-SNAPSHOT-sources.jar",
		lib
	) ! 
}

{
	println
	println("===================================================================")
	println("To publish this project, first run \"publish\", then \"publish-fixup\"!")
	println("===================================================================")
	println
	publishArtifact in (Compile, packageDoc) := false
}
