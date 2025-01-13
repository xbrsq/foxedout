
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
                new Command("owo", (parsedMessage) -> {
                    message("OwO, what's this?");
                    return STOP;
                }),

                new Command("test", (parsedMessage) -> {
                    FoxedoutClient.syncScheduler.addEvent(() -> {
                        ChatSender.bufferMessage("Hello There!");
                    }, 20);
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
}