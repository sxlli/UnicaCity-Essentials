package de.asxka.core.listener.Jobs;

import de.asxka.core.utils.PatternUtils;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import java.util.regex.Pattern;

public class HochseefischerListener {

  public PatternUtils patternUtils = new PatternUtils();
  public boolean isWaitingForHochseefischerBeginn = false;
  private boolean isWaitingForHochseefischerCatchFisch = false;
  private boolean isWaitingForHochseefischerFindSchwarm = false;

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String message = event.chatMessage().getPlainText();
    if (message == null)
      return;

    if (patternUtils.hochseefischerbeginn.matcher(message).find()) {
      isWaitingForHochseefischerBeginn = true;
    }

    if (patternUtils.hochseefischercatchfisch.matcher(message).find()) {
      isWaitingForHochseefischerCatchFisch = true;
    }

    if (patternUtils.hochseefischerfindschwarm.matcher(message).find()) {
      isWaitingForHochseefischerFindSchwarm = true;
    }
  }

  @Subscribe
  public void onHochseefischerBeginnTick(GameTickEvent event) {
    if (!isWaitingForHochseefischerBeginn)
      return;

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null)
      return;

      isWaitingForHochseefischerBeginn = false; // Zurücksetzen, damit es nur EINMAL ausgeführt wird
      sendCommand("/findschwarm");
  }

  @Subscribe
  public void onHochseefischerCatchFischTick(GameTickEvent event) {
    if (!isWaitingForHochseefischerCatchFisch)
      return;

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null)
      return;

      isWaitingForHochseefischerCatchFisch = false; // Zurücksetzen, damit es nur EINMAL ausgeführt wird
      sendCommand("/catchfish");
    }

  @Subscribe
  public void onHochseefischerFindSchwarmTick(GameTickEvent event) {
    if (!isWaitingForHochseefischerFindSchwarm)
      return;

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null)
      return;

    isWaitingForHochseefischerFindSchwarm = false; // Zurücksetzen, damit es nur EINMAL ausgeführt wird

    double x = player.position().getX();
    double y = player.position().getY();
    double z = player.position().getZ();

    // Überprüfe, ob der Spieler in der Nähe von -570, 62, 161 ist (z.B. innerhalb von 5 Blöcken)
    if (Math.abs(x - (-570)) < 5 && Math.abs(y - 62) < 5 && Math.abs(z - 161) < 5) {
      sendCommand("/navi -554/62/107");
    } else {
      sendCommand("/findschwarm");
    }
  }

  private void sendCommand(String command) {
    Laby.labyAPI().minecraft().chatExecutor().chat(command, false);
  }
}
