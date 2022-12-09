package Sac_A_Dos;

import java.io.InputStream;
import Util.Lecture;
import Algo_Genetiques.Population;

public class Client_Sac_A_Dos {

	/**
	 * lit une liste de poids dans un fichier
	 * 
	 * @param nomFichier nom du fichier texte contenant les poids
	 * @param nbr_objets nombre d'objets possibles
	 * @return tableau de poids
	 */
	public static double[] charge_poids(String nomFichier, int nbr_objets) {
		double[] poids = new double[nbr_objets];
		InputStream IS = Lecture.ouvrir(nomFichier);
		if (IS == null) {
			System.err.println("Impossible d'ouvrir le fichier \"" + nomFichier + "\" (n'existe pas ? pas au bon endroit ?)");
		}
		int i = 0;
		int somme = 0;
		while (!Lecture.finFichier(IS) && i < nbr_objets) {
			poids[i] = Lecture.lireDouble(IS);
			somme += poids[i];
			i++;
		}
		System.out.println("charge_poids (" + nomFichier + ") : poids total des objets = " + somme);
		Lecture.fermer(IS);
		return poids;
	}

	public static void main(String[] args) {

		/* paramètres */
		int nbr_indiv = 100;
		double prob_mut = 0.01;

		/*
		 * On initialise les poids en lisant un fichier
		 */

		// int nbr_objets = 28;
		// int capacite = 1581;

		int nbr_objets = 70;
		int capacite = 350;

		double[] poids = charge_poids("./data_sad/nbrobj" + nbr_objets + "_capacite" + capacite + ".txt", nbr_objets);

		/*
		 * on crée une population (aléatoire)
		 * de nbr_indiv individus associés au problème
		 * du sac à dos considéré
		 */

		Individu_SAD[] individus = new Individu_SAD[nbr_indiv];
		for (int i = 0; i < nbr_indiv; i++) {
			individus[i] = (new Individu_SAD(nbr_objets, capacite, poids));
		}
		Population<Individu_SAD> pop = new Population<Individu_SAD>(individus);

		/*
		 * on génére les générations successives
		 * en faisant se reproduire la population
		 * et on affiche l'adaptation moyenne et maximale de chaque génération
		 * on s'arrête si on a atteint la capacité ou si on fait un nombre donné
		 * (paramètre) d'itérations
		 * le résultat est alors donné par l'individu maximal de la dernière génération
		 */
		int iter = 0;
		int max_iter = 500;
		while (pop.adaptation_maximale() != capacite && iter <= max_iter) {
			iter++;
			pop.reproduction(prob_mut);
			System.out.println(pop.adaptation_maximale());
		}
		System.out.println("Nombre d'itérations : " + iter);
		System.out.println("Adaptation moyenne : " + pop.adaptation_moyenne());
		System.out.println("Adaptation maximale : " + pop.adaptation_maximale());
	}
}
