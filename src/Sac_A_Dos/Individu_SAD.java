package Sac_A_Dos;

import Algo_Genetiques.Individu;

public class Individu_SAD implements Individu {
  // 0 = non pris, 1 = pris
  private int objectTakenBinary;
  private int capacity;
  private double[] weights;

  public Individu_SAD(int capacity, double[] weights) {
    this.objectTakenBinary = (int) (Math.random() * Math.pow(2, weights.length));
    this.capacity = capacity;
    this.weights = weights;
  }

  public Individu_SAD(int objectTakenBinary, int capacity, double[] weights) {
    this.objectTakenBinary = objectTakenBinary;
    this.capacity = capacity;
    this.weights = weights;
  }

  @Override
  public double adaptation() {
    double sumWeights = 0;
    for (int i = 0; i < this.weights.length; i++) {
      if ((this.objectTakenBinary & (1 << i)) != 0) {
        sumWeights += this.weights[i];
      }
    }
    if (sumWeights > this.capacity) {
      return this.capacity - 2 * (sumWeights - this.capacity);
    }
    return sumWeights;
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
    return new Individu[] { new Individu_SAD(child1, this.capacity, this.weights),
        new Individu_SAD(child2, this.capacity, this.weights) };
  }

  @Override
  public void mutation(double prob) {
    for (int i = 0; i < this.weights.length; i++) {
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

  public double[] getWeights() {
    return weights;
  }
}
