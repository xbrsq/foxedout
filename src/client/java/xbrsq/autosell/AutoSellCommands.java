package xbrsq.autosell;

import xbrsq.FoxedoutClient;
import xbrsq.chat.ChatSender;
import xbrsq.commands.Command;
import xbrsq.commands.CommandModule;


import static xbrsq.commands.CommandController.*;

public class AutoSellCommands extends CommandModule {

    AutoSell seller;

    public AutoSellCommands(AutoSell autoSell){
        this.seller = autoSell;

        this.name = "Sell";
        this.enabled = true;
        this.moduleNames = new String[]{"sell", "quicksell"};

        this.commands = new Command[]{
                new Command(new String[]{"item"}, (parsedMessage)->{
                    if (assertArgNumber(parsedMessage, 1, false)) {
                        seller.item = parsedMessage[1];
                    }
                    message("Item to autosell: "+seller.item);
                    return disableIntercept;
                }),
                new Command(new String[]{""}, (parsedMessage)->{
                    ChatSender.broadcastCommand("sellall "+seller.item);
                    return disableIntercept;
                }),
                new Command("repeat", (parsedMessage)->{
                    FoxedoutClient.syncScheduler.addRepeating(()->{
                        ChatSender.broadcastCommand("sellall "+seller.item);
                    }, 5, 9999);
                    return disableIntercept;
                }),
        };
    }

    public String[] getHelp(){
        String p = FoxedoutClient.commandController.prefix+this.moduleNames[0];
        return new String[]{
                p+" item <name>: set the quicksell item",
                p+": quickly sell all quicksell items in your inventory",
                p+" repeat: sell all quicksell items repeatedly until empty",
        };
    }

    public String moduleHelp(){
        return "Quick sell commands";
    }
}
