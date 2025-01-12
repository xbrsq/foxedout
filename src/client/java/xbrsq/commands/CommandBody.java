package xbrsq.commands;

public interface CommandBody {
    boolean execute(String[] parsedMessage);
}
