# libs/ — Teamcenter SOA client JARs

The Teamcenter SOA Java client is **not** published to Maven Central.
Obtain the JARs from your Teamcenter installation / Siemens Support Center
(usually under `TC_ROOT/soa_client/java/` or the "SOA Client for Java" kit)
and copy them here, e.g.:

```
libs/
  TcSoaClient.jar
  TcSoaCommon.jar
  TcSoaStrongModel.jar
  TcSoaCoreStrong.jar
  ...
```

Then enable the matching `implementation ':TcSoaClient'` lines in `build.gradle`.

This directory is wired as a Gradle `flatDir` repository. JARs are **git-ignored**
by default (see .gitignore) since they are licensed Siemens binaries.
