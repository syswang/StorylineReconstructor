package EntityAnalysis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import ReviewEntityTool.Data;


public class GetEntitiesDataFromFile {
  private ArrayList<Data> entitiesData = new ArrayList<Data>();

  public ArrayList<Data> getEntitiesData() {
    return entitiesData;
  };

  public ArrayList<String> getAllEntitiesName() {
    ArrayList<String> names = new ArrayList<String>();
    for (Data data : entitiesData) {
      names.add(data.entity);
    }

    return names;
  }

  public void readEntityData(ArrayList<String> lines) {
    if (lines == null) return;
    
    for (String line : lines) {
      String[] entData = line.split(";"); 
      entitiesData.add(readStringArrayToData(entData));
    }
  }

  private Data readStringArrayToData(String[] parameter) {
    Data data = new Data();
    data.entity = parameter[0];
    data.count = Integer.parseInt(parameter[1]);
    data.probability = Double.parseDouble(parameter[2]);
    String[] nameArr = parameter[3].substring(1, parameter[3].length() - 1).split(",");
    ArrayList<String> names = new ArrayList<String>();
    for (int i = 0; i < nameArr.length; ++i) {
      names.add(nameArr[i]);
    }
    data.names = names;
    data.probability = Double.parseDouble(parameter[4]);

    return data;
  }
}
