package io.github.jagodevreede.repository.cleaner;

import org.apache.maven.plugin.MojoExecutionException;

public class HaltException extends MojoExecutionException {
    HaltException(String message) {
        super(message);
    }
}
