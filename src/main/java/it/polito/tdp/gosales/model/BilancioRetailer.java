package it.polito.tdp.gosales.model;


public class BilancioRetailer implements Comparable<BilancioRetailer>{

	private Retailers r;
	private int bilancio;
	
	public BilancioRetailer(Retailers r, int bilancio) {
		super();
		this.r = r;
		this.bilancio = bilancio;
	}

	public Retailers getR() {
		return r;
	}

	public void setR(Retailers r) {
		this.r = r;
	}

	public int getBilancio() {
		return bilancio;
	}

	public void setBilancio(int bilancio) {
		this.bilancio = bilancio;
	}

	@Override
	public int compareTo(BilancioRetailer o) {
		// TODO Auto-generated method stub
		return this.bilancio-o.bilancio;
	}

	
	
	
	
	
}
