package de.asxka.core.utils;

import java.util.regex.Pattern;

public class PatternUtils { 
  
  //Team
  public final Pattern TEAM_SocialMedia_Chat = Pattern.compile("^\\[Social\\-Media\\] ([^ ]+) (.*)$");
  
  //Fraktionen
  public final Pattern factionHeaderPattern = Pattern.compile("===\\s*Fraktionsmitglieder \\[(.*?)\\]\\s*===");
  public final Pattern factionallHeaderPattern = Pattern.compile("={3,}\\s*Mitglieder von (.*?)\\s*\\(\\d+/\\d+\\)\\s*={3,}");
  public final Pattern reinfPattern = Pattern.compile("(Unterstützung benötigt!|Medic benötigt!|Dringend!) (.+?) benötigt Unterstützung in der Nähe von (.+?)! \\((.+?) Meter entfernt\\)");
  public final Pattern reinfAcceptPattern = Pattern.compile("(?:(.+?) )?([a-zA-Z0-9_]+) kommt zum Verstärkungsruf von ([a-zA-Z0-9_]+)! \\((.+?) Meter entfernt\\)");

  //Nametag Änderungen
  public final Pattern wantedListPattern = Pattern.compile("^\\s*-\\s*(?:\\[.*?\\])?([a-zA-Z0-9_]+)\\s*[|¦]\\s*(\\d+)\\s*WPS");
  public final Pattern wantedLevelPattern = Pattern.compile("(?:\\[.*?\\])?([a-zA-Z0-9_]+)['´]s momentanes WantedLevel: (\\d+)");
  public final Pattern wantedClearedPattern = Pattern.compile("hat (?:\\[.*?\\])?([a-zA-Z0-9_]+)['´]s Akten gel[öo]scht");
  public final Pattern killedPattern = Pattern.compile("(?:\\[.*?\\])?([a-zA-Z0-9_]+) wurde von .*? get[öo]tet");
  public final Pattern jailedPattern = Pattern.compile("(?:\\[.*?\\])?([a-zA-Z0-9_]+) wurde von .*? eingesperrt");

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

  //Fraktionaktivitäten (Bombe, Staatsbank, usw)
  public final Pattern bombplacePattern = Pattern.compile("News: ACHTUNG! Es wurde eine Bombe in der Nähe von (.*?) gefunden!");
  public final Pattern bombdefusePattern = Pattern.compile("News: Die Bombe konnte entschärft werden!");
  public final Pattern bombexplodePattern = Pattern.compile("News: Die Bombe konnte nicht entschärft werden!");
  public final Pattern bankrobberyStartPattern = Pattern.compile("");
  public final Pattern bankrobberyEndPattern = Pattern.compile("");
  public final Pattern bankrobberyFailedPattern = Pattern.compile("");

  //Geld
  public final Pattern bankPattern = Pattern.compile("Bankguthaben betr.gt:\\s*[+-]?(\\d+)");
  public final Pattern bankUpdatePattern = Pattern.compile("Neuer Kontostand:\\s*[+-]?(\\d+)");
  public final Pattern depositPattern = Pattern.compile("Eingezahlt:\\s*\\+(\\d+)");
  public final Pattern withdrawPattern = Pattern.compile("Auszahlung:\\s*-(\\d+)");
  public final Pattern moneyPattern = Pattern.compile("Geld:\\s*[+-]?(\\d+)");
}
