package MainSecurity;
import static MainSecurity.DbConstants.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import SecuriyCommonUtility.MapUtility;

public class CheckPost2 extends CommonBaseClass
{
	 private String m_sToken;
	 private static SecuriyCommonUtility.MapUtility m_objMapUtility;
	 private  TimeZoneConvertor m_Timezone;
	 public CheckPost2(String token) 
		{
			super(token);
			
			//m_objProjectConstants = new ProjectConstants();
			//m_objMapUtility = new MapUtility();
			try
			{
				dbop_sec = new DbOperations(DB_SECURITY);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}	
		
		
	 public HashMap<Integer, Map<String, Object>> AddSecurityRound( int society_id, String create_by, int status,int checkpostID, int time_id,String logData )
	{
		 	
		    HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String security_round ="";
			long insertRound = 0;
			HashMap<Integer, Map<String, Object>> security_roundList = new HashMap<Integer, Map<String, Object>>();
				try
				{
					dbop_sec = new DbOperations(DB_SECURITY);
					//security_round =  "Insert into security_round (id,society_id,round_time,create_by,status) values('"+id+"','"+society_id+"','"+round_time+"','"+create_by+"','"+status+"')";
					security_round = "insert into security_round (`society_id`,`round_time`,`create_by`,`status`,`checkpost_data`,`checkpost_id`,`checkpost_time_id`) values('" + society_id + "',NOW(),'" + create_by +  "','" + status +  "','"+logData+"','"+checkpostID+"','"+time_id+"')";
					insertRound= dbop_sec.Insert(security_round);
					System.out.println(security_round);
					if(insertRound >0)
					{
						rows.put("success","1");
						rows2.put("message","inserted");
						rows.put("response",rows2);
					}
					else
					{
						rows.put("success","0");
						rows2.put("message","No data Found.Try Again");
						rows.put("response",rows2);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				//System.out.println(rows);
				
				return rows;
		}
			
	 
			
	 public  HashMap<Integer, Map<String, Object>> GetRoundList()
	 {
		    HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String Selectdata ="";
			HashMap<Integer, Map<String, Object>> results = new HashMap<Integer, Map<String, Object>>();
			
			try
			{
				dbop_sec = new DbOperations(DB_SECURITY);
				Selectdata = "select society_id,create_by,status from security_round";
				results = dbop_sec.Select(Selectdata);
				
			
				if(results.size()>0)
				{
					rows.put("success","1");
					rows2.put("news",results);
					rows.put("response",rows2);
				}
				else
				{
					rows.put("success","0");
					rows2.put("message","No News Found.Try Again");
					rows.put("response",rows2);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			//System.out.println(rows);
			
			
			return rows;
		}
		 
		 
	 
	public HashMap<Integer, Map<String, Object>> AddCheckPost( String login_id,int sround_id,int schedule_id,int post_no, int gate_no, int check_post_status,String code )
	{
		
	    HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String check_post ="";
		long postRound = 0;
		HashMap<Integer, Map<String, Object>> checkpost_roundList = new HashMap<Integer, Map<String, Object>>();
			try
			{
				dbop_sec = new DbOperations(DB_SECURITY);
				check_post = "insert into check_post (`login_id`,`sround_id`,schedule_id,`post_no`,`gate_no`,`check_post_status`,`time`,`timestamp`,`code`) values('" +login_id+ "','" + sround_id + "','" + schedule_id + "','" + post_no +  "','" + gate_no +  "','" + check_post_status +  "', NOW(), NOW(),'" + code +  "')";
				postRound= dbop_sec.Insert(	check_post);
				
				if(postRound >0)
				{
					rows.put("success","1");
					rows2.put("message","inserted");
					rows.put("response",rows2);
				}
				else
				{
					rows.put("success","0");
					rows2.put("message","No data Found.Try Again");
					rows.put("response",rows2);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			//System.out.println(rows);
			
			return rows;
	}
		
	public HashMap<Integer, Map<String, Object>> GetCheckpost(int type,int checkpostid)
	{
		    HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String selectdb ="",sSelectQuery1="",sScheduleName ="",ScheduleTime="", Checkposts ="";
			HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
			HashMap<Integer, Map<String, Object>> result1 = new HashMap<Integer, Map<String, Object>>();
			
			try
			{   dbop_sec = new DbOperations(DB_SECURITY);
				
				sSelectQuery1 = "SELECT * FROM `security_round` where checkpost_id='"+checkpostid+"' AND DATE(round_time)=CURDATE()";
				result = dbop_sec.Select(sSelectQuery1);
				//System.out.println(result);
				StringBuilder Checktimeid = new StringBuilder();
				String commaSeparatedList ="";
				if(result.size() > 0)
				{
					for(Entry<Integer, Map<String, Object>> entry : result .entrySet()) 
					{
						 String checkpostID=  entry.getValue().get("checkpost_id").toString();
						 String ckTimeId=  entry.getValue().get("checkpost_time_id").toString();
						 Checktimeid.append(ckTimeId.toString());
						 Checktimeid.append(",");
						 
					}
					commaSeparatedList = Checktimeid.substring(0, Checktimeid.length());
				}
				else
				{
					commaSeparatedList = Checktimeid.substring(0, Checktimeid.length());
				}
				
				commaSeparatedList=commaSeparatedList.replaceAll(",$","");
				System.out.println("Test 2 : "+ commaSeparatedList);
				if(!commaSeparatedList.equals(""))
				{  
					if(type == 0)
					{   // upcomming
						//selectdb = "SELECT c.id,c.login_id,TIME_FORMAT(c.time, '%H %i %p') as rtime,rm.desc,sm.schedule_name FROM `check_post` as c join round_master as rm on rm.id=c.sround_id join schedule_master as sm on sm.sid=c.schedule_id and c.id NOT IN("+commaSeparatedList+")";
						selectdb = "SELECT c.id,c.login_id,c.post_no,TIME_FORMAT(ct.Time, '%H %i %p') as rtime,ct.id as timeid,sm.schedule_name FROM `check_post` as c join schedule_master as sm on sm.sid=c.schedule_id join checkpost_time as ct on ct.checkpost_id = c.id where c.id='"+checkpostid+"' and ct.id NOT IN("+commaSeparatedList+")";
					}
					else
					{   // on going 
						//selectdb = "SELECT c.id,c.login_id,TIME_FORMAT(c.time, '%H %i %p') as rtime,rm.desc,sm.schedule_name FROM `check_post` as c join round_master as rm on rm.id=c.sround_id join schedule_master as sm on sm.sid=c.schedule_id and c.id IN("+commaSeparatedList+")";
						selectdb = "SELECT c.id,c.login_id,c.post_no,TIME_FORMAT(ct.Time, '%H %i %p') as rtime,ct.id as timeid,sm.schedule_name FROM `check_post` as c join schedule_master as sm on sm.sid=c.schedule_id join checkpost_time as ct on ct.checkpost_id = c.id where c.id='"+checkpostid+"' and ct.id IN("+commaSeparatedList+")";
					}
					//selectdb = "SELECT c.id,c.login_id,TIME_FORMAT(c.time, '%H %i %p') as rtime,rm.desc,sm.schedule_name FROM `check_post` as c join round_master as rm on rm.id=c.sround_id join schedule_master as sm on sm.sid=c.schedule_id and c.id IN("+commaSeparatedList+")";
					//HashMap<Integer, Map<String, Object>> result1 =  dbop_sec.Select(selectdb);
				}
				else 
				{
					//selectdb = "SELECT c.id,c.login_id,TIME_FORMAT(c.time, '%H %i %p') as rtime,rm.desc,sm.schedule_name FROM `check_post` as c join round_master as rm on rm.id=c.sround_id join schedule_master as sm on sm.sid=c.schedule_id";
					
					if(type == 0)
					{
						selectdb = "SELECT c.id,c.login_id,c.post_no,TIME_FORMAT(ct.Time, '%H %i %p') as rtime,ct.id as timeid,sm.schedule_name FROM `check_post` as c join schedule_master as sm on sm.sid=c.schedule_id join checkpost_time as ct on ct.checkpost_id=c.id where c.id='"+checkpostid+"'";
					}
					else
					{
						 selectdb = "SELECT c.id,c.login_id,c.post_no,TIME_FORMAT(ct.Time, '%H %i %p') as rtime,ct.id as timeid,sm.schedule_name FROM `check_post` as c join schedule_master as sm on sm.sid=c.schedule_id join checkpost_time as ct on ct.checkpost_id=c.id where c.id ='"+checkpostid+"' and ct.id IN('0')";
					}
				}
				
				// System.out.println(selectdb); 
				result1 = dbop_sec.Select(selectdb);
				
				for(Entry<Integer, Map<String, Object>> entry1 : result1 .entrySet()) 
				{
					
					sScheduleName = "Schedule Name : "+ entry1.getValue().get("schedule_name").toString();
					ScheduleTime = "Round Time : "+ entry1.getValue().get("rtime").toString();
					Checkposts = "No of Checkpost : "+ entry1.getValue().get("post_no").toString();
					//entry1.getValue().put("<b>Schedule Name :</b> "+entry1.getValue().get("schedule_name"));
					//entry1.getValue().put("<b>Round Time :</b> ", entry1.getValue().get("rtime"));
					entry1.getValue().put("sch_name", sScheduleName);
					entry1.getValue().put("round_Time", ScheduleTime);
					entry1.getValue().put("no_of_post", Checkposts);
				}
				
				if(result1.size() > 0)
				{
					rows.put("success","1");
					rows2.put("CheckpostList",MapUtility.HashMaptoList(result1));
					rows.put("response",rows2);
				}
				else
				{
					rows.put("success","0");
					rows2.put("message","No checkpost Found!");
					rows.put("response",rows2);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return rows;
	}
	public HashMap<Integer, Map<String, Object>> GetCheckpostDetails(int id)
	{
		 HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String Selectdata ="";
			HashMap<Integer, Map<String, Object>> results = new HashMap<Integer, Map<String, Object>>();
			
			try
			{
				dbop_sec = new DbOperations(DB_SECURITY);
				Selectdata = "SELECT id,Check_post_id,checkpost_count,Name,Code,Status FROM `gate_master` where Check_post_id ="+id;
				results = dbop_sec.Select(Selectdata);
				System.out.println(Selectdata);
				
				if(results.size()>0)
				{
					rows.put("success","1");
					rows2.put("CheckPostList",MapUtility.HashMaptoList(results));
					rows.put("response",rows2);
				}
				else
				{
					rows.put("success","0");
					rows2.put("CheckPostList","No News Found.Try Again");
					rows.put("response",rows2);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			//System.out.println(rows);
			
			
			return rows;
	}
	public HashMap<Integer, Map<String, Object>> GetScheduleList()
	{
		    HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String selectdb ="",selectwing="",wingName="";
			HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
			
			try
			{   dbop_sec = new DbOperations(DB_SECURITY);
				
				selectdb = "select c.id,c.wing,c.post_no,sm.schedule_name,r.desc from check_post as c join schedule_master as sm on sm.sid=c.schedule_id join round_master as r on r.id=c.sround_id";
				
				  
				//HashMap<Integer, Map<String, Object>> result =  dbop_sec.Select(selectdb);
				result = dbop_sec.Select(selectdb);
				
				for(Entry<Integer, Map<String, Object>> entry : result .entrySet()) 
				{
					if(entry.getValue().get("wing").toString().equals("0") || entry.getValue().get("wing").toString().equals(""))
					{
						entry.getValue().put("Wing_Name","");
						continue;
					}
					dbop_sec = new DbOperations(DB_SOCIETY);
					selectwing ="SELECT wing FROM `wing` where wing_id  = '"+entry.getValue().get("wing")+"'";
					HashMap<Integer, Map<String, Object>> wingDetails = dbop_sec.Select(selectwing);
					for(Entry<Integer, Map<String, Object>> entry1 : wingDetails.entrySet()) 
					{
						wingName = entry1.getValue().get("wing").toString();
					}
					//System.out.println("WIng"+wingName);
					entry.getValue().put("Wing_Name",wingName);
				}
				if(result.size()>0)
				{
					rows.put("success","1");
					rows2.put("ScheuleList",MapUtility.HashMaptoList(result));
					rows.put("response",rows2);
				}
				else
				{
					rows.put("success","0");
					rows2.put("message","No checkpost Found.Try Again");
					rows.put("response",rows2);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return rows;
	}
	public HashMap<Integer, Map<String, Object>> getGateMaster()
	{
		    HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String selectdb ="";
			HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
			
			try
			{   dbop_sec = new DbOperations(DB_SECURITY);
				
				  selectdb = "SELECT * FROM `gate_master`";
				  result = dbop_sec.Select(selectdb);
				
			
				if(result.size()>0)
				{
					rows.put("success","1");
					rows2.put("masterlist",MapUtility.HashMaptoList(result));
					rows.put("response",rows2);
				}
				else
				{
					rows.put("success","0");
					rows2.put("message","No checkpost Found.Try Again");
					rows.put("response",rows2);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return rows;
	}
	 public HashMap<Integer, Map<String, Object>> AddScheduleRound( int master_id, String round_type, int status )
		{
			 	
			    HashMap rows = new HashMap<>();
				HashMap rows2 = new HashMap<>();
				String schedule_round ="";
				long roundInsert = 0;
				HashMap<Integer, Map<String, Object>> security_roundList = new HashMap<Integer, Map<String, Object>>();
					try
					{
						dbop_sec = new DbOperations(DB_SECURITY);
						schedule_round = "insert into round_schedule (`master_id`,`round_type`,`status`,`timestamp`) values('" + master_id + "','" + round_type +"','" + status + "',NOW())";
						roundInsert= dbop_sec.Insert(schedule_round);
						
						if(roundInsert >0)
						{
							rows.put("success","1");
							rows2.put("message","inserted");
							rows.put("response",rows2);
						}
						else
						{
							rows.put("success","0");
							rows2.put("message","No data Found.Try Again");
							rows.put("response",rows2);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					//System.out.println(rows);
					
					return rows;
			}
	 	public HashMap<Integer, Map<String, Object>> GetScheduleRound()
		{
			    HashMap rows = new HashMap<>();
				HashMap rows2 = new HashMap<>();
				String selectData ="";
				HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
				
				try
				{   dbop_sec = new DbOperations(DB_SECURITY);
				selectData = "select master_id, round_type, status from round_schedule";
					result = dbop_sec.Select(selectData);
					
				
					if(result.size()>0)
					{
						rows.put("success","1");
						rows2.put("news",result);
						rows.put("response",rows2);
					}
					else
					{
						rows.put("success","0");
						rows2.put("message","No News Found.Try Again");
						rows.put("response",rows2);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				return rows;
		}
	 

		public HashMap<Integer, Map<String, Object>> postCheckROundData()
		{
			    HashMap rows = new HashMap<>();
				HashMap rows2 = new HashMap<>();
				String selectdb ="";
				HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
				
				try
				{   dbop_sec = new DbOperations(DB_SECURITY);
					selectdb = "select time,login_id,code from check_post";
					result = dbop_sec.Select(selectdb);
					
				
					if(result.size()>0)
					{
						rows.put("success","1");
						rows2.put("news",result);
						rows.put("response",rows2);
					}
					else
					{
						rows.put("success","0");
						rows2.put("message","No News Found.Try Again");
						rows.put("response",rows2);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				return rows;
		}
	
		public  HashMap<Integer, Map<String, Object>> updateCheckPost(int sround_id)
		{
		
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String sUpdateQuery = "";
		
			try
			{		
				dbop_sec = new DbOperations(DB_SECURITY);
				sUpdateQuery = "Update check_post set login_id='sujit',time=now(),code='jfhdkcnsg' Where sround_id ='"+ sround_id +"'";
				//System.out.println(sUpdateQuery);
				long exit = dbop_sec.Update(sUpdateQuery);
				
//				dbop_soc_root = new DbOperations(DB_ROOT_NAME);
//				sUpdateQuery = "Update `service_prd_reg` set security_status='0' Where service_prd_reg_id = '"+staff_id+"'";
//				//System.out.println(sUpdateQuery);
//				long entry = dbop_soc_root.Update(sUpdateQuery);
				//System.out.println(exit);
				if(exit  > 0)
				{
					//add member to map
					 rows2.put("StaffID",sround_id );
					 rows2.put("message","Staff exit marked" );
					 rows.put("success",1);
					 rows.put("response",rows2);			 
				}
				else
				{
					//member not found
					 rows2.put("StaffID",sround_id);
					 rows2.put("message","No Staff Inside Entry found" );
					 rows.put("success",0);
					 rows.put("response",rows2);
				}
			
			}
			catch(Exception e)
			{
				e.printStackTrace();
				rows2.put("exception ",e );
				rows.put("success",0);
				rows.put("response",rows2);
			}
			//System.out.println("satff exit"+rows);
			return rows;
		}
		
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		String sToken = "yCbgcJz2vLaYV0BZaSy1pcyNElPAfUjprvbbySCeJPe3_c7Lgr7Ad6kW1uI7MqqQTX3uS1ZJjGFooFsq1k3QkUdjeSoqQLHs2C3kIke2eSjcrkqjlytp1do3yULqraViNthJibtz9xHimUSBNx3zqvveB4CL7LQW6fukMQE4X3ugPA0uxNPZ_0nQGJssVTHO";
		
		CheckPost2 st=new CheckPost2(sToken); 
		//HashMap objHash = st.AddSecurityRound( );
		//HashMap objhashh = st.AddCheckPost( 12,3,6,1,"vnjchunvg");
		// st.GetCheckpost(1,130);
		// st.GetRoundList();
		 Gson objGson = new Gson();
		//String objStr = objGson.toJson(objHash);
		//System.out.println("Dates:"+objStr);
		 System.out.println(st.GetCheckpost(0,131));
		// System.out.println(st.AddSecurityRound(59,"security",1,130,1,""));
		
		// AddSecurityRound( int society_id, String create_by, int status,int checkpostID,String logData )
		
		
		
		//AddSecurityRound(int id, int society_id, String round_time , String create_by, int status);
    }
}
