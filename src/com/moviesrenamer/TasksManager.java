package com.moviesrenamer;

import com.moviesrenamer.tasks.Task;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class TasksManager {

    private Stack<Task> taskStack;
    private Thread[] threads;
    private final Object stackLock = new Object();
    private boolean running;

    public TasksManager() {
        taskStack = new Stack<>();
        threads = new Thread[3];
        running = false;
    }

    @NotNull
    private Thread getFetchInfoThread() {
        return new Thread(() -> {
            while (running) {
                while (taskStack.size() > 0) {
                    Task task;
                    synchronized (stackLock) {
                        task = taskStack.pop();
                    }
                    task.execute();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void start() {
        running = true;
        for (int i = 0; i < threads.length; i++) {
            threads[i] = getFetchInfoThread();
            threads[i].start();
        }
    }

    public void stop() {
        running = false;
    }

    public void addTask(Task task) {
        taskStack.push(task);
    }
}
