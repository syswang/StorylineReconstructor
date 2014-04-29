package FileParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import ReviewEntityTool.Data;
import ReviewEntityTool.Stemmer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class FileParser {
  private static HashMap<String, Integer> twoEntitiesFreq = new HashMap<String, Integer>();
  private static HashMap<String, HashMap<String, Data>> entitiesRelation =
      new HashMap<String, HashMap<String, Data>>();
  public static void printTwoEntitiesFreq() {
    ArrayList<Data> res = new ArrayList<Data>();
    for (String entities : twoEntitiesFreq.keySet()) {
      Data data = new Data();
      data.entity = entities;
      data.count = twoEntitiesFreq.get(entities); 
      res.add(data);
    }
    
    Collections.sort(res, new Comparator<Data>() {
      public int compare(Data a, Data b) {
        return b.count - a.count;
      }
    });
    
    for (Data d : res) {
      if (d.count < 5) continue; 
      System.out.println(d.entity + " : "+ d.count);
    }
  }
  public static void writeEntitiesRelationToFile(String pFilename) throws IOException {
    FileOutputStream fos = new FileOutputStream(pFilename, true);
    OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
    StringBuffer sb = new StringBuffer();
    for (String entitiesKey : entitiesRelation.keySet()) {
      sb.append(entitiesKey);
      sb.append('\n');
      HashMap<String, Data> vHash = new HashMap<String, Data>();
      vHash = entitiesRelation.get(entitiesKey);
      for (String v : vHash.keySet()) {
        sb.append(v + ":" + vHash.get(v).count);
        sb.append('\n');
      }
      sb.append('\n');
    }
    
    out.write(sb.toString());
    out.flush();
    out.close();
  }

  public static void printEntitiesRelation() {
    for (String entitiesKey : entitiesRelation.keySet()) {
      System.out.println(entitiesKey);
      HashMap<String, Data> vHash = new HashMap<String, Data>();
      vHash = entitiesRelation.get(entitiesKey);
      for (String v : vHash.keySet()) {
        System.out.println("  " + v + " : " + vHash.get(v).count);
      }
    }
  }

  private static String stemmingTrans(String str) {
    Stemmer s = new Stemmer();
    if (str.length() == 0)
      return "";
    for (int i = 0; i < str.length(); ++i) {
      s.add(Character.toLowerCase(str.charAt(i)));
    }
    s.stem();
    return s.toString();
  }

  public static void entitiesRelationAndVerb(String inputFilePath, String outputFilePath) throws IOException {
    InputStream is = new FileInputStream(inputFilePath);
    String line;
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    line = reader.readLine();

    while (line != null) {
      if (!line.contains("_NN")) {
        line = reader.readLine();
        continue;
      }

      String[] words = line.split("[ ]");
      int startNindex = 0;
      while (!words[startNindex].contains("_NN")) {
        startNindex++;
      }

      ArrayList<String> verbs = new ArrayList<String>();
      ArrayList<String> pasVerbs = new ArrayList<String>();
      String halfKey1 = stemmingTrans(words[startNindex].split("_NN")[0]);
      for (int i = startNindex + 1; i < words.length; ++i) {
        if (words[i].contains("_NN")) {
          String noun = words[i].split("_NN")[0];
          String halfKey2 = stemmingTrans(noun);
          String key1 = halfKey1 + "," + halfKey2;
          String key2 = halfKey2 + "," + halfKey1;

          if (verbs.size() != 0)
            addVerbAndWeightToHash(key1, verbs);
          if (pasVerbs.size() != 0)
            addVerbAndWeightToHash(key2, pasVerbs);
          updateTwoEntitiesFreq(key1);
          verbs = new ArrayList<String>();
          pasVerbs = new ArrayList<String>();
        }

        if (words[i].contains("_V")) {
          if (words[i].contains("_VBN")) {
            pasVerbs.add(words[i].split("_VBN")[0]);
          } else {
            verbs.add(words[i].split("_V")[0]);
          }
        }
      }

      line = reader.readLine();
    }
    reader.close();
    is.close();
  }

  private static void updateTwoEntitiesFreq(String key) {
    String[] partKeys = key.split(",");
    String anotherKey = partKeys[1] + "," + partKeys[0];

    if (!twoEntitiesFreq.containsKey(key) && !twoEntitiesFreq.containsKey(anotherKey)) {
      twoEntitiesFreq.put(key, 1);
    } else {
      int count =
          twoEntitiesFreq.containsKey(key) ? twoEntitiesFreq.get(key) : twoEntitiesFreq.get(anotherKey);
      count++;
    }
  }

  private static void addVerbAndWeightToHash(String key1, ArrayList<String> verbs) {
    HashMap<String, Data> vData = entitiesRelation.get(key1);
    if (vData == null) {
      vData = new HashMap<String, Data>();
      entitiesRelation.put(key1, vData);
    }
    for (String verb : verbs) {
      String verbKey = stemmingTrans(verb);
      Data data = vData.get(verbKey);
      if (data == null) {
        Data newData = new Data();
        newData.entity = verbKey;
        ArrayList<String> names = new ArrayList<String>();
        names.add(verb);
        newData.names = names;
        newData.count = 1;
        vData.put(verbKey, newData);
      } else {
        data.count++;
        if (!data.names.contains(verb))
          data.names.add(verb);
      }
    }
  }

  public static void entityStatisticsOverCorpus(String inputFilePath, String outputFilePath,
      String outputParentPath) throws IOException {
    InputStream is = new FileInputStream(inputFilePath);

    String line;
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    line = reader.readLine();
    HashMap<String, Integer> hash = new HashMap<String, Integer>();
    while (line != null) {
      String[] lineWords = line.split("[ ,.!]+");
      for (int i = 0; i < lineWords.length; ++i) {
        if (!lineWords[i].contains("_NN"))
          continue;
        String key = lineWords[i].split("_N")[0].toLowerCase();

        if (!hash.containsKey(key))
          hash.put(key, 0);
        hash.put(key, hash.get(key) + 1);

      }
      line = reader.readLine();

    }

    HashMap<String, Integer> standardHash = new HashMap<String, Integer>();
    HashMap<String, ArrayList<String>> namesHash = new HashMap<String, ArrayList<String>>();

    for (String key : hash.keySet()) {
      String standardKey = stemmingTrans(key);
      ArrayList<String> names = namesHash.get(standardKey);
      if (names == null) {
        names = new ArrayList<String>();
        names.add(key);
        namesHash.put(standardKey, names);
        standardHash.put(standardKey, hash.get(key));
      } else {
        names.add(key);
        int sumVal = standardHash.get(standardKey) + hash.get(key);
        standardHash.put(standardKey, sumVal);
      }
    }



    ArrayList<Data> list2 = hashToArr(standardHash, namesHash);
    HashMap<String, Double> allHash = readEntityProbInAllReviewsFromFile(outputParentPath);
    for (int i = 0; i < list2.size(); ++i) {
      String key = list2.get(i).entity;
      if (!allHash.containsKey(key)) {
        list2.get(i).probdiff = -1;
      } else {
        //
        list2.get(i).probdiff = list2.get(i).probability - allHash.get(key);
      }
    }
    list2 = rankArrByProbDiff(list2);

    // for (Data d : list2) {
    // System.out.println(d.entity+ " "+ d.probdiff);
    // }
    writeToFile(outputFilePath, readDataToBuffer(list2));
    reader.close();
    is.close();

  }

  public static void posTagger(String inputFilePath, String outputFilePath) throws IOException {
    InputStream is = new FileInputStream(inputFilePath);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    String line = reader.readLine();
    MaxentTagger tagger = new MaxentTagger("taggers/wsj-0-18-bidirectional-nodistsim.tagger");
    StringBuffer resText = new StringBuffer();

    while (line != null) {
      if (line.length() == 0) {
        line = reader.readLine();
        continue;
      }
      String tagged = tagger.tagString(line);
      resText.append(tagged);
      line = reader.readLine();
    }
    writeToFile(outputFilePath, resText);
    reader.close();
    is.close();
  }

  public static StringBuffer readDataToBuffer(ArrayList<Data> dataArr) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < dataArr.size(); ++i) {
      // if (dataArr.get(i).count < 50)
      // break;
      sb.append(dataArr.get(i).entity + ";" + dataArr.get(i).count + ";" + dataArr.get(i).probability + ";"
          + dataArr.get(i).names + ";" + dataArr.get(i).probdiff);
      sb.append("\n");
    }

    return sb;
  }

  public static void print(ArrayList<Data> dataArr) {
    for (int i = 0; i < dataArr.size(); ++i) {
      if (dataArr.get(i).count < 100)
        break;
      System.out.println(dataArr.get(i).entity + ";" + dataArr.get(i).count + ";"
          + dataArr.get(i).probability + ";" + dataArr.get(i).names + ";" + dataArr.get(i).probdiff);
    }
  }

  public static ArrayList<Data> calculateOverallProb(ArrayList<Data> arr) {
    int allCount = 0;
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMinimumFractionDigits(4);

    for (int i = 0; i < arr.size(); ++i) {
      allCount += arr.get(i).count;
    }
    for (int i = 0; i < arr.size(); ++i) {
      arr.get(i).probability = Double.parseDouble(nf.format((double) (arr.get(i).count * 100.0 / allCount)));
    }

    return arr;
  }

  public static ArrayList<Data> hashToArr(HashMap<String, Integer> hash,
      HashMap<String, ArrayList<String>> namesHash) {
    ArrayList<Data> arr = new ArrayList<Data>();
    for (String key : hash.keySet()) {
      if (key.length() == 0)
        continue;
      if (!Character.isLetter(key.charAt(0)))
        continue;
      StringBuffer ent = new StringBuffer();
      ent.append(Character.toUpperCase(key.charAt(0))).append(key.substring(1));
      Data data = new Data();
      data.entity = ent.toString();
      data.count = hash.get(key);
      data.names = namesHash.get(key);
      arr.add(data);
    }

    arr = calculateOverallProb(arr);
    return arr;
  }

  public static ArrayList<Data> rankArrByProbDiff(ArrayList<Data> arr) {
    Collections.sort(arr, new Comparator<Data>() {
      public int compare(Data a, Data b) {
        if (b.probdiff - a.probdiff > 0)
          return 1;
        else
          return -1;
      }
    });

    return arr;
  }

  public static HashMap<String, Double> readEntityProbInAllReviewsFromFile(String inputFilePath)
      throws IOException {
    InputStream is = new FileInputStream(inputFilePath + "all.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    String line = reader.readLine();
    HashMap<String, Double> hash = new HashMap<String, Double>();
    while (line != null) {
      String[] lines = line.split(";");
      hash.put(lines[0], Double.parseDouble(lines[2]));
      line = reader.readLine();
    }
    reader.close();
    is.close();
    return hash;
  }

  public static void writeToFile(String pFilename, StringBuffer pData) throws IOException {
    FileOutputStream fos = new FileOutputStream(pFilename + ".csv", true);
    OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");

    out.write(pData.toString());
    out.flush();
    out.close();
  }


}
