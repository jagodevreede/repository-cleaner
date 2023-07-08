# Repository Cleaner

![GitHub](https://img.shields.io/github/license/jagodevreede/repository-cleaner)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/jagodevreede/repository-cleaner?label=Latest%20release)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/jagodevreede/repository-cleaner/maven.yml?branch=main)

The Repository Cleaner Maven plugin cleans your local repository by removing old dependencies that are no longer used.
By default it will remove any artifact in your local repository that is not access for over a year.

## Prerequisites

This plugin requires Maven 3.3.9 and Java 11 or higher to be able to run.

## Usage

This plugin has 2 goals `analyze` and `clean`.

The following parameters are available:

| Name               | Default value | Description                                                                                                         |
|--------------------|---------------|---------------------------------------------------------------------------------------------------------------------|
| daysToKeep         | 356           | Number of days that the files may not be accessed before marked for deletion                                        |
| clean.snapshotOnly | false         | If set to `true` then the plugin will only remove old SNAPSHOT's                                                    |
| clean.dryRun       | false         | Only effective in the clean goal, if set to `true` then plugin will do nothing, only show how big the repository is |

### analyze

This goal is non-destructive and shows you what will happen if you run clean with the same parameters.

```
mvn io.github.jagodevreede:repository-cleaner:analyze
```

example with all parameters set to different values
```
mvn io.github.jagodevreede:repository-cleaner:analyze -DdaysToKeep=14 -Dclean.snapshotOnly=true
```

### clean

This goal is will actually clean.

```
mvn io.github.jagodevreede:repository-cleaner:clean
```

example with all parameters set to different values
```
mvn io.github.jagodevreede:repository-cleaner:analyze -DdaysToKeep=14 -Dclean.snapshotOnly=true -Dclean.dryRun=true
```

----

## Open source licensing info

1. [LICENSE](LICENSE)

----
