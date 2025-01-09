package xbrsq;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BossBarExtractor {
    @SuppressWarnings("unchecked")
    public static Map<UUID, ClientBossBar> getBossBars() {
        return MinecraftClient.getInstance().inGameHud.getBossBarHud().bossBars;
    }

    public static void tick(){
        if(xbrsq.BossBarExtractor.getBossBars() == null){
            return;
        }
        for (Map.Entry<UUID, ClientBossBar> entry : BossBarExtractor.getBossBars().entrySet()) {
//            System.out.println("Bass Bars Exist!");
            ClientBossBar bossBar = entry.getValue();
//            System.out.println("Boss Bar Title: " + bossBar.getName().getString());
//            System.out.println("Progress: " + bossBar.getPercent());
            SkillTracker.parseBar(bossBar.getName().getString(), bossBar.getPercent());

//            Excavation Lv.12


        }
    }
}