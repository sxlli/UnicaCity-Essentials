package de.asxka.core.listener;

import de.asxka.core.utils.PatternUtils;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.chat.ChatMessageSendEvent;
import java.util.regex.Matcher;
import de.asxka.api.InventoryClicker;
import de.asxka.core.widgets.CarLockWidget;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import java.util.Timer;
import java.util.TimerTask;

public class CarListener {

  public PatternUtils patternUtils = new PatternUtils();
  private final InventoryClicker inventoryClicker;
  private final CarLockWidget carLockWidget;
  private boolean wasInVehicle = false;

  public CarListener(InventoryClicker inventoryClicker, CarLockWidget carLockWidget) {
    this.inventoryClicker = inventoryClicker;
    this.carLockWidget = carLockWidget;
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    String plainMessage = event.chatMessage().getPlainText();
    Matcher CarLockmatcher = patternUtils.carClosePattern.matcher(plainMessage);
    Matcher CarFindmatcher = patternUtils.carFindPattern.matcher(plainMessage);
    Matcher carLockedMatcher = patternUtils.carLockedPattern.matcher(plainMessage);
    Matcher carUnlockedMatcher = patternUtils.carUnlockedPattern.matcher(plainMessage);

    if (CarLockmatcher.find()) {
      Laby.labyAPI().minecraft().chatExecutor().chat("/car lock");
      new Timer("CarLockDelay").schedule(new TimerTask() {
        @Override
        public void run() {
          if (inventoryClicker != null) {
            inventoryClicker.clickSlot(0); // 0 = Erster Slot (Redstoneblock)
          }
        }
      }, 50);
    }

    if (carLockedMatcher.find()) {
      this.carLockWidget.setLocked(true, carLockedMatcher.group(1));
    }

    if (carUnlockedMatcher.find()) {
      this.carLockWidget.setLocked(false, carUnlockedMatcher.group(1));
    }

    if (CarFindmatcher.find()) {
      String X_Cordi = CarFindmatcher.group(1);
      String Y_Cordi = CarFindmatcher.group(2);
      String Z_Cordi = CarFindmatcher.group(3);

      Laby.labyAPI().minecraft().chatExecutor().chat("/navi " + X_Cordi + "/" + Y_Cordi + "/" + Z_Cordi);
    }
  }

  @Subscribe
  public void onTick(GameTickEvent event) {
    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null) return;

    Entity vehicle = player.getVehicle();
    boolean isInVehicle = (vehicle != null);

    if (isInVehicle && !wasInVehicle) {
      if (vehicle.getClass().getSimpleName().toLowerCase().contains("minecart")) {
        new Timer("CarStartDelay").schedule(new TimerTask() {
          @Override
          public void run() {
            Laby.labyAPI().minecraft().chatExecutor().chat("/car start");
          }
        }, 500);
      }
    }
    wasInVehicle = isInVehicle;
  }

  @Subscribe
  public void onChatSend(ChatMessageSendEvent event) {
    if (event.getMessage().equalsIgnoreCase("/car lock")) {
      new Timer("CarLockDelaySend").schedule(new TimerTask() {
        @Override
        public void run() {
          if (inventoryClicker != null) {
            inventoryClicker.clickSlot(0);
          }
        }
      }, 50);
    }
  }
}
