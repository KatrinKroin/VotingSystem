package com.example;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Result implements Serializable{
	public static final long serialVersionUID = 1L;

	public String Name;
	public String Amount;

	public Result( String Name, String Amount ) {	
		this.Name = Name;
		this.Amount = Amount;
	}
	
	
	public String toString() {
		JSONObject temp = new JSONObject();
		try {
			temp.put("Name", Name);
			temp.put("Amount", Amount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temp.toString();
	}
	

}
