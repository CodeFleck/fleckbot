package br.com.codefleck.tradebot.models.strategy;


import java.util.HashMap;
import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Domain object representing a Strategy config.
 */
public class StrategyConfig {

  private String id;
  private String name;
  private String description;
  private String className;
  private Map<String, String> configItems = new HashMap<>();


  // required for Jackson
  public StrategyConfig() {
  }

  public StrategyConfig(StrategyConfig other) {
    this.id = other.id;
    this.name = other.name;
    this.description = other.description;
    this.className = other.className;
    this.configItems = other.configItems;
  }

  public StrategyConfig(String id, String name, String description, String className, Map<String, String> configItems) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.className = className;
    this.configItems = configItems;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public Map<String, String> getConfigItems() {
    return configItems;
  }

  public void setConfigItems(Map<String, String> configItems) {
    this.configItems = configItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StrategyConfig that = (StrategyConfig) o;
    return Objects.equal(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .add("description", description)
        .add("className", className)
        .add("configItems", configItems)
        .toString();
  }
}
