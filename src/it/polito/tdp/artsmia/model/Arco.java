package it.polito.tdp.artsmia.model;

public class Arco {
	
	private ArtObject partenza;
	private ArtObject arrivo;
	private int peso;
	
	public Arco(ArtObject partenza, ArtObject arrivo, int peso) {
		super();
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.peso = peso;
	}

	public ArtObject getPartenza() {
		return partenza;
	}

	public void setPartenza(ArtObject partenza) {
		this.partenza = partenza;
	}

	public ArtObject getArrivo() {
		return arrivo;
	}

	public void setArrivo(ArtObject arrivo) {
		this.arrivo = arrivo;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
	

}
