package de.asxka.core.listener;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.scoreboard.ScoreboardTeam;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;
import de.asxka.core.UnicaCityEssentials;
import de.asxka.core.utils.FactionCache;
import de.asxka.core.utils.AllianceCache;

public class DutyNameTagListener {

    private final UnicaCityEssentials addon;

    public DutyNameTagListener(UnicaCityEssentials addon) {
        this.addon = addon;
    }

    @Subscribe
    public void onPlayerNameTagRender(PlayerNameTagRenderEvent event) {
        if (!addon.configuration().transferTablistColors().get()) {
            return;
        }

        // Ignoriere TAB_LIST, um Endlos-Render-Schleifen/Lags in der Tablist zu verhindern!
        if (event.context() == PlayerNameTagRenderEvent.Context.TAB_LIST) {
            return;
        }

        NetworkPlayerInfo info = event.getPlayerInfo();
        if (info == null || info.profile() == null) {
            return;
        }

        String username = info.profile().getUsername();
        boolean inFaction = addon.configuration().factionMemberColor().enableFactionMemberColor().get() 
                            && FactionCache.getMembers().contains(username);
        
        boolean inAlliance = addon.configuration().factionMemberColor().enableAllianceMemberColor().get()
                             && AllianceCache.getMembers().contains(username);

        net.labymod.api.client.component.format.TextColor factionColor = null;
        if (inFaction) {
            factionColor = addon.configuration().factionMemberColor().color().get().getColor();
        } else if (inAlliance) {
            factionColor = addon.configuration().factionMemberColor().allianceColor().get().getColor();
        }

        // Fetch TabList Nametag directly onto the player rendering
        Component tabListDisplayName = info.displayName();
        
        Component finalName;
        if (factionColor != null) {
             finalName = Component.text(username).color(factionColor);
        } else if (tabListDisplayName != null) {
             finalName = tabListDisplayName;
        } else {
             finalName = Component.text(username);
        }

        ScoreboardTeam team = info.getTeam();
        if (team != null) {
            event.setNameTag(team.formatDisplayName(finalName));
        } else {
            event.setNameTag(finalName);
        }
    }
}
