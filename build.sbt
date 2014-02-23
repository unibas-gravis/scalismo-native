import AssemblyKeys._

// this must appear as a standalone line before any other instructions related
// to https://github.com/sbt/sbt-assembly
assemblySettings

organization := "org.statismo"

name := "nativelibs"

version := "1.0.0"

scalaVersion := "2.10.3"

EclipseKeys.withSource := true

publishTo := Some(Resolver.file("file",  new File( "/export/contrib/statismo/repo" )) )

// it's too freaking complicated to figure out the path from the configuration, so here it is, duplicated:

TaskKey[Unit]("publish-fixup") <<= (unmanagedBase in Compile) map {
	(lib) => """ant -Djarfile=%s -Dsrcfile=%s -Dlibdir=%s""".format(
		"/export/contrib/statismo/repo/org/statismo/nativelibs_2.10/1.0.0/nativelibs_2.10-1.0.0.jar",
		"/export/contrib/statismo/repo/org/statismo/nativelibs_2.10/1.0.0/nativelibs_2.10-1.0.0-sources.jar",
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

//assemblyOption in packageDependency ~= { ao => ao.copy(includeScala = false) }

//artifact in (Compile, packageDependency) ~= { art =>
//  art.copy(`classifier` = Some("dependencies"), configurations = Array(new Configuration("compile")))
//}

//addArtifact(artifact in (Compile, packageDependency), packageDependency)


