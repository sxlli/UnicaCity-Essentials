package de.asxka.core.utils;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternUtils { 
  
  //Team
  public final Pattern TEAM_SocialMedia_Chat = Pattern.compile("^\\[Social\\-Media\\] ([^ ]+) (.*)$");
  
  //Fraktionen
  public final Pattern factionHeaderPattern = Pattern.compile("===\\s*Fraktionsmitglieder \\[(.*?)\\]\\s*===");
  public final Pattern factionallHeaderPattern = Pattern.compile("={3,}\\s*Mitglieder von (.*?)\\s*\\(\\d+/\\d+\\)\\s*={3,}");
  public final Pattern reinfPattern = Pattern.compile("(Unterstützung benötigt!|Medic benötigt!|Dringend!|Drogenabnahme!) (.+?) benötigt Unterstützung (.*?)! \\((\\d+) Meter entfernt\\)");
  public final Pattern reinfAcceptPattern = Pattern.compile("(.+?) kommt zum Verstärkungsruf von (.+?)! \\((\\d+) Meter entfernt\\)");
  public final Pattern takeGunsPattern = Pattern.compile("Beamter (.+?) hat (.+?) die Waffen abgenommen.");
  public final Pattern takeDrugsPattern = Pattern.compile("Beamter (.+?) hat (.+?) seine Drogen abgenommen!");
  public final Pattern fbankDepositPattern = Pattern.compile("\\[F-Bank\\]\\s*([a-zA-Z0-9_]+) hat (\\d[\\d.]*)\\$ in die Fraktionsbank eingezahlt\\.");
  public final Pattern fbankWithdrawPattern = Pattern.compile("\\[F-Bank\\]\\s*([a-zA-Z0-9_]+) hat (\\d[\\d.]*)\\$ aus der Fraktionsbank (?:ausgezahlt|abgehoben)\\.");
  public final Pattern fbankReasonPattern = Pattern.compile("Grund:\\s*(.+)$");
  public final Pattern asservatenkammerPattern = Pattern.compile("HQ:\\s*(?:.*?\\s+)?([a-zA-Z0-9_]+)\\s+hat\\s+(.+?)\\s+in der Asservatenkammer verstaut.*");

  //Nametag Änderungen
  // optionaler Prefix wie [UC] wird akzeptiert; der eigentliche Spielername steht in der ersten Capturing-Group
  public final Pattern wantedListPattern = Pattern.compile("^\\s*-\\s*(?:\\[[^\\]]+\\]\\s*)?([a-zA-Z0-9_]+)\\s*[|¦]\\s*(\\d+)\\s*WPS");
  public final Pattern wantedLevelPattern = Pattern.compile("(?:\\[[^\\]]+\\]\\s*)?([a-zA-Z0-9_]+).*?s momentanes WantedLevel.*?(\\d+)");
  public final Pattern wantedReasonPattern = Pattern.compile("Gesuchter.*?(?:\\[[^\\]]+\\]\\s*)?([a-zA-Z0-9_]+).*?Grund.*?[:\\s]+(.*?)(?:\\s*\\[.*?\\])?$");
  public final Pattern wantedClearedPattern = Pattern.compile("HQ.*?(?:\\[[^\\]]+\\]\\s*)?([a-zA-Z0-9_]+) hat (?:\\[[^\\]]+\\]\\s*)?([a-zA-Z0-9_]+).*?Akten gel.*scht");
  public final Pattern killedPattern = Pattern.compile("(?:\\[[^\\]]+\\]\\s*)?([a-zA-Z0-9_]+) wurde von (.*?) get[öo]tet");
  public final Pattern jailedPattern = Pattern.compile("HQ:\\s*(?:\\[[^\\]]+\\]\\s*)?([a-zA-Z0-9_]+) wurde von (.*?) eingesperrt");
  public final Pattern reasonPattern = Pattern.compile("HQ:\\s*Fahndungsgrund:\\s*(.*?)\\s*[|¦]\\s*Fahndungszeit:\\s*(\\d+)\\s*Minute(?:n)?.*");
  public final Pattern wantedChangePattern = Pattern.compile("HQ:\\s*(?:.*?\\s+)?(?:\\[[^\\]]+\\]\\s*)?([a-zA-Z0-9_]+)\\s+hat\\s+(?:\\[[^\\]]+\\]\\s*)?([a-zA-Z0-9_]+)s\\s+WantedPunkte\\s+verändert!");
  public final Pattern wantedChangeAmountPattern = Pattern.compile("HQ:\\s*Neuer Grund:\\s*(.*?)\\s*(?:[|¦]\\s*)?\\[(\\d+)\\s*[»>]\\s*(\\d+)\\s*WantedPunkte\\](?:\\s*\\[.*?\\])?$");

  //Jobs
  public final Pattern hochseefischerbeginn = Pattern.compile("\"^\\\\[Fischer] Mit /findschwarm kannst du dir den nächsten Fischschwarm anzeigen lassen\\\\.$\"");
  public final Pattern hochseefischercatchfisch = Pattern.compile("Du hast einen Fischschwarm gefunden!");
  public final Pattern hochseefischerfindschwarm = Pattern.compile("frischen Fisch gefangen!");

  public final Pattern dropfischPattern = Pattern.compile("Du hast keine Netze mehr. Bring den gefangenen Fisch zur.ck zum Steg.");
  public final Pattern droptabakPattern = Pattern.compile("Bringe es nun zur Shishabar und gibt es mit /droptabak ab.");
  public final Pattern dropblumenPattern = Pattern.compile("Bring die Blumen nun zum Gärtner zurück und gebe sie mit /dropblumen ab.");

  //Sonstiges
  public final Pattern friendStatusPattern = Pattern.compile("» Freundesliste: (.*?) ist nun (Online|Offline)", Pattern.CASE_INSENSITIVE);
  public final Pattern paydayTimer = Pattern.compile("Pay[dD]ay.*?(\\d+)\\s*/\\s*60\\s*Minuten");
  public final Pattern carClosePattern = Pattern.compile("^\\[Car\\] Dein Fahrzeug ist abgeschlossen");
  public final Pattern carFindPattern = Pattern.compile("\\[Car\\] Das Fahrzeug befindet sich bei » X: ([-\\d.]+) \\| Y: ([-\\d.]+) \\| Z: ([-\\d.]+)");
  public final Pattern carLockedPattern = Pattern.compile("^\\[Car\\] Du hast dein(?:en)? (.*) abgeschlossen\\.");
  public final Pattern carUnlockedPattern = Pattern.compile("^\\[Car\\] Du hast dein(?:en)? (.*) aufgeschlossen\\.");

  //Fraktionaktivitäten (Bombe, Staatsbank, usw)
  public final Pattern bombplacePattern = Pattern.compile("News: ACHTUNG! Es wurde eine Bombe in der Nähe von (.*?) gefunden!");
  public final Pattern bombdefusePattern = Pattern.compile("News: Die Bombe konnte erfolgreich entschärft werden!");
  public final Pattern bombexplodePattern = Pattern.compile("News: Die Bombe konnte nicht entschärft werden!");
  public final Pattern bankrobberyStartPattern = Pattern.compile("");
  public final Pattern bankrobberyEndPattern = Pattern.compile("");
  public final Pattern bankrobberyFailedPattern = Pattern.compile("");

  //Geld
  public final Pattern bankPattern = Pattern.compile("Bankguthaben betr.gt:\\s*[+-]?(\\d[\\d.]*)");
  public final Pattern bankUpdatePattern = Pattern.compile("Neuer Kontostand:\\s*[+-]?(\\d[\\d.]*)");
  public final Pattern paydayNewAmountPattern = Pattern.compile("Neuer Betrag:\\s*(\\d[\\d.]*)\\$");
  public final Pattern depositPattern = Pattern.compile("(?i)(?:Eingezahlt)[:\\s]*\\+?\\s*(\\d[\\d.]*)\\$?");
  public final Pattern withdrawPattern = Pattern.compile("(?i)(?:Auszahlung|Abgehoben)[:\\s]*-?\\s*(\\d[\\d.]*)\\$?");
  public final Pattern moneyPattern = Pattern.compile("Geld[:\\s]*?(\\d[\\d.]*)\\$");
  public final Pattern statsPattern = Pattern.compile("(?i)\\b(inventar|stat|stats|statistik|statistiken)\\b");
  public final Pattern accountStatementPattern = Pattern.compile("(?i)\\b(kontoauszug|vorheriger kontostand|neuer kontostand|kontostand)\\b");
  public final Pattern moneyChangePattern = Pattern.compile("([+\u002D\u2212])\\s*(\\d[\\d.]*)\\s*\\$");
}
