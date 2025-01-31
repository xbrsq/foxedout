package xbrsq.scheduler;

import java.util.LinkedList;

public class Scheduler {
    private static int currentID = 0;
    private int time = 0;

    private final LinkedList<ScheduledEvent> events;

    public Scheduler(){
        events = new LinkedList<>();
    }

    public int addEvent(EventBody body, int execDelay){
        events.addFirst(new ScheduledEvent(body, time+execDelay));
        return events.getFirst().setID(currentID++);
    }
    public int addRepeating(EventBody body, int repeatTime, int numRepeats){
        events.addFirst(new RepeatingEvent(body, time+repeatTime, repeatTime, numRepeats));
        return events.getFirst().setID(currentID++);
    }

    public void removeEvent(int ID) {
        for (ScheduledEvent event : events) {
            if (event.checkID(ID)) {
                events.remove(event);
                return;
            }
        }
    }

    public void tick(){
        for(ScheduledEvent event: events){
            if(event.doExecute(time)){
                event.getBody().execute();
                if(event.doRemove(time)){
                    events.remove(event);
                }
            }
        }
        time++;
    }

    public int getTime() {
        return time;
    }
}
