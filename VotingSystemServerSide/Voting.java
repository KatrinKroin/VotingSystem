package com.example;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

public class Voting implements Serializable{
	public static final long serialVersionUID = 1L;
	
	public int VoteNum;
	public String Start;
	public String Finish;
	public String VoteName;
	public String VoteDescription;
	
	public Voting(int VoteNum, String Start, String Finish, String VoteName, String VoteDescription ) {	
		this.VoteNum = VoteNum;
		this.Start = Start;
		this.Finish = Finish;
		this.VoteName = VoteName;
		this.VoteDescription = VoteDescription;
	}

	
	public String toString() {
		JSONObject temp = new JSONObject();
		try {
			temp.put("VoteNum", VoteNum);
			temp.put("Start", Start);
			temp.put("Finish", Finish);
			temp.put("VoteName", VoteName);
			temp.put("VoteDescription", VoteDescription);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temp.toString();
	}


}
