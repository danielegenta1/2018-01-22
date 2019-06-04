package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	// punto 1 b
	public List<Match> allMatchesTeam(Team team)
	{
		String teamName = team.getTeam();
		String sql = "SELECT * " + 
					"FROM Matches " + 
					"WHERE (matches.HomeTeam = ? OR matches.AwayTeam = ?) " + 
					"GROUP BY match_id, matches.Season";
		List<Match> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, teamName);
			st.setString(2, teamName);
			ResultSet res = st.executeQuery();

			while (res.next()) 
			{
				Season season = new Season(res.getInt("season"), null);
				Team hTeam = new Team(res.getString("homeTeam"));
				Team aTeam = new Team(res.getString("awayTeam"));
				result.add(new Match(res.getInt("match_id"), season,
						res.getString("div"), res.getDate("date").toLocalDate(), hTeam, aTeam, res.getInt("fthg"),
						res.getInt("ftag"), res.getString("ftr")));
			}

			conn.close();
			return result;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
