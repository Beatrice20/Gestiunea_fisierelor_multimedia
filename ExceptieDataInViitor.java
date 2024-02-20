package ro.ase.ebusiness.proiect;

/**
 * Clasa ExceptieDataInViitor  este o subclasa a clasei Exception si este utilizata
 * pentru a gestiona exceptiile legate de data crearii unui fisier.
 * Aceasta exceptie este aruncata atunci când o data unui fisier este mai mare decat data curenta.
 * @author Dragoi Beatrice
 */
public class ExceptieDataInViitor extends Exception{
	
	/**
	 * Constructorul clasei ExceptieDataInViitor 
	 * @param mesaj - mesajul de eroare asociat cu aceasta exceptie
	 */
	public ExceptieDataInViitor (String mesaj) {
		super(mesaj);
	}
}
