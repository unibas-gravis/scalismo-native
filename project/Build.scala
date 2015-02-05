import sbt.Keys._
import sbt._

object Build extends sbt.Build {

  lazy val productVersion = "2.0.0"
  lazy val productPackage = Seq("ch", "unibas", "cs", "gravis")

  lazy val scalaMinorVersion = "2.10"
  lazy val scalaReleaseVersion = "4"

  lazy val publishPrefix = "/tmp/export/contrib/statismo/repo/public"
  lazy val publishLocalPrefix = s"${System.getProperty("user.home")}/.ivy2/local"

  // these task and settings keys are needed for the implementation project
  val publishLocalFixup = TaskKey[Unit]("publish-local-fixup")
  val publishFixup = TaskKey[Unit]("publish-fixup")
  val fixupExclude = SettingKey[Option[String]]("fixup-Exclude")

  lazy val stub = Project(id = "stub", base = file("stub"))

  def implProject(suffix: String, excludes: Option[String]) = Project(
    id = s"impl-$suffix",
    base = file("implementation"),
    settings = Defaults.defaultSettings ++ Seq(
      target := file(s"implementation/target-$suffix"),
      name := s"scalismo-native-$suffix",
      fixupExclude := excludes
    )
  ) dependsOn stub

  lazy val impl_all = implProject("all", None)
  lazy val impl_linux64 = implProject("linux64", Some("mac_x86_64 windows_amd64 windows_x86"))
  lazy val impl_mac64 = implProject("mac64", Some("linux_amd64 windows_amd64 windows_x86"))
  lazy val impl_win64 = implProject("windows32", Some("linux_amd64 mac_x86_64 windows_amd64"))
  lazy val impl_win32 = implProject("windows64", Some("linux_amd64 mac_x86_64 windows_x86"))
  lazy val impl_win = implProject("windows", Some("linux_amd64 mac_x86_64"))

  lazy val root = Project(id = "scalismo-native", base = file(".")) aggregate(stub, impl_all, impl_linux64, impl_mac64, impl_win64, impl_win32, impl_win)

}
