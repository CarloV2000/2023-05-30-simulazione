package it.polito.tdp.gosales.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
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
	
	
	/*--------------------------ALTRI METODI UTILI------------------------------------------------------------*/
	
	/**
	 * determina il percorso minimo tra le due fermate con grafo pesato
	 * @param partenza indica la fermata di partenza
	 * @param arrivo indica la fermata di arrivo
	 * @return lista di fermate rappresentanti il percorso piu breve
	 */
	public List<Retailers>percorso(Retailers partenza, Retailers arrivo){
		
		DijkstraShortestPath<Retailers, DefaultWeightedEdge> sp = new DijkstraShortestPath<>(this.grafo);
		GraphPath<Retailers, DefaultWeightedEdge> gp = sp.getPath(partenza, arrivo);
		List<Retailers>percorso = new ArrayList<>(gp.getVertexList());
		
		return percorso;
	}
	
	/**
	 * metodo che crea grafo e in caso di archi ripetuti incrementa il peso all'arco gia esistente
	 * @param nMinCompagnieAeree
	 */
		/*public void creaGrafo(int nMinCompagnieAeree) {
			//grafo(gia creato nel costruttore)
			//idMap
			for(Airport x : this.allAeroporti) {
				this.aeroportiIdMap.put(x.getId(), x);
			}
			
			//vertici (gia compresi i vincoli sul numero di compagnie aeree)
			Graphs.addAllVertices(grafo, this.dao.getVertici(nMinCompagnieAeree, aeroportiIdMap));
			
			//archi
			List<CoppiaA>edges = dao.getArchi(aeroportiIdMap);
			for(CoppiaA x : edges) {
				Airport origin = x.getPartenza();
				Airport destination = x.getArrivo();
				int peso = x.getN();
			//metto controllo del tipo: se esistono i vertici: se l'arco esiste gia ci incremento il peso, altrimenti lo creo nuovo
				if(grafo.vertexSet().contains(origin) && grafo.vertexSet().contains(destination)) {
					DefaultWeightedEdge edge = this.grafo.getEdge(origin, destination);
					if(edge!=null) {
						double weight = this.grafo.getEdgeWeight(edge);
						weight += peso;
						this.grafo.setEdgeWeight(origin, destination, weight);
					} else {
						this.grafo.addEdge(origin, destination);
						this.grafo.setEdgeWeight(origin, destination, peso);
					}
				}
				
				
			}*/

	
	
	/**
	 * posto bilancio = (somma pesi archi entranti) - (somma peso archi uscenti) in caso di grafo orientato
	 * (per grafi non orientati: vedi this.getWeightOfConnectedComponents(a)--->(sopra))
	 * @param a è il vertice di cui calcolare il bilancio
	 * @return un int rappresentante il bilancio
	 */
	public int getBilancio(Retailers a){
		
		int bilancio = 0;
		List<DefaultWeightedEdge>entranti = new ArrayList<>(this.grafo.incomingEdgesOf(a));
		List<DefaultWeightedEdge>uscenti = new ArrayList<>(this.grafo.outgoingEdgesOf(a));
		
		for(DefaultWeightedEdge x : entranti) {
			bilancio += this.grafo.getEdgeWeight(x);
		}
		for(DefaultWeightedEdge x : uscenti) {
			bilancio -= this.grafo.getEdgeWeight(x);
		}
		
		return bilancio;
	}
	
	
	/**
	 * metodo per trovare tutti i vertici successori di un vertice in un grafo orientato ed ordinarli in base al bilancio(calcolato sopra)
	 * @param x è la fermata di cui calcolare i successori
	 * @return una lista di oggetti BilancioFermata(classe appositamente creata per avere due parametri(Fermata e int bilancio per ordinarli facilmente))
	 */
	public List<BilancioRetailer> successoriDiRetailer(Retailers x){
		List<Retailers> successori = new ArrayList<>(Graphs.successorListOf(this.grafo, x));
		List<BilancioRetailer>bilancioSuccessori = new ArrayList<>();
		for(Retailers a: successori) {
			BilancioRetailer bil = new BilancioRetailer(a, getBilancio(a));
			bilancioSuccessori.add(bil);
		}
		Collections.sort(bilancioSuccessori);
		return bilancioSuccessori;
	}
	
	
	/**
	 * metodo che conta i (Country) confinanti dato un (Country)
	 * @param c è la fermata di cui calcolare il numero di confinanti
	 * @param anno è l'anno inserito dall'utente (entro il quale i cambiamenti di confini storici sono validi)
	 * @return un int rappresentante il numero di stati confinanti
	 */
	/*public int contaConfinantiDatoPaese(Fermata c) {
		int n = 0;
		DAO dao = new BordersDAO();
		for(Border b : dao.getCountryPairs(anno, countryIdMap)) {
			if(b.getPaese1().equals(c)) {
				n++;
			}
		}
		return n;
	}*/
	
	
	
	/**
	 * viene visualizzata la lista di tutti i vertici raggiungibili nel grafo non orientato
	a partire da un vertice selezionato, che coincide con la componente connessa del grafo relativa allo stato
	scelto (ho usato il metodo Graphs.neighborListOf(grafo, vertice) per trovare tutti i confinanti).
	 * @param c è la (Fermata) di cui voglio sapere le confinanti
	 * @return una list di (Country):  adiacenti a quello inserito
	 */
	public List<Retailers> trovaConfinanti(Retailers c, int anno) {
		List<Retailers>confinanti = new ArrayList<Retailers>();
		confinanti = Graphs.neighborListOf(grafo, c);		
		return confinanti;
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
