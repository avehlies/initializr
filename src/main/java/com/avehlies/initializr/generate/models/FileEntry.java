package com.avehlies.initializr.generate.models;

public class FileEntry {
  private String src;
  private String dest;
  private boolean isDirectory;
  private byte[] content;
  private boolean read;
  private boolean write;
  private boolean execute;

  public FileEntry() {}

  public String getSrc() {
    return src;
  }

  public String getDest() {
    return dest;
  }

  public boolean isDirectory() {
    return isDirectory;
  }

  public byte[] getContent() {
    return content;
  }

  public FileEntry setSrc(String src) {
    this.src = src;
    return this;
  }

  public FileEntry setDest(String dest) {
    this.dest = dest;
    return this;
  }

  public FileEntry setDirectory(boolean isDirectory) {
    this.isDirectory = isDirectory;
    return this;
  }

  public FileEntry setContent(byte[] content) {
    this.content = content;
    return this;
  }

  public boolean isRead() {
    return read;
  }

  public FileEntry setRead(boolean read) {
    this.read = read;
    return this;
  }

  public boolean isWrite() {
    return write;
  }

  public FileEntry setWrite(boolean write) {
    this.write = write;
    return this;
  }

  public boolean isExecute() {
    return execute;
  }

  public FileEntry setExecute(boolean execute) {
    this.execute = execute;
    return this;
  }
}
