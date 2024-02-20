package ro.ase.ebusiness.proiect;

/**
 * Interfata ce defineste antetul metodelor ce urmeaza a fi implementate in clasele ierarhiei
 * @author Dragoi Beatrice
 */

public interface Componenta {
	/**
	 * Metoda pentru adaugarea unei entitati
	 * @param componenta - entitatea care este adaugata
	 */
	public void adaugaEntitate(Componenta componenta);
	
	/**
	 * Metoda pentru stergerea unei entitati
	 * @param componenta - entitatea care urmeaza a fi stearsa
	 */
	public void stergeEntitate(Componenta componenta);
	
	/**
	 * Metoda folosita pentru afisarea ierarhiei de elemente
	 * @param nivel - nivelul de la care incepem afisarea
	 */
	public void afisareDetalii(int nivel);
	
	/**
	 * Metoda folosita pentru afisarea unor informatii despre elemente
	 * @return  - returneaza un String
	 */
	public String getNume();
}
