import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

//this opens the TCP connection and is used to create a socket thread
public class SocketClientThread extends Thread{
    private Socket cs;
    //this constructs the socket header
    public SocketClientThread(Socket cs) throws IOException {
        this.cs = cs;
    }
    //this is where most of the code creates the response
    @Override
    public void run() {
        try {
            //reads the input string puts it in a buffer
            BufferedReader myBuffer = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            ArrayList<String> myInfo = new ArrayList<String>();
            String currentline;
            //I use this in order to create an arraylist of the return headers and fields and url
            while(!(currentline=myBuffer.readLine()).isBlank()){
                myInfo.add(currentline);
            }
            //this returns the values and also creates one long string of the values
            StringBuilder fullString = new StringBuilder();
            for(int i = 0; i < myInfo.size(); i++){
                System.out.println(myInfo.get(i));
                fullString.append(myInfo.get(i));
            }
            //this is used in order to get variables that I need for the req res
            String requestType =  myInfo.get(0);
            String url = myInfo.get(1);
            String connectionType = myInfo.get(2);
            String CacheControl = myInfo.get(3);
            String searchType = myInfo.get(4);
            int cookie = 0;
            String[] strArr = null;
            //this is used to get the value of the cookie
            if(fullString.toString().contains("Cookie")) {
                String myString = myInfo.get(15);
                strArr = myString.toString().split(": ")[1].split(" ");
                for(int i = 0; i< strArr.length; i++) {
                    if(strArr[i].contains("mhf25_visits")) {
                        cookie = Integer.parseInt(strArr[i].split("=")[1]);
                    }
                }
                }
            ArrayList<String> a1 = new ArrayList<String>();
            String a2 = "";
            StringBuilder strBuilder = new StringBuilder();
            String endStringOfPath = returnThePath(requestType);
            //this is the path to the html objects
            String workingDir = System.getProperty("user.dir");;
            String path = workingDir + endStringOfPath;
            //this gets the simplified path to load the correct objects
            Path filePath = Paths.get(path);
            String myPathVisit = "visits.html";
            DataOutputStream outputStuff = new DataOutputStream(cs.getOutputStream());
            //this checks to see if the filepath exists
            if(Files.exists(filePath) == true){
                outputStuff.writeBytes("HTTP/1.1 200 OK\r\n" + "ContentType: text/html\r\n" + "Set-Cookie: mhf25_visits=" + (cookie+1) + "; Path=/mhf25" + "\r\n");
                String comp = endStringOfPath.substring(1);
                if(!comp.contentEquals(myPathVisit)) {
                    outputStuff.write(Files.readAllBytes(filePath));
                }else{
                    outputStuff.writeBytes("<!DOCTYPE html>"+'\n'+"<html>"+'\n'+"<head>"+'\n'+"<title>MikePage</title>"+'\n'+'\n'+"</head>"+'\n'+"<body>"+'\n'
                            +'\n'+"<p>Number of times this webpage has been visited: "+(cookie+1)+"</p>"
                            +'\n'+"</body>"+'\n'+"</html>");
                }
                outputStuff.close();
            }else{
                    //this is used to write a not found error if the file does not exist
                    outputStuff.write(("HTTP/1.1 404 Not Found Error").getBytes(StandardCharsets.UTF_8));
                   Path myLostFile = Paths.get(workingDir + "/notFound.HTML");
                   outputStuff.write(Files.readAllBytes(myLostFile));
            }
            cs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //this is used create the simplified path for the users
    public String returnThePath(String path1){
        int i = 0;
        int counter = 0;
        boolean errorCorrect = false;
        StringBuilder a1 = new StringBuilder();
        while(path1.charAt(i) != '/' && counter != 1){
            if(path1.charAt(i) == '/' && counter == 0) {
                counter = 1;
            }
            i++;
        }
        while(path1.charAt(i) != ' '){
            a1.append(path1.charAt(i));
            i++;
            errorCorrect = true;
        }
        if (errorCorrect) {
            return a1.toString().substring(6);
        }else{
            return " ";
        }
    }
 }

