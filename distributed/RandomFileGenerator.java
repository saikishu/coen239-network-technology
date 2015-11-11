/**
Simple Random File Generator
Usage: java RandomFileGenerator <buffer size in MB>
Max size: ((Integer.MAX_VALUE-5)/(1024*1024)) MB
**/
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RandomFileGenerator {

  public static final int MEGA_MULTIPLIER = 1048576;

  public static void main(String[] args) {
      long startTime = System.currentTimeMillis();
      System.out.println("Start time: " + startTime);

      if(args.length == 0) return;

      int numBytes = Integer.parseInt(args[0]);
      byte[] buffer = new byte[numBytes * MEGA_MULTIPLIER];

      //write to file
      File file;
      FileOutputStream fop = null;
      try {
        file = new File("file"); //data file
        fop = new FileOutputStream(file);
        if (!file.exists()) {
  				file.createNewFile();
  			}
        fop.write(buffer);
        fop.flush();
        fop.close();
      } catch (IOException e) {
			     e.printStackTrace();
		  } finally {
    			try {
    				if (fop != null) {
    					fop.close();
    				}
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
		  }
      long endTime = System.currentTimeMillis();
      System.out.println("End time: " + endTime);

      System.out.println("Process took: " + (endTime - startTime)/1000.0 + " seconds");
  }

}
