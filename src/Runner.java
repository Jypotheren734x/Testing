import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by komar on 12/20/2016.
 */
public class Runner {
    public static void main(String[] args) throws IOException {
        Test test = new Test();
        writeFile(args[0], test.series);
    }
    private static boolean writeFile(String outputFile, HashMap<String, HashMap<Integer, Integer>> map) throws IOException {
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
