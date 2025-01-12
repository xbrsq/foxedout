package xbrsq.skills;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ClientBossBar;

import java.util.Map;
import java.util.UUID;

public class BossBarExtractor {
    public static Map<UUID, ClientBossBar> getBossBars() {
        return MinecraftClient.getInstance().inGameHud.getBossBarHud().bossBars;
    }

    public static void tick(){
        if(BossBarExtractor.getBossBars() == null){
            return;
        }
        for (Map.Entry<UUID, ClientBossBar> entry : BossBarExtractor.getBossBars().entrySet()) {
            ClientBossBar bossBar = entry.getValue();
            SkillTracker.parseBar(bossBar.getName().getString(), bossBar.getPercent());
        }
    }
}