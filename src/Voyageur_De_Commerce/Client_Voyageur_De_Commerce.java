package Voyageur_De_Commerce;

import java.io.*;
import Util.Lecture;
import Algo_Genetiques.Population;

public class Client_Voyageur_De_Commerce {

	/**
	 * lit une liste de poids dans un fichier
	 * 
	 * @param nomFichier nom du fichier texte contenant les coordonnées des villes
	 * @param nbr_villes nombre de villes
	 * @param coord_x    et coord_y : les 2 tableaux que la fonction remplit et qui
	 *                   vont contenir les coordonnées des villes
	 */
	public static void charge_coords(String nomFichier, int nbr_villes, double[] coord_x, double[] coord_y) {
		assert (coord_x.length == coord_y.length) : "charge_coords : coord_x et coord_y n'ont pas la même taille ?";
		InputStream IS = Lecture.ouvrir(nomFichier);
		if (IS == null) {
			System.err.println("pb d'ouverture du fichier " + nomFichier);
		}
		int i = 0;
		while (!Lecture.finFichier(IS) && i < coord_x.length) {
			coord_x[i] = Lecture.lireDouble(IS);
			coord_y[i] = Lecture.lireDouble(IS);
			i++;
		}
		Lecture.fermer(IS);
	}

	public static void main(String[] args) throws InterruptedException {

		/*
		 * on initialise les coordonnées des villes en les lisant ds un fichier
		 */
		int nbr_villes = 250;
		// int nbr_villes = 256;
		double prob_mut = 0.005;
		double[] coord_x = new double[nbr_villes];
		double[] coord_y = new double[nbr_villes];
		// charge_coords("data_vdc/spirale_" + nbr_villes + ".txt", nbr_villes, coord_x, coord_y);
		charge_coords("data_vdc/" + nbr_villes + "coords.txt", nbr_villes, coord_x, coord_y);

		/*
		 * Exemple d'utilisation de Display_VDCC (il faut d'abord faire le constructeur
		 * pour ce test fonctionne, ainsi que compléter les accesseurs)
		 */

		Individu_VDC[] individus = new Individu_VDC[nbr_villes];
		for (int i = 0; i < nbr_villes; i++) {
			individus[i] = (new Individu_VDC(coord_x, coord_y));
		}
		Population<Individu_VDC> pop = new Population<Individu_VDC>(individus);

		Display_VDC disp = new Display_VDC(pop.individu_maximal()); // on l'affiche
		Thread.sleep(5000); // pause de 1 seconde (pour avoir le temps de voir le premier affichage)
		disp.refresh(pop.individu_maximal()); // on met à jour l'affichage avec le nouveau
		int iter = 0;
		int max_iter = 5000;
		while (iter <= max_iter) {
			iter++;
			pop.reproduction(prob_mut);
			if (iter % 100 == 0) {
				Thread.sleep(100); // pause de 1 seconde (pour avoir le temps de voir le premier affichage)
				System.out.println(iter + " " + pop.adaptation_maximale());
				disp.refresh(pop.individu_maximal()); // on met à jour l'affichage avec le nouveau
			}
		}

	}
}