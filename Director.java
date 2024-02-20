package ro.ase.ebusiness.proiect;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa Director folosita pentru a stoca datele despre un director
 * @author Dragoi Beatrice
 */

public class Director implements Componenta{
	private String caleDirector;
	private int id;
	private List<Componenta> componente;
	
	/**
	 * Constructorul clasei Director
	 * @param caleDirector - calea unde se gaseste directorul
	 * @param id - identificatroul unic asociat directorului
	 * @throws ExceptieCaleInvalida
	 */
	public Director(String caleDirector, int id) throws ExceptieCaleInvalida {
		String regex = "^[CDEF]:\\\\[a-zA-Z0-9 _\\-]+";
        if (!caleDirector.matches(regex)) {
            throw new ExceptieCaleInvalida("Calea nu respecta formatul specificat.");
        }
        for (int i = 0; i < caleDirector.length() - 1; i++) {
            char caracterCurent = caleDirector.charAt(i);
            char caracterUrmator = caleDirector.charAt(i + 1);
            if (caracterCurent == '\\' && caracterUrmator == '\\') {
                throw new ExceptieCaleInvalida("Calea conține mai multe caractere - \\.");
            }
        }
        int numarSemne = 0;
        for (char ch : caleDirector.toCharArray()) {
            if (ch == ':') {
                numarSemne++;
            }
            if (numarSemne > 1) {
                throw new ExceptieCaleInvalida("Calea conține mai mult de un semn ':'.");
            }
        }
        this.caleDirector = caleDirector;
        this.id = id;
        this.componente = new ArrayList<>();
    }
	
	public String getCale() {
		return caleDirector;
	}

	/**
	 * Setter prin care realizez validarea pentru calea fisierului. 
	 * Aceasta poate contine: litere mari si mici, cifre, cratima, punct(.), spatiu si doua puncte(:)
	 * @param cale - locul unde este stocat fisierul
	 * @throws ExceptieCaleInvalida 
	 */
	public void setCale(String caleDirector) throws ExceptieCaleInvalida {
		String regex = "^[CDEF]:\\\\[a-zA-Z0-9 _\\-]+";
        if (!caleDirector.matches(regex)) {
            throw new ExceptieCaleInvalida("Calea nu respecta formatul specificat.");
        }
        for (int i = 0; i < caleDirector.length() - 1; i++) {
            char caracterCurent = caleDirector.charAt(i);
            char caracterUrmator = caleDirector.charAt(i + 1);
            if (caracterCurent == '\\' && caracterUrmator == '\\') {
                throw new ExceptieCaleInvalida("Calea conține mai multe caractere - \\.");
            }
        }
        int numarSemne = 0;
        for (char ch : caleDirector.toCharArray()) {
            if (ch == ':') {
                numarSemne++;
            }
            if (numarSemne > 1) {
                throw new ExceptieCaleInvalida("Calea conține mai mult de un semn ':'.");
            }
        }
        this.caleDirector = caleDirector;
    }
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<Componenta> getComponente() {
		return componente;
	}

	/**
	 * Metoda mostenita din interfata implementata folosita pentru 
	 * adaugarea unei componente in lista
	 */
	@Override
	public void adaugaEntitate(Componenta componenta) {
		this.componente.add(componenta);
	}

	/**
	 * Metoda mostenita din interfata implementata folosita pentru 
	 * stergerea unei componente din lista
	 */
	@Override
	public void stergeEntitate(Componenta componenta) {
		this.componente.remove(componenta);
	}

	/**
	 * Metoda mostenita din interfata folosita pentru a oferi informatii
	 * despre un director
	 */
	@Override
	public String getNume() {
		return "Directorul cu calea: " + caleDirector + " si id-ul " + id;
	}

	/**
	 * Metoda mostenita din interfata folosita pentru afisarea structurii
	 * ierarhice de directoare si fisiere
	 */
	@Override
	public void afisareDetalii(int nivel) {
		StringBuilder indentare = new StringBuilder();
		for (int i = 0; i < nivel; i++) {
			indentare.append("\t");
		}
		System.out.println(indentare.toString() + getNume() + " contine componentele: ");
		for (Componenta componenta : componente) {
			componenta.afisareDetalii(nivel+1);
		}
	}
}
