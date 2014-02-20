# phorosservlet #

This repository is a fork of `citeservlet`.  The build file includes tasks to overlay project-specific material on the generic servlet, but is planned to allow for pulling in updates from `citeservlet`.  To minimize conflicts when pulling in changes from the generic `citeservlet`, the fork leaves all files in the upstream repository unchanged, and only adds new material.

The project specific build file is `phoros.gradle`.  You will also want an installation-specific configuration file (as explained in the generic `citeservlet` documentation).  A typical invocation of the `jettyRunWar` could then use the project's build and configuration files like this:

    gradle -b phoros.gradle -Pconf=phconf.gradle  jettyRunWar