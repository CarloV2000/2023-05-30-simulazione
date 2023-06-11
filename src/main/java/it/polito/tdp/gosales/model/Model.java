package it.polito.tdp.gosales.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.gosales.dao.GOsalesDAO;

public class Model {
	
	private List<String>allCountries;
	private Graph<Retailers, DefaultWeightedEdge>grafo;
	private List<Retailers>allRetailers;
	private GOsalesDAO dao;
	
	public Model() {
		this.dao = new GOsalesDAO();
		this.allCountries = new ArrayList<>(dao.getAllCountries());
		this.allRetailers = new ArrayList<>();
	}

	public String creaGrafo(int anno, String nazione, int minimoProdottiInComune) {
		this.grafo = new SimpleWeightedGraph<Retailers, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.allRetailers = dao.getAllRetailers(nazione);
		Graphs.addAllVertices(grafo, this.allRetailers);
		
		for(Retailers x : this.allRetailers) {
			for(Retailers y : this.allRetailers) {
				if(!x.equals(y)) {
					int peso = dao.getPeso(x, y, anno);
					if(peso >= minimoProdottiInComune) {
						grafo.addEdge(x, y);
						grafo.setEdgeWeight(x, y, peso);
					}
				}
			}	
		}
		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.";
	}
	
	public List<CoppiaA> listArchi(){
		List<CoppiaA>archi = new ArrayList<>();
		for(DefaultWeightedEdge x : grafo.edgeSet()) {
			Retailers r1 = grafo.getEdgeSource(x);
			Retailers r2 = grafo.getEdgeTarget(x);
			int peso = (int)grafo.getEdgeWeight(x);
			
			CoppiaA arco = new CoppiaA(r1, r2, peso);
			archi.add(arco);
		}
		Collections.sort(archi);
		return archi;
	}
	
	public int getNumberOfConnectedComponents(Retailers r){
		int nComponentiConnesse = 0;
		
		ConnectivityInspector<Retailers, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(this.grafo);
                  Set<Retailers> connectedComponents = inspector.connectedSetOf(r);
                  for (Retailers component : connectedComponents) {
                 	    nComponentiConnesse++;
	         }
        
        return nComponentiConnesse;
    }
	
	public int getWeightOfConnectedComponents(Retailers r) {
		// Trova componente connessa  (Connectivity Inspector)
		ConnectivityInspector<Retailers, DefaultWeightedEdge> inspector =
				new ConnectivityInspector<Retailers, DefaultWeightedEdge>(this.grafo);
		Set<Retailers> connessi = inspector.connectedSetOf(r);
		
		//calcola il peso totale degli archi nella componente connessa
		// Possiamo prendere gli archi del grafo uno a uno, e verificare se i suoi 
		// vertici sono nella componente connessa. In caso affermativo, possiamo aggiungere
		//il suo peso al totale.
		int peso = 0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (connessi.contains(this.grafo.getEdgeSource(e)) &&
					connessi.contains(this.grafo.getEdgeTarget(e))) {
				peso += (int)this.grafo.getEdgeWeight(e);
			}
		}
		return peso;
	}
	
	public SimResult simula(Retailers r, Products p, int Q, int N, int anno, int nConnessi) {
		Simulatore sim = new Simulatore(r, anno, p, N, Q, nConnessi );
		sim.initialize();
		sim.run();
		SimResult result = sim.getRisultato();
		return result;
	}
	
	
	
	public List<String> getAllCountries() {
		return allCountries;
	}

	public void setAllCountries(List<String> allCountries) {
		this.allCountries = allCountries;
	}

	public List<Retailers> getAllRetailers() {
		return allRetailers;
	}

	public void setAllRetailers(List<Retailers> allRetailers) {
		this.allRetailers = allRetailers;
	}

	public Graph<Retailers, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<Retailers, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	public List<Products>getAllProducts(Retailers r){
		return dao.getAllProducts(r);
	}
	public List<DailySale>getAllVendite(Products p, Retailers r, int anno){
		return dao.getAllVendite(p, r, anno);
	}
	
}
