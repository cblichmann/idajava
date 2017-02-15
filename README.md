IdaJava 0.3
===========

IdaJava is a plugin for IDA Pro that allows to write IDA plugins in Java. In
other words: IdaJava is to Java like IDAPython is to Python...
The plugin creates an in-process Java VM and looks for JAR files in IDA's
plugins directory. Each Java based plugin gets their own menu item in
`Edit|Plugins`.

**NOTE (2017): This is old code that will likely no longer work. It's mainly
               here for reference. I don't plan on making further changes.**


Table of Contents
=================

   * [IdaJava 0.3](#idajava-03)
      * [Requirements](#requirements)
      * [Bugs](#bugs)
      * [How to build](#how-to-build)
      * [Installation](#installation)
      * [Developing Java Plugins for IDA](#developing-java-plugins-for-ida)
      * [Future Directions](#future-directions)
      * [Copyright/Licensing](#copyrightlicensing)


Requirements
------------

Obviously:
  - Windows version of IDA Pro, preferrably 5.7 (earlier versions are
    untested but may work)
  - 32-bit Sun JRE (even on 64-bit Windows), version 1.6.0 or higher (tested
    with 1.6.0_21)

To rebuild language bindings:
  - [Swig 2.0.0](http://www.swig.org/)


Bugs
----

There are quite a few rough edges:
  - When closing IDA, the embedded Java VM does properly release its
    resources, but due to a bug in the VM itself, it always exits with an
    assertion. IDA databases are properly closed and the only thing that
    a user will notice is a `hs-xxxxx.log` file after closing IDA.

  - The API is quite incomplete. Need more SWIG bindings. Started conversion
    of `idc.idc` (about 20% done top-to-bottom).

  - Runs only on Windows at the moment

  - Painting issues when creating TForm's with embedded AWT or Swing GUIs.
    The custom WindowProc that is set on the handle returned by IDA is never
	called. The embedded window always has width x height = 10000 x 10000.

  - When using RMI to remotely control IDA, exiting IDA may result in
    un-marshalling errors.

  - Generally not production-level code, since I (Christian) wrote the plugin
    initially as an automation tool for myself.


How to build
------------

IdaJava was developed using Visual Studio 2010, but should compile on Visual
Studio 2005/2008 as well. Just link with `jvm.lib` from a 32-bit Sun JDK and
the ida.lib from the IDA 5.7 SDK. There are no other dependencies.

To rebuild the SWIG bindings, edit CallSwig.cmd and execute it. It will
produce quite a few warnings, though.

The Java part of IdaJava was developed with Eclipse 3.6. Earlier versions will
work as well. The complete project is included.

Source code is in part documented using JavaDoc-style comments. Java sources
can thus be processed with JavaDoc, C++ sources with Doxygen.

The folder structure is roughly:
```
idajava/
+- javasrc/      Java project
|  +- bin/       Compiled Java classes
|  +- src/       Java source folder
|  +- external/  External source packages of used libraries
|  +- lib/       Jar files needed for building
+- cppsrc/       C++ project
|  +- Debug/     Debug build result files
|  +- Release/   Release build results files
|  +- src/       C++ sources
+- CONTRIBUTERS
+- LICENSE
+- README.md
```


Installation
------------

1. Copy the file `cppsrc\Debug\idajava.plw` to your IDA plugin directory.
2. Copy the folder `javasrc\bin\de` folder to your IDA plugin directory
2. Copy the sample plugin `javasrc\sample-plugin.jar` to a sub directory
   `java` in your IDA directory.
3. Import `cppsrc\EmptyPluginSettings.reg` into the registry (the registry
   path `HKCU\Software\blichmann.de\IdaJava\0.3` must exist).

Using the steps above, the Java part of IdaJava will be run from the compiled
.class files instead of a JAR file.
After loading a database, invoke the plugin with CTRL+8, it should display a
small about dialog. To invoke the sample plugin, use the
`Edit|IdaJava Hello World Plugin` menu item. It should display the
current screen address in decimal.


Developing Java Plugins for IDA
--------------------------------

1. Extend your plugin class from `de.blichmann.idajava.api.plugin.IdaPlugin`
   and implement the `getDisplayName()`, `getHotkey()` and `run()` methods
   (see the Java source, esp. `de.blichmann.idajava.samples.HelloIdaPlugin`).
2. Package your plugin as a JAR file (you can use Eclipse or an Ant task for
   that) and drop it in a directory `IDADIR\java`.

You can attach a debugger to the running Java VM inside of IDA. Your other
options are of course `System.out.println()` and/or logging, sorry.

Access to a large part of the IDA SDK is provided through the classes of the
`de.blichmann.idajava.natives package`. Constants are defined in
`de.blichmann.idajava.natives.IdaJavaConstants`, API functions in
`de.blichmann.idajava.natives.IdaJava`. A PrintStream for convenient
output to IDA's message window is available in
`de.blichmann.idajava.api.IdaConsole`.
A part of idc.idc has been converted as well, it is available in the
`de.blichmann.api.idc.IdcCompat` class (expect bugs!).


Future Directions
-----------------

  - Support for unloading and/or automatic reloading of plugins to aid
    development
  - Provide an object-oriented Java-style API to the IDA SDK
  - Provide an API to call IDA remotely via RMI
  - More samples, i.e. port some of the SDK samples
  - Properly support embedding AWT/Swing GUIs in IDA.
  - Linux version with support for external "embedded" AWT/Swing GUIs (i.e.
    the IDA main window using the X11 version of libturbovision.so plus
	external windows)
  - Support for Java based scripting languages as extlangs in IDA (think
    Groovy, JavaScript or even Jython)


Copyright/Licensing
-------------------

IDAJava version 0.3
Copyright (c)2007-2017 Christian Blichmann <idajava@blichmann.eu>

IdaJava is subject to the terms of the GPLv2. See LICENSE for details.
This also applies to all classes in the de.blichmann.framework package.
