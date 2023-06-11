package it.polito.tdp.gosales.model;

import java.time.LocalDate;
import java.util.PriorityQueue;

import it.polito.tdp.gosales.dao.GOsalesDAO;
import it.polito.tdp.gosales.model.Evento.EventType;

	public class Simulatore {

		//parametri di ingresso
		private int anno;
		private int N;
		
		//parametri
		int avgD;
		int avgQ;
		private GOsalesDAO dao;
		double costoUnitario;
		double prezzoUnitario;
		double threshold;
		
		//variabili di uscita
		private int clientiTot;
		private int clientiSoddisfatti;
		private double costo;
		private double ricavo;
		
		//stato del mondo
		private int Q;
		
		//coda degli eventi
		PriorityQueue<Evento> queue;

		public Simulatore(Retailers r, int anno, Products p, int n, int q, int nConnessi) {
			super();
			
			this.dao = new GOsalesDAO();
			this.anno = anno;
			N = n;
			Q = q;
			
			avgD = this.dao.getAvgD(r, p, anno);
			avgQ = this.dao.getAvgQ(r, p, anno);
			this.costoUnitario = p.isUnit_cost();
			this.prezzoUnitario = p.isUnit_price();
			
			this.threshold = Math.min(0.2 + 0.01*nConnessi, 0.5);
		}
		
		
		
		/**
		 * metodo che popola la coda degli eventi
		 */
		public void initialize() {
			this.queue = new PriorityQueue<Evento>();
			
			//eventi rifornimento
			for (int i = 1; i<=12; i++) {
				this.queue.add(new Evento(EventType.RIFORNIMENTO, LocalDate.of(anno, i, 1)));
			}
			
			//eventi vendita
			LocalDate data = LocalDate.of(anno, 1, 15);
			while(data.isBefore(LocalDate.of(anno, 12, 31))) {
				this.queue.add(new Evento(EventType.VENDITA, data));
				data = data.plusDays(avgD);
			}
		}
		
		public void run() {
			this.clientiSoddisfatti = 0;
			this.clientiTot = 0;
			this.costo = 0;
			this.ricavo = 0;
			
			while(!queue.isEmpty()) {
				Evento e = queue.poll();
				
				switch(e.getType()) {
				case RIFORNIMENTO:
					double prob = Math.random();
					if(prob <= this.threshold) {
						Q += 0.8*N;
						this.costo += this.costoUnitario*0.8*N;
					}else {
						Q +=N;
						this.costo += this.costoUnitario*N;
					}
					break;
				case VENDITA:
					this.clientiTot++;
					if (Q >= 0.9*avgQ) {
						this.clientiSoddisfatti++;
					}
					if(Q >=avgQ) {
						this.ricavo += this.prezzoUnitario*avgQ;
						Q-=avgQ;
					}else {
						Q = 0;
						this.ricavo += this.prezzoUnitario*Q;
					}
					break;
				default:
					break;
				}
			}
		}
		
		public SimResult getRisultato() {
			double ricavo = this.ricavo;
			double costo = this.costo;
			double percentClientiSodd = (double)(this.clientiSoddisfatti*100)/this.clientiTot;
			SimResult res = new SimResult(costo, ricavo, percentClientiSodd);
			return res;
		}
		
		
	
	
}
