package ro.ase.ebusiness.proiect;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Meniu {

	public static boolean existaDirectorDupaId(List<Componenta> directoare, int idDirector) {
		for(Componenta componenta : directoare) {
			if(componenta instanceof Director) {
				Director director = (Director) componenta;
				if(director.getId() == idDirector) {
					return true; 
				}
			}
		}
		return false; 
	}

	public static String citesteExtensieValida(Scanner scanner) {
		Set<String> extensiiValide = new HashSet<>();
		StringBuilder extensiiDisponibile = new StringBuilder("Extensiile permise sunt: ");

		for (TipFisier tip : TipFisier.values()) {
			extensiiValide.add(tip.name().toLowerCase());
			extensiiDisponibile.append(tip.name().toLowerCase()).append(", ");
		}
		// elimin ultima virgula si spatiu
		extensiiDisponibile.delete(extensiiDisponibile.length() - 2, extensiiDisponibile.length()); 
		System.out.println(extensiiDisponibile);
		while (true) {
			System.out.print("Introduceți extensia fișierului pe care doriți să îl adăugați: ");
			String extensie = scanner.next().toLowerCase();
			if (extensiiValide.contains(extensie)) {
				return extensie;
			} else {
				System.out.println("Extensie invalidă. Vă rugăm să introduceți o extensie validă.");
			}
		}
	}

	public static void afiseazaDirectoareCuCaleSiId(List<Componenta> directoare) {
		System.out.println("Directoare disponibile:");
		for (Componenta componenta : directoare) {
			if (componenta instanceof Director) {
				Director director = (Director) componenta;
				System.out.println("\t Id: " + director.getId() + ", cale: " + director.getCale());
			}
		}
	}

	public static void afiseazaFisiere(List<Componenta> directoare) {
		System.out.println("Fisierele existente: ");
		for(Componenta componenta : directoare) {
			if(componenta instanceof Director) {
				Director director = (Director) componenta;
				List<Componenta> fisiere = director.getComponente();
				for(Componenta componentaFisier : fisiere) {
					if(componentaFisier instanceof Fisier) {
						Fisier fisier = (Fisier) componentaFisier;
						System.out.println("\t" + fisier.getNume());
					}
				}
			}
		}
	}

	public static boolean verificaExistentaFisier(List<Componenta> directoare, String numeFisier) {
		for (Componenta componenta : directoare) {
			if (componenta instanceof Director) {
				Director director = (Director) componenta;
				for (Componenta componentaFisier : director.getComponente()) {
					if (componentaFisier instanceof Fisier) {
						Fisier fisier = (Fisier) componentaFisier;
						if (fisier.getNumeFisier().equals(numeFisier)) {
							return true; 
						}
					}
				}
			}
		}
		return false;
	}

	public static String obtineNumeLuna(int luna) {
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(new Locale("ro", "RO"));
		return dateFormatSymbols.getMonths()[luna];
	}

	private static void afiseazaMeniu() {
		System.out.println("\n");
		System.out.println("Meniul aplicatiei:");
		System.out.println("\t Operatii generale");
		System.out.println("\t \t 1. Afiseaza ierarhie directoare si fisiere");
		System.out.println("\t Operatii asupra directoarelor");
		System.out.println("\t \t 2. Adaugare director nou");
		System.out.println("\t \t 3. Stergere director");
		System.out.println("\t \t 4. Redenumire director");
		System.out.println("\t \t 5. Statistici directoare");
		System.out.println("\t Operatii asupra fisierelor");
		System.out.println("\t \t 6. Adauga fisier in director");
		System.out.println("\t \t 7. Sterge fisier din director");
		System.out.println("\t \t 8. Redenumeste fisier");
		System.out.println("\t \t 9. Muta fisier");
		System.out.println("\t \t 10. Calculeaza dimensiunea fisierelor tinand cont de extensie");
		System.out.println("\t \t 11. Determina numarul de fisiere modificate in functie de data");
		System.out.println("\t \t 12. Afiseaza fisierele cu o anumita extensie");
		System.out.println("\t \t 13. Determina fisierele cu dimensiune minima/maxima pentru fiecare extensie");
		System.out.println("\t 0. Iesire");
		System.out.println("\n");
		System.out.print("Alegeti o optiune: ");
	}

	private static List<Componenta> directoare = new ArrayList<> ();

	public static void main(String[] args) throws ExceptieCaleInvalida, ExceptieDataInViitor {
		Scanner scanner = new Scanner(System.in); 
		List<String> dateLocatii = new ArrayList<>();
		List<String> dateFisiere = new ArrayList<>();

		System.out.println("Bine ai venit!\n");

		ManipulatorFisier cititor = new ManipulatorFisier();
		String fisierDirectoare = "locatii_directoare.txt";
		dateLocatii = cititor.citesteFisier(fisierDirectoare);
		String informatiiDirectoare = "informatii_fisiere.txt";
		dateFisiere =  cititor.citesteFisier(informatiiDirectoare);
		if(!dateLocatii.isEmpty() && !informatiiDirectoare.isEmpty())
			System.out.print("Datele au fost incarcate din fisier!");
		try {
			OperatiiDirectoare.organizeazaFisiere(dateLocatii, dateFisiere, directoare);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ExceptieDataInViitor e) {
			e.printStackTrace();
		}

		int optiune;
		do {
			afiseazaMeniu();
			optiune = scanner.nextInt();

			switch (optiune) {
			case 1:
				for(Componenta componenta : directoare) {
					componenta.afisareDetalii(0);
				}
				break;
			case 2:
				//adaugarea unui nou director in lista de directoare
				System.out.print("Introduceti calea noului director: ");
				scanner.nextLine();
				String caleDirectorNou = scanner.nextLine();
				try {
					OperatiiDirectoare.adaugaDirector(directoare, caleDirectorNou);
				} catch (ExceptieCaleInvalida e) {
					System.out.println("Exceptie: " + e.getMessage());
				}
				break;
			case 3:
				//stergerea unui director din lista de directoare
				afiseazaDirectoareCuCaleSiId(directoare);
				System.out.print("Introduceti id-ul directorului pe care doriti sa il stergeti: ");
				scanner.nextLine();
				int idDirectorDeSters = scanner.nextInt();
				if(existaDirectorDupaId(directoare, idDirectorDeSters)) {
					OperatiiDirectoare.stergeDirectorDupaCale(directoare, idDirectorDeSters);
				}
				else {
					System.out.println("Id-ul " + idDirectorDeSters + " nu aparține niciunui director!");
				}
				break;
			case 4:
				// redenumirea unui director
				afiseazaDirectoareCuCaleSiId(directoare);
				System.out.print("Tastați id-ul directorului pe care doriți să-l redenumiți: ");
				int idDirector = scanner.nextInt();
				scanner.nextLine();
				if(existaDirectorDupaId(directoare, idDirector)) {
					System.out.print("Introduceți noul nume al directorului: ");
					String noulNume = scanner.nextLine();
					try {
						OperatiiDirectoare.redenumesteDirector(directoare, idDirector, noulNume);
					} catch (ExceptieCaleInvalida e) {
						System.out.println("Exceptie: " + e.getMessage());
					}
				} else {
					System.out.println("Id-ul " + idDirector + " nu aparține niciunui director!");
				}
				break;
			case 5:
				// statistici pentru director
				afiseazaDirectoareCuCaleSiId(directoare);
				System.out.print("Introduceți id-ul directorului pentru care doriți statistici: ");
				scanner.nextLine();
				int idDirectorStatistici = scanner.nextInt();
				String statisticiDirector = Statistici.calculareStatisticiDirector(directoare, idDirectorStatistici);
				if (!statisticiDirector.startsWith("Id-ul introdus nu există in lista de directoare.") &&
						!statisticiDirector.startsWith("Directorul pentru care doriti statistici nu conține componente.")) {
					System.out.println(statisticiDirector);
					System.out.print("Doriți să salvați statisticile într-un fișier text? (da/nu): ");
					String raspuns = scanner.next().toLowerCase();
					if (raspuns.equals("da")) {
						String numeFisierSalvare = "statistici_director_" + idDirectorStatistici + ".txt";
						ManipulatorFisier.salveazaStatisticiInFisier(statisticiDirector, numeFisierSalvare);
						System.out.println("Statistici salvate în fișierul " + numeFisierSalvare + ".");
					} else if (raspuns.equals("nu")) {
						System.out.println("Statisticile nu au fost salvate în fișier.");
					} else {
						System.out.println("Raspuns invalid. Statisticile nu au fost salvate.");
					}
				} else {
					System.out.println(statisticiDirector);
				}
				break;
			case 6:
				//adaugare fisier in director specificat
				afiseazaDirectoareCuCaleSiId(directoare);
				System.out.print("Tastati id-ul directorului in care doriti sa adaugati fisierul: ");
				scanner.nextLine();
				int idDirectorAdaugare = scanner.nextInt();
				scanner.nextLine();
				if (!existaDirectorDupaId(directoare, idDirectorAdaugare)) {
					System.out.println("Id-ul directorului introdus nu exista.");
					break;
				}
				System.out.print("Introduceti numele fisierului pe care doriti sa il adaugati: ");
				String numeFisier = scanner.nextLine();
				if (verificaExistentaFisier(directoare, numeFisier)) {
					System.out.println("Fisierul cu numele " + numeFisier + " deja există în acest director.");
				} else {
					String extensie = citesteExtensieValida(scanner);
					System.out.print("Introduceti dimensiunea fisierului: ");
					int dimensiune = scanner.nextInt();
					OperatiiFisiere.adaugaFisier(directoare, idDirectorAdaugare, numeFisier, extensie, dimensiune);
				}
				break;
			case 7:
				//stergere fisier
				afiseazaFisiere(directoare);
				System.out.print("Introduceti numele fisierului pe care doriti sa il stergeti: ");
				scanner.nextLine();
				String numeFisierDeSters = scanner.nextLine();
				OperatiiFisiere.stergeFisier(directoare, numeFisierDeSters);
				break;
			case 8:
				//redenumire fisier
				afiseazaFisiere(directoare);
				System.out.print("Introduceti numele actual al fisierului: ");
				scanner.nextLine();
				String numeCurent = scanner.nextLine();
				try {
					if (!verificaExistentaFisier(directoare, numeCurent)) {
						System.out.println("Fisierul cu numele " + numeCurent + " nu exista in niciun director.");
					} else {
						System.out.print("Introduceti noul nume: ");
						String numeNou = scanner.nextLine();
						OperatiiFisiere.redenumesteFisier(directoare, numeCurent, numeNou);
					}
				} catch (Exception e) {
					System.out.println("Exceptie: " + e.getMessage());
				}
				break;
			case 9:
				//mutare fisier
				for(Componenta componenta : directoare) {
					componenta.afisareDetalii(0);
				}
				System.out.print("Introduceti numele fisierului pe care doriti sa il mutati: ");
			    String numeFisierMutare = scanner.next(); // Folosim next() pentru a citi numele fisierului
			    
			    if (!verificaExistentaFisier(directoare, numeFisierMutare)) {
			        System.out.println("Fisierul cu numele " + numeFisierMutare + " nu exista.");
			    } else {
			        System.out.print("Introduceti id-ul directorului unde vreti sa mutati fisierul: ");
			        int idDirectorMutare = scanner.nextInt();
			        scanner.nextLine(); 
			        
			        if (!existaDirectorDupaId(directoare, idDirectorMutare)) {
			            System.out.println("Directorul cu id-ul " + idDirectorMutare + " nu exista.");
			        } else {
			            OperatiiFisiere.mutaFisier(directoare, numeFisierMutare, idDirectorMutare);
			        }
			    }
			    break;
			case 10:
				//calculare dimensiune fisier dupa extensie
				String statisticiExtensiiFisier = Statistici.calculareDimensiuneFisierePeExtensii(directoare);
				System.out.println(statisticiExtensiiFisier);
				System.out.print("Doriti sa salvati statisticile intr-un fisier text? (da/nu): ");
				String raspunsSalvare = scanner.next().toLowerCase();
				if (raspunsSalvare.equals("da")) {
					String numeFisierSalvare = "statistici_dimensiune_fisiere_extensii.txt";
					ManipulatorFisier.salveazaStatisticiInFisier(statisticiExtensiiFisier, numeFisierSalvare);
					System.out.println("Statistici salvate in fisierul " + numeFisierSalvare + ".");
				}
				else if (raspunsSalvare.equals("nu")) {
					System.out.println("Statisticile nu au fost salvate in fisier.");
				} else {
					System.out.println("Raspuns invalid. Statisticile nu au fost salvate.");
				}
				break;
			case 11:
				//calculare numar fisiere salvate intr-o anumita perioada de timp
				System.out.print("Introduceți anul de început: ");
				int anStart = scanner.nextInt();
				System.out.print("Introduceți anul de sfârșit: ");
				int anSfarsit = scanner.nextInt();
				StringBuilder stringBuilder = new StringBuilder();
				if (anSfarsit < anStart) {
					System.out.println("Anul de sfârșit trebuie să fie mai mare decât anul de început.");
				} else if (anSfarsit > LocalDate.now().getYear()) {
					System.out.println("Anul de sfarsit nu poate sa fie mai mare decat anul curent.");
				}else {
					int[][] numarFisierePeAnSiLuna = Statistici.calculareNumarFisierePeAnSiLuna(directoare, anStart, anSfarsit);
					for (int an = 0; an < numarFisierePeAnSiLuna.length; an++) {
						for (int luna = 0; luna < 12; luna++) {
							if (numarFisierePeAnSiLuna[an][luna] != 0) {
								String numeLuna = obtineNumeLuna(luna);
								stringBuilder.append("Anul ").append(anStart + an).append(", luna ").append(numeLuna)
								.append(": ").append(numarFisierePeAnSiLuna[an][luna]).append(" fisiere salvate\n");
							}
						}
					}
					System.out.println(stringBuilder.toString());
					System.out.print("Doriti sa salvati statisticile intr-un fisier text? (da/nu): ");
					String raspunsSalvareStatistici = scanner.next().toLowerCase();
					if (raspunsSalvareStatistici.equals("da")) {
						String numeFisierSalvare = "statistici_numar_fisiere_salvate.txt";
						ManipulatorFisier.salveazaStatisticiInFisier(stringBuilder.toString(), numeFisierSalvare);
						System.out.println("Statistici salvate in fisierul " + numeFisierSalvare + ".");
					}
					else if (raspunsSalvareStatistici.equals("nu")) {
						System.out.println("Statisticile nu au fost salvate in fisier.");
					} else {
						System.out.println("Raspuns invalid. Statisticile nu au fost salvate.");
					}
				}

				break;
			case 12:
				//creaza Set cu fisierele cu o anumita extensie
				String extensieInput = citesteExtensieValida(scanner);
				try {
					Set<Fisier> fisiereGasite = Statistici.gasesteFisiereCuExtensie(directoare, TipFisier.valueOf(extensieInput.toUpperCase()));
					StringBuilder stringBuilderSet = new StringBuilder();
					stringBuilderSet.append("Fisiere cu extensia ").append(extensieInput).append(" găsite:\n");
					for (Fisier fisier : fisiereGasite) {
						stringBuilderSet.append("\t Nume fisier: ").append(fisier.getNume()).append("\n");
					}
					System.out.println(stringBuilderSet.toString());
					System.out.println("Doriți să salvați statisticile într-un fișier text? (da/nu): ");
					String raspunsSalvareSet = scanner.next().toLowerCase();
					if (raspunsSalvareSet.equals("da")) {
						String numeFisierSalvare = "fisiere_extensia_" + extensieInput + ".txt";
						ManipulatorFisier.salveazaStatisticiInFisier(stringBuilderSet.toString(), numeFisierSalvare);
						System.out.println("Statistici salvate în fișierul " + numeFisierSalvare + ".");
					} else if (raspunsSalvareSet.equals("nu")) {
						System.out.println("Statisticile nu au fost salvate în fișier.");
					} else {
						System.out.println("Răspuns invalid. Statisticile nu au fost salvate.");
					}
				} catch (IllegalArgumentException e) {
					System.out.println("Extensie invalidă. Vă rugăm să introduceți o extensie validă.");
				}
				break;
			case 13:
				//determinam fisierele cu dimensiune minima/maxima
				Map<TipFisier, Fisier> fisierMinimPerExtensie = Statistici.gasesteFisierMinimPerExtensie(directoare);
				Map<TipFisier, Fisier> fisierMaximPerExtensie = Statistici.gasesteFisierMaximPerExtensie(directoare);
				List<TipFisier> extensii = new ArrayList<>(fisierMinimPerExtensie.keySet());
				StringBuilder sbFisiereInfo = new StringBuilder ("Fisierele cu dimensiunea minima/maxima sunt:\n");
				for (TipFisier extensieFisier : extensii) {
					sbFisiereInfo.append("\t Extensie: ").append(extensieFisier).append("\n");
					Fisier fisierMinim = fisierMinimPerExtensie.get(extensieFisier);
					sbFisiereInfo.append("\t\t Nume fisier: ").append(fisierMinim.getNume()).append(" ,dimensiune: ")
					.append(fisierMinim.getDimensiune()).append(" kilobytes\n");
					Fisier fisierMaxim = fisierMaximPerExtensie.get(extensieFisier);
					sbFisiereInfo.append("\t\t Nume fisier: ").append(fisierMaxim.getNume()).append(" ,dimensiune: ")
					.append(fisierMaxim.getDimensiune()).append(" kilobytes\n");

				}
				System.out.println(sbFisiereInfo.toString());
				System.out.println("Doriti sa salvati statisticile intr-un fisier text? (da/nu): ");
				String raspunsSalvareStatFisiere = scanner.next().toLowerCase();
				if (raspunsSalvareStatFisiere.equals("da")) {
					String numeFisierSalvare = "statistici_fisiere.txt";
					ManipulatorFisier.salveazaStatisticiInFisier(sbFisiereInfo.toString(), numeFisierSalvare);
					System.out.println("Statistici salvate in fisierul " + numeFisierSalvare + ".");
				}
				else if (raspunsSalvareStatFisiere.equals("nu")) {
					System.out.println("Statisticile nu au fost salvate in fisier.");
				} else {
					System.out.println("Raspuns invalid. Statisticile nu au fost salvate.");
				}
				break;
			case 0:
				ManipulatorFisier.actualizeazaFisiere(directoare, fisierDirectoare, informatiiDirectoare);
				System.out.println("Aplicatia se inchide. La revedere!");
				break;
			default:
				System.out.println("Optiune invalida. Te rog sa alegi din nou.");
				break;
			}
		} while (optiune != 0);
		scanner.close();
	}
}
