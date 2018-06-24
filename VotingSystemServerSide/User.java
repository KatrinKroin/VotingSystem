package com.example;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable{
	public static final long serialVersionUID = 1L;
	
	public String UserID;
	public String Name;
	public String Email;
	public String Password;
	public boolean Admin;
	
	public User(String UserID, String Name, String Email, String Password, boolean Admin ) {	
		this.UserID = UserID;
		this.Name = Name;
		this.Email = Email;
		this.Password = Password;
		this.Admin = Admin;
	}
	
	public String toString() {
		JSONObject temp = new JSONObject();
		try {
			temp.put("UserID", UserID);
			temp.put("Name", Name);
			temp.put("Email", Email);
			temp.put("Password", "");
			temp.put("Admin", Admin);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temp.toString();
	}

}

