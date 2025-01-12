package xbrsq.commands;

import org.jetbrains.annotations.NotNull;
import xbrsq.chat.ChatSender;
import xbrsq.chat.MessageType;

import java.util.Arrays;
import java.util.LinkedList;

public class CommandController {

    public static final boolean PASS = true;
    public static final boolean STOP = false;

    public static boolean disableIntercept = STOP;

    public String prefix = ".";

    public String[][] helpMessages = {{"Help not created yet"}};


    // disable to force all chat messages to be allowed through

    public final LinkedList<CommandModule> modules = new LinkedList<>();

    public boolean parseMessage(@NotNull String rawMessage){
        // returns whether to allow message
        if(!rawMessage.startsWith(prefix)){
            return PASS;
        }
        String[] parsedMessage = rawMessage.substring(prefix.length()).trim().split(" ");

        String[] sliced = Arrays.copyOfRange(parsedMessage, 1, parsedMessage.length);
        if(sliced.length==0){
            sliced = new String[]{""};
        }
        for(CommandModule module: modules){
            if(module.isMine(parsedMessage)){
                if(!module.enabled) {
//                    message("module "+module.name+" is disabled");
                    continue;
                }
                if(module.useRaw){
                    return module.parseMessage(parsedMessage);
                }
                return module.parseMessage(sliced);
            }
        }
        message("Unknown module: "+parsedMessage[0]+".");
        return disableIntercept;
    }

    public static void immediateMessage(String s) {
        ChatSender.fakeReceive(s);
    }
    public static void immediateMessage(String s, MessageType type) {
        ChatSender.fakeReceive(s, type);
    }
    public static void message(String s) {
        message(s, MessageType.NORMAL);
    }

    public static void message(String s, MessageType type) {
        ChatSender.bufferMessage(s, type);
    }
    public static void messages(String[] s) {
        ChatSender.bufferMessages(s);
    }

    public static boolean assertArgNumber(String[] parsedMessage, int numArgs, boolean useFailMessage){
        if(parsedMessage==null){
            if(useFailMessage) message("parsedMessage is null! AAAAAAAAAHHHHHHH!", MessageType.ERROR);
            return false;
        }
        if(parsedMessage.length <= numArgs){ // <= because of off-by-one from root command
            if(useFailMessage) message("Too little arguments: "+numArgs+" required.", MessageType.ERROR);
            return false;
        }
        return true;
    }

    public static boolean assertArgNumber(String[] parsedMessage, int numArgs){
        return assertArgNumber(parsedMessage, numArgs, true);
    }

    public static boolean assertIntegerArgument(String[] parsedMessage, int argNum, boolean useFailMessage){
        try{
            Integer.parseInt(parsedMessage[argNum+1]);
            return true;
        } catch(Exception E){
            if(useFailMessage)
                message("Argument "+argNum+" is not an integer", MessageType.ERROR);
            return false;
        }
    }
    public static boolean assertIntegerArgument(String[] parsedMessage, int argNum) {
        return assertIntegerArgument(parsedMessage, argNum, true);
    }

    }
