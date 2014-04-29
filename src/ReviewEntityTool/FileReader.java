package ReviewEntityTool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileReader {
  public ArrayList<String> readFile(String filePath) throws IOException {
    InputStream is = new FileInputStream(filePath);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    String line = reader.readLine();
    ArrayList<String> textArr = new ArrayList<String>();
    while (line != null) {
      try {
        textArr.add(line);
      } catch (NullPointerException e) {
        System.out.println(line);
        e.printStackTrace();
      }
      line = reader.readLine();
    }
    reader.close();
    is.close();
    return textArr;
  }
}
