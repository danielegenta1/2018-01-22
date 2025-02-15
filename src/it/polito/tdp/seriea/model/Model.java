package it.polito.tdp.seriea.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model 
{
	private List<Team> teams;
	private SerieADAO dao;
	SeasonHistory sh;
	
	Graph<Integer, DefaultWeightedEdge> grafo;
	
	public Model()
	{
		teams = new LinkedList<Team>();
		dao = new SerieADAO();
	}

	public List<Team> getTeams() 
	{
		return dao.listTeams();
	}

	
	public String handleSquadraSelezionata(Team selectedItem) 
	{
		List<Match>result = dao.allMatchesTeam(selectedItem);
		sh = new SeasonHistory(selectedItem);
		for (Match m : result)
		{
			int punti = calcolaPunti(selectedItem.getTeam(), m);
			if (!sh.storico.containsKey(m.getSeason().getSeason()))
			{
				sh.storico.put(m.getSeason().getSeason(),punti);
			}
			else
			{
				int vecchiPunti = sh.storico.get(m.getSeason().getSeason());
				sh.storico.put(m.getSeason().getSeason(),vecchiPunti+punti);
			}
		}
		
		String res = String.format("Storico per squadra: %s\n", selectedItem.getTeam());
		for (int season : sh.storico.keySet())
			res += (String.format("Annata: %d - punti %d\n", season, sh.storico.get(season)));
		return res;
	}

	private int calcolaPunti(String team, Match m)
	{
		if (m.getFtr().compareTo("D") == 0)
			return 1;
		//home
		if (m.getHomeTeam().getTeam().compareTo(team) == 0)
		{
			if (m.getFtr().compareTo("H") == 0)
				return 3;
			else
				return 0;
		}
		//away
		else
		{
			if (m.getFtr().compareTo("H") == 0)
				return 0;
			else
				return 3;
		}
	}

	public String handleAnnataOro()
	{
		grafo = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, sh.storico.keySet());
		
		for (int season : grafo.vertexSet())
		{
			for (int season2 : grafo.vertexSet())
			{
				if (season != season2)
				{
					if (sh.storico.get(season) < sh.storico.get(season2))
					{
						//arco da 1 a 2
						int peso = sh.storico.get(season2) - sh.storico.get(season);
						
						if (!grafo.containsEdge(season, season2))
							Graphs.addEdge(this.grafo, season, season2, peso);
					}
					else if (sh.storico.get(season) > sh.storico.get(season2))
					{
						//arco da 2 a 1 
						int peso = sh.storico.get(season) - sh.storico.get(season2);
						if (!grafo.containsEdge(season2, season))
							Graphs.addEdge(this.grafo, season2, season, peso);
					}
				}
			}
		}
		
		int oro = 0;
		int seasonOro = -1;
		for (int s : grafo.vertexSet())
		{
			int pesoIn = 0; 
			Set<DefaultWeightedEdge> inDwe = grafo.incomingEdgesOf(s);
			for (DefaultWeightedEdge aus : inDwe)
			{
				pesoIn += grafo.getEdgeWeight(aus);
			}
			int pesoOut = 0; 
			Set<DefaultWeightedEdge> outDwe = grafo.outgoingEdgesOf(s);
			for (DefaultWeightedEdge aus2 : outDwe)
			{
				pesoOut += grafo.getEdgeWeight(aus2);
			}
			
			if ((pesoIn - pesoOut) > oro)
			{
				oro = pesoIn - pesoOut;
				seasonOro = s;
			}
			
		}
		
		return String.format("\n\nStagione oro: %d con differenza pesi: %d\n", seasonOro, oro);
		
	}

	// ricorsione? vado in ordine e considero solamente una squadra
	public void handleCamminoVirtuoso()
	{
		// ottengo primo vertice del grafo
		
		// esiste stagione succesiva?
		
		// aggiungo a sol_best
	}

}
