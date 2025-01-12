package xbrsq.commands;

import java.util.ArrayList;

import static xbrsq.commands.CommandController.STOP;

public class CommandModule {
    protected boolean enabled = false;
    protected boolean useRaw = false;
    public String name = "defaultname";
    protected String[] moduleNames = new String[0];

    protected Command[] commands;

    public boolean parseMessage(String[] parsedMessage) {
        // returns whether to allow message
        for(Command comm: commands){
            if(comm.check(parsedMessage)){
                return comm.execute(parsedMessage);
            }

        }

        return STOP;

    }
    public boolean isMine(String[] parsedMessage){
        if(parsedMessage.length<1){
            return false;
        }
        return isMine(parsedMessage[0]);
    }

    protected boolean isMine(String name){
        if(!enabled)
            return false;
        for(String moduleName: moduleNames){
            if(name.equals(moduleName)){
                return true;
            }
        }
        return false;
    }

}
