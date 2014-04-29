package EntitySubset;

import java.util.ArrayList;


public class Subset {
  private ArrayList<String> entities;
  private int weight;
  
  public int getWeight() {
    return weight;
  }

  public ArrayList<String> getEntities() {
    return entities;
  }

  public Subset(){}
  public Subset(ArrayList<String> subset) {
    this.entities = subset;
    this.weight = 1;
  }
}
