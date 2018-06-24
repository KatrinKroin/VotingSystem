package com.project.katri.votingsce;

import java.util.ArrayList;

/**
 * Created by Katri on 09/02/2018.
 */

public interface AsyncResponse {
    void processFinish(ArrayList<Voting> voteList, int flag);
}
