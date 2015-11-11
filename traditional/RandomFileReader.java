/**
Simple Random File Reader - Sequential read
Usage: java RandomFileReader file_name
**/
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class RandomFileReader {

  public static final int MEGA_MULTIPLIER = 1048576;

  public static void main(String[] args) {
      long startTime = System.currentTimeMillis();

      if(args.length == 0) return;

      String fileName = args[0];
      byte[] buffer = new byte[MEGA_MULTIPLIER]; // 1 MB at a time

      //read from file
      FileInputStream fin = null;
      try {
        fin = new FileInputStream(fileName);
        int total = 0;
        int bytesRead = 0;
        while((bytesRead = fin.read(buffer)) != -1 ) {
          //System.out.println(new String(buffer));
          total += bytesRead;
        }
        fin.close();
        System.out.println("Read bytes: " + total);
      } catch (FileNotFoundException e) {
			     e.printStackTrace();
		  } catch (IOException e) {
    				e.printStackTrace();
    	}

      long endTime = System.currentTimeMillis();
      System.out.println("Start time: " + startTime);
      System.out.println("End time: " + endTime);
      System.out.println("Process took: " + (endTime - startTime)/1000.0 + " seconds");
  }

}
