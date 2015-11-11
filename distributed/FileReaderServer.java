/**
Simple Random File Reader Server

**/
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FileReaderServer {

  public static final int MEGA_MULTIPLIER = 1048576;

  public static void main(String[] args) throws IOException {

      int port = Integer.parseInt(args[0]);
      ServerSocket listener = new ServerSocket(port);
      System.out.println("Starting server...");

      try {
        while(true) {
          Socket socket = listener.accept();
          BufferedReader incoming =
            new BufferedReader(new InputStreamReader(socket.getInputStream()));
          String fileName = incoming.readLine();
          if (!fileName.equals(null)) {
              System.out.println("Task received to count bytes for file: " + fileName);
              int bytesRead = readFile(fileName);
              try {
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                out.println(bytesRead);
             } finally {
               socket.close();
             }
          }
        }

      } finally {
        listener.close();
        System.out.println("Exiting server...");
      }
  }

  private static int readFile(String fileName) {
      //read from file
      FileInputStream fin = null;
      int total = 0;
      byte[] buffer = new byte[MEGA_MULTIPLIER]; // 1 MB at a time

      try {
        fin = new FileInputStream(fileName);
        int bytesRead = 0;
        while((bytesRead = fin.read(buffer)) != -1 ) {
          total += bytesRead;
        }
        fin.close();
        System.out.println("Read bytes: " + total);
      } catch (FileNotFoundException e) {
           e.printStackTrace();
      } catch (IOException e) {
            e.printStackTrace();
      }

      return total;

  }

}
