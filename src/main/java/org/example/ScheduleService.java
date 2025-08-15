package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;

public class ScheduleService {



    //传入任务和delay，来完成定时任务
    Trigger trigger = new Trigger();

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void schedule(Runnable task, long delay , long startTime) {
        Task scheduleTask = new Task();
        scheduleTask.setScheduledTask(task);
        scheduleTask.setStartTime(startTime);
        scheduleTask.setDelay(delay);
        trigger.queue.add(scheduleTask);
        trigger.wakeUp();
    }
    //触发器，用于将任务定时放入线程池。
    class Trigger{
        //优先阻塞队列，用于存放任务。
        PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();

        //这里开一个线程是为了使整个定时任务不阻塞主线程。
//        这里先从优先阻塞队列中poll出task。
//        如果队列为空，则等待。
//        如果队列不为空，则将task放入线程池中执行。并且用junit中的locksupport来加锁。
//        之后如果有新的任务传入，则执行一个唤醒函数解锁。

        Thread thread = new Thread(()->{
            while (true){
                while (queue.isEmpty()){
                    LockSupport.park();
                }
                Task task = queue.peek();
                if(task.getStartTime() <= System.currentTimeMillis()){
                   task = queue.poll();
                   executorService.execute(task.getScheduledTask());
                   if(task.getDelay() !=0) {
                        Task nextTask = new Task();
                        nextTask.setScheduledTask(task.getScheduledTask());
                        nextTask.setStartTime(System.currentTimeMillis() + task.getDelay());
                        nextTask.setDelay(task.getDelay());
                        queue.add(nextTask);
                    }
                }else{
                    LockSupport.parkUntil(task.getStartTime());
                }
            }
        });

        {
            thread.start();
            System.out.println("触发器启动");
        }

        public void wakeUp(){

            LockSupport.unpark(thread);
        }

    }
}
