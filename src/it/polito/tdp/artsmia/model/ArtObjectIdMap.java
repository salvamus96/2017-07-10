package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;

public class ArtObjectIdMap {

	private Map <Integer, ArtObject> map;
	
	public ArtObjectIdMap () {
		
		this.map = new HashMap <> ();
	}
	
	public ArtObject get (int artObjectId) {
		return map.get(artObjectId);
	}

	public ArtObject get (ArtObject artObject) {
		ArtObject old = map.get(artObject.getId());
		if (old == null) {
			map.put(artObject.getId(), artObject);
			return artObject;
		}
		return old;
	}
	
	public void put (ArtObject artObject, int artObjectId) {
		map.put(artObjectId, artObject);
	}
}
