package ro.ase.ebusiness.proiect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Clasa folosita pentru a realiza diferite statistici pentru fisierele existente in lista de componente
 * @author Dragoi Beatrice
 */
public class Statistici {
	
	/**
	 * Metoda calculeaza recursiv dimensiunea fisierelor continute intr-un director
	 * @param director - directorul pentru care se calculeaza dimensiunea
	 * @return - returneaza dimensiunea unui director
	 */
    private static int calculeazaDimensiuneTotala(Director director) {
        int dimensiuneTotala = 0;
        for (Componenta componenta : director.getComponente()) {
            if (componenta instanceof Fisier) {
                Fisier fisier = (Fisier) componenta;
                dimensiuneTotala += fisier.getDimensiune();
            } else if (componenta instanceof Director) {
                dimensiuneTotala += calculeazaDimensiuneTotala((Director) componenta);
            }
        }
        return dimensiuneTotala;
    }

    /**
     * Metoda calculeaza recursiv numarul de fisiere din directorul primit ca parametru
     * @param director - directorul pentru care se calculeaza numarul de fisiere
     * @return - returneaza numarul de fisiere din director
     */
    private static int numaraFisiere(Director director) {
        int numarFisiere = 0;
        for (Componenta componenta : director.getComponente()) {
            if (componenta instanceof Fisier) {
                numarFisiere++;
            } else if (componenta instanceof Director) {
                numarFisiere += numaraFisiere((Director) componenta);
            }
        }
        return numarFisiere;
    }
    
    /**
     * Metoda calculeaza numarul de fisiere si dimensiunea in KB a directorului primit ca parametru
     * @param directoare - lista de commponente (directoare si fisiere)
     * @param idDirector - identificatorul directorului pentru care vrem sa calculam statistica
     * @return - returneaza rezultatul sub forma unui StringBuilder
     */
    public static String calculareStatisticiDirector(List<Componenta> directoare, int idDirector) {
        int dimensiuneTotala = 0;
        int numarFisiere = 0;
        String caleDirector = null;
        boolean directorExista = false;
        for (Componenta componentaDirector : directoare) {
            if (componentaDirector instanceof Director) {
                Director director = (Director) componentaDirector;
                if (director.getId() == idDirector) {
                    directorExista = true;
                    caleDirector = director.getCale();
                    dimensiuneTotala = calculeazaDimensiuneTotala(director);
                    numarFisiere = numaraFisiere(director);
                    break;
                }
            }
        }
        if (!directorExista) {
            return "Id-ul introdus nu există in lista de directoare.";
        }
        if (numarFisiere == 0) {
            return "Directorul pentru care doriti statistici nu conține componente.";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Statistici pentru directorul cu calea ").append(caleDirector).append(":\n");
        stringBuilder.append("\tDimensiune totala: ").append(dimensiuneTotala).append(" kilobytes\n");
        stringBuilder.append("\tNumarul de fisiere: ").append(numarFisiere).append("\n");
        return stringBuilder.toString();
    }

	/**
	 * Metoda calculeaza dimensiunea totala a fisierelor pentru fiecare tip de extensie
	 * @param directoare - lista de componente (directoare si fisiere) care exista
	 * @return - returneaza rezultatul sub forma unui StringBuilder
	 */
	public static String calculareDimensiuneFisierePeExtensii(List<Componenta> directoare) {
	    int[] dimensiuniPeExtensii = new int[TipFisier.values().length];
	    for (int i = 0; i < dimensiuniPeExtensii.length; i++) {
	        dimensiuniPeExtensii[i] = 0;
	    }
	    for (Componenta componentaDirector : directoare) {
	        if (componentaDirector instanceof Director) {
	            Director director = (Director) componentaDirector;
	            List<Componenta> fisiere = director.getComponente();
	            for (Componenta componentaFisier : fisiere) {
	                if (componentaFisier instanceof Fisier) {
	                    Fisier fisier = (Fisier) componentaFisier;
	                    TipFisier extensie = fisier.getExtensie();
	                    int dimensiuneFisier = fisier.getDimensiune();
	                    dimensiuniPeExtensii[extensie.ordinal()] += dimensiuneFisier;
	                }
	            }
	        }
	    }
	    StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < dimensiuniPeExtensii.length; i++) {
            stringBuilder.append("Fisierele cu extensia ").append(TipFisier.values()[i])
                    .append(" au dimensiunea totala de ").append(dimensiuniPeExtensii[i]).append(" kilobytes\n");
        }
        return stringBuilder.toString();
	}
	
