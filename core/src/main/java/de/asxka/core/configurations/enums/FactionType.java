package de.asxka.core.configurations.enums;

public enum FactionType {
  ZIVILIST("Zivilist", ""),
  POLIZEI("Polizei", "polizei"),
  FBI("FBI", "fbi"),
  MEDIC("Rettungsdienst", "medic"),
  LCN("La Cosa Nostra", "lcn"),
  YAKUZA("Yakuza", "yakuza"),
  CALDERON("Calderón Kartell", "calderon"),
  BALLAS("Ballas", "ballas"),
  SOLDNER("Söldner", "söldner");

  private final String name;
  private final String commandName;

  FactionType(String name, String commandName) {
    this.name = name;
    this.commandName = commandName;
  }

  public String getName() {
    return name;
  }

  public String getCommandName() {
    return commandName;
  }

  @Override
  public String toString() {
    return this.name;
  }
}

