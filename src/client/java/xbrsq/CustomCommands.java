package xbrsq;

import org.jetbrains.annotations.NotNull;
import xbrsq.chat.ChatSender;
import xbrsq.chat.MessageType;

import java.util.ArrayList;

public class CustomCommands {

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



        switch(parsedMessage[0].toLowerCase()){
            // main commands
            case "?":
            case "help": {
                int page = 1;
                if (assertIntegerArgument(parsedMessage, 0, false)) {
                    page = Integer.parseInt(parsedMessage[1]);
                }
                if(page<=0 || page>helpMessages.length){
                    message("Invalid page number: "+page, MessageType.ERROR);
                    return disableIntercept;
                }
                message("§l§n§6FoxedOut "+FoxedoutClient.version);
                messages(helpMessages[page-1]); // 1-indexing to 0-indexing conversion
                message("§o§6Page: "+(page)+"/"+helpMessages.length);
                return disableIntercept;
            }
            case "movex": {
                if (!assertArgNumber(parsedMessage, 1, true)) {
                    return STOP;
                }
                if (!assertIntegerArgument(parsedMessage, 0, true)) {
                    return STOP;
                }
                FoxedoutClient.X = Integer.parseInt(parsedMessage[1]);
                message("Moving x=" + FoxedoutClient.X);
                return disableIntercept;
            }
            case "movey": {
                if (!assertArgNumber(parsedMessage, 1, true)) {
                    return STOP;
                }
                if (!assertIntegerArgument(parsedMessage, 0, true)) {
                    return STOP;
                }
                FoxedoutClient.Y = Integer.parseInt(parsedMessage[1]);
                message("Moving y=" + FoxedoutClient.Y);
                return disableIntercept;
            }
            case "render": {
                FoxedoutClient.doRender = !FoxedoutClient.doRender;
                message("Rendering: " + FoxedoutClient.doRender);
                return disableIntercept;
            }
            case "intercept": {
                disableIntercept = !disableIntercept;
                message("intercept system messages: " + !disableIntercept);
                return disableIntercept;
            }
            case "skillmessages": {
                FoxedoutClient.hideSkillMessages = !FoxedoutClient.hideSkillMessages;
                message("Skill messages: " + (FoxedoutClient.hideSkillMessages?"shown":"hidden"));
                return disableIntercept;
            }
            case "clearcache": {
                SkillTracker.clearSkills();
                message("Clearing skill cache...");
                return disableIntercept;
            }
            case "focus": {
                if (!assertArgNumber(parsedMessage, 1, true)) {
                    return disableIntercept;
                }
                if(parsedMessage[1].equalsIgnoreCase("clear")){
                    SkillTracker.focus = "";
                    message("Cleared focus");
                    return disableIntercept;
                }
                SkillEntry se = SkillTracker.getByName(parsedMessage[1]);
                if(se==null){
                    message("Skill not loaded", MessageType.ERROR);
                    return disableIntercept;
                }
                SkillTracker.focus = se.name;
                message("Focusing: "+SkillTracker.focus);
                return disableIntercept;
            }
            case "highlight": {
                if (!assertArgNumber(parsedMessage, 1, true)) {
                    return disableIntercept;
                }
                if (parsedMessage[1].equalsIgnoreCase("clear")) {
                    ArrayList<SkillEntry> skills = SkillTracker.getSkills(true);
                    for (SkillEntry se : skills) {
                        se.highlighted = false;
                    }
                    message("Cleared highlights");
                    return disableIntercept;
                }
                SkillEntry se = SkillTracker.getByName(parsedMessage[1]);
                if (se == null) {
                    message("Skill not loaded", MessageType.ERROR);
                    return disableIntercept;
                }
                se.highlighted = !se.highlighted;
                message(se.name + " is now " + (se.highlighted ? "" : "not ") + "highlighted");
                return disableIntercept;
            }
            case "sort": {
                if (!assertArgNumber(parsedMessage, 1, true)) {
                    return disableIntercept;
                }
                
                int s = 0;
                switch(parsedMessage[1].toLowerCase()){
                    case "none":
                        s = SkillSorter.S_NONE;
                        break;
                    case "name":
                        if(FoxedoutClient.sorting<0)
                            s = SkillSorter.S_NAME;
                        break;
                    case "level":
                        s = SkillSorter.S_LEVEL;
                        break;
                    case "percent":
                        s = SkillSorter.S_PERCENT;
                        break;
                    case "time":
                        s = SkillSorter.S_TIME;
                        break;
                    case "pinned":
                    case "pin":
                        s = SkillSorter.S_PIN;
                        break;
                    case "asc":
                        parsedMessage[1] = "ascending";
                        if(FoxedoutClient.sorting<0){
                            s = -1;
                        }
                        break;
                    case "desc":
                        parsedMessage[1] = "descending";
                        if(FoxedoutClient.sorting>0){
                            s = -1;
                        }
                        break;
                    default:
                        message("Unknown sorting: "+parsedMessage[1], MessageType.ERROR);
                        return disableIntercept;
                }

                // war crime equality checking:
                if(s==0 || (((FoxedoutClient.sorting>=0)?(FoxedoutClient.sorting):(-FoxedoutClient.sorting))==s)){
                    message("Already sorting this way", MessageType.WARN);
                    return disableIntercept;
                }
                else if(s<0){
                    FoxedoutClient.sorting *= -1;
                }
                else {
                    if(FoxedoutClient.sorting<0){
                        s = -s;
                    }
                    FoxedoutClient.sorting = s;
                }

                message("Sorting: "+parsedMessage[1].toLowerCase());
                return disableIntercept;
            }
            case "refresh":
            case "stats": {
                ChatSender.broadcastCommand("stats");
                return disableIntercept;
            }
            case "inspect":
            case "lookup": {
                ChatSender.broadcastCommand("mcinspect " + parsedMessage[1]);
                return disableIntercept;
            }
            case "pin": {
                if (!assertArgNumber(parsedMessage, 1, true)) {
                    return disableIntercept;
                }
                if (parsedMessage[1].equalsIgnoreCase("clear")) {
                    ArrayList<SkillEntry> skills = SkillTracker.getSkills(true);
                    for (SkillEntry se : skills) {
                        se.pinned = false;
                    }
                    message("Cleared pins");
                    return disableIntercept;
                }
                SkillEntry se = SkillTracker.getByName(parsedMessage[1]);
                if (se == null) {
                    message("Skill not loaded", MessageType.ERROR);
                    return disableIntercept;
                }
                se.pinned = !se.pinned;
                message(se.name + " is now " + (se.pinned ? "" : "not ") + "pinned");
                return disableIntercept;
            }
            case "limit": {
                if (!assertArgNumber(parsedMessage, 1, false)) {
                    message("Current limit is: "+FoxedoutClient.limit);
                    return disableIntercept;
                }
                if (!assertIntegerArgument(parsedMessage, 0, true)) {
                    return STOP;
                }
                if(Integer.parseInt(parsedMessage[1])<0){
                    message("Limit can not be less than 0", MessageType.ERROR);
                    return STOP;
                }
                FoxedoutClient.limit = Integer.parseInt(parsedMessage[1]);
                message("Setting limit to " + FoxedoutClient.limit);
                return disableIntercept;
            }

            case "test": {
                FoxedoutClient.syncScheduler.addEvent(()->{ChatSender.bufferMessage("Hello There!");}, 20);
                return disableIntercept;
            }



            // for dev only, because I keep forgetting to break after the last main command
            // if you see this, something went quite wrong
            case " oops, forgot the return": {
                message("YOU FORGOT TO BREAK OUT OF THE RETURN, MADAM DUMBASS", MessageType.SUPERERROR);
                break;
                }

            // utility
            case "crash": {
                immediateMessage("Attempting to crash...", MessageType.WARN);
                // to force crash;
                int x = 0/0;
                return STOP;
            }

            // just for fun
            case "owo": {
                message("OwO, what's this?");
                return STOP;
            }
            case " to avoid compiler warnings": {
                // inaccessable, to avoid compiler error of unreachable code after switch.
                break;
            }
            default: {
                message("Unknown command: "+parsedMessage[0], MessageType.ERROR);
                return disableIntercept;
            }

        }

