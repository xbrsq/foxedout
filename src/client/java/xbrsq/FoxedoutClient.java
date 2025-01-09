package xbrsq;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import java.util.ArrayList;

public class FoxedoutClient implements ClientModInitializer {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	private static TextRenderer textRenderer = client.textRenderer;

	public static int X = 10;
	public static int Y = 10;
	public static int lineSize = 10;
	public static boolean hideSkillMessages = false;

	public static boolean doRender = true;

//	private static ArrayList<String[]> Texts = new ArrayList<String[]>();

	public static void setPos(int x, int y){
		X = x;
		Y = y;
	}

//	public static void addText(String[] strs){
//		Texts.add(strs);
//	}
//	public static void setTexts(ArrayList<SkillEntry> skillEntries){
//		Texts = texts;
//	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.



		// rendering
		HudRenderCallback.EVENT.register((DrawContext context, RenderTickCounter tickDelta) -> {
			if(!doRender) return;
			if (textRenderer != null) {
				// Safe to call methods on textRenderer
				context.drawText(textRenderer, "Skills:",	X+00, Y,		0xFFFFFFFF,	true);
				context.drawText(textRenderer, "Level:",	X+60, Y,		0xFFFFFFFF,	true);
				context.drawText(textRenderer, "XP:",		X+100, Y,	0xFFFFFFFF,	true);

				int linenum = 1;
                for (SkillEntry entry : SkillTracker.getSkills()) {
					context.drawText(textRenderer, entry.name, X+000, Y + (lineSize*linenum), 0xFFFFFFFF, true);
					context.drawText(textRenderer, String.valueOf(entry.level), X+65, Y + (lineSize*linenum), 0xFFFFFFFF, true);
					context.drawText(textRenderer, String.valueOf(entry.getPercent()) + "%", X + 100, Y + (lineSize * linenum), getPercentageColor(entry.getPercent()), true);
					linenum++;
                }
			} else {
				// create textRenderer
				System.err.println("TextRenderer is null!");

				textRenderer = client.textRenderer;
			}
		});

		// Tick functions
		ClientTickEvents.END_CLIENT_TICK.register((client)->{
			BossBarExtractor.tick();
//			setTexts(SkillTracker.getText());
			ChatSender.tick();
		});

		// chat message received
		ClientReceiveMessageEvents.ALLOW_GAME.register((text, unknownBoolean)->{
			if(SkillTracker.parseText(text.getString())){
				return hideSkillMessages; // hide message
			}
			return true;
		});

		ClientSendMessageEvents.ALLOW_CHAT.register(CustomCommands::parseMessage);
	}

	private int getPercentageColor(int percent){
		if(percent<10){
			return 0x880000;
		}
		if(percent<25){
			return 0xFF0000;
		}
		if(percent<50){
			return 0xFF8800;
		}
		if(percent<80){
			return 0xFFFF00;
		}
		if(percent<90){
			return 0x00FF00;
		}

		return 0x4444FF;
	}


}