package xbrsq.autosell;

import xbrsq.FoxedoutClient;
import xbrsq.chat.ChatSender;

public class AutoSell {

    public boolean enabled = false;
    public int delay = 20;
    public String item = "BASALT";
    public int sellID = -1;

    public void parseText(String str) {
        if(str.contains("items found which can be sold")){
            FoxedoutClient.syncScheduler.removeEvent(sellID);
            sellID = -1;
        }
    }
}
