package xbrsq;

import xbrsq.chat.ChatSender;
import xbrsq.commands.Command;
import xbrsq.commands.CommandBody;

import static xbrsq.commands.CommandController.disableIntercept;

public class ColoredMessageCommand extends Command{
    String[] prefixes={};

    public ColoredMessageCommand(String[] names, String[] prefixes) {
        super(names, (parsedMessage) -> {
            StringBuilder sb = new StringBuilder();
            StringBuilder message = new StringBuilder();
            for(int i=1;i<parsedMessage.length;i++){
                message.append(parsedMessage[i]);
                if(i<parsedMessage.length-1)
                    message.append(" ");
            }
            int spaces = 0;
            for(int i=0;i<message.length();i++){
                if(message.charAt(i)==' ')
                    spaces++;
                else
                    sb.append(prefixes[(i-spaces)%prefixes.length]);
                sb.append(message.charAt(i));
            }
            ChatSender.broadcastMessage(sb.toString());
            return disableIntercept;

        });
        this.prefixes = prefixes;
    }
}
