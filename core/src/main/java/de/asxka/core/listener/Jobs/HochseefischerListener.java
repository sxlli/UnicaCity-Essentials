package de.asxka.core.listener.Jobs;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import java.util.regex.Pattern;

public class HochseefischerListener {

  private final Pattern hochseefischerbeginn = Pattern.compile(
      "Fahre nun zu den Fischschw.rmen und wird dein Fischenetz mit /catchfish aus.");

  private final Pattern hochseefischercatchfisch = Pattern.compile(
      "Du hast einen Fischschwarm gefunden!");

  private final Pattern hochseefischerfindschwarm = Pattern.compile(
      "frischen Fisch gefangen!");


  public boolean isWaitingForHochseefischerBeginn = false;
  private boolean isWaitingForHochseefischerCatchFisch = false;
  private boolean isWaitingForHochseefischerFindSchwarm = false;

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String message = event.chatMessage().getPlainText();
    if (message == null)
      return;

    if (hochseefischerbeginn.matcher(message).find()) {
      isWaitingForHochseefischerBeginn = true;
    }

    if (hochseefischercatchfisch.matcher(message).find()) {
      isWaitingForHochseefischerCatchFisch = true;
    }

    if (hochseefischerfindschwarm.matcher(message).find()) {
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
      sendCommand("/findschwarm");
    }

  private void sendCommand(String command) {
    Laby.labyAPI().minecraft().chatExecutor().chat(command, false);
  }
}
