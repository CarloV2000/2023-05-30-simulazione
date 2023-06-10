package it.polito.tdp.gosales.model;

public class CoppiaA implements Comparable<CoppiaA>{

	private Retailers r1;
	private Retailers r2;
	private int peso;
	public CoppiaA(Retailers r1, Retailers r2, int peso) {
		super();
		this.r1 = r1;
		this.r2 = r2;
		this.peso = peso;
	}
	public Retailers getR1() {
		return r1;
	}
	public void setR1(Retailers r1) {
		this.r1 = r1;
	}
	public Retailers getR2() {
		return r2;
	}
	public void setR2(Retailers r2) {
		this.r2 = r2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(CoppiaA o) {
		return this.peso-o.peso;
	}
	
}
