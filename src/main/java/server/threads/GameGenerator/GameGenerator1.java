package server.threads.GameGenerator;

import server.Config;
import server.MatchManager;
import server.threads.GameManager;
import shared.Logger;
import server.concurrency.ConcurrencyManager;
import server.concurrency.GeneralTask;

import java.util.ArrayList;
import java.util.Queue;

public class GameGenerator1 extends GeneralTask {
    private Integer sleepTime = Config.timeout1;
    private final Object obj = MatchManager.getObj();
    private static boolean start = false;

    /**
     * @param value is set if the Game has started or not
     */
    static synchronized void setStart(Boolean value) {
        start = value;
    }

    /**
     * handle the connections request under timing scope. Timer starts when queue has two player and generates the game when timer runs out.
     */
    @Override
    public void run() {
        super.run();
        Logger.log("GameGenerator1 online. Timer runs out every: " + sleepTime / 1000 + "s");
        ArrayList<String> clients;
        Queue<String> queue = MatchManager.getQ();
        boolean t = true;
        while (t) {
            try {
                synchronized (obj) {
                    if (queue.size() == 4 || queue.size() < 2 || !start)
                        obj.wait();
                    else {
                        clients = new ArrayList<>(queue.size());
                        clients.addAll(queue);
                        queue.clear();
                        setStart(false);

                        ConcurrencyManager.submit(new GameManager(clients));
                    }
                }
                if (start)
                    Thread.sleep(sleepTime);
                else if (queue.size() > 1) {
                    Thread.sleep(sleepTime);
                    setStart(true);
                }
            } catch (Exception e) {
                Logger.log("Error waiting on lock!");
                e.printStackTrace();
            }
        }
    }

}