using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Http;
using System.Net.Http.Headers;
using Flurl;
using System.Net;
using System.IO;
using System.Dynamic;
using System.Configuration;
using Newtonsoft.Json;
using System.Collections.Specialized;
using System.Runtime.InteropServices;
using System.Web.Script.Serialization;
using System.Runtime.Serialization;
using System.Text.RegularExpressions;
using Newtonsoft.Json.Linq;

namespace VotingSystem
{
    class Server
    {
        public List<Voting> SetPool(string UserID)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["UserID"] = "'"+ UserID + "'";
                string response = GetResultsStringFromServer(values, "https://morning-anchorage-32230.herokuapp.com/admingetallvotese", client);
                return JsonConvert.DeserializeObject<List<Voting>>(response);                 
             }
        }

        public List<Voting> SetUserPool(string UserID)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["UserID"] = "'" + UserID + "'";
                string response = GetResultsStringFromServer(values, "https://morning-anchorage-32230.herokuapp.com/admingetvotingsbyuseride", client);
                return JsonConvert.DeserializeObject<List<Voting>>(response);
            }
        }


        private string GetResultsStringFromServer(NameValueCollection values, string url, WebClient client)
        {
            var encryptedvalues = new NameValueCollection();
            JsonCreation(encryptedvalues, values);
            var response = client.UploadValues(url, encryptedvalues);
            var responseString = Encoding.Default.GetString(response);
            if (string.IsNullOrEmpty(responseString)) throw new Exception("The data you requested doesn't exists!");
            JObject jObject = JObject.Parse(responseString);
            string encryptedVotings = (string)jObject["Str"];
            return AES.decrypt(encryptedVotings, null);
        }

        void JsonCreation(NameValueCollection encryptedvalues, NameValueCollection values)
        {
            string json = "";
            foreach (var handleMultipleValuesPerKey in new bool[] { false, true })
            {
                var dict = NvcToDictionary(values, handleMultipleValuesPerKey);
                json = new JavaScriptSerializer().Serialize(dict);
            }
            encryptedvalues["Str"] = AES.encrypt(json, null);
        }

        public List<Result> SetResults(String VoteNum)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["VoteNum"] = "'" + VoteNum + "'";
                string response = GetResultsStringFromServer(values, "https://morning-anchorage-32230.herokuapp.com/admingetallresultse", client);
                return JsonConvert.DeserializeObject<List<Result>>(response);
            }
        }

        public List<Candidate> SetCandidates(String VoteNum)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["VoteNum"] = "'" + VoteNum + "'";
                string response = GetResultsStringFromServer(values, "https://morning-anchorage-32230.herokuapp.com/admingetallcandidatese", client);
                return JsonConvert.DeserializeObject<List<Candidate>>(response);
            }
        }

        public List<User> SetUsers()
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                string response = GetResultsStringFromServer(values, "https://morning-anchorage-32230.herokuapp.com/admingetalluserse", client);
                return JsonConvert.DeserializeObject<List<User>>(response);
            }
        }

        public List<User> SetAssignedUsers(string VoteNum)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["VoteNum"] = "'" + VoteNum + "'";
                string response = GetResultsStringFromServer(values, "https://morning-anchorage-32230.herokuapp.com/admingetusersbyvotenume", client);
                return JsonConvert.DeserializeObject<List<User>>(response);
            }
        }

        public string AddVoting(string VoteName, string VoteDescription,string Start, string Finish)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["VoteName"] = "'" + VoteName + "'";
                values["VoteDescription"] = "'" + VoteDescription + "'";
                values["Start"] = "'" + Start + "'";
                values["Finish"] = "'" + Finish + "'";
                var encryptedvalues = new NameValueCollection();
                JsonCreation(encryptedvalues, values);
                var response = client.UploadValues("https://morning-anchorage-32230.herokuapp.com/adminaddvotinge", encryptedvalues);
                return Convert.ToString(Encoding.Default.GetString(response)); 
            }
        }

        public string RemoveVoting(string VoteNum)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["VoteNum"] = VoteNum;
                var encryptedvalues = new NameValueCollection();
                JsonCreation(encryptedvalues, values);
                var response = client.UploadValues("https://morning-anchorage-32230.herokuapp.com/adminremovevotinge", encryptedvalues);
                return Convert.ToString(Encoding.Default.GetString(response));
            }
        }

        public string AddUser(string UserID, string Name, string Email, string Password)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["UserID"] = "'" + UserID + "'";
                values["Name"] = "'" + Name + "'";
                values["Email"] = "'" + Email + "'";
                values["Password"] = "'" + Password + "'";
                var encryptedvalues = new NameValueCollection();
                JsonCreation(encryptedvalues, values);
                var response = client.UploadValues("https://morning-anchorage-32230.herokuapp.com/adminaddusere", encryptedvalues);
                return Convert.ToString(Encoding.Default.GetString(response));
            }
        }

        public string RemoveUser(string UserID)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["UserID"] = UserID;
                var encryptedvalues = new NameValueCollection();
                JsonCreation(encryptedvalues, values);
                var response = client.UploadValues("https://morning-anchorage-32230.herokuapp.com/adminremoveusere", encryptedvalues);
                return Convert.ToString(Encoding.Default.GetString(response));
            }
        }

        public User Login(string UserEmail, string UserPassword)
        {        
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();//{Str:{}}
                values["Email"] = "'" + UserEmail + "'";
                values["Password"] = "'" + UserPassword + "'";

                string response = GetResultsStringFromServer(values, "https://morning-anchorage-32230.herokuapp.com/adminlogine", client);
                return JsonConvert.DeserializeObject<User>(response);
            }
        }


        static Dictionary<string, object> NvcToDictionary(NameValueCollection nvc, bool handleMultipleValuesPerKey)
        {
            var result = new Dictionary<string, object>();
            foreach (string key in nvc.Keys)
            {
                if (handleMultipleValuesPerKey)
                {
                    string[] values = nvc.GetValues(key);
                    if (values.Length == 1)
                    {
                        result.Add(key, values[0]);
                    }
                    else
                    {
                        result.Add(key, values);
                    }
                }
                else
                {
                    result.Add(key, nvc[key]);
                }
            }

            return result;
        }

        public string EditVoting(string VoteNum,string VoteName, string VoteDescription, string Start, string Finish)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["VoteNum"] = VoteNum;
                values["VoteName"] = "'" + VoteName + "'";
                values["VoteDescription"] = "'" + VoteDescription + "'";
                values["Start"] = "'" + Start + "'";
                values["Finish"] = "'" + Finish + "'";
                var encryptedvalues = new NameValueCollection();
                JsonCreation(encryptedvalues, values);
                var response = client.UploadValues("https://morning-anchorage-32230.herokuapp.com/admineditvotinge", encryptedvalues);
                return Convert.ToString(Encoding.Default.GetString(response));
            }
        }

        public string EditUser(string UserID, string Name, string Email, string Password)
        {
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["UserID"] = "'" + UserID + "'";
                values["Name"] = "'" + Name + "'";
                values["Email"] = "'" + Email + "'";
                values["Password"] = "'" + Password + "'";
                var encryptedvalues = new NameValueCollection();
                JsonCreation(encryptedvalues, values);
                var response = client.UploadValues("https://morning-anchorage-32230.herokuapp.com/adminupdateusere", encryptedvalues);
                return Convert.ToString(Encoding.Default.GetString(response));
            }
        }


        public string UpdateCandidates(string VoteNum, List<Candidate> Candidates)
        {
            List<string> Names = new List<string>();
            foreach (Candidate c in Candidates) Names.Add("'" + c.CandidateName + "'");
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["VoteNum"] = "'" + VoteNum + "'";
                string[] names = Names.ToArray();
                values["CandidateName"] = "[" + string.Join(",", names) + "]";
                var encryptedvalues = new NameValueCollection();
                JsonCreation(encryptedvalues, values);
                var response = client.UploadValues("https://morning-anchorage-32230.herokuapp.com/adminupdatecandidatese", encryptedvalues);
                return Convert.ToString(Encoding.Default.GetString(response));
            }
        }

        public string UpdateUsers(string VoteNum, List<User> Users)
        {
            List<string> Names = new List<string>();
            foreach (User u in Users) Names.Add("'" + u.UserID + "'");
            using (var client = new WebClient())
            {
                var values = new NameValueCollection();
                values["VoteNum"] = "'" + VoteNum + "'";
                string[] names = Names.ToArray();
                values["Users"] = "[" + string.Join(",", names) + "]";
                var encryptedvalues = new NameValueCollection();
                JsonCreation(encryptedvalues, values);
                var response = client.UploadValues("https://morning-anchorage-32230.herokuapp.com/adminsetusersforvotinge", encryptedvalues);
                return Convert.ToString(Encoding.Default.GetString(response));
            }
        }
    }
}
