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

  lazy val productVersion = "3.1.0"
  lazy val productPackage = Seq("ch", "unibas", "cs", "gravis")

  lazy val publishPrefix = "/export/contrib/statismo/repo/public"
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
