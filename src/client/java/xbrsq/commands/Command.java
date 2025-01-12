package xbrsq.commands;

public class Command {
    private final CommandBody body;
    private final String[] names;
    public Command(String[] names, CommandBody body){
        this.body = body;
        this.names = names;
    }
    public Command(String name, CommandBody body){
        this.body = body;
        this.names = new String[]{name};
    }

    public boolean check(String[] parsedMessage){
        if(parsedMessage.length<1){
            return false;
        }
        for(String name: names){
            if(parsedMessage[0].equals(name)){
                return true;
            }
        }
        return false;
    }
    public boolean execute(String[] parsedMessage){
        return body.execute(parsedMessage);
    }
}
