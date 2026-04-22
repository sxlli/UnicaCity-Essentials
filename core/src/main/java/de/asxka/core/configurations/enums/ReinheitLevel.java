package de.asxka.core.configurations.enums;

public enum ReinheitLevel {
  LEVEL_0("Sehr Gute Reinheit", "0"),
  LEVEL_1("Gute Reinheit", "1"),
  LEVEL_2("Mittlere Reinheit", "2"),
  LEVEL_3("Schlechte Reinheit", "3");

  private final String displayName;
  private final String value;

  ReinheitLevel(String displayName, String value) {
    this.displayName = displayName;
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.displayName;
  }
}

