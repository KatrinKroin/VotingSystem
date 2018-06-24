package com.project.katri.votingsce;

/**
 * Created by Katri on 09/02/2018.
 */

public class Candidate {
    private String CandidateID;
    private String CandidateName;

    public Candidate(String CandidateID, String CandidateName){
        this.CandidateID = CandidateID;
        this.CandidateName = CandidateName;
    }

    public String getCandidateName() {
        return CandidateName;
    }
    public String getCandidateID() {
        return CandidateID;
    }
}
