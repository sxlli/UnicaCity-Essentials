package de.asxka.core.configurations.enums;

import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

public enum FactionColor {
  RED("Rot", NamedTextColor.RED),
  DARK_RED("Dunkelrot", NamedTextColor.DARK_RED),
  BLUE("Blau", NamedTextColor.BLUE),
  DARK_BLUE("Dunkelblau", NamedTextColor.DARK_BLUE),
  AQUA("Aqua", NamedTextColor.AQUA),
  DARK_AQUA("Dunkelaqua", NamedTextColor.DARK_AQUA),
  GREEN("Grün", NamedTextColor.GREEN),
  DARK_GREEN("Dunkelgrün", NamedTextColor.DARK_GREEN),
  YELLOW("Gelb", NamedTextColor.YELLOW),
  GOLD("Gold", NamedTextColor.GOLD),
  PINK("Pink", NamedTextColor.LIGHT_PURPLE),
  PURPLE("Lila", NamedTextColor.DARK_PURPLE),
  WHITE("Weiß", NamedTextColor.WHITE),
  GRAY("Grau", NamedTextColor.GRAY),
  DARK_GRAY("Dunkelgrau", NamedTextColor.DARK_GRAY),
  BLACK("Schwarz", NamedTextColor.BLACK);

  private final String name;
  private final TextColor color;

  FactionColor(String name, TextColor color) {
    this.name = name;
    this.color = color;
  }

  public TextColor getColor() {
    return color;
  }

  @Override
  public String toString() {
    return this.name;
  }
}

