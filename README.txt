Book Review Java Analysis Code User Guideline
=========================================
Below is the steps required  for getting the entity frequency in book reviews. Before running this code, user need to prepare a book or movie review and a corpus (many book reviews text), which might be crawled from the web. 

**Note: Each step has to be run in process, which might be a command line executed in terminal or the code block in project main function locating in /src/FileUtils.java should be run

Step 1) Split the text file to small file size pieces by using command “split -b byte_count FILENAME”. Running the Stanford POS tagger might take exponential time regarding to the file size. If it happen execution termination due to memory out of heap error, it can be solved be tune to have a larger memory heap usage in java or the file size should be split to smaller size.

Step 2) Running Stanford POS tagger FileParser.posTagger(inputFile, outputFile) method to all small text reviews. Make sure the stanford-postagger.jar has been set to the java project library path correctly before doing this step.

Step 3) Use command “cat FILENAME_PREFIX * ” to cat all small text have been tagged POS.  

Step 4) Scanning and checking all tagged text and finding consecutive nouns and grouping them as one noun by running Analyzer.groupConsecutiveEntities() method and write the result to file.

Step 5) Using FileParser.entityStatisticsOverCorpus(inputFile) method to analyze to entity occurrence and frequency. Input file is the result of Step 4. Note that the all book reviews (corpus file) need to be analyzed and be written to file first for computing probability difference between individual review and corpus reviews)  

Step 6) Using fp.entitiesRelationAndVerb(inputFile, outputFile) method to analyze the taggedText. Any verb existing between two ahead and back nouns is taken as the nouns edge relationship. The relationship for all two noun entities are directional (active or passive). Result will be written in output files. 

 

