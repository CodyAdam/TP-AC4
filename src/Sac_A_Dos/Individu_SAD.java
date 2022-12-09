package Sac_A_Dos;

import Algo_Genetiques.Individu;

public class Individu_SAD implements Individu {
  // 0 = non pris, 1 = pris
  private int objectTakenBinary;
  private int capacity;

  public Individu_SAD(int numberOfObject, int capacity) {
    this.objectTakenBinary = (int) (Math.random() * Math.pow(2, numberOfObject));
    this.capacity = capacity;
  }

  public Individu_SAD(int objectTakenBinary, int numberOfObject, int capacity) {
    this.objectTakenBinary = objectTakenBinary;
    this.capacity = capacity;
  }

  @Override
  public double adaptation() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Individu[] croisement(Individu conjoint) {
    if (!(conjoint instanceof Individu_SAD)) {
      return null;
    }
    Individu_SAD conjointSAD = (Individu_SAD) conjoint;
    int indexOfCrossOver = (int) (Math.random() * this.objectTakenBinary);
    int mask = (int) (Math.pow(2, indexOfCrossOver) - 1);
    int child1 = (this.objectTakenBinary & mask) | (conjointSAD.getObjectTakenBinary() & ~mask);
    int child2 = (conjointSAD.getObjectTakenBinary() & mask) | (this.objectTakenBinary & ~mask);
    return new Individu[] { new Individu_SAD(child1, this.objectTakenBinary, this.capacity),
        new Individu_SAD(child2, this.objectTakenBinary, this.capacity) };
  }

  @Override
  public void mutation(double prob) {
    for (int i = 0; i < this.objectTakenBinary; i++) {
      if (Math.random() < prob) {
        // flip the bit at index i
        this.objectTakenBinary ^= (1 << i);
      }
    }
  }

  public int getCapacity() {
    return capacity;
  }

  public int getObjectTakenBinary() {
    return objectTakenBinary;
  }
}
