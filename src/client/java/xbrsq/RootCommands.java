
package xbrsq;

import xbrsq.chat.ChatSender;
import xbrsq.chat.MessageType;
import xbrsq.commands.Command;
import xbrsq.commands.CommandModule;

import static xbrsq.commands.CommandController.*;

public class RootCommands extends CommandModule {

    public RootCommands(){
        enabled = true;
        useRaw = true;
        commands = new Command[]{
                new Command("intercept", (parsedMessage) -> {
                    disableIntercept = !disableIntercept;
                    message("intercept system messages: " + !disableIntercept);
                    return disableIntercept;
                }),
                new Command("crash", (parsedMessage) -> {
                    immediateMessage("Attempting to crash...", MessageType.WARN);
                    // to force crash;
                    int x = 0 / 0;
                    return STOP;
                }),

                new ColoredMessageCommand(new String[]{"rainbow", "rb"}, new String[]{"&c", "&6", "&e", "&a", "&3", "&5", "&d"}),
                new ColoredMessageCommand(new String[]{"trans", "tr"}, new String[]{"&b", "&d", "&f", "&d", "&b"}),

                new Command("owo", (parsedMessage) -> {
                    message("OwO, what's this?");
                    return STOP;
                }),
                new Command("help", (parsedMessage)->{
                    int linesPerPage=8;
                    int page=1;
                    if (assertArgNumber(parsedMessage, 1, false)) {
                        if (!assertIntegerArgument(parsedMessage, 0, false)) {
                            for(CommandModule mod: FoxedoutClient.commandController.modules){
                                if(parsedMessage[1].equalsIgnoreCase(mod.name)){
                                    String[] h = mod.getHelp();
                                    message("§6"+mod.name+" Help");
                                    for (String s : h) {
                                        message(s);
                                    }
                                    return disableIntercept;
                                }
                            }
                            message("Unknown module: "+parsedMessage[0]);
                            return STOP;
                        }
                        page = Integer.parseInt(parsedMessage[1]);
                    }
                    String[] totalHelp = FoxedoutClient.commandController.getHelp();

                    if(totalHelp.length<(page-1)*linesPerPage || page<=0){
                        message("Page index out of bounds", MessageType.ERROR);
                        return STOP;
                    }
                    message("§6FoxedOut Module Help (Page "+page+"/"+(((totalHelp.length-1)/linesPerPage)+1)+")");
                    for(int i=(page-1)*linesPerPage;i<totalHelp.length && i<page*linesPerPage;i++){
                        message(totalHelp[i]);
                    }

                    return disableIntercept;
                }),
                new Command("modules", (parsedMessage)->{
                    message("Modules:");
                    for(CommandModule mod: FoxedoutClient.commandController.modules){
                        message(mod.name);
                    }
                    return disableIntercept;
                }),

                new Command("test", (parsedMessage) -> {
                    FoxedoutClient.syncScheduler.addRepeating(() -> {
                        ChatSender.bufferMessage("Hello There!");
                    }, 10, 10);
                    return disableIntercept;
                }),
                new Command("version", (parsedMessage) -> {
                    message("FoxedOut "+FoxedoutClient.version);
                    return disableIntercept;
                }),
        };
    }

    @Override
    public boolean isMine(String[] parsedMessage){
        for(Command comm: commands){
            if(comm.check(parsedMessage)){
                return true;
            }
        }
        return false;
    }



    public String[] getHelp(){
        String p = FoxedoutClient.commandController.prefix;
        return new String[]{
                p+"help [page | module]: show help",
                p+"intercept: toggle intercept of custom commands",
                p+"crash: crash the game",
        };
    }
}