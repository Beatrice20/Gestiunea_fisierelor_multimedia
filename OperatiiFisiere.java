package ro.ase.ebusiness.proiect;

import java.time.LocalDate;
import java.util.List;

/**
 * Clasa contine metode pentru gestionarea si manipularea fisierelor in cadrul sistemului de fișiere virtual 
 * @author Dragoi Beatrice
 */

public class OperatiiFisiere {

	/**
	 * Metoda adauga un fisier intr-un director specificat, identificat dupa id. 
	 * Fisierul este creat cu datele primite ca parametrii.
	 * @param directoare - lista de componente (directoare si fisiere) care exista
	 * @param idDirector - id-ul directorului in care se doreste adaugarea
	 * @param numeFisier - numele fisierului care urmeaza sa fie adaugat
	 * @param extensie - extensia noului fisier
	 * @param dimensiune - dimensiunea fisierului
	 * @throws ExceptieDataInViitor - exceptie aruncata daca data de creare a fisierului este in viitor
	 */
	public static void adaugaFisier(List<Componenta> directoare, int idDirector, String numeFisier, String extensie, int dimensiune) throws ExceptieDataInViitor {
		for (Componenta componenta : directoare) {
			if (componenta instanceof Director) {
				Director director = (Director) componenta;
				if (director.getId() == idDirector) {
					LocalDate dataCurenta = LocalDate.now();
					Fisier fisierNou = new Fisier(idDirector, numeFisier, TipFisier.valueOf(extensie.toUpperCase()), dataCurenta, dimensiune);
					director.adaugaEntitate(fisierNou);
					System.out.println("Fisierul " + fisierNou.getNume() + " a fost adaugat cu succes in directorul cu id-ul " + director.getId() + " si calea " + director.getCale() + "!");
					return;
				}
			}
		}
	}

	/**
	 * Metoda sterge un fisier din lista pe baza numelui sau
	 * @param directoare - lista de componente (directoare si fisiere) care exista
	 * @param numeFisier - numele fisierului pe care doresc sa il sterg
	 */
	public static void stergeFisier (List<Componenta> directoare, String numeFisier) {
		String caleDirector = null;
		int idDirector = 0;
		for (Componenta director : directoare) {
			if (director instanceof Director) {
				Director directorCurent = (Director) director;
				List<Componenta> fisiere = directorCurent.getComponente();
				for (Componenta fisier : fisiere) {
					if (fisier instanceof Fisier && ((Fisier) fisier).getNumeFisier().equals(numeFisier)) {
						caleDirector = directorCurent.getCale();
						idDirector = directorCurent.getId();
						fisiere.remove(fisier);
						System.out.println("Fisierul " + fisier.getNume() + " a fost sters cu succes din directorul cu id-ul " + idDirector + " si calea '" + caleDirector + "'!");
						return;
					}
				}
			}
		}
		System.out.println("Nu s-a gasit niciun fisier cu numele " + numeFisier);
	}

	/**
	 * Metoda redenumeste un fisier pe baza numelui sau curent și actualizeaza data la data curenta
	 * @param directoare - lista de componente (directoare si fisiere) care exista
	 * @param numeCurent - numele curent al fisierului
	 * @param numeNou - noul nume pe care vreau sa il dau fisierului
	 * @throws ExceptieDataInViitor - exceptie aruncata daca data de creare a fisierului este in viitor
	 */
	public static void redenumesteFisier(List<Componenta> directoare, String numeCurent, String numeNou) throws ExceptieDataInViitor {
		for(Componenta componenta : directoare) {
			if(componenta instanceof Director) {
				Director director = (Director) componenta;
				List<Componenta> entitati = director.getComponente();
				for(Componenta entitate : entitati) {
					if(entitate instanceof Fisier) {
						Fisier fisier = (Fisier) entitate;
						if(fisier.getNumeFisier().equals(numeCurent)) {
							fisier.setNumeFisier(numeNou);
							fisier.setDataCrearii(LocalDate.now());
							System.out.println("Fisierul " + numeCurent + " a fost redenumit " + numeNou + 
									" si se afla in directorul cu calea " + director.getCale() + "!");
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * Metoda folosita pentru a muta un fisier intr-un alt director
	 * @param directoare - lista de componente (directoare si fisiere) care exista
	 * @param numeFisier - numele fisierului pe care doresc sa il mut
	 * @param idDirectorDestinatie - id-ul directorului unde vreau sa mut fisierul
	 * @throws ExceptieDataInViitor - exceptie aruncata daca data de creare a fisierului este in viitor
	 */
	public static void mutaFisier (List<Componenta> directoare, String numeFisier, int idDirectorDestinatie) throws ExceptieDataInViitor {
		Fisier fisierDeMutat = null;
		for(Componenta componenta : directoare) {
			if(componenta instanceof Director) {
				Director director = (Director)componenta;
				List<Componenta> fisiere = director.getComponente();
				for(Componenta fisier : fisiere) {
					if(fisier instanceof Fisier) {
						if(((Fisier) fisier).getNumeFisier().equals(numeFisier)) {
							fisierDeMutat = (Fisier) fisier;
							break;
						}
					}
				}
				if(fisierDeMutat != null) {
					break;
				}
			}
		}
		if(fisierDeMutat == null) {
			System.out.println("Nu exista niciun fisier cu numele " + numeFisier);
			return;
		}
		stergeFisier(directoare, numeFisier);
		adaugaFisier(directoare, idDirectorDestinatie, fisierDeMutat.getNumeFisier(), fisierDeMutat.getExtensie().name(), fisierDeMutat.getDimensiune());
	}
}
