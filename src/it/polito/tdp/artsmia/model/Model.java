package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph <ArtObject, DefaultWeightedEdge> graph;
	private List <ArtObject> artObjects;
	
	private ArtObjectIdMap map;
	
	private ArtsmiaDAO adao;
	
	private List <ArtObject> soluzione;
	private int pesoMax;
	
	public Model () {
		this.adao = new ArtsmiaDAO();
		this.map = new ArtObjectIdMap();
		this.artObjects = adao.listObjects(map);
	}
	
	public void creaGrafo () {
		
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.graph, this.artObjects);
		
		// aggiungo gli archi e il loro peso
		List<Arco> archi = adao.getAllEdgesWithWeight(map);
		
		for (Arco a : archi)
			Graphs.addEdgeWithVertices(this.graph, a.getPartenza(), a.getArrivo(), a.getPeso());
	}

	public int getVertici() {
		return this.graph.vertexSet().size();
	}

	public int getArchi() {
		return this.graph.edgeSet().size();
	}

	public boolean isValid(int objectId) {
		if (map.get(objectId) != null)
			return true;
		return false;
	}

	
	// La componente connessa in un grafo non orientato si trova effettuando una visita
	// tutti i vertici che vengono raggiunti dalla visita fanno parte della componente connessa
	public List <ArtObject> getComponenteConnessa(int objectId) {
		
		// trovare il vertice di partenza per la visita
		ArtObject partenza = map.get(objectId);
		
		// effettuare la visita 
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> dfi = new DepthFirstIterator<ArtObject, DefaultWeightedEdge>(this.graph, partenza);
		
		// salvare ogni vertici raggiunto (visitato) nella lista
		// La lista può essere un SET per questioni di efficienza
		// Se voglio tener traccia dell'ordine in cui sono stati visitati occorre usare LIST
		List <ArtObject> componenteConnessa = new ArrayList<>();
		while (dfi.hasNext())
			componenteConnessa.add(dfi.next());
		
		return componenteConnessa;
	}

	
	// 2° PUNTO
	
	
	public List<ArtObject> getCamminoMassimo(int objectId, int lun) {
		this.soluzione = new ArrayList<>();
		
		List <ArtObject> parziale = new ArrayList<>();
		parziale.add(map.get(objectId));
		
		this.pesoMax = 0;
		
		this.ricorsiva (parziale, lun, 1);
		
		return soluzione;
	}

	private void ricorsiva(List<ArtObject> parziale, int lun, int livello) {
		
		// condizione di terminazione
		if (livello == lun) {
			if (this.calcoloPeso(parziale) > pesoMax) {
				this.pesoMax = this.calcoloPeso(parziale);
				this.soluzione = new ArrayList<>(parziale);
			}
			
			//Siccome è un calcolo di massimo, 3 scelte possibili:
			// 1)termino questa ricorsione, ma continuo a fare le altre
			// 2)termino questa ricorsione ed esco
			// 3) termino questa ricorsione, ma proseguo con lunghezze maggiori
			
			return;
		}
		
		
		// trova i vertici adiacenti all'ultimo di parziale
		ArtObject ultimo = parziale.get(parziale.size() - 1);
		
		// restituisce i vertici vicini
		List <ArtObject> adiacenti = Graphs.neighborListOf(this.graph, ultimo);
		
		for (ArtObject a : adiacenti)
			if (!parziale.contains(a) && 
					a.getClassification().equals(ultimo.getClassification())) {
				
				parziale.add(a);
				this.ricorsiva(parziale, lun, livello + 1);
				parziale.remove(parziale.size() - 1);
			}		
		
	}

	private int calcoloPeso(List<ArtObject> parziale) {
		int pesoTot = 0;
		for (int i = 0; i < parziale.size() - 1; i++) {
		
			DefaultWeightedEdge arco = this.graph.getEdge(parziale.get(i), parziale.get(i+1));
			int peso = (int) this.graph.getEdgeWeight(arco);
			pesoTot += peso;
		}
		
		return pesoTot;
	}

	public int getPesoMax() {
		return pesoMax;
	}

	
}
