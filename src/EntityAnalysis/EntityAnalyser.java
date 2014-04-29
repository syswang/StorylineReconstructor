package EntityAnalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import EntitySubset.Subset;
import ReviewEntityTool.FileReader;
import ReviewEntityTool.Stemmer;

public class EntityAnalyser {
  private ArrayList<String> sentences = new ArrayList<String>();
  private HashMap<Subset, Integer> hash = new HashMap<Subset, Integer>();

  public ArrayList<String> getSentences() {
    return sentences;
  }

  public void readFromTagFile(String filePath) throws IOException {
    ArrayList<String> lines = new ArrayList<String>();
    FileReader fr = new FileReader();
    lines = fr.readFile(filePath);
    // System.out.println(lines.size());
    String pattern = "([.]+_:[ ]|[?!]+_[A-Z]+[ ]|[.?!]_\\.[ ]+|;_:[ ])";
    System.out.println(lines.size());
    sentences.addAll(Arrays.asList(lines.get(0).split(pattern)));

    System.out.println(sentences.size());
  }

  public void groupConsecutiveEntities() {
    ArrayList<String> resultLines = new ArrayList<String>();
    for (String sentence : sentences) {
      if (sentence.length() == 0)
        continue;

      ArrayList<String> wordsWithTag = new ArrayList<String>();
      wordsWithTag.addAll(Arrays.asList(sentence.split("[ ]")));
      for (int i = 0; i < wordsWithTag.size(); ++i) {
        if (i + 1 < wordsWithTag.size() && wordsWithTag.get(i).contains("NN")
            && wordsWithTag.get(i + 1).contains("NN")) {

          StringBuffer sb = new StringBuffer();
          sb.append(wordsWithTag.get(i).split("_")[0].toLowerCase() + "_");
          ++i;
          while (i < wordsWithTag.size() && wordsWithTag.get(i).contains("NN")) {
            sb.append(wordsWithTag.get(i).split("_")[0].toLowerCase() + "_");
            wordsWithTag.remove(i);
          }
          wordsWithTag.set(i - 1, sb.toString() + "NN");
//          System.out.println(sb.toString() + "NN");
        }
      }

      StringBuffer resSb = new StringBuffer();
      for (String wordWithTag : wordsWithTag) {
        resSb.append(wordWithTag);
        resSb.append(" ");
      }
      resultLines.add(resSb.toString().trim());
    }

    sentences = resultLines;
  }

  // public void sentenceSegmentation()
  public String stemmingTrans(String str) {
    Stemmer s = new Stemmer();
    if (str.length() == 0)
      return "";
    for (int i = 0; i < str.length(); ++i) {
      s.add(Character.toLowerCase(str.charAt(i)));
    }
    s.stem();
    return s.toString();
  }
}
