package com.example;


import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;

import org.json.JSONObject;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;



@Controller
@SpringBootApplication
public class Main {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Autowired
	private DataSource dataSource;

	private static SimpleDateFormat formatter;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
	}

	// ------------------------------------- SCE App -----------------------------------------------

	@RequestMapping(value = "scelogine", method = RequestMethod.POST) 
	public @ResponseBody String scelogine(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			String Email = str.getString("Email");
			String PW = str.getString("Password");
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE email=" + Email + " AND password=" + PW);         
			User output =  null;
			while (rs.next()) {
				output=new User(rs.getString("UserID"), rs.getString("Name"), null, null, rs.getBoolean("Admin"));
			} 

			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(output.toString(), null));
			return obj.toString();   
		}catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "scegetvotese", method = RequestMethod.POST)
	public @ResponseBody String scegetvotese(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();      
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			String ID = str.getString("UserID");
			ArrayList<Voting> output = new ArrayList<Voting>();          
			ResultSet rs = stmt.executeQuery("SELECT * FROM voting WHERE votenum IN(SELECT votenum FROM allowedtovote WHERE userid=" + ID +"AND voted=false)");                               
			while (rs.next()) {
				output.add(new Voting(rs.getInt("votenum"), rs.getString("start"), rs.getString("finish"), rs.getString("votename"), rs.getString("votedescription")));	               	  
			}         
			ArrayList<Voting> sorted = SortVotingList(output);
			JSONArray array = new JSONArray();
			for (Voting temp : sorted) {
				JSONObject A = new JSONObject(temp.toString());
				array.put(A);
			}

			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(array.toString(), null));
			return obj.toString();               

		}catch (Exception e) {
			return null;
		}
	}



	@RequestMapping(value = "scegetallvotese", method = RequestMethod.POST)
	public @ResponseBody String scegetallvotese(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			String ID = str.getString("UserID");
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE userid=" + ID);    
			boolean Admin = false;
			while (rs.next()) {
				Admin = rs.getBoolean("Admin");            	  
			}
			if(Admin == false)
				return null;
			Statement stmt2 = connection.createStatement();               
			ArrayList<Voting> output = new ArrayList<Voting>();          
			ResultSet rs2 = stmt2.executeQuery("SELECT * FROM voting");                               
			while (rs2.next()) {
				output.add(new Voting(rs2.getInt("votenum"), rs2.getString("start"), rs2.getString("finish"), rs2.getString("votename"), rs2.getString("votedescription")));	               	  
			}

			ArrayList<Voting> sorted = collect_closed(output);
			JSONArray array = new JSONArray();
			for (Voting temp : sorted) {
				JSONObject A = new JSONObject(temp.toString());
				array.put(A);
			}


			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(array.toString(), null));
			return obj.toString();  


		}catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "scegetcandidatese", method = RequestMethod.POST)
	public @ResponseBody String scegetcandidatese(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement(); 
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			String VoteNum = str.getString("VoteNum");
			ArrayList<Candidate> output = new ArrayList<Candidate>();          
			ResultSet rs = stmt.executeQuery("SELECT * FROM candidate WHERE votenum=" + VoteNum);                               
			while (rs.next()) {
				output.add(new Candidate(rs.getInt("votenum"), rs.getInt("candidateid"), rs.getString("candidatename")));	               	  
			}
			JSONArray array = new JSONArray();
			for (Candidate temp : output) {
				JSONObject A = new JSONObject(temp.toString());
				array.put(A);
			}

			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(array.toString(), null));
			return obj.toString();


		}catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "sceuservotede", method = RequestMethod.POST)
	public @ResponseBody String sceuservotede(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt1 = connection.createStatement();
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			String votenum = str.getString("VoteNum");

			Voting output = null;  
			ResultSet rs2 = stmt1.executeQuery("SELECT * FROM voting WHERE VoteNum=" + votenum + "");                               
			while (rs2.next()) {
				output=new Voting(rs2.getInt("votenum"), rs2.getString("start"), rs2.getString("finish"), rs2.getString("votename"), rs2.getString("votedescription"));	               	  
			}
			Date start = formatter.parse(output.Start.replaceAll("Z$", "+0000"));
			Date finish = formatter.parse(output.Finish.replaceAll("Z$", "+0000"));
			String current = formatter.format(new Date());
			Date curr = formatter.parse(current.replaceAll("Z$", "+0000"));
			String result;
			if (curr.compareTo(start)>0 && finish.compareTo(curr)>0)
			{
				Statement stmt = connection.createStatement();               
				String ID = str.getString("UserID");
				int count = stmt.executeUpdate("UPDATE allowedtovote SET voted=true WHERE userid=" + ID + "AND votenum=" + votenum +" AND voted=false");         
				if(count > 0)          	  
				{    

					result = "true";
				} 
				else
					result = "false";    		  
			}
			else
				result = "Timeout";    


			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(result, null));
			return obj.toString();   

		}catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping(value = "scesendballote", method = RequestMethod.POST)
	public @ResponseBody String scesendballote(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			Statement stmt = connection.createStatement();               
			String Candidate_ID = str.getString("CandidateID");
			String votenum = str.getString("VoteNum");
			String key = str.getString("VoteKey");
			String result;
			int count = stmt.executeUpdate("INSERT INTO ballot VALUES (" + votenum + ", " + key + ", " + Candidate_ID + ")");         
			if(count > 0)
				result = "true";
			else
			{
				Statement stmt2 = connection.createStatement();    

				long random = ThreadLocalRandom.current().nextLong();
				Date date = new Date(random);
				String day = formatter.format(date);
				int count2 = stmt2.executeUpdate("INSERT INTO ballot VALUES (" + votenum + ", " + "'" +  key.replace("'", "") + day  + "'" + ", " + Candidate_ID + ")");    
				if(count2 > 0)
					result = "true";
				else
					result = "false";

			}         
			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(result, null));
			return obj.toString();         

		}catch (Exception e) {
			return e.getMessage();
		}
	}



	@RequestMapping(value = "sceresultse", method = RequestMethod.POST)
	public @ResponseBody String sceresultse(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();   
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			String VoteNum = str.getString("VoteNum");
			ArrayList<Result> output = new ArrayList<Result>();
			ResultSet rs = stmt.executeQuery("SELECT b.candidatename, COUNT(a.candidateid) "
					+ "FROM ballot a, candidate b "
					+ "WHERE a.votenum=" + VoteNum + "AND a.votenum=b.votenum AND a.candidateid=b.candidateid "
					+ "GROUP BY a.candidateid,b.candidatename");  

			while (rs.next()) {
				output.add(new Result(rs.getString("candidatename"), String.valueOf(rs.getInt("count"))));	
			}

			JSONArray array = new JSONArray();
			for (Result temp : output) {
				JSONObject A = new JSONObject(temp.toString());
				array.put(A);
			}

			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(array.toString(), null));
			return obj.toString();

		}catch (Exception e) {
			return null;
		}
	}



	//------------------------------------- SCE App Help Functions -----------------------------------------------

	private ArrayList<Voting> collect_closed(ArrayList<Voting> output){
		ArrayList<Voting> past = new ArrayList<Voting>();  
		String current = formatter.format(new Date());
		Date curr;
		try {
			curr = formatter.parse(current.replaceAll("Z$", "+0000"));
			for (Voting object: output) {
				Date finish = formatter.parse(object.Finish.replaceAll("Z$", "+0000"));
				if(curr.compareTo(finish)>0) 
					past.add(object);  
			}
			return past;

		} catch (ParseException e) {
			return null;
		}

	}

	private ArrayList<Voting> SortVotingList(ArrayList<Voting> output){
		ArrayList<Voting> open = new ArrayList<Voting>(); 
		ArrayList<Voting> future = new ArrayList<Voting>(); 
		ArrayList<Voting> past = new ArrayList<Voting>(); 

		String current = formatter.format(new Date());
		Date curr;
		try {
			curr = formatter.parse(current.replaceAll("Z$", "+0000"));
			for (Voting object: output) {
				Date start = formatter.parse(object.Start.replaceAll("Z$", "+0000"));
				Date finish = formatter.parse(object.Finish.replaceAll("Z$", "+0000"));
				if(curr.compareTo(start)>0 && finish.compareTo(curr)>0) 
					open.add(object);
				else if(start.compareTo(curr)>0) 
					future.add(object);
				else if(curr.compareTo(finish)>0) 
					past.add(object);  
			}
			ArrayList<Voting> out = new ArrayList<Voting>();
			out.addAll(past);
			out.addAll(open);
			out.addAll(future);
			return out;

		} catch (ParseException e) {
			return null;
		}

	}




	//------------------------------------- SCE App End -----------------------------------------------


	//------------------------------------- SCE Admin Program -----------------------------------------------




	@RequestMapping(value = "adminlogine", method = RequestMethod.POST) 
	public @ResponseBody String adminlogine(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			String Email = str.getString("Email");
			String PW = str.getString("Password");
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE email=" + Email + " AND password=" + PW + " AND admin=true");         
			User output =  null;
			while (rs.next()) {
				output=new User(rs.getString("UserID"), rs.getString("Name"), null, null, rs.getBoolean("Admin"));
			}       

			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(output.toString(), null));
			return obj.toString();   

		}catch (Exception e) {
			return e.getMessage();
		}
	}



	@RequestMapping(value = "admingetallvotese", method = RequestMethod.POST)
	public @ResponseBody String admingetallvotese(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			String ID = str.getString("UserID");

			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE userid=" + ID);    
			boolean Admin = false;
			while (rs.next()) {
				Admin = rs.getBoolean("Admin");            	  
			}
			if(Admin == false)
				return null;
			
			
			Statement stmt2 = connection.createStatement();               
			ArrayList<Voting> output = new ArrayList<Voting>();          
			ResultSet rs2 = stmt2.executeQuery("SELECT * FROM voting");                               
			while (rs2.next()) {
				output.add(new Voting(rs2.getInt("votenum"), rs2.getString("start"), rs2.getString("finish"), rs2.getString("votename"), rs2.getString("votedescription")));	               	  
			}
			ArrayList<Voting> sorted = SortVotingList(output);
			

			JSONArray array = new JSONArray();
			for (Voting temp : sorted) {
				JSONObject A = new JSONObject(temp.toString());
				array.put(A);
			}

			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(array.toString(), null));
			return obj.toString();
			
		}catch (Exception e) {
			return e.getMessage();
		}
	}

	
	@RequestMapping(value = "admingetallresultse", method = RequestMethod.POST)
	public @ResponseBody String admingetallresultse(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();     
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			
			String VoteNum = str.getString("VoteNum");
			ArrayList<Result> output = new ArrayList<Result>();
			ResultSet rs = stmt.executeQuery("SELECT b.candidatename, COUNT(a.candidateid) "
					+ "FROM ballot a, candidate b "
					+ "WHERE a.votenum=" + VoteNum + "AND a.votenum=b.votenum AND a.candidateid=b.candidateid "
					+ "GROUP BY a.candidateid,b.candidatename");  

			while (rs.next()) {
				output.add(new Result(rs.getString("candidatename"), String.valueOf(rs.getInt("count"))));	
			}

			ResultSet rs2 = stmt.executeQuery("SELECT candidatename FROM candidate WHERE votenum="+VoteNum+" EXCEPT SELECT b.candidatename FROM ballot a, candidate b WHERE a.votenum="+VoteNum+" AND a.votenum=b.votenum AND a.candidateid=b.candidateid GROUP BY a.candidateid,b.candidatename");
			while (rs2.next()) {
				output.add(new Result(rs2.getString("candidatename"), "0"));	
			} 
		
			JSONArray array = new JSONArray();
			for (Result temp : output) {
				JSONObject A = new JSONObject(temp.toString());
				array.put(A);
			}

			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(array.toString(), null));
			return obj.toString();
			
		}catch (Exception e) {
			return e.getMessage();
		}
	}


	
	@RequestMapping(value = "admingetalluserse", method = RequestMethod.POST)
	public @ResponseBody String admingetalluserse(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {

			Statement stmt = connection.createStatement();  
			ArrayList<User> out = new ArrayList<User>();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");         

			while (rs.next()) {
				out.add(new User(rs.getString("UserID"), rs.getString("Name"), rs.getString("Email"), null, rs.getBoolean("Admin")));
			}  
			
			JSONArray array = new JSONArray();
			for (User temp : out) {
				JSONObject A = new JSONObject(temp.toString());
				array.put(A);
			}

			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(array.toString(), null));
			return obj.toString();

		}catch (Exception e) {
			return e.getMessage();
		}
	}
	

	@RequestMapping(value = "admingetallcandidatese", method = RequestMethod.POST)
	public @ResponseBody String admingetallcandidatese(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();     
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));		
			String VoteNum = str.getString("VoteNum");
			ArrayList<Candidate> output = new ArrayList<Candidate>();          
			ResultSet rs = stmt.executeQuery("SELECT * FROM candidate WHERE votenum=" + VoteNum);                               
			while (rs.next()) {
				output.add(new Candidate(rs.getInt("votenum"), rs.getInt("candidateid"), rs.getString("candidatename")));	               	  
			}
			
			JSONArray array = new JSONArray();
			for (Candidate temp : output) {
				JSONObject A = new JSONObject(temp.toString());
				array.put(A);
			}

			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(array.toString(), null));
			return obj.toString();
			
		}catch (Exception e) {
			return e.getMessage();
		}
	}

	
	
	@RequestMapping(value = "adminaddvotinge", method = RequestMethod.POST)
	public @ResponseBody String adminaddvotinge(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {

			Statement get_votenum = connection.createStatement();
			ResultSet result = get_votenum.executeQuery("SELECT MAX(votenum) FROM voting");
			int Num = 0;
			while (result.next()) 
			{
				Num = result.getInt("max");
			}
			Num++;

			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));		
			
			String VoteNum = String.valueOf(Num);
			String Start = str.getString("Start");
			String Finish = str.getString("Finish");
			String VoteName = str.getString("VoteName");
			String VoteDescription = str.getString("VoteDescription");

			Statement stmt = connection.createStatement();  

			int count = stmt.executeUpdate("INSERT INTO voting VALUES (" + VoteNum + ", " + Start + ", " + Finish + ", " + VoteName + ", "+ VoteDescription + ")");         
			if(count <= 0)
				return "cannot insert";                                 
			return "true";      
		}catch (Exception e) {
			return e.getMessage();
		}
	}
	
	
	@RequestMapping(value = "admineditvotinge", method = RequestMethod.POST)
	public @ResponseBody String admineditvotinge(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));	
			
			String VoteNum = str.getString("VoteNum");
			String Start = str.getString("Start");
			String Finish = str.getString("Finish");
			String VoteName = str.getString("VoteName");
			String VoteDescription = str.getString("VoteDescription");        
			Statement stmt = connection.createStatement();           
			stmt.executeUpdate("UPDATE voting SET Start="+ Start + ", Finish=" + Finish + ", VoteName=" + VoteName + ", VoteDescription=" + VoteDescription + " WHERE VoteNum=" + VoteNum);                                  
			return "true";      
		}catch (Exception e) {
			return e.getMessage();
		}
	}


	@RequestMapping(value = "adminremovevotinge", method = RequestMethod.POST)
	public @ResponseBody String adminremovevotinge(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));	
			String VoteNum = str.getString("VoteNum");
			Statement stmt = connection.createStatement();  
         
			Voting output = null;          
			ResultSet rs = stmt.executeQuery("SELECT * FROM voting WHERE votenum=" + VoteNum);                               
			while (rs.next()) { 
				output = new Voting(rs.getInt("votenum"), rs.getString("start"), rs.getString("finish"), rs.getString("votename"), rs.getString("votedescription"));	               	  
			}
			if(output == null)
				return "Voting doesn't exists";
			String current = formatter.format(new Date());
			Date curr = formatter.parse(current.replaceAll("Z$", "+0000"));
			Date start = formatter.parse(output.Start.replaceAll("Z$", "+0000"));
			Date finish = formatter.parse(output.Finish.replaceAll("Z$", "+0000"));
			if(curr.compareTo(start)>0 && finish.compareTo(curr)>0)      		  
			{
				return "Open voting cannot be deleted!"; 		  
			}	    		  

			stmt = connection.createStatement();                   
			stmt.executeUpdate("DELETE FROM allowedtovote WHERE VoteNum=" + VoteNum); 

			stmt = connection.createStatement();                       
			stmt.executeUpdate("DELETE FROM candidate WHERE VoteNum=" + VoteNum); 

			stmt = connection.createStatement();                       
			stmt.executeUpdate("DELETE FROM ballot WHERE VoteNum=" + VoteNum);        

			stmt = connection.createStatement();        
			int count = stmt.executeUpdate("DELETE FROM voting WHERE VoteNum=" + VoteNum);            
			if(count > 0)
				return "true";
			return "false";
		}catch (Exception e) {
			return e.getMessage();
		}
	}

	
	
	@RequestMapping(value = "adminupdatecandidatese", method = RequestMethod.POST)
	public @ResponseBody String adminupdatecandidatese(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));	
			String VoteNum = str.getString("VoteNum");
			String Candidates = str.getString("CandidateName"); 
			Statement stmt = connection.createStatement();                  
			stmt.executeUpdate("DELETE FROM candidate WHERE votenum=" + VoteNum); 			
			Candidates = Candidates.substring(1, Candidates.length()-1);
			String[] names = Candidates.split(",");
			for(int i = 0 ; i < names.length ; i++)
			{
				stmt = connection.createStatement();
				stmt.executeUpdate("INSERT INTO candidate VALUES (" + VoteNum + ", " + i + ", " + names[i] + ")");         	     	  
			}
			return "true";
		}catch (Exception e) {
			return e.getMessage();
		}
	}
	
	
	@RequestMapping(value = "adminaddusere", method = RequestMethod.POST)
	public @ResponseBody String adminaddusere(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();  
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));	
			String UserID = str.getString("UserID");
			String Name = str.getString("Name");
			String Email = str.getString("Email");
			String Password = str.getString("Password");    
			int count = stmt.executeUpdate("INSERT INTO users VALUES (" + UserID + ", " + Email + ", " + Password + ", false, " + Name +")");         
			if(count > 0)
				return "true";
			return "false";
		}catch (Exception e) {
			return e.getMessage();
		}
	}
	


	@RequestMapping(value = "adminupdateusere", method = RequestMethod.POST)
	public @ResponseBody boolean adminupdateusere(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();              
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));	
			String UserID = str.getString("UserID");
			String Name = str.getString("Name");
			String Email = str.getString("Email");
			String Password = str.getString("Password");        
			int count = stmt.executeUpdate("UPDATE users SET Name="+ Name + ", Email=" + Email + ", Password=" + Password + " WHERE UserID=" + UserID);         
			if(count > 0)
				return true;
			return false;
		}catch (Exception e) {
			return false;
		}
	}
	
	
	@RequestMapping(value = "adminremoveusere", method = RequestMethod.POST)
	public @ResponseBody String adminremoveusere(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();         
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));	
			String UserID = str.getString("UserID");     
			ArrayList<Voting> output = new ArrayList<Voting>();          
			ResultSet rs = stmt.executeQuery("SELECT * FROM voting WHERE votenum IN(SELECT votenum FROM allowedtovote WHERE userid=" + UserID +"AND voted=false)");                               
			while (rs.next()) { 
				output.add(new Voting(rs.getInt("votenum"), rs.getString("start"), rs.getString("finish"), rs.getString("votename"), rs.getString("votedescription")));	               	  
			}

			String current = formatter.format(new Date());
			Date curr = formatter.parse(current.replaceAll("Z$", "+0000"));
			for (Voting object: output) {
				Date start = formatter.parse(object.Start.replaceAll("Z$", "+0000"));
				Date finish = formatter.parse(object.Finish.replaceAll("Z$", "+0000"));
				if(curr.compareTo(start)>0 && finish.compareTo(curr)>0)      		  
				{
					return "User has open elections"; 		  
				}	    		  
			}         

			stmt.executeUpdate("DELETE FROM allowedtovote WHERE UserID=" + UserID); 
			stmt = connection.createStatement();  
			int count = stmt.executeUpdate("DELETE FROM users WHERE UserID=" + UserID);         
			if(count > 0)
				return "true";
			return "Something went wrong";
		}catch (Exception e) {
			return e.getMessage();
		}
	}
	
	
	@RequestMapping(value = "adminsetusersforvotinge", method = RequestMethod.POST)
	public @ResponseBody String adminsetusersforvotinge(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();  
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));	

			String VoteNum = str.getString("VoteNum");       
			stmt.executeUpdate("DELETE FROM allowedtovote WHERE VoteNum=" + VoteNum);    

			String Users = str.getString("Users");        
			Users = Users.substring(1, Users.length()-1);
			String[] ids = Users.split(",");
			for(int i = 0 ; i < ids.length ; i++)
			{
				stmt = connection.createStatement();
				stmt.executeUpdate("INSERT INTO allowedtovote VALUES (" + ids[i] + ", " + VoteNum + ",  false)");        	     	  
			}
			return "true";

		}catch (Exception e) {
			return e.getMessage();
		}
	}

	
	
	@RequestMapping(value = "admingetusersbyvotenume", method = RequestMethod.POST)
	public @ResponseBody String admingetusersbyvotenume(HttpServletRequest request, HttpServletResponse res) throws IOException {

		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();           
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));	

			String VoteNum = str.getString("VoteNum");
			

			ArrayList<User> output = new ArrayList<User>();          
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE userid IN(SELECT UserID FROM allowedtovote WHERE votenum=" + VoteNum +"AND voted=false)");                               
			while (rs.next()) { 
				output.add(new User(rs.getString("UserID"), rs.getString("Name"), rs.getString("Email"), null, rs.getBoolean("Admin")));
			}

			JSONArray array = new JSONArray();
			for (User temp : output) {
				JSONObject A = new JSONObject(temp.toString());
				array.put(A);
			}

			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(array.toString(), null));
			return obj.toString();

		}catch (Exception e) {
			return null;
		}
	}
	

	
	
	@RequestMapping(value = "admingetvotingsbyuseride", method = RequestMethod.POST)
	public @ResponseBody String admingetvotingsbyuseride(HttpServletRequest request, HttpServletResponse res) throws IOException {

		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();     
			
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));
			String UserID = str.getString("UserID");

			ArrayList<Voting> output = new ArrayList<Voting>();          
			ResultSet rs = stmt.executeQuery("SELECT * FROM voting WHERE votenum IN(SELECT votenum FROM allowedtovote WHERE UserID=" + UserID +")");                               
			while (rs.next()) { 
				output.add(new Voting(rs.getInt("votenum"), rs.getString("start"), rs.getString("finish"), rs.getString("votename"), rs.getString("votedescription")));	
			}
			output = SortVotingList(output);

			JSONArray array = new JSONArray();
			for (Voting temp : output) {
				JSONObject A = new JSONObject(temp.toString());
				array.put(A);
			}


			JSONObject obj = new JSONObject();
			obj.put("Str", AES.encrypt(array.toString(), null));
			return obj.toString();
			

		}catch (Exception e) {
			return e.getMessage();
		}
	}
	

	
	@RequestMapping(value = "adminuserelectionse", method = RequestMethod.POST)
	public @ResponseBody String adminuserelectionse(HttpServletRequest request, HttpServletResponse res) throws IOException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();    
			JSONObject str = new JSONObject(AES.decrypt(request.getParameter("Str"), null));  
			String UserID = str.getString("UserID");            
			String Votings = str.getString("Votings"); 
			JSONArray arr = new JSONArray(Votings);   
			ArrayList<Voting> output = new ArrayList<Voting>();          
			ResultSet rs = stmt.executeQuery("SELECT * FROM voting WHERE votenum IN(SELECT votenum FROM allowedtovote WHERE userid=" + UserID +"AND voted=false)");                               
			while (rs.next()) { 
				output.add(new Voting(rs.getInt("votenum"), rs.getString("start"), rs.getString("finish"), rs.getString("votename"), rs.getString("votedescription")));	               	  
			}

			String current = formatter.format(new Date());
			Date curr = formatter.parse(current.replaceAll("Z$", "+0000"));
			for (Voting object: output) {
				Date start = formatter.parse(object.Start.replaceAll("Z$", "+0000"));
				if(start.compareTo(curr)>0)     		  
				{
					stmt = connection.createStatement(); 
					stmt.executeUpdate("DELETE FROM allowedtovote WHERE UserID=" + UserID + " AND VoteNum=" + object.VoteNum);    		  
				}	    		  
			}

			String VoteNum = null;
			for(int i = 0 ; i < arr.length() ; i++)
			{
				JSONObject object = arr.getJSONObject(i);
				VoteNum = object.getString("VoteNum");
				stmt = connection.createStatement();
				stmt.executeUpdate("INSERT INTO allowedtovote VALUES (" + UserID + ", " + VoteNum + ",  false)");         	     	  
			}
			return "true";

		}catch (Exception e) {
			return e.getMessage();
		}

	}
	

	//------------------------------------- SCE Admin Program End ----------------------------------------------

	// -------------------------------------------------------------- Privacy Policy and other docs----------------------------------------------------------

	@RequestMapping(value = "Privacypolicy", method = RequestMethod.GET) 
	public @ResponseBody String Privacypolicy(HttpServletRequest request, HttpServletResponse res) throws IOException {
		String str = "<!DOCTYPE html><html> <head> <meta charset=\"utf-8\"> <meta name=\"viewport\" content=\"width=device-width\"> <title>Privacy Policy</title> <style>body{font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif; padding:1em;}</style></head> <body><h2>Privacy Policy</h2> <p> yuliakls built the VotingSCE app as a Free app. This SERVICE is provided by yuliakls at no cost and is intended for use as is.\r\n" + 
				"                  </p> <p>This page is used to inform website visitors regarding my policies with the collection, use, and\r\n" + 
				"                    disclosure of Personal Information if anyone decided to use my Service.\r\n" + 
				"                  </p> <p>If you choose to use my Service, then you agree to the collection and use of information in relation\r\n" + 
				"                    to this policy. The Personal Information that I collect is used for providing and improving the\r\n" + 
				"                    Service. I will not use or share your information with anyone except as described\r\n" + 
				"                    in this Privacy Policy.\r\n" + 
				"                  </p> <p>The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is accessible\r\n" + 
				"                    at VotingSCE unless otherwise defined in this Privacy Policy.\r\n" + 
				"                  </p> <p><strong>Information Collection and Use</strong></p> <p>For a better experience, while using our Service, I may require you to provide us with certain\r\n" + 
				"                    personally identifiable information. The information that I request is retained on your device and is not collected by me in any way\r\n" + 
				"                  </p> <p>The app does use third party services that may collect information used to identify you.</p> <div><p>Link to privacy policy of third party service providers used by the app</p> <ul><li><a href=\"https://www.google.com/policies/privacy/\" target=\"_blank\">Google Play Services</a></li> <!----> <!----> <!----> <!----> <!----></ul></div> <p><strong>Log Data</strong></p> <p> I want to inform you that whenever you use my Service, in a case of an\r\n" + 
				"                    error in the app I collect data and information (through third party products) on your phone\r\n" + 
				"                    called Log Data. This Log Data may include information such as your device Internet Protocol (“IP”) address,\r\n" + 
				"                    device name, operating system version, the configuration of the app when utilizing my Service,\r\n" + 
				"                    the time and date of your use of the Service, and other statistics.\r\n" + 
				"                  </p> <p><strong>Cookies</strong></p> <p>Cookies are files with small amount of data that is commonly used an anonymous unique identifier. These\r\n" + 
				"                    are sent to your browser from the website that you visit and are stored on your device internal memory.\r\n" + 
				"                  </p> <p>This Service does not use these “cookies” explicitly. However, the app may use third party code and libraries\r\n" + 
				"                    that use “cookies” to collection information and to improve their services. You have the option to either\r\n" + 
				"                    accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to\r\n" + 
				"                    refuse our cookies, you may not be able to use some portions of this Service.\r\n" + 
				"                  </p> <p><strong>Service Providers</strong></p> <p> I may employ third-party companies and individuals due to the following reasons:</p> <ul><li>To facilitate our Service;</li> <li>To provide the Service on our behalf;</li> <li>To perform Service-related services; or</li> <li>To assist us in analyzing how our Service is used.</li></ul> <p> I want to inform users of this Service that these third parties have access to your\r\n" + 
				"                    Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they\r\n" + 
				"                    are obligated not to disclose or use the information for any other purpose.\r\n" + 
				"                  </p> <p><strong>Security</strong></p> <p> I value your trust in providing us your Personal Information, thus we are striving\r\n" + 
				"                    to use commercially acceptable means of protecting it. But remember that no method of transmission over\r\n" + 
				"                    the internet, or method of electronic storage is 100% secure and reliable, and I cannot guarantee\r\n" + 
				"                    its absolute security.\r\n" + 
				"                  </p> <p><strong>Links to Other Sites</strong></p> <p>This Service may contain links to other sites. If you click on a third-party link, you will be directed\r\n" + 
				"                    to that site. Note that these external sites are not operated by me. Therefore, I strongly\r\n" + 
				"                    advise you to review the Privacy Policy of these websites. I have no control over\r\n" + 
				"                    and assume no responsibility for the content, privacy policies, or practices of any third-party sites\r\n" + 
				"                    or services.\r\n" + 
				"                  </p> <p><strong>Children’s Privacy</strong></p> <p>These Services do not address anyone under the age of 13. I do not knowingly collect\r\n" + 
				"                    personally identifiable information from children under 13. In the case I discover that a child\r\n" + 
				"                    under 13 has provided me with personal information, I immediately delete this from\r\n" + 
				"                    our servers. If you are a parent or guardian and you are aware that your child has provided us with personal\r\n" + 
				"                    information, please contact me so that I will be able to do necessary actions.\r\n" + 
				"                  </p> <p><strong>Changes to This Privacy Policy</strong></p> <p> I may update our Privacy Policy from time to time. Thus, you are advised to review\r\n" + 
				"                    this page periodically for any changes. I will notify you of any changes by posting\r\n" + 
				"                    the new Privacy Policy on this page. These changes are effective immediately after they are posted on\r\n" + 
				"                    this page.\r\n" + 
				"                  </p> <p><strong>Contact Us</strong></p> <p>If you have any questions or suggestions about my Privacy Policy, do not hesitate to contact\r\n" + 
				"                    me.\r\n" + 
				"                  </p> <p>This privacy policy page was created at <a href=\"https://privacypolicytemplate.net\" target=\"_blank\">privacypolicytemplate.net</a>                    and modified/generated by <a href=\"https://app-privacy-policy-generator.firebaseapp.com/\" target=\"_blank\">App Privacy Policy Generator</a></p></body></html>";
		return str;
	}


	@RequestMapping(value = "TermsAndConditions", method = RequestMethod.GET) 
	public @ResponseBody String TermsAndConditions(HttpServletRequest request, HttpServletResponse res) throws IOException {
		String str = "<!DOCTYPE html><html> <head> <meta charset=\"utf-8\"> <meta name=\"viewport\" content=\"width=device-width\"> <title>Privacy Policy</title> <style>body{font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif; padding:1em;}</style></head> <body><h2>Terms &amp; Conditions</h2> <p>By downloading or using the app, these terms will automatically apply to you – you should make sure therefore that you read them carefully before using the app. You’re not allowed to copy, or modify the app, any part of the app, or our trademarks in any way. You’re not allowed to attempt to extract the source code of the app, and you also shouldn’t try to translate the app into other languages, or make derivative versions. The app itself, and all the trade marks, copyright, database rights and other intellectual property rights related to it, still belong to yuliakls.</p> <p>yuliakls is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you’re paying for.</p> <p>The [App Name]VotingSCE app stores and processes personal data that you have provided to us, in order to provide my Service. It’s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone’s security features and it could mean that the [App Name]VotingSCE app won’t work properly or at all. </p> <p>You should be aware that there are certain things that yuliakls will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Wi-Fi, or provided by your mobile network provider, but yuliakls cannot take responsibility for the app not working at full functionality if you don’t have access to Wi-Fi, and you don’t have any of your data allowance left.</p><p></p><p>If you’re using the app outside of an area with Wi-Fi, you should remember that your terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third party charges. In using the app, you’re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you’re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.</p> <p>Along the same lines, yuliakls cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged – if it runs out of battery and you can’t turn it on to avail the Service, yuliakls cannot accept responsibility</p> <p>With respect to yuliakls’s responsibility for your use of the app, when you’re using the app, it’s important to bear in mind that although we endeavour to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. yuliakls accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.</p> <p>At some point, we may wish to update the app. The app is currently available on Android and iOS – the requirements for both systems (and for any additional systems we decide to extend the availability of the app to) may change, and you’ll need to download the updates if you want to keep using the app. yuliakls does not promise that it will always update the app so that it is relevant to you and/or works with the iOS/Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.</p> <p><strong>Changes to This Terms and Conditions</strong></p> <p> I may update our Terms and Conditions from time to time. Thus, you are advised to review\r\n" + 
				"                      this page periodically for any changes. I will notify you of any changes by posting\r\n" + 
				"                      the new Terms and Conditions on this page. These changes are effective immediately after they are posted on\r\n" + 
				"                      this page.\r\n" + 
				"                    </p> <p><strong>Contact Us</strong></p> <p>If you have any questions or suggestions about my Terms and Conditions, do not hesitate to contact\r\n" + 
				"                      me.\r\n" + 
				"                    </p> <p>This Terms and Conditions page was generated by <a href=\"https://app-privacy-policy-generator.firebaseapp.com/\" target=\"_blank\">App Privacy Policy Generator</a></p></body></html>";
		return str;
	}


	//********************************************** BASIC FUNCTIONS **********************************************


	@Bean
	public DataSource dataSource() throws SQLException {
		if (dbUrl == null || dbUrl.isEmpty()) {
			return new HikariDataSource();
		} else {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(dbUrl);
			return new HikariDataSource(config);
		}
	}

}
