package Voyageur_De_Commerce;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Algo_Genetiques.Individu;

public class Individu_VDC implements Individu {
	private double[] coord_x;
	private double[] coord_y;
	private int[] parcours;

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

	/*
	 * Classes de l'interface Individu
	 */
	@Override
	public double adaptation() {
		double sum = 0;
		// Pas de retour au parcout initial
		for (int i = 0; i < parcours.length - 1; i++) {
			sum += distance(parcours[i], parcours[i + 1]);
		}
		return 1 / sum;
	}

	public double distance(int i, int j) {
		return Math.sqrt(Math.pow(coord_x[i] - coord_x[j], 2) + Math.pow(coord_y[i] - coord_y[j], 2));
	}

	@Override
	public Individu[] croisement(Individu conjoint) {
		Individu_VDC conjoint_VDC = (Individu_VDC) conjoint;
		// Une possibilité: croisement "prudent"

		boolean[] b1 = new boolean[parcours.length];
		boolean[] b2 = new boolean[parcours.length];
		for (int i = 0; i < parcours.length; i++) {
			b1[i] = false;
			b2[i] = false;
		}
		Random r = new Random();
		int ind = r.nextInt(parcours.length);

		// on regarde les villes qu'on rencontre dans la premiere partie
		Individu_VDC[] enfants = new Individu_VDC[2];
		enfants[0] = new Individu_VDC(coord_x, coord_y);
		enfants[1] = new Individu_VDC(coord_x, coord_y);
		for (int i = 0; i < ind; i++) {
			enfants[0].parcours[i] = this.parcours[i];
			b1[this.parcours[i]] = true;

			enfants[1].parcours[i] = conjoint_VDC.parcours[i];
			b2[conjoint_VDC.parcours[i]] = true;
		}

		// deuxieme partie : si la ville n'a pas été visitée dans la premiere partie, on
		// prend

		int maxCities = parcours.length;
		// pour l'enfant 1
		for (int i = 0; i < b1.length; i++) {
			if (b1[i] == false) {
				enfants[0].parcours[i] = this.parcours[i];
			}
		}
		// pour l'enfant 2
		for (int i = 0; i < b2.length; i++) {
			if (b2[i] == false) {
				enfants[1].parcours[i] = this.parcours[i];
			}
		}

		// fin : on complète avec les villes non rencontrées
		return enfants;
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
