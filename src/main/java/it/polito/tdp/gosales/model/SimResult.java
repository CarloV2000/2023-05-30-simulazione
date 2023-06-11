package it.polito.tdp.gosales.model;

public class SimResult {
	private Double costoTOT;
	private Double ricavoTOT;
	private Double percentSoddisfatti;
	public SimResult(Double costoTOT, Double ricavoTOT, Double percentSoddisfatti) {
		super();
		this.costoTOT = costoTOT;
		this.ricavoTOT = ricavoTOT;
		this.percentSoddisfatti = percentSoddisfatti;
	}
	public Double getCostoTOT() {
		return costoTOT;
	}
	public void setCostoTOT(Double costoTOT) {
		this.costoTOT = costoTOT;
	}
	public Double getRicavoTOT() {
		return ricavoTOT;
	}
	public void setRicavoTOT(Double ricavoTOT) {
		this.ricavoTOT = ricavoTOT;
	}
	public Double getPercentSoddisfatti() {
		return percentSoddisfatti;
	}
	public void setPercentSoddisfatti(Double percentSoddisfatti) {
		this.percentSoddisfatti = percentSoddisfatti;
	}
	
}
