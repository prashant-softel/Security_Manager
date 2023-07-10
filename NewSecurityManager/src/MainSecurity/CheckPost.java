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

public class CheckPost extends CommonBaseClass
{
	 private String m_sToken;
	 private static SecuriyCommonUtility.MapUtility m_objMapUtility;
	 private  TimeZoneConvertor m_Timezone;
	 public CheckPost(String token) 
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
		
		
	 public HashMap<Integer, Map<String, Object>> AddSecurityRound( int society_id, String create_by, int status,int scheduleid, int round_id,String logData )
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
					security_round = "insert into security_round (`society_id`,`round_time`,`create_by`,`status`,`checkpost_data`,`schedule_id`) values('" + society_id + "',NOW(),'" + create_by +  "','" + status +  "','"+logData+"','"+scheduleid+"')";
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
		
	public HashMap<Integer, Map<String, Object>> GetCheckpost(int type)
	{
		    HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String selectdb ="",sSelectQuery1="",sScheduleName ="",ScheduleTime="", Checkposts ="";
			HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
			HashMap<Integer, Map<String, Object>> result1 = new HashMap<Integer, Map<String, Object>>();
			
			try
			{   dbop_sec = new DbOperations(DB_SECURITY);
				
				sSelectQuery1 = "SELECT * FROM `security_round` where  DATE(round_time)=CURDATE()";
				result = dbop_sec.Select(sSelectQuery1);
				//System.out.println(result);
				StringBuilder Checkscheduleid = new StringBuilder();
				String commaSeparatedList ="";
				if(result.size() > 0)
				{
					for(Entry<Integer, Map<String, Object>> entry : result .entrySet()) 
					{
						 String securityroundid=  entry.getValue().get("id").toString();
						 String ckScheduleId=  entry.getValue().get("schedule_id").toString();
						 Checkscheduleid.append(ckScheduleId.toString());
						 Checkscheduleid.append(",");
						 
					}
					commaSeparatedList = Checkscheduleid.substring(0, Checkscheduleid.length());
				}
				else
				{
					commaSeparatedList = Checkscheduleid.substring(0, Checkscheduleid.length());
				}
				
				commaSeparatedList=commaSeparatedList.replaceAll(",$","");
				//System.out.println("Test 2 : "+ commaSeparatedList);
				if(!commaSeparatedList.equals(""))
				{  
					if(type == 0)
					{   // upcomming
						//selectdb = "SELECT sm.id,sm.round_id,sm.schedule_name,TIME_FORMAT(sm.round_time, '%H %i %p') as rtime, if(sm.frequency =1, 'Daily','Weekly') as frequency,count(rcm.id) as no_of_checkpost FROM `schedule_master` as sm right join `round_checkpost_master` as rcm on rcm.round_id =sm.round_id where sm.id NOT IN("+commaSeparatedList+") group by sm.id";
						selectdb = "SELECT sm.id,sm.round_id,sm.schedule_name,TIME_FORMAT(sm.round_time, '%H %i %p') as rtime, sm.frequency as frequency,count(rcm.id) as no_of_checkpost FROM `schedule_master` as sm right join `round_checkpost_master` as rcm on rcm.round_id =sm.round_id where sm.id NOT IN("+commaSeparatedList+") group by sm.id";
					
					}
					else
					{   // on going 
						//selectdb = "SELECT sm.id,sm.round_id,sm.schedule_name,TIME_FORMAT(sm.round_time, '%H %i %p') as rtime, if(sm.frequency =1, 'Daily','Weekly') as frequency,count(rcm.id) as no_of_checkpost FROM `schedule_master` as sm right join `round_checkpost_master` as rcm on rcm.round_id =sm.round_id where sm.id IN("+commaSeparatedList+") group by sm.id";
						selectdb = "SELECT sm.id,sm.round_id,sm.schedule_name,TIME_FORMAT(sm.round_time, '%H %i %p') as rtime, sm.frequency  as frequency,count(rcm.id) as no_of_checkpost FROM `schedule_master` as sm right join `round_checkpost_master` as rcm on rcm.round_id =sm.round_id where sm.id IN("+commaSeparatedList+") group by sm.id";
					}
					//selectdb = "SELECT c.id,c.login_id,TIME_FORMAT(c.time, '%H %i %p') as rtime,rm.desc,sm.schedule_name FROM `check_post` as c join round_master as rm on rm.id=c.sround_id join schedule_master as sm on sm.sid=c.schedule_id and c.id IN("+commaSeparatedList+")";
					//HashMap<Integer, Map<String, Object>> result1 =  dbop_sec.Select(selectdb);
				}
				else 
				{
					
					if(type == 0)
					{
						//selectdb = "SELECT sm.id,sm.round_id,sm.schedule_name,TIME_FORMAT(sm.round_time, '%H %i %p') as rtime, if(sm.frequency =1, 'Daily','Weekly') as frequency,count(rcm.id) as no_of_checkpost FROM `schedule_master` as sm right join `round_checkpost_master` as rcm on rcm.round_id =sm.round_id group by sm.id";
						selectdb = "SELECT sm.id,sm.round_id,sm.schedule_name,TIME_FORMAT(sm.round_time, '%H %i %p') as rtime, sm.frequency as frequency,count(rcm.id) as no_of_checkpost FROM `schedule_master` as sm right join `round_checkpost_master` as rcm on rcm.round_id =sm.round_id group by sm.id";
					}
					else
					{
						//selectdb = "SELECT sm.id,sm.round_id,sm.schedule_name,TIME_FORMAT(sm.round_time, '%H %i %p') as rtime, if(sm.frequency =1, 'Daily','Weekly') as frequency,count(rcm.id) as no_of_checkpost FROM `schedule_master` as sm right join `round_checkpost_master` as rcm on rcm.round_id =sm.round_id where sm.id IN('0') group by sm.id";
						selectdb = "SELECT sm.id,sm.round_id,sm.schedule_name,TIME_FORMAT(sm.round_time, '%H %i %p') as rtime, sm.frequency as frequency,count(rcm.id) as no_of_checkpost FROM `schedule_master` as sm right join `round_checkpost_master` as rcm on rcm.round_id =sm.round_id where sm.id IN('0') group by sm.id";
					}
				}
				
				// System.out.println(selectdb); 
				result1 = dbop_sec.Select(selectdb);
				
				for(Entry<Integer, Map<String, Object>> entry1 : result1 .entrySet()) 
				{
					
					sScheduleName = "Schedule Name : "+ entry1.getValue().get("schedule_name").toString();
					ScheduleTime = "Time : "+ entry1.getValue().get("rtime").toString();
					Checkposts = "No of Check.: "+ entry1.getValue().get("no_of_checkpost").toString();
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
	public HashMap<Integer, Map<String, Object>> GetCheckpostDetails(int scheduleId, int roundId)
	{
		 HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String Selectdata ="",selectpost="";
			String checkpostId ="";
			
			HashMap<Integer, Map<String, Object>> results = new HashMap<Integer, Map<String, Object>>();
			HashMap<Integer, Map<String, Object>> results1 = new HashMap<Integer, Map<String, Object>>();
			
			try
			{
				dbop_sec = new DbOperations(DB_SECURITY);
				//Selectdata = "SELECT * FROM `round_checkpost_master` where round_id = '"+roundId+"'";
				Selectdata = "SELECT * FROM `checkpost_master` as cm join round_checkpost_master as rcm on rcm.checkpost_id=cm.id where rcm.round_id ='"+roundId+"'";
				results = dbop_sec.Select(Selectdata);
				
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
	public HashMap<Integer, Map<String, Object>> GetQRCodeMatched(String QRCode,int CheckpostID)
	{
		 HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String Selectdata ="";
			
			
			HashMap<Integer, Map<String, Object>> results = new HashMap<Integer, Map<String, Object>>();
			//HashMap<Integer, Map<String, Object>> results1 = new HashMap<Integer, Map<String, Object>>();
			
			try
			{
				dbop_sec = new DbOperations(DB_SECURITY);
				//Selectdata = "SELECT * FROM `round_checkpost_master` where round_id = '"+roundId+"'";
				Selectdata = "SELECT * FROM `checkpost_master` where `qrcode` = '"+QRCode+"'";
				results = dbop_sec.Select(Selectdata);
				int resID=0; 
				if(results.size() > 0)
				{
					for(Entry<Integer, Map<String, Object>> entry : results .entrySet()) 
					{
						resID = (int) entry.getValue().get("id");
						
					}
				}
				if(resID == CheckpostID)
				{
					//if(results.size()>0)
					//{
					rows.put("success","1");
					rows2.put("MatchResult",MapUtility.HashMaptoList(results));
					rows.put("response",rows2);
				}
				else
				{
					rows.put("success","0");
					rows2.put("MatchResult","Data Not Matched");
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
		String sToken = "1Fw2ckpu0FEn87nZdy-xSzhQ_-l4jsCEsZ88x8C5wP3nNkMbV3KwBZuIjriCsJS-UhQc2Ir-UdWmJwpIv6ELGP-fSiY_is3Qfk2KCL49QCWKWfFFI8r0c-N_mVSyfdKLBWqze4uT7-3IoUUC-qFWagzfovn_B4AGQ5Lww984x8jNMGKLL4MqZIv_arxPQoVD";
		
		CheckPost st=new CheckPost(sToken); 
		//HashMap objHash = st.AddSecurityRound( );
		//HashMap objhashh = st.AddCheckPost( 12,3,6,1,"vnjchunvg");
		// st.GetCheckpost(1,130);
		// st.GetRoundList();
		 Gson objGson = new Gson();
		//String objStr = objGson.toJson(objHash);
		//System.out.println("Dates:"+objStr);
		System.out.println(st.GetQRCodeMatched("59002",1));
		// System.out.println(st.AddSecurityRound(59,"security",1,1,1,""));
		
		// AddSecurityRound( int society_id, String create_by, int status,int checkpostID,String logData )
		
		
		
		//AddSecurityRound(int id, int society_id, String round_time , String create_by, int status);
    }
}
