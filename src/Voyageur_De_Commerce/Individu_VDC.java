package Voyageur_De_Commerce;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Algo_Genetiques.Individu;

public class Individu_VDC implements Individu {
	private double[] coord_x;
	private double[] coord_y;
	private int[] parcours;
	private double fitness;

	// Constructeur
	public Individu_VDC(double[] coord_x, double[] coord_y) {
		this.coord_x = coord_x;
		this.coord_y = coord_y;
		int len = coord_x.length;
		parcours = new int[len];
		Set<Integer> taken = new HashSet<Integer>();
		for (int i = 0; i < len; i++) {
			int rand = (int) (Math.random() * len);
			while (taken.contains(rand)) {
				rand = (int) (Math.random() * len);
			}
			taken.add(rand);
			parcours[i] = rand;
		}
	}

	public Individu_VDC(Individu_VDC other) {
		this.coord_x = other.coord_x;
		this.coord_y = other.coord_y;
		this.parcours = other.parcours.clone();
	}

	/*
	 * Classes de l'interface Individu
	 */
	@Override
	public double adaptation() {
		if (fitness == 0) {
			calculer_adaptation();
		}
		return fitness;
	}

	public double calculer_adaptation() {
		double sum = 0;
		// Avec un retour à la ville de départ
		for (int i = 0; i < parcours.length - 1; i++) {
			sum += distance(parcours[i], parcours[i + 1]);
		}
		sum += distance(parcours[parcours.length - 1], parcours[0]);
		double fit = 100 / sum;
		fitness = fit;
		return fit;
	}

	public double distance(int i, int j) {
		return Math.sqrt(Math.pow(coord_x[i] - coord_x[j], 2) + Math.pow(coord_y[i] - coord_y[j], 2));
	}

	public void optim_2opt() {
		for (int i = 0; i < parcours.length; i++) {
			for (int j = i + 1; j < parcours.length; j++) {
				if (gain(i, j) < 0) {
					for (int k = 0; k < (j - i + 1) / 2; k++) {
						int tmp = parcours[i + k];
						parcours[i + k] = parcours[j - k];
						parcours[j - k] = tmp;
					}
				}
			}
		}
	}

	private double gain(int i, int j) {
		int nb_villes = parcours.length;
		double gain = distance(parcours[i], parcours[(j + 1) % nb_villes])
				+ distance(parcours[(i + nb_villes - 1) % nb_villes], parcours[j])
				- distance(parcours[(i + nb_villes - 1) % nb_villes], parcours[i])
				- distance(parcours[j], parcours[(j + 1) % nb_villes]);
		return gain;
	}

	@Override
	public Individu[] croisement(Individu conjoint) {
		Individu_VDC conjoint_VDC = (Individu_VDC) conjoint;

		Set<Integer> seen1 = new HashSet<Integer>();
		Set<Integer> seen2 = new HashSet<Integer>();
		Random r = new Random();
		int ind = r.nextInt(parcours.length);
		// ex :
		// parent 1 : 0 4 1 | 3 2
		// parent 2 : 1 0 2 | 3 4

		// on regarde les villes qu'on rencontre dans la premiere partie
		// child 1 : 0 4 1 | ? ? -> seen : 0 4 1
		// child 2 : 1 0 2 | ? ? -> seen : 1 0 2
		Individu_VDC child1 = new Individu_VDC(coord_x, coord_y);
		Individu_VDC child2 = new Individu_VDC(coord_x, coord_y);
		for (int i = 0; i < ind; i++) {
			child1.parcours[i] = this.parcours[i];
			seen1.add(this.parcours[i]);
			child2.parcours[i] = conjoint_VDC.parcours[i];
			seen2.add(conjoint_VDC.parcours[i]);
		}

		// on remplit les villes non vues dans la deuxieme partie
		// child 1 : 0 4 1 | 3 . -> seen : 0 3 4 1
		// child 2 : 1 0 2 | 3 . -> seen : 1 0 2 3
		for (int i = ind; i < parcours.length; i++) {
			if (!seen1.contains(conjoint_VDC.parcours[i])) {
				child1.parcours[i] = conjoint_VDC.parcours[i];
				seen1.add(conjoint_VDC.parcours[i]);
			} else {
				// add a city that is not yet seen
				for (int j = 0; j < parcours.length; j++) {
					if (!seen1.contains(j)) {
						child1.parcours[i] = j;
						seen1.add(j);
						break;
					}
				}
			}

			if (!seen2.contains(this.parcours[i])) {
				child2.parcours[i] = this.parcours[i];
				seen2.add(this.parcours[i]);
			} else {
				// add a city that is not yet seen
				for (int j = 0; j < parcours.length; j++) {
					if (!seen2.contains(j)) {
						child2.parcours[i] = j;
						seen2.add(j);
						break;
					}
				}
			}
		}
		if (Math.random() < 0.8) {
			child1.optim_2opt();
			child2.optim_2opt();
		}
		child1.calculer_adaptation();
		child2.calculer_adaptation();
		return new Individu[] { child1, child2 };
	}

	@Override
	public void mutation(double prob) {
		for (int i = 0; i < parcours.length; i++) {
			if (Math.random() < prob) {
				int j = (int) (Math.random() * parcours.length);
				int tmp = parcours[i];
				parcours[i] = parcours[j];
				parcours[j] = tmp;
			}
		}
		calculer_adaptation();
	}

	/*
	 * Accesseurs (pour Display_VDC)
	 */
	public int[] get_parcours() {
		return parcours;
	}

	public double[] get_coord_x() {
		return coord_x;
	}

	public double[] get_coord_y() {
		return coord_y;
	}
}