	/**
	 * Metoda determina numarul de fisiere salvate in fiecare luna intr-un interval de ani specificat
	 * @param directoare - lista ce contine datele despre directoare si fisiere
	 * @param anStart - anul de inceput pentru calcularea statisticii
	 * @param anSfarsit - anul de sfarsit pentru calcularea statisticii
	 * @return - returneaza matricea finala care contine numarul de fisiere salvate pe fiecare luna
	 */
	public static int[][] calculareNumarFisierePeAnSiLuna(List<Componenta> directoare, int anStart, int anSfarsit) {
	    int[][] numarFisierePeAnSiLuna = new int[anSfarsit - anStart + 1][12]; //formula data pentru prima dimensiune a matricei
	    																	   //reprezinta nr. de ani din intervalul specificat
	    for (Componenta componentaDirector : directoare) {
	        if (componentaDirector instanceof Director) {
	            Director director = (Director) componentaDirector;
	            List<Componenta> fisiere = director.getComponente();
	            for (Componenta componentaFisier : fisiere) {
	                if (componentaFisier instanceof Fisier) {
	                    Fisier fisier = (Fisier) componentaFisier;
	                    int an = fisier.getDataCrearii().getYear();
	                    int luna = fisier.getDataCrearii().getMonthValue() - 1; 
	                    
	                    if (an >= anStart && an <= anSfarsit) //verific daca anul fisierului este in intervalul dat
	                    {
	                        numarFisierePeAnSiLuna[an - anStart][luna]++;
	                    }
	                }
	            }
	        }
	    }
	    return numarFisierePeAnSiLuna;
	}
	
	/**
	 * Metoda furnizeaza un Set ce contine toate fisierele cu o anumita extensie
	 * @param componente - lista de componente (directoare si fisiere) care exista
	 * @param extensieCautata - extensia dupa care se construieste setul
	 * @return - returneaza Set-ul cu componentele de tip Fisier
	 */
	public static Set<Fisier> gasesteFisiereCuExtensie(List<Componenta> directoare, TipFisier extensieCautata) {
        Set<Fisier> fisiereGasite = new HashSet<>();
        for (Componenta componentaDirector : directoare) {
        	if (componentaDirector instanceof Director) {
        		Director director = (Director)componentaDirector;
        		List<Componenta> fisiere = director.getComponente();
        		for(Componenta componentaFisier : fisiere) {
        			if (componentaFisier instanceof Fisier) {
                        Fisier fisier = (Fisier) componentaFisier;
                        if (fisier.getExtensie() == extensieCautata) {
                            fisiereGasite.add(fisier);
                        }
                    }
        		}
        	}
        }
        return fisiereGasite;
    }
	
	/**
	 * Metoda determina fisierul cu cea mai mica dimensiune
	 * @param componente - lista de componente (directoare si fisiere) care exista
	 * @return - returneaza un Map care contine fisierul cu dimensiunea minima pentru fiecare extensie
	 */
	public static Map<TipFisier, Fisier> gasesteFisierMinimPerExtensie(List<Componenta> componente) {
        Map<TipFisier, Fisier> fisierMinim = new HashMap<>();
        for (Componenta componentaDirector : componente) {
        	if(componentaDirector instanceof Director) {
        		Director director = (Director) componentaDirector;
        		List<Componenta> fisiere = director.getComponente();
        		for(Componenta componentaFisier : fisiere) {
        			if(componentaFisier instanceof Fisier) {
        				Fisier fisier = (Fisier) componentaFisier;
        				TipFisier extensie = fisier.getExtensie();
        				if (!fisierMinim.containsKey(extensie) || fisier.getDimensiune() < fisierMinim.get(extensie).getDimensiune()) {
        					fisierMinim.put(extensie, fisier);
        				}
        			}
        		}
        	}
        }
        return fisierMinim;
    }
	
	/**
	 * Metoda determina fisierul cu cea mai mica dimensiune
	 * @param componente - lista de componente (directoare si fisiere) care exista
	 * @return - returneaza un Map care contine fisierul cu dimensiunea maxima pentru fiecare extensie
	 */
	public static Map<TipFisier, Fisier> gasesteFisierMaximPerExtensie(List<Componenta> componente) {
        Map<TipFisier, Fisier> fisierMaxim = new HashMap<>();
        for (Componenta componentaDirector : componente) {
        	if(componentaDirector instanceof Director) {
        		Director director = (Director) componentaDirector;
        		List<Componenta> fisiere = director.getComponente();
        		for(Componenta componentaFisier : fisiere) {
        			if(componentaFisier instanceof Fisier) {
        				Fisier fisier = (Fisier) componentaFisier;
        				TipFisier extensie = fisier.getExtensie();
        				if (!fisierMaxim.containsKey(extensie) || fisier.getDimensiune() > fisierMaxim.get(extensie).getDimensiune()) {
        					fisierMaxim.put(extensie, fisier);
        				}
        			}
        		}
        	}
        }
        return fisierMaxim;
    }
}