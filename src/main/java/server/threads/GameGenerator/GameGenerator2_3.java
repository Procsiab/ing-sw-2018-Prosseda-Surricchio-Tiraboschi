package server.threads.GameGenerator;

import server.MatchManager;
import server.threads.GameManager;
import shared.Logger;
import shared.concurrency.ConcurrencyManager;
import shared.concurrency.GeneralTask;

import java.util.ArrayList;
import java.util.Queue;

@SuppressWarnings("InfiniteLoopStatement")
public class GameGenerator2_3 extends GeneralTask {
    private Integer sleepTime = 15000; //config
    private final Object obj2 = MatchManager.getObj2();
    private static boolean start = false;

    public synchronized static void setStart(Boolean value) {
        start = value;
    }

    @Override
    public void run() {
        super.run();

        ArrayList<String> clients;
        Queue<String> queue = MatchManager.getQ();
        boolean t = true;
        while (t) {
            try {
                synchronized (obj2) {
                    if (queue.size() == 4 || queue.size() < 2 || !start)
                        obj2.wait();
                    else {
                        clients = new ArrayList<>(queue.size());
                        clients.addAll(queue);
                        queue.clear();
                        setStart(false);

                        Logger.log("GmaeManager submit");
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
                Logger.log(e.toString());
            }
        }
    }
}