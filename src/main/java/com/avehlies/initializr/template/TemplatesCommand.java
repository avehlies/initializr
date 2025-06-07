package com.avehlies.initializr.template;

import com.avehlies.initializr.config.AppConfig;
import jakarta.inject.Inject;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "templates",
    subcommands = {CommandLine.HelpCommand.class},
    description = "Display a list of templates available")
public class TemplatesCommand implements Callable<Integer> {

  private AppConfig appConfig;

  @Inject
  public TemplatesCommand(AppConfig appConfig) {
    this.appConfig = appConfig;
  }

  @Override
  public Integer call() throws Exception {
    System.out.println("Templates");
    System.out.println("=========");
    for (String template : appConfig.templates()) {
      System.out.println("- " + template);
    }
    return 0;
  }
}