        ChatSender.fakeReceive("somehow, you got here. You shouldn't have been able to.");
        return STOP;

    }

    private static void immediateMessage(String s) {
        ChatSender.fakeReceive(s);
    }
    private static void immediateMessage(String s, MessageType type) {
        ChatSender.fakeReceive(s, type);
    }
    private static void message(String s) {
        message(s, MessageType.NORMAL);
    }

    private static void message(String s, MessageType type) {
        ChatSender.bufferMessage(s, type);
    }
    private static void messages(String[] s) {
        ChatSender.bufferMessages(s);
    }

    private static boolean assertArgNumber(String[] parsedMessage, int numArgs, boolean useFailMessage){
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

    private static boolean assertArgNumber(String[] parsedMessage, int numArgs){
        return assertArgNumber(parsedMessage, numArgs, true);
    }

    private static boolean assertIntegerArgument(String[] parsedMessage, int argNum, boolean useFailMessage){
        try{
            Integer.parseInt(parsedMessage[argNum+1]);
            return true;
        } catch(Exception E){
            if(useFailMessage)
                message("Argument "+argNum+" is not an integer", MessageType.ERROR);
            return false;
        }
    }
    private static boolean assertIntegerArgument(String[] parsedMessage, int argNum) {
        return assertIntegerArgument(parsedMessage, argNum, true);
    }

    }
