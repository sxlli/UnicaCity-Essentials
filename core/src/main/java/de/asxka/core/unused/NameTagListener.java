package de.asxka.core.unused;

import de.asxka.core.utils.GradientUtils;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.tag.tags.ComponentNameTag;
import net.labymod.api.client.render.state.entity.EntitySnapshot;

import java.util.Collections;
import java.util.List;

public class NameTagListener extends ComponentNameTag {

  private NameTagListener() {
    // Privat, da wir die create() Methode nutzen
  }

  //public static NameTagListener create() {
    //return new NameTagListener();
  //}

  @Override
  public float getScale() {
    // Normalgröße ist oft 1.0F, du kannst hier kleinere Werte nehmen wie 0.7F oder 0.8F
    return 0.55F;
  }

  @Override
  protected List<Component> buildComponents(EntitySnapshot snapshot) {
    // 1. HIER KANNST DU BEDINGUNGEN FESTLEGEN (z.B. nur bei bestimmten Spielern)
    // - snapshot hat sehr viele Informationen über den Spieler!
    // Du kannst auf Extras wie den Namen via "snapshot.name()" oder ähnlich zugreifen, 
    // je nachdem wo genau die Daten in deinem Projekt extrahiert werden.

    // 2. Gib die Text-Komponente zurück, die als Subtitle gerendert werden soll
    // Wenn nichts angezeigt werden soll, gib Collections.emptyList() zurück.
    return Collections.singletonList(
        Component.text("ᴜᴄᴇ", NamedTextColor.WHITE)
            .append(Component.text(" х ", NamedTextColor.GRAY))
            .append(GradientUtils.gradient("ᴅᴇᴠ", TextColor.color(0x5234eb), TextColor.color(0x871aed)))
    );
  }
}
