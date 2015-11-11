/**
Simple Random File Reader Client
**/
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;

class FileReaderCallable implements Callable<Integer> {
  private Thread reader;
  private String readerName;
  private String fileName;
  private String server;
  private int port;
  private int bytesRead = 0;

  FileReaderCallable(String readerName, String fileName, String server, int port) {
      this.readerName = readerName;
      this.fileName = fileName;
      this.server = server;
      this.port = port;
  }

  public Integer call() {
     System.out.println("Communicating with server: " + readerName);

     try {
       Socket socket = new Socket(server, port);
       try {
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         out.println(fileName);

         InputStreamReader incomingReader = new InputStreamReader(socket.getInputStream());

         byte attempts = 0; //exit if more than 100 attempts
         while(!incomingReader.ready() && attempts < 100){
            attempts++;
            Thread.sleep(10);
         }
         if(attempts >= 100) System.out.println("Timed out");
         BufferedReader incoming = new BufferedReader(incomingReader);
         bytesRead = Integer.parseInt(incoming.readLine());
       } catch (IOException ex) {
         ex.printStackTrace();
       } finally {
         socket.close();
       }
     } catch (Exception ex) {
       //bad but ok for demo
       ex.printStackTrace();
     }
     return new Integer(this.bytesRead);
  }

}

public class FileReaderClient {

  /*Full path to files for demo*/
  /*each file gets a server, make sure enough server-port combinations*/
  public static String[] fileNames = {
      "file1","file2", "file3", "file4"
  };

  /*Server Ports to communicate*/
  /*Simple constraint: ports[0] correspond to servers[0]*/
  public static int[] ports = {
      8000, 8001, 8002, 8003
  };

  public static String[] servers = {
      "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"
  };


  public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        final ExecutorService service = Executors.newFixedThreadPool(fileNames.length);
        final ArrayList<Future<Integer>> tasks = new ArrayList<Future<Integer>>(fileNames.length);
        int totalBytesRead = 0;

        for (int i=0; i<fileNames.length; ++i) {
            String serverName = servers[i]+':'+ports[i];
            System.out.println("Delegating task #"+i+" to server at " + serverName);
            FileReaderCallable reader = new FileReaderCallable(serverName, fileNames[i], servers[i], ports[i]);
            tasks.add(service.submit(reader));
        }

        try {
            int bytesRead = 0;
            for (Future<Integer> task : tasks) {
        		    bytesRead = task.get();
                totalBytesRead += bytesRead;
        		}
        } catch(final InterruptedException ex) {
            ex.printStackTrace();
        } catch(final ExecutionException ex) {
            ex.printStackTrace();
        }

        System.out.println("Total bytes read: " + totalBytesRead);
        long endTime = System.currentTimeMillis();
        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);
        System.out.println("Process took: " + (endTime - startTime)/1000.0 + " seconds");
        service.shutdownNow();
  }

}
