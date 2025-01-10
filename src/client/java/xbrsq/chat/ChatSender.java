package xbrsq.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class ChatSender {
    private static ChatHud chatHud;
    private static final ArrayList<Message> messageBuffer = new ArrayList<>();
    private static final ArrayList<Message> sendBuffer = new ArrayList<>();

    public static void fakeReceive(String message){
        fakeReceive(message, MessageType.NORMAL);
    }
    public static void fakeReceive(String message, MessageType type){
        if(!isOnline())
            return;
        chatHud.addMessage(Text.of(new Message(message, type).toString()));
    }

    public static void broadcastCommand(String command){
        if(MinecraftClient.getInstance().player == null){
            bufferMessage("player is null.");
            return;
        }
        MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command);
    }
    public static void broadcastMessage(String message){
        if(MinecraftClient.getInstance().player == null){
            bufferMessage("player is null.");
            return;
        }
        MinecraftClient.getInstance().player.networkHandler.sendChatMessage(message);
    }

    public static void bufferMessage(String message){
        bufferMessage(message, MessageType.NORMAL);
    }
    public static void bufferMessage(String message, MessageType type){
        messageBuffer.add(new Message(message, type));
    }
    public static void bufferMessages(String[] messages){
        for(String str: messages){
            messageBuffer.add(new Message(str));
        }
    }
    public static void bufferMessages(String[] messages, MessageType type){
        for(String str: messages){
            messageBuffer.add(new Message(str, type));
        }
    }

    public static boolean isOnline(){
        if(chatHud == null){
            System.out.println("Resetting chatHud...");
            chatHud = MinecraftClient.getInstance().inGameHud.getChatHud();
            if(chatHud==null){
                System.out.println("Reset failed!");
                return false;
            }
        }
        return true;
    }

    public static void tick(){
        if(!isOnline()){
            return;
        }
        while(!messageBuffer.isEmpty()){
            System.out.println("> "+messageBuffer.getFirst());
            fakeReceive(messageBuffer.removeFirst().toString());
        }
    }
}
