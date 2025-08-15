package org.example;

public class Task implements Comparable<Task> {
    //任务和开始时间
    private Runnable ScheduledTask;
    private long startTime;
    private long delay;
    @Override
    public int compareTo(Task other) {
        return Long.compare(this.startTime, other.startTime);
    }


    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }




    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setScheduledTask(Runnable scheduledTask) {
        ScheduledTask = scheduledTask;
    }


    public Runnable getScheduledTask() {
        return ScheduledTask;
    }

    public long getStartTime() {
        return startTime;
    }




}
