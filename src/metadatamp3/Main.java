package metadatamp3;

import controller.Utilities;

import java.util.Timer;

public class Main {

    public static void main(String[] args) throws Exception{
        Utilities util = new Utilities();
        try {
            util.reset("C:/vibes");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Timer timer = new Timer();
        timer.schedule(new Switch(), 2000, 5000);
    }

}
