package xbrsq;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import java.util.ArrayList;

public class ChatSender {
    private static ChatHud chatHud;
    private static ArrayList<String> messageBuffer = new ArrayList<String>();

    public static void fakeRecieve(String message){
        if(chatHud == null){
            System.out.println("Resetting client...");
            chatHud = MinecraftClient.getInstance().inGameHud.getChatHud();
            if(chatHud==null){
                System.out.println("Reset failed!");
                return;
            }
        }
        chatHud.addMessage(Text.of(message));
    }

    public static void bufferMessage(String message){
        messageBuffer.add(message);
    }
    public static void bufferMessages(String[] messages){
        for(String s: messages){
            messageBuffer.add(s);
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
            fakeRecieve(messageBuffer.removeFirst());
        }
    }
}
