import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

public class CloudImageClient {
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {

        if (args.length != 4){
            System.err.println("Usage java CloudImageClient <host name> <port number> <original file> <output file>");
            System.exit(1);
        }
        
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        File original_f = new File(args[2]+".jpg");
        String output_f = args[3];
        BufferedImage original, received;

        original = ImageIO.read(original_f);
        
        Socket echoSocket = new Socket(hostName, portNumber);
        OutputStream outToServer = echoSocket.getOutputStream();            
        InputStream inFromServer = echoSocket.getInputStream();

        String ss = "\n";
        byte[] b = ss.getBytes();

        boolean test = ImageIO.write(original,"jpg", outToServer);
        outToServer.write(b);
        outToServer.flush();
        System.out.println("Wrote"+test);        
        received = ImageIO.read(inFromServer);

        File file = new File(output_f);
        ImageIO.write(received,"jpg",file);
        System.out.println("Done");
    }
}