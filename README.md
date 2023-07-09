# Repository Cleaner

![GitHub](https://img.shields.io/github/license/jagodevreede/repository-cleaner)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/jagodevreede/repository-cleaner?label=Latest%20release)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/jagodevreede/repository-cleaner/maven.yml?branch=main)

The local Maven repository accumulates artifacts over time, but it never removes them. This can result in wasted disk
space, filled with artifacts that are no longer used or have been upgraded to newer versions. Additionally, building
numerous snapshots and running the mvn install command can clutter the local repository even further.

The Repository Cleaner Maven plugin is a powerful tool designed to address this issue. It enables you to efficiently
clean your local repository by removing outdated dependencies that are no longer necessary. By identifying artifacts
that have not been accessed for over a year, the plugin selectively removes them, freeing up valuable disk space and
decluttering your repository.

With the Repository Cleaner Maven plugin, you can ensure that your local repository remains streamlined, optimized, and
free from unnecessary artifacts.

## Prerequisites

This plugin requires Maven 3.3.9 and Java 11 or higher to be able to run.

## Usage

You will need to run this plugin from within a maven project.

This plugin provides two goals: `analyze` and `clean`. You can use the following parameters to customize the behavior:

| Name               | Default value | Description                                                                                                                         |
|--------------------|---------------|-------------------------------------------------------------------------------------------------------------------------------------|
| daysToKeep         | 356           | Number of days that the files may not be accessed before marked for deletion                                                        |
| clean.snapshotOnly | false         | If set to `true` then the plugin will only remove old SNAPSHOT's                                                                    |
| clean.dryRun       | false         | Only effective in the clean goal. If set to `true`, the plugin will only show the size of the repository without deleting any files |

### analyze

The `analyze` goal is non-destructive and provides a preview of what will happen if you run `clean` with the same
parameters.

To run the `analyze` goal, use the following command:

```
mvn io.github.jagodevreede:repository-cleaner:analyze
```

Here's an example with all parameters set to different values:

```
mvn io.github.jagodevreede:repository-cleaner:analyze -DdaysToKeep=14 -Dclean.snapshotOnly=true
```

### clean

The `clean` goal actually performs the cleaning operation.

To run the clean goal, use the following command:

```
mvn io.github.jagodevreede:repository-cleaner:clean
```

Here's an example with all parameters set to different values:

```
mvn io.github.jagodevreede:repository-cleaner:analyze -DdaysToKeep=14 -Dclean.snapshotOnly=true -Dclean.dryRun=true
```

----

## Open source licensing info

1. [LICENSE](LICENSE)

----
