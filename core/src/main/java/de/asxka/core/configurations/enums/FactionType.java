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

  public boolean matches(String rawName) {
    if (rawName == null) return false;
    String lowerRaw = rawName.toLowerCase();
    if (this == ZIVILIST) return false;
    if (this == POLIZEI && lowerRaw.contains("polizei")) return true;
    if (this == FBI && lowerRaw.contains("fbi")) return true;
    if (this == MEDIC && (lowerRaw.contains("rettungsdienst") || lowerRaw.contains("medic"))) return true;
    if (this == LCN && (lowerRaw.contains("cosa nostra") || lowerRaw.contains("lcn"))) return true;
    if (this == YAKUZA && lowerRaw.contains("yakuza")) return true;
    if (this == CALDERON && lowerRaw.contains("calder")) return true;
    if (this == BALLAS && lowerRaw.contains("ballas")) return true;
    if (this == SOLDNER && (lowerRaw.contains("söld") || lowerRaw.contains("soeld") || lowerRaw.contains("sÃ¶ld"))) return true;
    return false;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
