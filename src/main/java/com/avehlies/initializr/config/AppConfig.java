package com.avehlies.initializr.config;

import io.smallrye.config.ConfigMapping;
import java.util.List;

@ConfigMapping(prefix = "config")
public interface AppConfig {
  List<String> templates();
}
