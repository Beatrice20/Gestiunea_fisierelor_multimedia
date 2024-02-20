package ro.ase.ebusiness.proiect;

/**
 * Clasa ExceptieCaleInvalida este o subclasa a clasei Exception si este utilizata
 * pentru a gestiona exceptiile legate de caile invalide introduse in aplicatie.
 * Aceasta exceptie este aruncata atunci când o cale specificata nu respecta un
 * format de validare specificat.
 * @author Dragoi Beatrice
 */

public class ExceptieCaleInvalida extends Exception{
	
	/**
	 * Constructorul clasei ExceptieCaleInvalida
	 * @param mesaj - mesajul de eroare asociat cu aceasta exceptie
	 */
	public ExceptieCaleInvalida(String mesaj) {
		super(mesaj);
	}
}
