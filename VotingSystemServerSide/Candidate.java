package com.example;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Candidate implements Serializable{
	public static final long serialVersionUID = 1L;
	
	public int VoteNum;
	public int CandidateID;
	public String CandidateName;

	
	public Candidate(int VoteNum, int CandidateID, String CandidateName) {	
		this.VoteNum = VoteNum;
		this.CandidateID = CandidateID;
		this.CandidateName = CandidateName;
	}
	
	public String toString() {
		JSONObject temp = new JSONObject();
		try {
			temp.put("VoteNum", VoteNum);
			temp.put("CandidateID", CandidateID);
			temp.put("CandidateName", CandidateName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return temp.toString();
	}



}
