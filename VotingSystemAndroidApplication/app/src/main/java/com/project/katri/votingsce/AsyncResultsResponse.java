package com.project.katri.votingsce;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Katri on 09/02/2018.
 */

public interface AsyncResultsResponse {
    void processResult(ArrayList<HashMap<String, String>> resultList, int flag);
}
