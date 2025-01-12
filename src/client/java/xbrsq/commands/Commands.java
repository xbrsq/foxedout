package xbrsq.commands;

import org.jetbrains.annotations.NotNull;
import xbrsq.FoxedoutClient;
import xbrsq.chat.ChatSender;
import xbrsq.chat.MessageType;
import xbrsq.skills.SkillEntry;
import xbrsq.skills.SkillSorter;
import xbrsq.skills.SkillTracker;

import java.util.ArrayList;
import java.util.Arrays;

public class Commands {

    public static final boolean PASS = true;
    public static final boolean STOP = false;

    public static String prefix = ".";

    public static String[][] helpMessages =
            {
        {
            "prefix for all commands: \"" + prefix + "\"",
            "",
            prefix + "help [page]: shows help page",
            prefix + "movex <x>: move skills display horizontal position",
            prefix + "movey <y>: move skills display vertical position",
            prefix + "render: toggle skills display rendering",
            prefix + "intercept: toggle whether commands are sent to other players",
            prefix+"skillmessages: toggle whether skill messages are shown",
        },
        {
            prefix + "clearcache: clears the skill cache.",
            prefix + "focus <skill | 'clear'>: only shows selected skill.",
            prefix + "highlight <skill | 'clear'>: highlights skill.",
            prefix + "sort <field | 'asc' | 'desc'>: sorts based on field, or sets",
            "sorting direction.",
            prefix + "refresh, " + prefix + "stats: refresh skill data",
            prefix+"inspect <name>, "+prefix+"lookup <name>: get player data",
            prefix+"pin <skill | 'clear'>: pin a skill to the top or clear pins.",
        }, {
            prefix+"limit [number]: gets or sets limit",
            prefix + "owo: what's this?",
            prefix + "crash: crashes the game. For debug purposes only.",
        },
        {
            ""
        }
    };


    // disable to force all chat messages to be allowed through
    public static boolean disableIntercept = STOP;

    public static boolean parseMessage(@NotNull String rawMessage){
        // returns whether to allow message
        if(!rawMessage.startsWith(prefix)){
            return PASS;
        }
        String[] parsedMessage = rawMessage.substring(prefix.length()).trim().split(" ");

        switch (parsedMessage[0]) {
            // root commands:
            case "intercept": {
                disableIntercept = !disableIntercept;
                message("intercept system messages: " + !disableIntercept);
                return disableIntercept;
            }
            case "crash": {
                immediateMessage("Attempting to crash...", MessageType.WARN);
                // to force crash;
                int x = 0 / 0;
                return STOP;
            }
            case "owo": {
                message("OwO, what's this?");
                return STOP;
            }

            case "test": {
                FoxedoutClient.syncScheduler.addEvent(() -> {
                    ChatSender.bufferMessage("Hello There!");
                }, 20);
                return disableIntercept;
            }

            // submodules
            case "skill", "skills", "s": {
                return SkillCommands.parseMessage(Arrays.copyOfRange(parsedMessage, 1, parsedMessage.length));
            }

            default: {
                message("Unknown command: " + parsedMessage[0], MessageType.ERROR);
                return disableIntercept;
            }
        }
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
