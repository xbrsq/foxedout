package xbrsq;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

import xbrsq.autosell.AutoSell;
import xbrsq.autosell.AutoSellCommands;
import xbrsq.chat.ChatSender;
import xbrsq.commands.CommandController;
import xbrsq.skills.SkillCommands;
import xbrsq.scheduler.Scheduler;
import xbrsq.skills.BossBarExtractor;
import xbrsq.skills.SkillEntry;
import xbrsq.skills.SkillSorter;
import xbrsq.skills.SkillTracker;

import java.util.ArrayList;

public class FoxedoutClient implements ClientModInitializer {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	private static TextRenderer textRenderer = client.textRenderer;

	public static int X = 10;
	public static int Y = 10;
	public static int lineSize = 10;
	public static boolean hideSkillMessages = false;

	public static int sorting = SkillSorter.S_NONE*SkillSorter.M_ASC;
	public static int limit = 1024;

	public static boolean doRender = true;

	public static final String version = "0.3.1";

	// sync runs off of server ticks, async runs off of client ticks.
	public static Scheduler syncScheduler;
	public static Scheduler asyncScheduler;

	public static CommandController commandController;

	public AutoSell autoSell;

	public static void setPos(int x, int y){
		X = x;
		Y = y;
	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.


		// init: -------------------------------------------
		syncScheduler = new Scheduler();
		asyncScheduler = new Scheduler();
		commandController = new CommandController();
		autoSell = new AutoSell();


		// setup event handlers: ---------------------------

		// rendering
		HudRenderCallback.EVENT.register((DrawContext context, RenderTickCounter tickDelta) -> {
			if(!doRender) return;
			if (textRenderer != null) {
				// textRenderer exists, so you can use it
				context.drawText(textRenderer, "Skills:",	   X,		Y,		0xDDDDDD,	true);
				context.drawText(textRenderer, "Level:",	X+60,	Y,		0xDDDDDD,	true);
				context.drawText(textRenderer, "XP:",		X+100,	Y,	0xDDDDDD,	true);

				int linenum = 1;
				ArrayList<SkillEntry> skills = SkillSorter.sort(SkillTracker.getSkills(), new int[]{SkillSorter.S_PIN*SkillSorter.M_DESC, sorting});
				if(skills.size()>limit){
					skills = new ArrayList<>(skills.subList(0, limit));;
				}
				for (SkillEntry entry : skills) {
					int entryColor = entry.highlighted?0xFFFFFF:0xDDDDDD;
					context.drawText(textRenderer, entry.name, X, Y + (lineSize*linenum), entryColor, true);
					context.drawText(textRenderer, String.valueOf(entry.level), X+65, Y + (lineSize*linenum), entryColor, true);
					context.drawText(textRenderer, entry.getPercent() + "%", X + 100, Y + (lineSize * linenum), getPercentageColor(entry.getPercent()), true);
					linenum++;
				}

			} else {
				// create textRenderer
				System.err.println("TextRenderer is null! Attempting to set it.");

				textRenderer = client.textRenderer;
			}
		});

		// Client Tick functions
		ClientTickEvents.END_CLIENT_TICK.register((client)->{
			asyncScheduler.tick();
			BossBarExtractor.tick();
			ChatSender.tick();
		});

		// Server Tick functions
		ClientTickEvents.END_WORLD_TICK.register((world)-> {
			syncScheduler.tick();
		});

		// chat message received
		ClientReceiveMessageEvents.ALLOW_GAME.register((text, unknownBoolean)->{
			if(SkillTracker.parseText(text.getString())){
				return !hideSkillMessages; // hide message
			}
			return true;
		});

		ClientSendMessageEvents.ALLOW_CHAT.register(commandController::parseMessage);

		// add commands to the controller
		addCommandModules();
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

	void addCommandModules(){
		commandController.modules.add(new RootCommands());
		commandController.modules.add(new SkillCommands());
		commandController.modules.add(new AutoSellCommands(autoSell));
	}
}
