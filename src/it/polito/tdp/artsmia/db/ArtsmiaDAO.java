package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.ArtObjectIdMap;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects(ArtObjectIdMap map) {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(map.get(artObj));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Arco> getAllEdgesWithWeight(ArtObjectIdMap map) {
		
		String sql = "SELECT o1.object_id AS partenza, o2.object_id AS arrivo, COUNT(*) AS peso " + 
					 "FROM exhibition_objects AS o1, exhibition_objects AS o2 " + 
					 "WHERE o1.exhibition_id = o2.exhibition_id " + 
					 "		AND o1.object_id > o2.object_id " + 
					 "GROUP BY partenza, arrivo";
		
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				
				ArtObject partenza = map.get(res.getInt("partenza"));
				ArtObject arrivo = map.get(res.getInt("arrivo"));
				
				// il controllo è d'obbligo perchè chiedo 
				// all'Identity Map se l'oggetto è tra i vertici
				if (partenza != null && arrivo != null)
					result.add(new Arco (partenza, arrivo, res.getInt("peso")));
			
			}
			
			conn.close();
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
}
