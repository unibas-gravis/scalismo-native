# Scalismo-native - Native libraries packaging for Scalismo

[Scalismo](https://github.com/unibas-gravis/scalismo/) is a library for statistical shape modeling and model-based image analysis in Scala.

The scalismo-native project provides a build framework for packaging the native libraries needed by Scalismo into a single Java jar that can
be used on Linux, Windows and MacOS platforms.

Note that as a regular Scalismo user, you do not need to build this project and can simply use the already published artifacts as indicated in
 this [tutorial](https://github.com/unibas-gravis/scalismo/wiki/Setup-a-project-using-Scalismo).

ATTENTION: This project will probably only build (or rather: publish) properly on a Linux system!

These native libraries used and their corresponding licenses are:

* [JHDF5 v1.8.10](https://www.hdfgroup.org/products/java/hdf-object/), a Java interface to HDF5 objects, licensed under the following [custom license](https://www.hdfgroup.org/ftp/HDF5/releases/hdf5-1.10/hdf5-1.10.0/src/unpacked/COPYING)

* [JOGL v2.2.4](http://jogamp.org/jogl/www/), Java Binding for the OpenGL API, licensed under the  [New BSD 2-Clause License](https://jogamp.org/git/?p=jogl.git;a=blob;f=LICENSE.txt)

* [GlueGen v2.2.4](https://jogamp.org/gluegen/www/), JNI binding code generator, licensed under the  [New BSD 2-Clause License](https://jogamp.org/git/?p=gluegen.git;a=blob_plain;f=LICENSE.txt)

* [niftijio v1.1](https://github.com/cabeen/niftijio), A Java library for reading and writing NIfTI volumes, licensed under the [MIT license](https://github.com/cabeen/niftijio/blob/master/license.txt)

* [The Visualization Toolkit (VTK) v6.1.0](http://www.vtk.org/), licensed under the [BSD license](http://www.vtk.org/licensing/)



## Copyright and License
All code is available to you under the Apache license, version 2, available at http://www.apache.org/licenses/LICENSE-2.0.

Copyright, University of Basel, 2016.

