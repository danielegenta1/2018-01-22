package it.polito.tdp.seriea.model;

import java.util.Map;
import java.util.TreeMap;

public class SeasonHistory
{
	private Team team;
	Map<Integer, Integer> storico; //per ogni stagione voglio i punti totalizzati da un dato team
	
	public SeasonHistory(Team team) 
	{
		super();
		this.team = team;
		this.storico = new TreeMap<Integer, Integer>();
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	public void setStagione(Integer season, Integer points)
	{
		storico.put(season, points);
	}
	
	public void getStagione(Integer season)
	{
		storico.get(season);
	}
	
	

}
