package xbrsq.scheduler;

public class ScheduledEvent {

    protected EventBody body;
    protected int time;
    protected int ID=-1;

    public ScheduledEvent(EventBody body, int time) {
        this.body = body;
        this.time = time;
    }

    public int setID(int id){
        if(ID>=0)
            return ID = id;
        return -1;
    }
    public boolean checkID(int id){
        return ID == id;
    }

    public EventBody getBody(){
        return body;
    }

    public boolean doExecute(int time) {
        return time>=this.time;
    }

    public boolean doRemove(int time){
        return true;
    }
}
