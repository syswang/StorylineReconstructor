import java.io.File;

import EntityAnalysis.EntityAnalyser;
import FileParser.FileParser;

public class FileUtils {
  public static void main(String[] args) {
    String graphPath = "/Users/Documents/workspace/EE239 bookreview/bookreviews_graph/";
    String directoryInPath = "/Users/Documents/workspace/EE239 bookreview/bookreviews/";
    String directoryTaggedInPath = "/Users/Documents/workspace/EE239 bookreview/bookreviews_tag/";
    String directoryTaggedUnionNNInPath =
        "/Users/Documents/workspace/EE239 bookreview/bookreviews_tag2/";
    String directoryOutPath = "/Users/Documents/workspace/EE239 bookreview/bookreviews_out/";
    String directoryOut2Path = "/Users/Documents/workspace/EE239 bookreview/bookreviews_out2/";
    
    // Replace the INPUTFILE DIRECTORY to the parent directory of input files 
    File[] files = new File(INPUTFILE_DIRECTORY).listFiles();

    try {
      for (File file : files) {
        if (file.getName().startsWith("all"))
          continue;

        // Step 2) Running Stanford POS tagger FileParser.posTagger(inputFile, outputFile) method to all small text reviews. Make sure the stanford-postagger.jar has been set to the java project library path correctly before doing this step.
        // System.out.println(ea.getSentences().size());
        // System.out.print("Tagging file: " + file.getName() + " ...");
        // String outputName = file.getName().split("_")[0];
        // try {
        // FileParser.posTagger(directoryInPath + file.getName(), directoryTaggedInPath +
        // outputName);
        // } catch (OutOfMemoryError e) {
        // System.out.println("Tagging file: " + file.getName() + " out of memory heap!");
        // e.printStackTrace();
        // continue;
        // }
        // System.out.println("Done!");


        // Step 4) Scanning and checking all tagged text and finding consecutive nouns and grouping them as one noun by running Analyzer.groupConsecutiveEntities() method and write the result to file.
        // EntityAnalyser ea = new EntityAnalyser();
        // ea.readFromTagFile(directoryTaggedInPath + file.getName());
        // ea.groupConsecutiveEntities();
        // FileWriter fw = new FileWriter();
        // fw.writeFile(directoryTaggedUnionNNInPath +file.getName(), ea.getSentences());

        
        // Step 5) Using FileParser.entityStatisticsOverCorpus(inputFile) method to analyze to entity occurrence and frequency. Input file is the result of Step 4. Note that the all book reviews (corpus file) need to be analyzed and be written to file first for computing probability difference between individual review and corpus reviews)  
        // System.out.print("Entity frequency of file: " + file.getName() + " ...");
        // try {
        // FileParser.entityStatisticsOverCorpus(directoryTaggedUnionNNInPath + file.getName(),
        // directoryOut2Path + file.getName(),
        // directoryOut2Path);
        // System.out.println("Done!");
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        
        // Step 6) Using fp.entitiesRelationAndVerb(inputFile, outputFile) method to analyze the taggedText. Any verb existing between two ahead and back nouns is taken as the nouns edge relationship. The relationship for all two noun entities are directional (active or passive). Result will be written in output files. 
        // System.out.print("Build graph relation:" + file.getName() + "...");
        // FileParser fp = new FileParser();
        // fp.entitiesRelationAndVerb(directoryTaggedUnionNNInPath + file.getName(), graphPath +
        // file.getName());
        // //fp.printEntitiesRelation();
        // fp.writeEntitiesRelationToFile(graphPath + file.getName());
        // System.out.print("Done.");

      }



    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();

    }

  }
}
