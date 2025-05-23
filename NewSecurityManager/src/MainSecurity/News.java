package MainSecurity;
import static MainSecurity.DbConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import SecuriyCommonUtility.MapUtility;

		
public class News extends CommonBaseClass
{
	//public  DbOperations dbop;
//	private static SecuriyCommonUtility.MapUtility m_objMapUtility;

	//ProjectConstants m_objProjectConstants;
	//private  SecuriyCommonUtility.MapUtility m_objMapUtility;
	//public  DbOperations dbop;
	public News(String token) 
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
	public  HashMap<Integer, Map<String, Object>> getHomePageNews()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String news ="";
		HashMap<Integer, Map<String, Object>> newsList = new HashMap<Integer, Map<String, Object>>();
		try
		{
			news = "Select title,description from news ORDER BY edited_on DESC LIMIT 3";
			
			newsList = dbop_sec.Select(news);
			
			if(newsList.size()>0)
			{
				rows.put("success","1");
				rows2.put("news",MapUtility.HashMaptoList(newsList));
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
	
	
	public  HashMap<Integer, Map<String, Object>> getAllNews()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String news ="";
		HashMap<Integer, Map<String, Object>> newsList = new HashMap<Integer, Map<String, Object>>();
		try
		{
			news = "Select title,description from news order By id DESC ";//put condition on news to be displayed on the main news page
			newsList = dbop_sec.Select(news);
			
			if(newsList.size()>0)
			{
				rows.put("success","1");
				rows2.put("news",MapUtility.HashMaptoList(newsList));
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
	
	public  HashMap<Integer, Map<String, Object>> getEnableOTPFlag(int iSocietyID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String Features ="";
		HashMap<Integer, Map<String, Object>> FeaturesList = new HashMap<Integer, Map<String, Object>>();
		try
		{
			Features = "Select * from feature_setting";
			FeaturesList = dbop_sec.Select(Features);
			
			if(FeaturesList.size()>0)
			{
				rows.put("success","1");
				rows2.put("Setting",FeaturesList);
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","0");
				rows2.put("Setting","No Setting Found.Try Again");
				rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(rows);
		
		return rows;
	}
	
	public  HashMap<Integer, Map<String, Object>> updateNews(int loginId, String newsTitle, String newsDescription)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String newsUpdate ="";
		long updateStatus = 0;
		try
		{
			newsUpdate = "insert into news (updated_by,title,description) values ('"+loginId+"','"+newsTitle+"','"+newsDescription+"')";
			updateStatus = dbop_sec.Insert(newsUpdate);
			if(updateStatus > 0)
			{
				rows.put("success","1");
				rows2.put("message","News Updated");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","0");
				rows2.put("message","News Updation Failed");
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
	/*   ---------------------  SOS ALert msg ---------------------------- */
	public  HashMap<Integer, Map<String, Object>> getSOSAlert()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String SelectSOS ="";
		HashMap<Integer, Map<String, Object>> SOSAlertList = new HashMap<Integer, Map<String, Object>>();
		try
		{
			SelectSOS = "SELECT * FROM `sos_alert` where `AlertStatus`='0' and (`RaisedTimestamp` > DATE_SUB(now(), INTERVAL 1 DAY))";
			SOSAlertList = dbop_sec.Select(SelectSOS);
			//System.out.println("QUery :"+SelectSOS);
			if(SOSAlertList.size()>0)
			{
				rows.put("success","1");
				rows2.put("SOSAlert",SOSAlertList);
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","0");
				rows2.put("SOSAlert","No Alert");
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
	/* ---------------------show alert ----------------------------- */
	public  HashMap<Integer, Map<String, Object>> ShowAlert(int iALertID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String SelectSOS ="";
		HashMap<Integer, Map<String, Object>> ShowAlertList = new HashMap<Integer, Map<String, Object>>();
		try
		{
			SelectSOS = "SELECT * FROM `sos_alert` where sosID='"+iALertID+"'";
			ShowAlertList = dbop_sec.Select(SelectSOS);
			//System.out.println("QUery :"+SelectSOS);
			if(ShowAlertList.size()>0)
			{
				rows.put("success","1");
				rows2.put("SOSAlert",ShowAlertList);
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","0");
				rows2.put("SOSAlert","No Alert");
				rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	public  HashMap<Integer, Map<String, Object>> ResolvedIssues(int iALertID,String sLoginName,String sRole,String GateNo)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sUpdateQuery = "",sInsertQuery="",SelectQuery="";
		long result = 0,InsertResult=0;
		HashMap<Integer, Map<String, Object>> ShowAlertList = new HashMap<Integer, Map<String, Object>>();
		try
		{
			//if(SubmitType == 1)
			//{
				sUpdateQuery = "update sos_alert set AlertStatus='1', 	ClosedTimeStamp=now() where sosID='"+iALertID+"'";
				result = dbop_sec.Update(sUpdateQuery);
				sInsertQuery = "Insert into sos_Acknowledge (`sos_id`,`sos_acked_by`,`ack_Role`,`gate_no`) value ('"+iALertID+"','"+sLoginName+"','"+sRole+"','"+GateNo+"')";
				InsertResult = dbop_sec.Insert(sInsertQuery);
				//System.out.println("UpdateQuery :" +sUpdateQuery);
				//System.out.println("InsertResult :" +InsertResult);
				if( result  == 0)
				{
					rows.put("success","0");
					rows2.put("message"," Please try again");
					rows.put("response",rows2);
				}
				else
				{
					rows.put("success","1");
					rows2.put("message","Updated Successfully");
					rows2.put("Unitstatus",result);
					//rows2.put("GateNo",GateNo);
					rows.put("response",rows2);
				}
			//}
			/*else
			{
				SelectQuery ="Select * from sos_alert where sosID='"+iALertID+"' and AlertStatus IN (1,2)";
				ShowAlertList=dbop.Select(SelectQuery); 
				if(ShowAlertList.size() > 0)
				{
					rows.put("success","1");
					rows2.put("message","Updated Successfully");
					rows2.put("Unitstatus",ShowAlertList);
				}
				else
				{
					rows.put("success","0");
					rows2.put("message"," Please try again");
					rows.put("response",rows2);
				}
			}*/
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	public  HashMap<Integer, Map<String, Object>> getSocietyContact(int SocietyID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String Selectdb ="";
		HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
		
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
/*			
			Selectdb = "select `society_code`,`dbname` from `society` where society_id='"+SocietyID+"'";
			result = dbop_soc_root.Select(Selectdb);
			//System.out.println(Selectdb);
			String SocietyDbName="";
			String SocietyCode="";
			for(Entry<Integer, Map<String, Object>> entry : result .entrySet()) 
			{
				SocietyDbName = entry.getValue().get("dbname").toString();
				SocietyCode = entry.getValue().get("society_code").toString();
			//	System.out.println(SocietyDbName);
			}
			//System.out.println(SocietyDbName);
			*/
			
			dbop_soc = new DbOperations(DB_SOCIETY);
			Selectdb = "select phone,phone2 from `society` where society_id='"+SocietyID+"'";
			result = dbop_soc.Select(Selectdb);
			//}//
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
		//System.out.println(rows);
		
		return rows;
	}
	
	public  HashMap<Integer, Map<String, Object>> CheckResolvedStatus(int iAlertID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String SelectSOS ="";
		HashMap<Integer, Map<String, Object>> checkAlertList = new HashMap<Integer, Map<String, Object>>();
		try
		{
			SelectSOS = "SELECT s.*,sack.* FROM `sos_alert` as s join sos_Acknowledge as sack on s.sosID = sack.sos_id where s.sosID='"+iAlertID+"' and s.AlertStatus = '2' and sack.ack_Role ='Self' order by sack.id desc LIMIT 1";
			checkAlertList = dbop_sec.Select(SelectSOS);
			//System.out.println("QUery :"+SelectSOS);
			if(checkAlertList.size()>0)
			{
				rows.put("success","1");
				rows2.put("SOSAlert",checkAlertList);
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","0");
				rows2.put("SOSAlert","No Alert");
				rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	
	
	public  HashMap<Integer, Map<String, Object>> updatelogin(String sLoginName,String password,String device_id)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sUpdateQuery = "",sInsertQuery="",SelectQuery="";
		long result = 0,InsertResult=0;
		HashMap<Integer, Map<String, Object>> ShowAlertList = new HashMap<Integer, Map<String, Object>>();
		try
		{
			//if(SubmitType == 1)
			//{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			sUpdateQuery = "update login set login_status = '1',device_id = '"+device_id+"' where  `member_id` = '" + sLoginName + "' and password = '" +password+ "' and status = 'Y'";
				result = dbop_soc_root.Update(sUpdateQuery);
				if( result  == 0)
				{
					rows.put("success","0");
					rows2.put("message"," Please try again");
					rows.put("response",rows2);
				}
				else
				{
					rows.put("success","1");
					rows2.put("message","Updated Successfully");
				
					rows.put("response",rows2);
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	public  HashMap<Integer, Map<String, Object>> updatelogout(String sLoginName,String password)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sUpdateQuery = "",sInsertQuery="",SelectQuery="";
		long result = 0,InsertResult=0;
		HashMap<Integer, Map<String, Object>> ShowAlertList = new HashMap<Integer, Map<String, Object>>();
		try
		{
			//if(SubmitType == 1)
			//{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			sUpdateQuery = "update login set login_status = '0',device_id = '0' where  `member_id` = '" + sLoginName + "' and password = '" +password+ "' and status = 'Y'";
			result = dbop_soc_root.Update(sUpdateQuery);
			if( result  == 0)
			{
				rows.put("success","0");
				rows2.put("message"," Please try again");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","1");
				rows2.put("message","Updated Successfully");
			
				rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			rows.put("success","0");
			rows2.put("message"," Please try again");
			rows.put("response",rows2);
		}
		
		return rows;
	}
	public  HashMap<Integer, Map<String, Object>> getDeviceId(int UnitId,int SocietyId)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String SelectQuery="";
		
		int loginId = 0;
		String sMapID="";
		HashMap<Integer, Map<String, Object>> loginDetails = new HashMap<Integer, Map<String, Object>>();
		try
		{
			//if(SubmitType == 1)
			//{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			//String sqlLoginDetails = "Select id,login_id,unit_id from mapping where unit_id = "+UnitId+" and society_id = "+SocietyId+";";
			String sqlLoginDetails = "Select m.id as mapId,m.login_id,m.unit_id,d.device_id from mapping as m join device_details as d on d.login_id=m.login_id where m.unit_id = "+UnitId+" and m.society_id = "+SocietyId+" AND d.device_id <> '' ORDER BY d.id DESC";
			loginDetails = dbop_soc_root.Select(sqlLoginDetails);
			/*for(Entry<Integer, Map<String, Object>> entry1 : loginDetails.entrySet()) 
			{
				if(Integer.parseInt(entry1.getValue().get("unit_id").toString()) != 0)
				{
					sMapID = entry1.getValue().get("id").toString();
				}
				if(Integer.parseInt(entry1.getValue().get("login_id").toString()) != 0)
				{
					loginId = Integer.parseInt(entry1.getValue().get("login_id").toString());
					String sqlDeviceDetails = "Select device_id from device_details where login_id = "+loginId+" AND device_id <> '' ORDER BY id DESC LIMIT 0,1 ";
					HashMap<Integer, Map<String, Object>> deviceDetails = dbop_soc_root.Select(sqlDeviceDetails);
					//rows2.put("device_id", deviceDetails);
					entry1.getValue().put("device_id", deviceDetails);
				}
			}*/
			 
			if(loginDetails.size() > 0)
			{
				rows2.put("devices",MapUtility.HashMaptoList(loginDetails) );
				rows.put("success",1);
				rows.put("response",rows2);	
				
			}
			else
			{
				rows2.put("message ","");
				rows.put("success",0);
				rows.put("response",rows2);
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			rows.put("success","0");
			rows2.put("message"," Please try again");
			rows.put("response",rows2);
		}
		
		return rows;
	}
	
	public static void main(String[] args) //throws Exception
	{
		String sToken = "W4t7pR5degU6vlCFzVGTH8lopm0ug0Hcl2TuuH1ucdHLbTnnp6PLv_drgqZtnXzfOULjQoRtEGlF8QEPM8AI97F-c08ECuoozP1XgTp_ExxXt0478lTS2SjNDQJLBA9k9gjoDTbSA2_1qF9AvY2C1Wb_E5PO_jJ7QYJS6wOAn7Brh27A2QlE5upEIItM1iuD";
		News news = new News(sToken); //security
		HashMap objHash = news.getDeviceId(26,59);
		//news.getSocietyContact(156);
		//news.getHomePageNews();
//		HashMap objHash = news.updatelogin("sujit0304","0304","");
		//HashMap objHash = news.updatelogout("sujit0304","0304");
		Gson objGson = new Gson();
		String objStr = objGson.toJson(objHash);
		System.out.println("Dates:"+objStr);
		
	}

	
}