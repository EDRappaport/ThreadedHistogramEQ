import java.net.*;
import java.io.*;

public class EqualizerListener extends Thread {
	private int portNumber;
	private CubbyHole cubbyhole;

    public EqualizerListener(CubbyHole c, int port) {
        super("TwoListeners");
        cubbyhole = c;
        portNumber = port;
    }

    public void run() {

    	Data data = null;
        boolean haveData = false;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (true) {
                Socket s = serverSocket.accept();
                System.out.println("Have New Waiting Processing Server");
                
                if (haveData ||  ((data=cubbyhole.get())!=null)  ) { //value = null if contents of cubbyhole have been removed
                    System.out.println("Sending New Assignment");
                    System.out.println("Closed?"+s.isClosed());
                    System.out.println("Connected?"+s.isConnected());
                	if(s.isConnected()){
                        System.out.println("Sending New Assignment");
                        PrintWriter outToEqualizer = new PrintWriter(s.getOutputStream(), true);
                        outToEqualizer.println(data.ia.getHostName());
                        outToEqualizer.println(data.p);

                		haveData = false;
                		s.close();
                        System.out.println("Sent New Assignment");
                	} else {
                        haveData = true; 
                        System.out.println("He was impatient. Let's look for another processing server.");
                    }

            	} else {s.close(); break;}
            }
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-1);
        }
    }
}