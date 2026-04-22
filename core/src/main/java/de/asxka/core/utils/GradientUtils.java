package de.asxka.core.utils;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;

public class GradientUtils {

  /**
   * Erstellt ein Component mit einem schönen Farbverlauf.
   *
   * @param text Der anzuzeigende Text (z.B. "Solara Addon")
   * @param colors Die Farben für den Verlauf (min. 2 Farben werden benötigt)
   * @return Das fertig formatierte Text-Component
   */
  public static Component gradient(String text, TextColor... colors) {
    if (text == null || text.isEmpty()) {
      return Component.empty();
    }
    if (colors == null || colors.length == 0) {
      return Component.text(text);
    }
    if (colors.length == 1) {
      return Component.text(text, colors[0]);
    }

    Component result = Component.empty();
    char[] chars = text.toCharArray();
    int length = chars.length;

    for (int i = 0; i < length; i++) {
      float ratio = length == 1 ? 0 : (float) i / (float) (length - 1);
      float colorIndex = ratio * (colors.length - 1);
      int index = (int) colorIndex;
      float colorRatio = colorIndex - index;

      TextColor c1 = colors[index];
      TextColor c2 = index + 1 < colors.length ? colors[index + 1] : c1;

      TextColor interpolated = lerpColor(colorRatio, c1, c2);
      result = result.append(Component.text(String.valueOf(chars[i]), interpolated));
    }

    return result;
  }

  private static TextColor lerpColor(float ratio, TextColor c1, TextColor c2) {
    // Farben in RGB aufteilen
    int r1 = (c1.value() >> 16) & 0xFF;
    int g1 = (c1.value() >> 8) & 0xFF;
    int b1 = c1.value() & 0xFF;

    int r2 = (c2.value() >> 16) & 0xFF;
    int g2 = (c2.value() >> 8) & 0xFF;
    int b2 = c2.value() & 0xFF;

    // Werte interpolieren
    int r = Math.round(r1 + (r2 - r1) * Math.max(0, Math.min(1, ratio)));
    int g = Math.round(g1 + (g2 - g1) * Math.max(0, Math.min(1, ratio)));
    int b = Math.round(b1 + (b2 - b1) * Math.max(0, Math.min(1, ratio)));

    // Zurück zu einer TextColor konvertieren (Hex Wert)
    return TextColor.color((r << 16) | (g << 8) | b);
  }
}
