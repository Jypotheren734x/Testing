# Testing FrameWork
By Nickolas Komarnitsky

# About
I made this project to make running timing tests or any other sort of test easier. 

# Instructions
To use this framework, you only have to use a few lines of code.
```Java
public class Example extends Application{
  public static void main(String[] args){launch(args);}
   @Override
   public void start(Stage primaryStage) throws Exception {
       primaryStage = new Stage();
       TestPane testPane = new TestPane();
       testPane.addTests(
       new Test(() ->{
          Thread.sleep(500);
          return null;
        },100,1),
        new Test(),
        new Test(400,10)
        );
        testPane.setExec();
        primaryStage.setScene(new Scene(testPane));
        primaryStage.show();
    }
}
```
