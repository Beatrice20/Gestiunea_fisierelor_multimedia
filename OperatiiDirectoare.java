package ro.ase.ebusiness.proiect;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

/**
 * Clasa ofera functionalitati pentru organizarea datelor despre directoare si fisiere intr-un sistem de fisiere virtual
 * si realizarea operatiilor asupra directoarelor
 * @author Dragoi Beatrice
 *
 */
public class OperatiiDirectoare {
	
	/**
	 * Metoda este folosita pentru a organiza ierarhic lista de directoare și fisiere in sistem.
	 * @param dateDirectoare - lista de siruri de caractere care contine informatii despre directoarele din sistem
	 * @param dateFisiere - lista de siruri de caractere care contine informatii despre fisierele din sistem
	 * @param directoare - lista de componente (directoare si fisiere) care este actualizata cu noile obiecte create
	 * @throws ParseException - exceptie aruncata daca apare o eroare in procesul de parsare a datelor
	 * @throws ExceptieDataInViitor - exceptie aruncata daca data de creare a fisierului este in viitor
	 * @throws ExceptieCaleInvalida - exceptie aruncata daca calea furnizata pentru un director nu are formatul specificat
	 */
	public static void organizeazaFisiere (List<String> dateDirectoare, List<String> dateFisiere, List<Componenta> directoare) throws ParseException, ExceptieDataInViitor, ExceptieCaleInvalida {
		for(String linieLocatie : dateDirectoare) {
			String[] informatiiDirector = linieLocatie.split(",");
			String cale = informatiiDirector[0];
			int id = Integer.parseInt(informatiiDirector[1]);
			Director director = new Director(cale, id);
			directoare.add(director);
		}
		for (String linieFisier : dateFisiere) {
			String[] informatiiFisier = linieFisier.split(",");
			int idDirector = Integer.parseInt(informatiiFisier[0]);
			String numeFisier = informatiiFisier[1];

			String extensieString = informatiiFisier[2];
			TipFisier extensie = TipFisier.valueOf(extensieString.toUpperCase());

			String dataString = informatiiFisier[3];
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate data = null;

			try {
			    data = LocalDate.parse(dataString, formatter);
			} catch (DateTimeException e) {
			    e.printStackTrace(); 
			}

			int dimensiune = Integer.parseInt(informatiiFisier[4]);
			for(Componenta componenta : directoare) {
				if(componenta instanceof Director && ((Director) componenta).getId() == idDirector) {
					Fisier fisier = new Fisier(idDirector, numeFisier, extensie, data, dimensiune);
					((Director) componenta).adaugaEntitate(fisier);
					break;
				}
			}
		}
	}

	/**
	 * Metoda care verifica daca un director cu o cale specificata exista deja in lista de directoare
	 * @param directoare - lista de componente (directoare si fisiere) care exista
	 * @param cale - calea pentru care se verifica existenta
	 * @return - true daca directorul cu calea specificata exista in lista, false in caz contrar
	 */
	public static boolean existaDirector(List<Componenta> directoare, String cale) {
		for(Componenta componenta : directoare) {
			if(componenta instanceof Director) {
				Director director = (Director) componenta;
				if(director.getCale().equals(cale)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Metoda folosita pentru adaugarea unui nou director in lista de directoare, 
	 * verificand mai intai daca mai exista un director cu aceeasi cale
	 * @param directoare - lista de componente (directoare si fisiere) in care se efectueaza adaugarea
	 * @param cale - calea directorului care urmeaza sa fie adaugat.
	 * @throws ExceptieCaleInvalida - exceptie aruncata daca calea furnizata pentru un director nu are formatul specificat
	 */
	public static void adaugaDirector(List<Componenta> directoare, String cale) throws ExceptieCaleInvalida {
		if(!existaDirector(directoare, cale)) {
			int id = directoare.size()+ 1;
			Director director = new Director(cale,id);
			directoare.add(director);
			System.out.println("Noul director a fost adaugat in lista!");
		}
		else {
			System.out.println("Directorul cu calea specificata deja exista!");
		}
	}
	
	/**
	 * Metoda folosita pentru actualizarea id-urilor directoarelor atunci cand are loc stergerea unui director
	 * @param directoare - lista de componente (directoare si fisiere) in care se efectueaza adaugarea
	 */
	public static void reactualizeazaIdDupaStergere(List<Componenta> directoare) {
	    for (int i = 0; i < directoare.size(); i++) {
	        Componenta componenta = directoare.get(i);
	        if (componenta instanceof Director) {
	            Director director = (Director) componenta;
	            director.setId(i + 1);
	        }
	    }
	}
	
	/**
	 * Metoda care sterge un director din lista pe baza identificatorului
	 * @param directoare - lista de componente (directoare si fisiere) in care se efectueaza adaugarea
	 * @param idDirector -  identificatorul directorului care urmeaza sa fie sters
	 */
	public static void stergeDirectorDupaCale(List<Componenta> directoare, int idDirector) {
	    Iterator <Componenta> iterator = directoare.iterator();
	    while (iterator.hasNext()) {
	        Componenta componenta = iterator.next();
	        if (componenta instanceof Director) {
	            Director director = (Director) componenta;
	            if (director.getId() == idDirector) {
	                iterator.remove();
	                System.out.println("Directorul " + director.getCale() + " a fost șters din listă.");
	                reactualizeazaIdDupaStergere(directoare);
	                return; 
	            }
	        }
	    }
	}
	
	/**
	 * Metoda care redenumeste un director existent pe baza identificatorului, verificand
	 * dacă noul nume este unic în sistem
	 * @param directoare - lista de componente (directoare si fisiere) in care se efectueaza adaugarea
	 * @param idDirector - identificatorul directorului care urmeaza sa fie redenumit
	 * @param nouaCale - noul nume/noua cale a directorului 
	 * @throws ExceptieCaleInvalida - exceptie aruncata daca calea furnizata pentru un director nu are formatul specificat
	 */
	public static void redenumesteDirector(List<Componenta> directoare, int idDirector, String nouaCale) throws ExceptieCaleInvalida {
		if(existaDirector(directoare, nouaCale)) {
			System.out.println("Alegeti alt nume. Numele " + nouaCale + " exista deja in lista!");
			return;
		}
		for(Componenta componenta : directoare) {
			if(componenta instanceof Director) {
				Director director = (Director) componenta;
				if(director.getId() == idDirector) {
					director.setCale(nouaCale);
					System.out.println("Directorul a fost redenumit cu succes! Noua cale este: " + nouaCale);
					return;
				}
			}
		}
	}
}
