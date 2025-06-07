package com.avehlies.initializr.generate;

import com.avehlies.initializr.generate.models.FileEntry;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "generate")
public class GenerateCommand implements Callable<Integer> {

  @Option(
      names = {"--template"},
      description = "The template to use for project generation",
      required = true)
  private String template;

  @Option(
      names = {"--vertical"},
      description = "The business vertical",
      required = false)
  private String vertical;

  @Option(
      names = {"--name"},
      description = "The name of the project to generate",
      required = false)
  private String projectName;

  @Option(
      names = {"--output"},
      description = "The output directory for the generated project",
      required = false)
  private String outputDir;

  @Option(
      names = {"--package"},
      description = "The group package for the generated project",
      required = false)
  private String packageGroup;

  @Option(
      names = {"--email"},
      description = "The email address or distribution list of the owner of the project",
      required = false)
  private String email;

  @Override
  public Integer call() throws Exception {
    System.out.println("Generate command executed!");
    getFilesInTemplateDirectory("quarkus-monolith");
    return 0;
  }

  public List<String> getFilesInTemplateDirectory(String templateName) throws Exception {
    List<FileEntry> fileEntries = getFileEntries("templates/" + templateName);

    for (FileEntry fileEntry : fileEntries) {
      String dest = fileEntry.getDest();
      dest = new java.io.File(".").getCanonicalPath() + "/" + outputDir + "/" + projectName + "/" + dest.replace("PACKAGE_NAME", packageGroup.replace(".", "/"));
      fileEntry.setDest(dest);
      if (fileEntry.isDirectory()) {
        Files.createDirectories(Path.of(fileEntry.getDest()));
      } else {
        if (fileEntry.getDest().endsWith(".tpl")) {
          dest = dest.replace(".tpl", "");
          String content = new String(fileEntry.getContent(), Charset.forName("UTF-8"));
          content = content.replace("<projectName>", projectName);
          content = content.replace("<packageGroup>", packageGroup);
          Files.write(Path.of(dest), content.getBytes());
        } else {
          Files.write(Path.of(dest), fileEntry.getContent());
        }
      }
      // Path.of(fileEntry.getDest()).toFile().setReadable(fileEntry.isRead());
      // Path.of(fileEntry.getDest()).toFile().setWritable(fileEntry.isWrite());
      // Path.of(fileEntry.getDest()).toFile().setExecutable(fileEntry.isExecute());
      System.out.println(fileEntry.getDest());
    }
    return List.of();
  }

  // Get all paths from a folder that inside the JAR file
  private List<FileEntry> getFileEntries(String folder) throws URISyntaxException, IOException {

    final String path = folder;
    final File jarFile =
        new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

    List<Path> paths = new ArrayList<>();

    if (jarFile.isFile()) { // Run with JAR file

      URI resource = getClass().getClassLoader().getResource(folder).toURI();
      try (FileSystem fs = FileSystems.newFileSystem(resource, Collections.emptyMap())) {
        String[] jarPath = resource.toString().split("!", 2);
        String jarReference = jarPath[1].replace("!", "");
        return readFileEntries(fs.getPath(jarReference));
      }

    } else { // Run with IDE
      System.out.println("running with IDE");

      final URL url = getClass().getClassLoader().getResource(path);
      if (url != null) {
        try {
          System.out.println(url.toURI());
          final File apps = new File(url.toURI());
          return readFileEntries(apps.toPath());
        } catch (URISyntaxException ex) {
          // never happens
        }
      }
    }
    return List.of();
  }

  private List<FileEntry> readFileEntries(Path path) throws IOException {
    List<FileEntry> fileEntries = new ArrayList<>();
    Files.walk(path)
        .forEach(
            p -> {
              String relativePath = path.relativize(p).toString();
              if (relativePath.startsWith(".")) {
                return;
              }
              FileEntry fileEntry =
                  new FileEntry()
                      .setSrc(path.relativize(p).toString())
                      .setDest(path.relativize(p).toString())
                      .setDirectory(Files.isDirectory(p));
                      // .setRead(p.toFile().canRead())
                      // .setWrite(p.toFile().canWrite())
                      // .setExecute(p.toFile().canExecute());

              if (!fileEntry.isDirectory()) {
                try {
                  fileEntry.setContent(Files.readAllBytes(p));
                } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
              }
              fileEntries.add(fileEntry);
            });
    return fileEntries;
  }
}
