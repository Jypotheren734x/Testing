import javafx.concurrent.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by komar on 12/20/2016.
 */
public class Runner {
    String filename = "";
    ProgressForm progressForm = new ProgressForm();
    public static void main(String[] args) throws IOException {
        Runner run = new Runner(args[0]);
    }
    public Runner(String filename) throws IOException {
        this.filename = filename;
        Test test = new Test(progressForm);
        writeFile(filename+".graph", test.series);
    }
    private static boolean writeFile(String outputFile, HashMap<String, HashMap<Integer, Long>> map) throws IOException {
        try {
            FileWriter fw = new FileWriter(outputFile);
            PrintWriter pr = new PrintWriter(fw);
            pr.println(map.size());
            map.forEach((s,l) ->{
                pr.print("Test ");
                pr.print("X-Axis ");
                pr.print("Y-Axis ");
                pr.print("Test ");
                pr.print(s);
                pr.println();
                l.forEach((x,y)->{
                    pr.print(x + " " + y);
                    pr.println();
                });
                pr.print("-----------------------");
            });
            pr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
