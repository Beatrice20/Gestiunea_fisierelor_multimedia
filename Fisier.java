package ro.ase.ebusiness.proiect;

import java.time.LocalDate;

/**
 * Clasa pentru a stoca datele despre un fisier
 * @author Dragoi Beatrice
 */

/**
 * Enumerare ce retine extensiile pe care un fisier le poate avea
 * @author Dragoi Beatrice
 */
enum TipFisier {MP3, WAV, JPG, PNG, MP4};

public class Fisier implements Componenta{
	private int idDirector;
	private String numeFisier;
	private TipFisier extensie;
	private LocalDate dataCrearii;
	private int dimensiune;

	/**
	 * Constructor cu parametrii
	 * @param cale - locul unde este stocat fisierul
	 * @param numeFisier - numele prin care fisierul poate fi gasit
	 * @param extensie - tipul de fisier
	 * @param dataCrearii - data cand fisierul a fost creat
	 * @param dimensiune - dimensiunea pe care o are fisierul
	 * @throws ExceptieDataInViitor 
	 */
	public Fisier(int id, String numeFisier, TipFisier extensie, LocalDate dataCrearii, int dimensiune) throws ExceptieDataInViitor {
		this.idDirector = id;
		
		//validare nume fisier
		if (numeFisier.length() > 256 || numeFisier.matches(".*[\\\\/:*?\"<>|].*")) {
			throw new IllegalArgumentException("Numele fisierului nu este valid.");
		}
		this.numeFisier = numeFisier;
		this.extensie = extensie;
		//validare data de creare
		LocalDate dataCurenta = LocalDate.now();
        if (dataCrearii.isAfter(dataCurenta)) {
            throw new ExceptieDataInViitor("Data de creare nu poate fi în viitor.");
        }
        this.dataCrearii = dataCrearii;

		//validare dimensiune
		if (dimensiune < 0) {
			throw new IllegalArgumentException("Dimensiunea fisierului nu este valida.");
		}
		this.dimensiune = dimensiune;
	}
	
	public int getIdDirector() {
		return idDirector;
	}

	public void setIdDirector(int idDirector) {
		this.idDirector = idDirector;
	}

	public String getNumeFisier() {
		return numeFisier;
	}

	/**
	 * Setter prin care realizez validarea pentru numele fisierului. 
	 * Aceasta nu poate avea mai mult de 256 caractere si nu poate contine caracterele: \\ / : * ? \" < > |.
	 * @param numeFisier - numele fisierului
	 */
	public void setNumeFisier(String numeFisier) {
		if (numeFisier.length() > 256) {
			throw new IllegalArgumentException("Numele fisierului nu poate avea mai mult de 256 de caractere.");
		}
		if (numeFisier.matches(".*[\\\\/:*?\"<>|].*")) {
			throw new IllegalArgumentException("Numele fisierului nu poate contine caracterele \\ / : * ? \" < > |.");
		}
		this.numeFisier = numeFisier;
	}

	public TipFisier getExtensie() {
		return extensie;
	}

	/**
	 * Setter pentru extensia fisierului.
	 * @param extensie - tipul de fisier
	 */
	public void setExtensie(TipFisier extensie) {
		this.extensie = extensie;
	}

	public LocalDate getDataCrearii() {
		return dataCrearii;
	}
	
	/**
	 * Setter pentru validarea datei la care a fost creat fisierul.
	 * Data crearii nu poate fi in viitor
	 * @param dataCrearii - data cand a fost creat fisierul
	 * @throws ExceptieDataInViitor 
	 */
	public void setDataCrearii(LocalDate dataCrearii) throws ExceptieDataInViitor {
        LocalDate dataCurenta = LocalDate.now();
        if (dataCrearii.isAfter(dataCurenta)) {
            throw new ExceptieDataInViitor("Data de creare nu poate fi în viitor.");
        }
        this.dataCrearii = dataCrearii;
    }

	public int getDimensiune() {
		return dimensiune;
	}

	/**
	 * Setter pentru validarea dimensiunii unui fisier
	 * Aceasta nu poate fi un numar mai mic decat 0
	 * @param dimensiune
	 */
	public void setDimensiune(int dimensiune) {
		if (dimensiune < 0) {
			throw new IllegalArgumentException("Dimensiunea fișierului nu este validă.");
		}
		this.dimensiune = dimensiune;
	}

	/**
	 * Metoda mostenita din interfata pentru care in aceasta clasa nu poate fi data o implementare
	 */
	@Override
	public void adaugaEntitate(Componenta componenta) {
		throw new IllegalArgumentException("Clasa Fisier nu suporta operatia de adaugare!");
	}

	/**
	 * Metoda mostenita din interfata pentru care in aceasta clasa nu poate fi data o implementare
	 */
	@Override
	public void stergeEntitate(Componenta componenta) {
		throw new IllegalArgumentException("Clasa Fisier nu suporta operatia de adaugare!");
	}

	/**
	 * Metoda mostenita din interfata pentru a obtine numele complet al fisierului (inclusiv extensia).
	 */
	@Override
	public String getNume() {
		return this.numeFisier + "." + this.extensie.name().toLowerCase();
	}

	/**
	 * Metoda mostenita din interfata pentru a afisa detaliile despre fisier
	 */
	@Override
	public void afisareDetalii(int nivel) {
		StringBuilder indentare = new StringBuilder();
		for (int i = 0; i < nivel; i++) {
			indentare.append("\t");
		}
		System.out.println(indentare.toString() + "Fisierul " + getNume() + " a fost creat la data: " + getDataCrearii() + " si are dimensiunea de " + getDimensiune() + " KB.");
	}
}
