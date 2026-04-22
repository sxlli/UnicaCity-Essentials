package de.asxka.core.listener.Jobs;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import java.util.regex.Pattern;

public class JobDropListener {

  private final Pattern dropfischPattern = Pattern.compile(
      "Du hast keine Netze mehr. Bring den gefangenen Fisch zur.ck zum Steg.");
  private final Pattern droptabakPattern = Pattern.compile(
      "Bringe es nun zur Shishabar und gibt es mit /droptabak ab.");
  private final Pattern dropblumenPattern = Pattern.compile(
      "Bring die Blumen nun zum Gärtner zurück und gebe sie mit /dropblumen ab.");
  private boolean isWaitingForDropFisch = false;
  private boolean isWaitingForDropTabak = false;
  private boolean isWaitingForDropBlumen = false;

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String message = event.chatMessage().getPlainText();
    if (message == null)
      return;

    if (dropfischPattern.matcher(message).find()) {
      isWaitingForDropFisch = true;
    }

    if (droptabakPattern.matcher(message).find()) {
      isWaitingForDropTabak = true;
    }

    if (dropblumenPattern.matcher(message).find()) {
      isWaitingForDropBlumen = true;
    }
  }

  @Subscribe
  public void onDropFischTick(GameTickEvent event) {
    if (!isWaitingForDropFisch)
      return;

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null)
      return;

    if (isPlayerNear(player, -503, 63, 198, 3.0)) {
      isWaitingForDropFisch = false;
      sendCommand("/dropfisch");
    }
  }

  @Subscribe
  public void onDropTabakTick(GameTickEvent event) {
    if (!isWaitingForDropTabak)
      return;

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null)
      return;

    if (isPlayerNear(player, -172, 70, -70, 3.0)) {
      isWaitingForDropTabak = false; // Zurücksetzen, damit es nur EINMAL ausgeführt wird
      sendCommand("/droptabak");
    }
  }

  @Subscribe
  public void onDropBlumenTick(GameTickEvent event) {
    if (!isWaitingForDropBlumen)
      return;

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null)
      return;

    if (isPlayerNear(player, -121, 71, 421, 3.0)) {
      isWaitingForDropBlumen = false; // Zurücksetzen, damit es nur EINMAL ausgeführt wird
      sendCommand("/dropblumen");
    }
  }

  private void sendCommand(String command) {
    Laby.labyAPI().minecraft().chatExecutor().chat(command, false);
  }

  private boolean isPlayerNear(ClientPlayer player, double targetX, double targetY, double targetZ,
      double radius) {
    double dx = player.position().getX() - targetX;
    double dy = player.position().getY() - targetY;
    double dz = player.position().getZ() - targetZ;
    return Math.sqrt(dx * dx + dy * dy + dz * dz) <= radius;
  }
}
