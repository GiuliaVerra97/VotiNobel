package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.dao.EsameDAO;



//approccio 1: due soluzioni possibili (inserisco o no il corso nelle soluzioni? Richiamo 2 volte la funzione ricorsiva)

public class Model {
	
	private List<Esame> esami;		//esami letti dal db
	private List<Esame> best; 		//per la gestione della ricorsione, lista degli esami che mi permettono di ottenere i crditi indicati e la più alta media possibile
	private double media_best;

	
	public Model() {
		EsameDAO dao=new EsameDAO();
		esami=dao.getTuttiEsami();
		
	}
	
	
	
	/**
	 * trova combinazione di corsi avente la somma di crediti come quella passata come parametro e che abbia la media dei voti massima
	 * Richiama la funzione ricorsiva.
	 * @param numeroCrediti inseriti dall'utente
	 * @return elenco ottimale dei corsi, oppure null se non vi è nessuna combinazione di corsi che arriva al numero di crediti richiesto
	 */
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		//inizializzo
		best=null;		//oppure posso mettere new ArrayList<Esame>() per indicare che la lista ottimale degli esami è vuota
		media_best=0; 
		Set<Esame> parziale=new HashSet<Esame>();		//set non contiene duplicati al suo interno, è un controllo in più
		
		cerca(parziale, 0, numeroCrediti);
		
		return best;
	}
	
	
	
	
	/**Funzione ricorsiva
	 * @param parziale lista possibile di esami per raggiungere il numero di crediti desiderato e la media massima
	 * @param L livello
	 * @param m numero di crediti da raggiungere
	 */
	public void cerca(Set<Esame> parziale, int L, int m) {
		
		//casi terminale
		int crediti=sommaCrediti(parziale);
		if(crediti>m) {
			return;
		}
		
		if(crediti==m) {
			double media=calcolaMedia(parziale);
			if(media>media_best) {
				best=new ArrayList<Esame>(parziale);		//clono
				media_best=media;
				return;		//ritorno, esco perchè ho trovato la soluzione migliore e non posso aggiungere altri corsi per migliorarla perchè dovrei aumentere il num di crediti e non posso
			}else {
				return;
			}
		}
		
		if(L==esami.size()) {		//se crediti<m e non ci sono più corsi disponibili da aggiungere, lo metto dopo crediti==m così che se sono all'ultimo esame, ma crediti=m allora elaboro
			return;
		}
			
		//casi normali, generiamo  i 2 sotto problemi-> ci si chiede se si vuole aggiungere l'esame[L] alla lista parziale
		
		//1 provo a non aggiungere l'esame L, richiamando la ricorsiva dico che l'esame L non è stato messo nel parziale e ci si chiede se mettere l'esame L+1
		//in questo caso non c'è nessun motivo per  fare un backtraking perchè non l'ho aggiunto
		cerca(parziale, L+1, m);
		
		//2 provo ad aggiungerlo e quindi poi faccio anche il backtraking per toglierlo
		parziale.add(esami.get(L));		//prendo l'esame in posizione L e lo metto nell'insieme delle soluzioni parziali
		cerca(parziale, L+1, m);
		parziale.remove(esami.get(L));
		
	}

	
	
	
	
	
	private int sommaCrediti(Set<Esame>parziale) {
		int somma=0;
		for(Esame e: parziale) {
			somma=somma+e.getCrediti();
		}
		return somma;
	}
	
	
	
	
	public double calcolaMedia(Set<Esame> parziale) {
		double media=0;
		int crediti=0;
		for(Esame e: parziale) {
			media=media+e.getVoto()*e.getCrediti();
			crediti=crediti+e.getCrediti();
		}
		return media/crediti;
	}
	
}
