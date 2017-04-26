package Tests;

import javafx.application.Application;

/**
 * @Author Nickolas Komarnitsky
 * Testing.Tests
 * 4/26/2017
 * 2:45 AM
 */
public class Main {
    public static void main(String[] args){
        new Thread(() -> Application.launch(Timing.class)).start();
        Timing.addTests(
        new Test(() ->{
            Thread.sleep(500);
            return null;
        },100,1),
        new Test(),
        new Test(400,10)
        );
    }
}
