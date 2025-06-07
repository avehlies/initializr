package com.avehlies.initializr;

import com.avehlies.initializr.generate.GenerateCommand;
import com.avehlies.initializr.template.TemplatesCommand;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@TopCommand
@Command(
    name = "initializr",
    subcommands = {CommandLine.HelpCommand.class, GenerateCommand.class, TemplatesCommand.class})
public class AppCommand {}
