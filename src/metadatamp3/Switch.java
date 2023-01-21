package metadatamp3;

import controller.Utilities;

import java.util.TimerTask;
import java.util.Date;

public class Switch extends TimerTask {
    @Override
    public void run() {
        System.out.println(new Date() + "loop begun");
        Utilities util = new Utilities();
        try {
            util.recentReset("C:/vibes");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(new Date() + "loop finished");
    }
}
