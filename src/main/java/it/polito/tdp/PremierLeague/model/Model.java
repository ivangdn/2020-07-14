package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private Graph<Team, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer, Team> idMap;
	
	private double avgReporter;
	private int matchCritici;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<>();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		dao.listAllTeams(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(Team t1 : grafo.vertexSet()) {
			for(Team t2 : grafo.vertexSet()) {
				if(!t1.equals(t2)) {
					int diffPunti = dao.calcolaPunti(t1) - dao.calcolaPunti(t2);
					if(diffPunti!=0) {
						if(diffPunti>0)
							Graphs.addEdge(grafo, t1, t2, diffPunti);
						else
							Graphs.addEdge(grafo, t2, t1, diffPunti*-1);
					}
				}
			}
		}
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Team> getVertici() {
		return new ArrayList<Team>(this.idMap.values());
	}
	
	public List<Team> squadreMigliori(Team s) {
		List<Team> squadreMigliori = Graphs.predecessorListOf(grafo, s);
		Collections.sort(squadreMigliori, new Comparator<Team>() {

			@Override
			public int compare(Team t1, Team t2) {
				return t1.getPunti() - t2.getPunti();
			}
			
		});
		return squadreMigliori;
	}
	
	public List<Team> squadrePeggiori(Team s) {
		List<Team> squadrePeggiori = Graphs.successorListOf(grafo, s);
		Collections.sort(squadrePeggiori, new Comparator<Team>() {

			@Override
			public int compare(Team t1, Team t2) {
				return t2.getPunti() - t1.getPunti();
			}
			
		});
		return squadrePeggiori;
	}
	
	/*
	public String classificaSquadra(Team s) {
		String result = "";
		List<Team> squadreMigliori = new ArrayList<>();
		List<Team> squadrePeggiori = new ArrayList<>();
		for(Team t : Graphs.neighborListOf(grafo, s)) {
			if(grafo.getEdge(s, t)!=null) {
				squadrePeggiori.add(t);
			} else if(grafo.getEdge(t, s)!=null) {
				squadreMigliori.add(t);
			}
		}
		
		result += "SQUADRE MIGLIORI:\n";
		for(Team migliore : squadreMigliori) {
			result += migliore+"("+grafo.getEdgeWeight(grafo.getEdge(migliore, s))+")\n";
		}
		
		result += "\nSQUADRE PEGGIORI:\n";
		for(Team peggiore : squadrePeggiori) {
			result += peggiore+"("+grafo.getEdgeWeight(grafo.getEdge(s, peggiore))+")\n";
		}
		
		return result;
	}
	*/
	
	public void simula(int N, int X) {
		Simulatore sim = new Simulatore(grafo);
		sim.init(N, X, dao.listAllMatches(), idMap);
		sim.run();
		this.avgReporter = sim.calcolaAvgReporter();
		this.matchCritici = sim.getMatchCritici();
	}
	
	public double getAvgReporter() {
		return this.avgReporter;
	}
	
	public int getMatchCritici() {
		return this.matchCritici;
	}
	
	
}
