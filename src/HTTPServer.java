import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HTTPServer {
    //this is the configuration file used to create the sockets and get the porot
    public static void main(String[] args) throws IOException {
        String workingDir = System.getProperty("user.dir");
        System.out.print(workingDir);
        File file = new File(workingDir +
                "/Port.txt");
        BufferedReader br
                = new BufferedReader(new FileReader(file));
        String st;
        int a1 = 0;
        while ((st = br.readLine()) != null)
        {
            a1 = Integer.parseInt(st);
        }
        WelcomeSocket welcomeSocket = new WelcomeSocket(a1);
        welcomeSocket.start();
    }
}