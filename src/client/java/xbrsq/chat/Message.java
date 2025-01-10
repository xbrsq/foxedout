package xbrsq.chat;

class Message {
    public String content;
    public MessageType type;

    public Message(){
        this("", MessageType.NORMAL);
    }
    public Message(String content){
        this(content, MessageType.NORMAL);
    }
    public Message(String content, MessageType type){
        this.type = type;
        this.content = content;
    }

    public String toString(){
        String prefix="";
        switch(type){
            case MessageType.NORMAL -> prefix="";
            case MessageType.WARN -> prefix="§6";
            case MessageType.ERROR -> prefix="§c§o";
            case MessageType.SUPERERROR -> prefix="§4§l§n";
        }
        return prefix+content;
    }
}
