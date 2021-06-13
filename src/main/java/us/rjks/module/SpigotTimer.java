package us.rjks.module;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 04.06.2021 / 18:25
 *
 **************************************************************************/

public class SpigotTimer {

    private int timer;
    private long numb1, numb2;
    private boolean running = false;

    private Plugin instance;

    public SpigotTimer(long numb1, long numb2, Plugin instance) {
        this.numb1 = numb1;
        this.numb2 = numb2;
        this.instance = instance;
    }

    public void execute() {

    }

    public void start() {
        if (!running) {
            running = true;
            timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
                @Override
                public void run() {
                    execute();
                }
            }, numb1, numb2);
        }
    }

    public void stop() {
        if (running && (timer != 0)) {
            Bukkit.getScheduler().cancelTask(timer);
            running = false;
        }
    }

    public boolean isRunning() {
        return running;
    }
}
