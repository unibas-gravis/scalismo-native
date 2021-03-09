/*
 * Copyright 2016 University of Basel, Graphics and Vision Research Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt.Keys._
import sbt._

object Build extends sbt.Build {

  // IMPORTANT:
  // ==========
  //
  // ( version terminology: MAJOR.MINOR.PATCH, thus 1.5.2 == MAJOR=1, MINOR=5, PATCH=2 )
  // ( dependencies: the STUB *requires* an IMPLEMENTATION with the *same* MAJOR version, and an *equal or higher* MINOR version)
  //
  // - If ANY part of the PUBLICLY available classes/methods/fields in EITHER the stub or the implementation of THIS project changes in a breaking way, bump the MAJOR version.
  // - If ANY part of the PUBLIC interface of the BUNDLED files (VTK, jhfd5, ...) changes in a breaking way, bump the MAJOR version.
  // - If ANY part of either THIS project or a BUNDLED file changes in a non-breaking (backwards-compatible) way, bump the MINOR version.
  // - It's probably best/easiest to not use PATCH versions (leave them at 0, and increment the MINOR version instead), as the internal logic doesn't consider them.
  //
  //
  // EVEN MORE IMPORTANT
  // ===================
  //
  // !!! This isn't the only place where version information is stored !!!
  // ---------------------------------------------------------------------
  //
  // If you change the productVersion here, you MUST also change the MAJOR_VERSION and MINOR_VERSION fields in these two files accordingly:
  //
  // stub/src/main/java/scalismo/support/nativelibs/NativeLibraryBundles.java
  // implementation/src/main/java/scalismo/support/nativelibs/NativeLibraryBundlesImplementation.java
  //
  lazy val productVersion = "4.0.1"


  lazy val productPackage = Seq("ch", "unibas", "cs", "gravis")

  lazy val publishPrefix = s"${System.getProperty("java.io.tmpdir")}"
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
