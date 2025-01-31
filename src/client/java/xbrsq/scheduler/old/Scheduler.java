package xbrsq.scheduler.old;

import java.util.LinkedList;

public class Scheduler {

    private final LinkedList<ScheduledEvent> events = new LinkedList<>();

    private int time = 0;

    public void addEvent(EventBody body, int executionDelay){
        events.addFirst(new ScheduledEvent(body, time+ executionDelay));
    }
    public void addRepeating(EventBody body, int executionDelay){
        events.addFirst(new RepeatingEvent(body, time, executionDelay));
    }

    public void tick(){
        time++;
        ScheduledEvent event;
        for(int i=0;i<events.size();i++){
            event = events.get(i);
            if(event.doExecute(time)){
                event.getBody().execute();
                if(event.doRemove())
                    events.remove(i--);
            }
        }
    }

    public int getTime(){
        return time;
    }
}
