package Algo_Genetiques;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Population<Indiv extends Individu> {

	// Liste contenant les différents individus d'une génération
	private List<Indiv> population;

	/**
	 * construit une population à partir d'un tableau d'individu
	 */
	public Population(Indiv[] popu) {
		population = new ArrayList<Indiv>(Arrays.asList(popu));
	}

	/**
	 * sélectionne un individu (sélection par roulette par exemple, cf TD)
	 * 
	 * @param adapt_totale somme des adaptations de tous les individus (pour ne pas
	 *                     avoir à la recalculer)
	 * @return indice de l'individu sélectionné
	 * 
	 *         exemple de sélection par roulette :
	 *         rand adapt_totale
	 *         |---------------|-------|-------|-------|---------------|---|---|-------|-------|
	 *         | | | | | | | | |
	 *         0 1 2 3 4 5 | 6 7
	 * 
	 */
	public int selection(double adapt_totale) {
		double randomMaxedByAdapt = Math.random() * adapt_totale;
		double sum = 0;
		for (int i = 0; i < population.size(); i++) {
			sum += population.get(i).adaptation();
			if (sum >= randomMaxedByAdapt) {
				return i;
			}
		}
		return population.size() - 1;
	}

	/**
	 * remplace la génération par la suivante
	 * (croisement + mutation)
	 * 
	 * @param prob_mut probabilité de mutation
	 */
	@SuppressWarnings("unchecked")
	public void reproduction(double prob_mut) {

		/***** on construit la nouvelle génération ****/
		List<Indiv> new_generation = new ArrayList<Indiv>();

		/* élitisme */
		new_generation.add(individu_maximal());

		// tant qu'on n'a pas le bon nombre
		while (new_generation.size() < population.size()) {
			// on sélectionne les parents

			double adapt_totale = 0;
			for (Indiv i : population) {
				adapt_totale += i.adaptation();
			}
			Indiv parent1 = population.get(selection(adapt_totale));
			Indiv parent2 = population.get(selection(adapt_totale));

			// ils se reproduisent
			Indiv[] childs = (Indiv[]) parent1.croisement(parent2);
			Indiv child1 = childs[0];
			Indiv child2 = childs[1];

			// on les ajoute à la nouvelle génération
			new_generation.add(child1);
			new_generation.add(child2);
		}

		// on applique une éventuelle mutation à toute la nouvelle génération
		for (Indiv i : new_generation) {
			i.mutation(prob_mut);
		}

		// on remplace l'ancienne par la nouvelle
		population = new_generation;
	}

	/**
	 * renvoie l'individu de la population ayant l'adaptation maximale
	 */
	public Indiv individu_maximal() {
		Indiv master = population.get(0);
		for (Indiv i : population) {
			if (i.adaptation() > master.adaptation()) {
				master = i;
			}
		}
		return master;
	}

	/**
	 * renvoie l'adaptation moyenne de la population
	 */
	public double adaptation_moyenne() {
		double avgRate = 0;
		for (Indiv i : population) {
			avgRate = avgRate + i.adaptation();
		}
		avgRate = avgRate / (double) population.size();
		return avgRate;
	}

	/**
	 * renvoie l'adaptation maximale de la population
	 */
	public double adaptation_maximale() {
		double max = 0;
		for (Indiv i : population) {
			if (i.adaptation() > max) {
				max = i.adaptation();
			}
		}
		return max;
	}
}
