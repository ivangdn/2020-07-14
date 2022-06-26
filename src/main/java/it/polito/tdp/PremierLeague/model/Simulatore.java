package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulatore {
	
	//dati in ingresso
	private int N;
	private int X;
	private Map<Integer, Team> teamsIdMap;
	
	//dati in uscita
	private int numReporter;
	private int matchCritici;
	
	//modello del mondo
	Map<Team, Integer> mapReporter;
	int countMatch;
	private Graph<Team, DefaultWeightedEdge> grafo;
	
	//coda degli eventi
	private PriorityQueue<Match> queue;
	
	public Simulatore(Graph<Team, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	
	public void init(int N, int X, List<Match> matches, Map<Integer, Team> teamsIdMap) {
		this.N = N;
		this.X = X;
		this.teamsIdMap = teamsIdMap;
		
		this.numReporter = 0;
		this.matchCritici = 0;
		
		this.countMatch = 0;
		this.mapReporter = new HashMap<Team, Integer>();
		for(Team team : grafo.vertexSet()) {
			mapReporter.put(team, this.N);
		}
		
		this.queue = new PriorityQueue<>();
		this.queue.addAll(matches);
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Match m = queue.poll();
			countMatch ++;
			processEvent(m);
		}
	}
	
	public void processEvent(Match m) {
		int numReporterTeam1 = this.mapReporter.get(this.teamsIdMap.get(m.getTeamHomeID()));
		int numReporterTeam2 = this.mapReporter.get(this.teamsIdMap.get(m.getTeamAwayID()));
		this.numReporter += (numReporterTeam1 + numReporterTeam2);
		
		if((numReporterTeam1 + numReporterTeam2)<this.X) {
			this.matchCritici++;
		}
		
		int risultato = m.getResultOfTeamHome();
		switch(risultato) {
		case 1:
			aggiornaReporterSquadraVincente(m.getTeamHomeID());
			aggiornaReporterSquadraPerdente(m.getTeamAwayID());
			break;
		case -1:
			aggiornaReporterSquadraVincente(m.getTeamAwayID());
			aggiornaReporterSquadraPerdente(m.getTeamHomeID());
			break;
		case 0:
			//non cambia niente
			break;
		}
		
	}

	private void aggiornaReporterSquadraVincente(Integer teamID) {
		if(Math.random()<0.5) {
			Team squadraVincente = this.teamsIdMap.get(teamID);
			int numReporter = this.mapReporter.get(squadraVincente);
			
			if(numReporter==0)
				return;
			
			this.mapReporter.put(squadraVincente, numReporter-1);
			
			List<Team> squadreMigliori = squadreMigliori(squadraVincente);
			if(squadreMigliori.isEmpty())
				return;
			
			int indexSquadraMigliore = (int) (Math.random() * squadreMigliori.size());
			Team squadraMigliore = squadreMigliori.get(indexSquadraMigliore);
			int numReporterSquadraMigliore = this.mapReporter.get(squadraMigliore);
			this.mapReporter.put(squadraMigliore, numReporterSquadraMigliore+1);
		}
	}
	
	private void aggiornaReporterSquadraPerdente(Integer teamID) {
		if(Math.random()<0.2) {
			Team squadraPerdente = this.teamsIdMap.get(teamID);
			int numReporter = this.mapReporter.get(squadraPerdente);
			
			if(numReporter==0)
				return;
			
			int randomNum = (int) (Math.random() * numReporter) + 1;
			this.mapReporter.put(squadraPerdente, numReporter-randomNum);
			
			List<Team> squadrePeggiori = squadrePeggiori(squadraPerdente);
			if(squadrePeggiori.isEmpty())
				return;
			
			int indexSquadraPeggiore = (int) (Math.random() * squadrePeggiori.size());
			Team squadraPeggiore = squadrePeggiori.get(indexSquadraPeggiore);
			int numReporterSqaudraPeggiore = this.mapReporter.get(squadraPeggiore);
			this.mapReporter.put(squadraPeggiore, numReporterSqaudraPeggiore+randomNum);
		}
	}
	
	private List<Team> squadreMigliori(Team team) {
		return Graphs.predecessorListOf(grafo, team);
	}
	
	private List<Team> squadrePeggiori(Team team) {
		return Graphs.successorListOf(grafo, team);
	}
	
	public double calcolaAvgReporter() {
		double avg = this.numReporter / this.countMatch;
		return avg;
	}
	
	public int getMatchCritici() {
		return this.matchCritici;
	}

}
