
package xbrsq.skills;

import xbrsq.FoxedoutClient;
import xbrsq.chat.ChatSender;
import xbrsq.chat.MessageType;
import xbrsq.commands.Command;
import xbrsq.commands.CommandModule;

import java.util.ArrayList;

import static xbrsq.commands.CommandController.*;

public class SkillCommands extends CommandModule {

    public SkillCommands(){
        this.name = "Skills";
        this.enabled = true;
        this.moduleNames = new String[]{"skills", "skill", "s"};

        this.commands = new Command[]{
                new Command("movex", (parsedMessage) -> {
                    if (!assertArgNumber(parsedMessage, 1, true)) {
                        return STOP;
                    }
                    if (!assertIntegerArgument(parsedMessage, 0, true)) {
                        return STOP;
                    }
                    FoxedoutClient.X = Integer.parseInt(parsedMessage[1]);
                    message("Moving x=" + FoxedoutClient.X);
                    return disableIntercept;
                }),
                new Command("movey", (parsedMessage) -> {
                    if (!assertArgNumber(parsedMessage, 1, true)) {
                        return STOP;
                    }
                    if (!assertIntegerArgument(parsedMessage, 0, true)) {
                        return STOP;
                    }
                    FoxedoutClient.Y = Integer.parseInt(parsedMessage[1]);
                    message("Moving y=" + FoxedoutClient.Y);
                    return disableIntercept;
                }),
                new Command("render", (parsedMessage)-> {
                FoxedoutClient.doRender = !FoxedoutClient.doRender;
                message("Rendering: " + FoxedoutClient.doRender);
                return disableIntercept;
            }),
                new Command("skillmessages", (parsedMessage)-> {
                FoxedoutClient.hideSkillMessages = !FoxedoutClient.hideSkillMessages;
                message("Skill messages: " + (FoxedoutClient.hideSkillMessages ? "shown" : "hidden"));
                return disableIntercept;
            }),
                new Command("clearcache", (parsedMessage)-> {
                SkillTracker.clearSkills();
                message("Clearing skill cache...");
                return disableIntercept;
            }),
                new Command("focus", (parsedMessage)-> {
                if (!assertArgNumber(parsedMessage, 1, true)) {
                    return disableIntercept;
                }
                if (parsedMessage[1].equalsIgnoreCase("clear")) {
                    SkillTracker.focus = "";
                    message("Cleared focus");
                    return disableIntercept;
                }
                SkillEntry se = SkillTracker.getByName(parsedMessage[1]);
                if (se == null) {
                    message("Skill not loaded", MessageType.ERROR);
                    return disableIntercept;
                }
                SkillTracker.focus = se.name;
                message("Focusing: " + SkillTracker.focus);
                return disableIntercept;
            }),
                new Command("highlight", (parsedMessage)-> {
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
            }),
                new Command("sort", (parsedMessage)-> {
                if (!assertArgNumber(parsedMessage, 1, true)) {
                    return disableIntercept;
                }

                int s = 0;
                switch (parsedMessage[1].toLowerCase()) {
                    case "none":
                        s = SkillSorter.S_NONE;
                        break;
                    case "name":
                        if (FoxedoutClient.sorting < 0)
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
                        if (FoxedoutClient.sorting < 0) {
                            s = -1;
                        }
                        break;
                    case "desc":
                        parsedMessage[1] = "descending";
                        if (FoxedoutClient.sorting > 0) {
                            s = -1;
                        }
                        break;
                    default:
                        message("Unknown sorting: " + parsedMessage[1], MessageType.ERROR);
                        return disableIntercept;
                }

                // war crime equality checking:
                if (s == 0 || (((FoxedoutClient.sorting >= 0) ? (FoxedoutClient.sorting) : (-FoxedoutClient.sorting)) == s)) {
                    message("Already sorting this way", MessageType.WARN);
                    return disableIntercept;
                } else if (s < 0) {
                    FoxedoutClient.sorting *= -1;
                } else {
                    if (FoxedoutClient.sorting < 0) {
                        s = -s;
                    }
                    FoxedoutClient.sorting = s;
                }

                message("Sorting: " + parsedMessage[1].toLowerCase());
                return disableIntercept;
            }),
                new Command("pin", (parsedMessage)-> {
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
            }),
                new Command("limit", (parsedMessage)-> {
                if (!assertArgNumber(parsedMessage, 1, false)) {
                    message("Current limit is: " + FoxedoutClient.limit);
                    return disableIntercept;
                }
                if (!assertIntegerArgument(parsedMessage, 0, true)) {
                    return STOP;
                }
                if (Integer.parseInt(parsedMessage[1]) < 0) {
                    message("Limit can not be less than 0", MessageType.ERROR);
                    return STOP;
                }
                FoxedoutClient.limit = Integer.parseInt(parsedMessage[1]);
                message("Setting limit to " + FoxedoutClient.limit);
                return disableIntercept;
            }),

                new Command(new String[]{"refresh", "skills", "stats"}, (parsedMessage)-> {
                    ChatSender.broadcastCommand("stats");
                    return disableIntercept;
                }),
                new Command(new String[]{"inspect", "lookup"}, (parsedMessage)-> {
                    ChatSender.broadcastCommand("mcinspect " + parsedMessage[1]);
                    return disableIntercept;
                }),
        };
    }


    public String[] getHelp(){
        String p = FoxedoutClient.commandController.prefix+this.moduleNames[0]+" ";
        return new String[]{
                p+"movex <pos>: move skills display x position",
                p+"movey <pos>: move skills display y position",
                p+"render: toggle skills display rendering",
                p+"skillmessages: toggle hiding skill levelup messages",
                p+"clearcache: clear locally stored skill data",
                p+"focus <skill>: focus on skill, only displaying it",
                p+"highlight <skill>: highlight skill",
                p+"sort <name | level | percent | time>: sort skills based on given column",
                p+"sort <asc | desc>: sort skills ascending/descending",
                p+"pin <skill>: pin skill. Pinned skills are shown before unpinned.",
                p+"limit <value>: limit display maximum entries",
                p+"refresh: refresh skill data",
                p+"lookup: looks up data of another player. Only works if close.",

        };
    }

    public String moduleHelp(){
        return "commands about local skills recording & displaying";
    }
}