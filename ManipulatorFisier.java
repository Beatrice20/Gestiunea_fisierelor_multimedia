package ro.ase.ebusiness.proiect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa ce furnizeaza metode pentru citirea si actualizarea datelor din fisierele text
 * @author Dragoi Beatrice
 */

public class ManipulatorFisier {

	/**
	 * Metoda folosita pentru citirea datelor din fisier
	 * @param numeFisier - numele fisierului din care se realizeaza citirea
	 * @return - returneaza o listă de siruri de caractere, fiecare sir reprezentand o linie din fisier
	 */
	public List<String> citesteFisier(String numeFisier) {
		List<String> continutFisier = new ArrayList<>();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(numeFisier))) {
			String linie;
			while ((linie = bufferedReader.readLine()) != null) {
				continutFisier.add(linie.trim());
			}
		} catch (IOException e) {
			System.out.println("Eroare la citirea fișierului: " + e.getMessage());
		}
		return continutFisier;
	}

	/**
	 * Metoda actualizeaza continutul celor doua fisiere cu datele aflate in structura de directoare primita
	 * @param directoare - lista ce contine directoarele si fisierele din cele doua fisiere
	 * @param informatiiDirectoare - numele fisierului ce contine informatiile despre directoare
	 * @param informatiiFisiere - numele fisierului ce contine informatii despre fisiere
	 */
	public static void actualizeazaFisiere (List<Componenta> directoare, String informatiiDirectoare, String informatiiFisiere) {
		try(BufferedWriter locatiiWriter = new BufferedWriter (new FileWriter(informatiiDirectoare,false));
				BufferedWriter fisiereWriter = new BufferedWriter(new FileWriter(informatiiFisiere,false))){
			for(Componenta componenta : directoare) {
				if(componenta instanceof Director) {
					Director director = (Director) componenta;
					String linieLocatie = director.getCale() + "," + director.getId();
					locatiiWriter.write(linieLocatie);
					locatiiWriter.newLine();

					List<Componenta> componenteInterne = director.getComponente();
					for(Componenta componentaInterna : componenteInterne) {
						if(componentaInterna instanceof Fisier) {
							Fisier fisier = (Fisier)componentaInterna;
							String linieFisier = director.getId() + "," + fisier.getNumeFisier() + "," + 
									fisier.getExtensie() + "," + fisier.getDataCrearii() + "," + fisier.getDimensiune();
							fisiereWriter.write(linieFisier);
							fisiereWriter.newLine();
						}
					}
				}
			}
			System.out.println("Fisierele au fost actualizate cu succes!");
		} catch (IOException e) {
			System.out.println("Eroare la scrierea in fisier " + e.getMessage());
		}
	}

	/**
	 * Metoda folosita pentru salvarea statisticilor intr-un fisier text
	 * @param statistici - continutul ce trebuie salvat
	 * @param numeFisier - numele fisierului unde se salveaza statisticile
	 */
	public static void salveazaStatisticiInFisier(String statistici, String numeFisier) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(numeFisier))) {
			writer.write(statistici);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
