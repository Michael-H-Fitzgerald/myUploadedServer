import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WelcomeSocket extends Thread{
        //this creates the welcome socket
        private ServerSocket welcomSoc;
        private int port;
        //this is used to initilaize the welcoming socket
        public WelcomeSocket(int port) throws IOException {
            this.port = port;
            this.welcomSoc = new ServerSocket(this.port);
        }
        //this is used in order to open the connnection for the welcoming socket
        @Override
        public void run(){
            while(!welcomSoc.isClosed()){
                try {
                    Socket s1 = welcomSoc.accept();
                    SocketClientThread mySecondSocket = new SocketClientThread(s1);
                    mySecondSocket.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //this closes the welcoming socket when the conneciton is closed
            if(welcomSoc != null){
                try {
                    welcomSoc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }
}
