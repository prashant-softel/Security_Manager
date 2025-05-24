package MainSecurity;

import static MainSecurity.DbConstants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.google.gson.Gson;

import SecuriyCommonUtility.MapUtility;

public class OTP extends CommonBaseClass
{

	//ProjectConstants m_objProjectConstants;
	//private  SecuriyCommonUtility.MapUtility m_objMapUtility;
	//public  DbOperations dbop;
	public OTP(String token) 
	{
		super(token);
		//m_objProjectConstants = new ProjectConstants();
		//m_objMapUtility = new MapUtility();
		//DB_SOCIETY = "hostmjbt_society46";
		//DB_SECURITY = "sm_1";
		
		try
		{
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	//public static  HashMap<Integer, Map<String, Object>> createVisitor(int iLoginID, String sFname,String sLname,String sMobile) throws ClassNotFoundException 
	public   HashMap<Integer, Map<String, Object>> createVisitor(int iLoginID,String sMobile,String SocietyName) //throws ClassNotFoundException 
	{
		//------------------Creating connection to Security database of societies------------------------------
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> db = null;
		long lCreateVisID = 0;
		 
		try
		{
			String vcompany = "";
			String vvehicle="";
			String otpSent = String.format("%04d", new Random().nextInt(10000));
			//String Send_Status;
			//String username = "societ";
			//Your authentication key
			String authkey = "25E434CE63126E";//"8cce074932XX";
			//Multiple mobiles numbers separated by comma (max 200)
			
			//Sender ID,While using route4 sender id should be 6 characters long.
			String senderId = "SECURE";
			//Your message to send, Add URL encoding here.
			//String message = "Your OTP is "+otpSent+ " valid for 2 Minutes Only";
			String message = "Welcome to "+SocietyName+"! The OTP for security verification  is "+otpSent+ " valid for 2 minutes only. Powered by Way2Society.com";
			//define route
			String accusage="1";
			
			//Prepare Url
			HttpURLConnection myURLConnection=null;
			URL myURL=null;
			BufferedReader reader=null;

			//encoding message
			String encoded_message=URLEncoder.encode(message);
			
			//Send SMS API
			//String mainUrl="http://sms.transaction.surewingroup.infosubmitsms.jsp?";
			String mainUrl="http://kutility.in/app/smsapi/index.php?";
			//mainUrl = "http://way2society.surewingroup.info/submitsms.jsp?";
			try
			{
				String sRoute = "415";
				String sType = "flash";
				//Prepare parameter string
				StringBuilder sbPostData= new StringBuilder(mainUrl);
				//sbPostData.append("user="+username);
				sbPostData.append("key="+authkey);
				sbPostData.append("&routeid="+sRoute);
				sbPostData.append("&type="+sType);
				sbPostData.append("&contacts="+sMobile);
				sbPostData.append("&senderid="+senderId);
				sbPostData.append("&msg="+encoded_message);
				//sbPostData.append("&accusage="+accusage);
				
				
				//final string
				mainUrl = sbPostData.toString();
				System.out.println("mainUrl......"+mainUrl.toString());
				
				//prepare connection
				myURL = new URL(mainUrl);
				myURLConnection =(HttpURLConnection)myURL.openConnection();
				myURLConnection.connect();
				
				reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
				//reading response
				String response;
				//System.out.println(myURLConnection.getResponseCode());
				while ((response = reader.readLine()) != null)
				//print response
				{	
					System.out.println("OTP Response......"+response);
				}
				
//				if(myURLConnection.getResponseCode()==200)
//				{
//					System.out.println("successfully sent otp to "+sMobile);
//				}
//				else
//				{
//					System.out.println("otp sending failed");
//				}
			
				//finally close connection
				reader.close();
				
			}
			catch (IOException e)
			{
				System.out.println("error message :   "+e.getMessage());
				e.printStackTrace();
			}
			
			int id=0;
			String no="";
			System.out.println("DB_SECURITY " + DB_SECURITY);
			dbop_sec = new DbOperations(DB_SECURITY);
			String sLoginID = this.getLoginID();
			//String sQuery = "INSERT INTO `visitordetails` (`login_id`, `visitorFname`, `visitorLname`,`visitorMobile`,`doc_id`,`doc_no`,`otpGenerated`,`company`,`vehicle`) VALUES ('"+iLoginID+"','"+sFname+"', '"+sLname+"', '"+sMobile+"','"+id+"','"+no+"','"+otpSent+"','"+vcompany+"','"+vvehicle+"')";
			String sQuery = "INSERT INTO `visitorentry` (`login_id`,`visitorMobile`,`otpGenerated`) VALUES ('"+iLoginID+"', '"+sMobile+"','"+otpSent+"')";
			//System.out.println(sQuery);
			lCreateVisID = dbop_sec.Insert(sQuery);
			
			//System.out.println("sQuery : " + sQuery);
			//System.out.println("lCreateVisID : " + lCreateVisID);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		if(lCreateVisID  == 0)
		{
			rows.put("success","0");
			rows2.put("message","Failed to add Visitors Details. Please try again");
			rows.put("response",rows2);
		}
		else
		{
	        rows.put("success","1");
			rows2.put("message","Visitor Entered Successfully");
			rows2.put("ServiceReqID", lCreateVisID);
			rows.put("response",rows2);
		}
		return rows;
	}
	
	public   HashMap<Integer, Map<String, Object>> createVisitorapi(String sMobile,String SocietyName) throws ClassNotFoundException 
	{
		//------------------Creating connection to Security database of societies------------------------------
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> db = null;
		long lCreateVisID = 0;
		 String otpSent = "",message ="";
		try
		{
			String vcompany = "";
			String vvehicle="";
			otpSent = String.format("%04d", new Random().nextInt(10000));
			message = "Welcome to "+SocietyName+"! The OTP for security verification  is "+otpSent+ " valid for 2 minutes only. Powerd by Way2society.com";
			
			String sLoginID = this.getLoginID();

			//String sQuery = "INSERT INTO `visitordetails` (`login_id`, `visitorFname`, `visitorLname`,`visitorMobile`,`doc_id`,`doc_no`,`otpGenerated`,`company`,`vehicle`) VALUES ('"+iLoginID+"','"+sFname+"', '"+sLname+"', '"+sMobile+"','"+id+"','"+no+"','"+otpSent+"','"+vcompany+"','"+vvehicle+"')";
			String sQuery = "INSERT INTO `visitorentry` (`login_id`,`visitorMobile`,`otpGenerated`) VALUES ('"+sLoginID+"', '"+sMobile+"','"+otpSent+"')";
			//System.out.println(sQuery);
//			System.out.println("DB_SECURITY " + DB_SECURITY);
			dbop_sec = new DbOperations(DB_SECURITY);
			lCreateVisID = dbop_sec.Insert(sQuery);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		if(lCreateVisID  == 0)
		{
			rows.put("success","0");
			rows2.put("message","Failed to add Visitors Details. Please try again");
			rows.put("response",rows2);
		}
		else
		{
	        rows.put("success","1");
			rows2.put("message","Visitor Entered Successfully");
			rows2.put("ServiceReqID", lCreateVisID);
			rows2.put("msg", message);
			rows.put("response",rows2);
		}
		return rows;
	}

	
	public  HashMap<Integer, Map<String, Object>> verifyOtp(String otp,String mobileNo,int vid) //throws ClassNotFoundException
	{
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String new_dbname ="";
		HashMap<Integer, Map<String, Object>> db = new HashMap<Integer, Map<String, Object>>();
		
		try
		{
			System.out.println("DB_SECURITY " + DB_SECURITY);
			dbop_sec = new DbOperations(DB_SECURITY);
			
			//String sQuery = "Select otpGenerated,DATE_FORMAT( CONVERT_TZ( otpGtimestamp,  '+00:00',  '+0:00' ) , '%d-%m-%Y %T' ) as gtime From visitordetails Where visitorMobile = '" + mobileNo + "'";
			String sQuery = "Select otpGenerated,DATE_FORMAT( CONVERT_TZ( otpGtimestamp,  '+00:00',  '+0:00' ) , '%d-%m-%Y %T' ) as gtime From `visitorentry` Where visitorMobile = '" + mobileNo + "' and otpGenerated = '" + otp + "'";
			System.out.print(sQuery+"\n");
			HashMap<Integer, Map<String, Object>> otpVerify = dbop_sec.Select(sQuery);
			//System.out.print(otpVerify+"\n");
			long result = 0;
			String status = null;
			
			for(Entry<Integer, Map<String, Object>> entry : otpVerify.entrySet()) 
			{
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String dateotp=  (String) entry.getValue().get("gtime");
				Date d = format.parse(dateotp);
//				System.out.println("date rceived --->" + d +" db time: --->"+d.getTime());
//				System.out.println("db time min : "+TimeUnit.MILLISECONDS.toMinutes(d.getTime()));
				
				Date now = new Date();
//				System.out.println("c date ---> "+now+" current time: "+now.getTime());
//				System.out.println("current time min: "+TimeUnit.MILLISECONDS.toMinutes(now.getTime()));
				
				long timeUp = 120000;// otp validity time 2 min (1 min = 60000)
				//System.out.println("diff: "+ (now.getTime() - d.getTime()));
				
				status = ((now.getTime() - d.getTime()) > timeUp ? "expired" : "valid");
//				System.out.println(status);
				
				result = dbop_sec.Update("update `visitorentry` set otpStatus= '"+status+"' Where id='"+vid+"' AND otpGenerated='"+otp+"'");
				System.out.println("----------------- Result : "+result+"---------------");
			}
			if( result  == 0)
			{
				rows.put("success","0");
				rows2.put("message","OTP Verification Failed. Please try again");
				rows.put("response",rows2);
			}
			else if(status.equals("expired") && result  == 1)
			{
		        rows.put("success","1");
				rows2.put("message","OTP expired");
				rows2.put("Verified","1");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","1");
				rows2.put("message","OTP Verified");
				rows2.put("Verified","2");
				rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	
	//Image function
//												submit1(int loginId,String SocietyId,String unit,int purpose,int vEntryID, int VisitorRepetedId,String Mobile,String EntryGate,String Fname,String LName,String company,String Vehicle,String note,int EntryDoc_id, float Temp,float Oxygen,int Pulse) throws ClassNotFoundException
	
	public  HashMap<Integer, Map<String, Object>> submit(String socid,int LoginID, String vcompany,String vvehicle,String unitID, int purposeID,int visitorEntryId,int visitorId,String Mobile, int EnryGateNo,String vNote,int EntryDoc_id, float Temp,float Oxygen,int Pulse) throws ClassNotFoundException
	{
		//company,vehicle,unit,purpose,vID,verified
		//long visitorEntryId = 0;
		String prev_sent_check = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//HashMap rows3 = new HashMap<>();
		String new_dbname ="", visitorName = "",purpose="",unitNo="",memName="";
		HashMap<Integer, Map<String, Object>> db = new HashMap<Integer, Map<String, Object>>();
		String sUpdateQuery = "";
		try
		{
			vvehicle=vvehicle.toUpperCase();
			long result = 0;
			String status = null;
			dbop_sec = new DbOperations(DB_SECURITY);
			if(visitorEntryId > 0)
			{
				//visitorEntryId = vid;
				//sUpdateQuery ="update visitordetails set company= '"+vcompany+"',vehicle='"+vvehicle+"', unit_id='"+unitID+"',purpose_id='"+purposeID+"',verified='"+verified+"' Where id='"+vid+"'";
				if(Temp > 0 && Oxygen > 0 && Pulse > 0) 
				{
					sUpdateQuery ="update visitorentry set Entry_With = ' " +EntryDoc_id + "',visitor_ID = '"+visitorId+"',company= '"+vcompany+"',vehicle='"+vvehicle+"', unit_id='"+unitID+"',purpose_id='"+purposeID+"', Entry_Gate='"+EnryGateNo+"',visitor_note='"+vNote+"',temp='"+Temp+"',oxygen='"+Oxygen+"',pulse='"+Pulse+"' Where id='"+visitorEntryId+"'";
				}
				else 
				{
					sUpdateQuery ="update visitorentry set Entry_With = ' " +EntryDoc_id + "',visitor_ID = '"+visitorId+"',company= '"+vcompany+"',vehicle='"+vvehicle+"', unit_id='"+unitID+"',purpose_id='"+purposeID+"', Entry_Gate='"+EnryGateNo+"',visitor_note='"+vNote+"' Where id='"+visitorEntryId+"'";
				}
				result = dbop_sec.Update(sUpdateQuery);
				//System.out.println(result);
			}
			else
			{
				if(Temp > 0 && Oxygen > 0 && Pulse > 0) sUpdateQuery = "INSERT INTO `visitorentry` (`login_id`,`visitor_ID`,`visitorMobile`,`unit_id`,`purpose_id`,`company`,`vehicle`,`otpGenerated`,`status`,`Entry_Gate`,`visitor_note`,Entry_With,temp,oxygen,pulse) VALUES ('"+LoginID+"','"+visitorId+"','"+Mobile+"','"+unitID+"','"+purposeID+"','"+vcompany+"','"+vvehicle+"','none','inside','"+EnryGateNo+"','"+vNote+"',' " +EntryDoc_id +"','"+Temp+"','"+Oxygen+"','"+Pulse+ "')";
				else sUpdateQuery = "INSERT INTO `visitorentry` (`login_id`,`visitor_ID`,`visitorMobile`,`unit_id`,`purpose_id`,`company`,`vehicle`,`otpGenerated`,`status`,`Entry_Gate`,`visitor_note`,Entry_With) VALUES ('"+LoginID+"','"+visitorId+"','"+Mobile+"','"+unitID+"','"+purposeID+"','"+vcompany+"','"+vvehicle+"','none','inside','"+EnryGateNo+"','"+vNote+"',' " +EntryDoc_id + "')";
				visitorEntryId = (int)dbop_sec.Insert(sUpdateQuery);
			}
			//System.out.println(sUpdateQuery);
			//System.out.println("VisitorEntryId : "+visitorEntryId);
			//result = Update("update visitordetails set company= '"+vcompany+"',vehicle='"+vvehicle+"', unit_id='"+unitID+"',purpose_id='"+purposeID+"',verified='"+verified+"' Where id='"+vid+"'");
			//System.out.println("------------"+result+"---------------");
			dbop_soc = new DbOperations(DB_SOCIETY);
			List<String> Unit = Arrays.asList(unitID.split(","));
			System.out.println("outside loop unitID " + unitID);
			
			for (int i = 0; i < Unit.size(); i++)
			{
			String sqlForunitNo = "Select u.`unit_no`, m.`owner_name` from unit as u, member_main as m where u.`unit_id` = '"+ Unit.get(i)+"' and u.society_id='"+socid+"' and u.`unit_id` = m.`unit` and m.`status` = 'Y' and m.`ownership_status` = 1";
			HashMap<Integer, Map<String, Object>> unitMemDetails = dbop_soc.Select(sqlForunitNo);
			//System.out.println("unitMemDetails: "+unitMemDetails);
			for(Entry<Integer, Map<String, Object>> entry1 : unitMemDetails.entrySet()) 
			{
				unitNo = entry1.getValue().get("unit_no").toString();
				memName = entry1.getValue().get("owner_name").toString();
				System.out.println("inside loop unitNo " + unitNo);
			}
			}
			if( result  == 0)
			{
				//System.out.println("inside lf");
				rows.put("success","0");
				rows2.put("message","OTP Verification Failed. Please try again");
				rows.put("response",rows2);
			}
			else
			{
				//System.out.println("inside else");
				dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
				//String sqlVisitorDetails = "Select CONCAT(`visitorFname`,' ',`visitorLname`) as name from visitordetails where id = "+visitorId+";";
				String sqlVisitorDetails = "Select CONCAT(`Fname`,' ',`Lname`) as name from `visitors` where visitor_id = "+visitorId+";";
				HashMap<Integer, Map<String, Object>> visitorDetails = dbop_sec_root.Select(sqlVisitorDetails);
				//System.out.println("visitorDetails:"+sqlVisitorDetails);
				for(Entry<Integer, Map<String, Object>> entry : visitorDetails.entrySet()) 
				{
					 visitorName = entry.getValue().get("name").toString();
				}
				dbop_sec = new DbOperations(DB_SECURITY);
				String sqlPurposeDetails = "Select purpose_name from purpose where purpose_id = "+purposeID+";";
				HashMap<Integer, Map<String, Object>> purposeDetails = dbop_sec.Select(sqlPurposeDetails);
				for(Entry<Integer, Map<String, Object>> entry : purposeDetails.entrySet()) 
				{
					 purpose = entry.getValue().get("purpose_name").toString();
				}
				int loginId = 0;
				String sMapID="";
				dbop_soc_root = new DbOperations(DB_ROOT_NAME);
				List<String> Unit1 = Arrays.asList(unitID.split(","));
				
				for (int i = 0; i < Unit1.size(); i++)
				{
					
				//String sqlLoginDetails = "Select id,login_id from mapping where unit_id = "+Unit1.get(i)+";";
				String sqlLoginDetails = "Select id,login_id,unit_id from mapping where unit_id = "+Unit1.get(i)+";";
				HashMap<Integer, Map<String, Object>> loginDetails = dbop_soc_root.Select(sqlLoginDetails);
				//System.out.println("Login details:"+loginDetails);
				for(Entry<Integer, Map<String, Object>> entry1 : loginDetails.entrySet()) 
				{
					// add condition in unit id 0
					if(Integer.parseInt(entry1.getValue().get("unit_id").toString()) != 0)
					{
						sMapID = entry1.getValue().get("id").toString();
					
					//System.out.println("MapID:"+sMapID);
					if(Integer.parseInt(entry1.getValue().get("login_id").toString()) != 0)
					{
						loginId = Integer.parseInt(entry1.getValue().get("login_id").toString());
						String sqlDeviceDetails = "Select device_id from device_details where login_id = "+loginId+";";
						//String sqlDeviceDetails ="SELECT d.login_id, l.login_id, l.member_id, d.device_id, m.id as 'map_id' from `device_details` as d JOIN `login` as l on l.login_id = d.login_id JOIN `mapping` as m on m.login_id = d.login_id WHERE m.unit_id = '" +unitID+  "' AND m.society_id = '"+society_id+ "' AND l.member_id = '"+sUserEmail+ "' AND d.device_id <> '' ORDER BY d.id DESC LIMIT 0,1";
						//System.out.println("Query: "+sqlDeviceDetails);
						HashMap<Integer, Map<String, Object>> deviceDetails = dbop_soc_root.Select(sqlDeviceDetails);
						//System.out.println(deviceDetails);
						String deviceId="",title ="",msg = "";
						AndroidPush ap = new AndroidPush();
						for(Entry<Integer, Map<String, Object>> entry2 : deviceDetails.entrySet()) 
						{
							deviceId = entry2.getValue().get("device_id").toString().trim();
						}
						//System.out.println("Device Id: "+deviceId);
						title = "Visitor Notification";
						//msg = memName+", visitor "+visitorName.toUpperCase()+" is verified and is on the way to Unit No "+unitNo+" for "+purpose+" .";
						//if(company.lenth > 0 )
						//{
							//msg = "Visitor "+visitorName.toUpperCase()+" from "+company_name+" is on the way to Unit No "+unitNo+" for "+purpose+" .";
						//}
						//else
						//{
							msg = "Visitor"+visitorName.toUpperCase()+" is on the way to Unit No "+unitNo+" for "+purpose+" .";
						//}
						if(deviceId == "")
						{
						
						}
						else
						{
							if(prev_sent_check != msg){
								prev_sent_check = msg;
								String EntryID=String.valueOf(visitorEntryId);
								ap.sendPushNotification(title,msg, deviceId, sMapID, "10", "VisitorInPage",EntryID); 
								//String sTitle, String sMessage, String sDeviceToken, String sMapID, String sPageRef, String sPageName, String sDetails
							}
						}
					}
				}
				}
				}
			}
				rows.put("success","1");
				rows2.put("message","Details Insert");
				rows2.put("VisitorID",visitorEntryId);
				rows2.put("VisiInID",visitorId);
				//rows2.put("PushNotificationStatus", pushNotificationResult);
				rows.put("response",rows2);
			//}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//System.out.println(rows);
		return rows;
	}
	
	public  HashMap<Integer, Map<String, Object>> submitapproval(int v_id,String unitID,String Approval) throws ClassNotFoundException
	{
		
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//String new_dbname ="",visitorName="";
		//HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
		String sInsertQuery = "";
		long result = 0;
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			List<String> Unit = Arrays.asList(unitID.split(","));
			List<String> ApprovedFlag = Arrays.asList(Approval.split(","));
			
			for (int i = 0; i < Unit.size(); i++)
			{
				String approve_flag = ApprovedFlag.get(i);
			sInsertQuery ="INSERT INTO `visit_approval`(`v_id`,`unit_id`, `Entry_flag`) VALUES  ('"+v_id+"','"+Unit.get(i)+"','"+ ApprovedFlag.get(i)+"')";
			//System.out.println("Submit Without Otp :"+sInsertQuery); 
			result = dbop_sec.Insert(sInsertQuery);
			 //long attend = dbop_sec.Insert(sInsertQuery);
			
			if( result  == 0)
			{
				rows.put("success","0");
				rows2.put("message"," Please try again");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","1");
				rows2.put("message","Details Inserted");
				
				rows.put("response",rows2);
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	
	
	
	public  HashMap<Integer, Map<String, Object>> submitapprovalnew(int v_id,String unitID,String Approval,String id,String name) throws ClassNotFoundException
	{
		
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//String new_dbname ="",visitorName="";
		//HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
		String sInsertQuery = "";
		long result = 0;
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			List<String> Unit = Arrays.asList(unitID.split(","));
			List<String> ApprovedFlag = Arrays.asList(Approval.split(","));
			
			for (int i = 0; i < Unit.size(); i++)
			{
				String approve_flag = ApprovedFlag.get(i);
			sInsertQuery ="UPDATE `visit_approval` set `Entry_flag`='"+ApprovedFlag.get(i)+"',login_id='"+id+"',login_name='"+name+"' where `v_id`='"+v_id+"' and `unit_id`='"+Unit.get(i)+"'";
			//System.out.println("Submit Without Otp :"+sInsertQuery); 
			result = dbop_sec.Update(sInsertQuery);
			 //long attend = dbop_sec.Insert(sInsertQuery);
			
			if( result  == 0)
			{
				rows.put("success","0");
				rows2.put("message"," Please try again");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","1");
				rows2.put("message","Details Inserted");
				
				rows.put("response",rows2);
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}

	
	public  HashMap<Integer, Map<String, Object>> submitapprovalnewdata(int v_id,String unitID,String Approval,String id,String name,String visitorid) throws ClassNotFoundException
	{
		
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//String new_dbname ="",visitorName="";
		//HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
		String sInsertQuery = "";
		String visitorName= "";
		String check_prev_msg = "";
		long result = 0;
		try
		{ 
			List<String> Unit = Arrays.asList(unitID.split(","));
			List<String> ApprovedFlag = Arrays.asList(Approval.split(","));
			
			for (int i = 0; i < Unit.size(); i++)
			{
				String approve_flag = ApprovedFlag.get(i);
				dbop_sec = new DbOperations(DB_SECURITY);	
			sInsertQuery ="INSERT INTO `visit_approval`(`v_id`, `unit_id`, `Entry_flag`, `login_id`, `login_name`) VALUES ('"+v_id+"','"+Unit.get(i)+"','"+ApprovedFlag.get(i)+"','"+id+"','"+name+"')";
			//System.out.println("Submit Without Otp :"+sInsertQuery); 
			result = dbop_sec.Insert(sInsertQuery);
			
			dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
			String sqlVisitorDetails = "Select CONCAT(`Fname`,' ',`Lname`) as name from `visitors` where visitor_id = "+visitorid+";";
			HashMap<Integer, Map<String, Object>> visitorDetails = dbop_sec_root.Select(sqlVisitorDetails);
			//System.out.println("visitorDetails:"+sqlVisitorDetails);
			
			//System.out.println("visitorDetails:"+visitorDetails);
			for(Entry<Integer, Map<String, Object>> entry : visitorDetails.entrySet()) 
			{
				 visitorName = entry.getValue().get("name").toString();
				// System.out.println("name : "+ visitorName );
			}
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			String sqlLoginDetails = "Select id,login_id,unit_id from mapping where unit_id = "+Unit.get(i)+";";
				HashMap<Integer, Map<String, Object>> loginDetails = dbop_soc_root.Select(sqlLoginDetails);
				//System.out.println("Login details:"+loginDetails);
				int loginID = 0;
				String sMapID="";
				for(Entry<Integer, Map<String, Object>> entry1 : loginDetails.entrySet()) 
				{
					
					if(Integer.parseInt(entry1.getValue().get("unit_id").toString()) != 0)
					{
						sMapID = entry1.getValue().get("id").toString();
					
					//System.out.println("MapID:"+sMapID);
					if(Integer.parseInt(entry1.getValue().get("login_id").toString()) != 0)
					{
						loginID = Integer.parseInt(entry1.getValue().get("login_id").toString());
						//System.out.println("Login " +loginID);
						String sqlDeviceDetails = "Select device_id from device_details where login_id = "+loginID+";";
						//String sqlDeviceDetails ="SELECT d.login_id, l.login_id, l.member_id, d.device_id, m.id as 'map_id' from `device_details` as d JOIN `login` as l on l.login_id = d.login_id JOIN `mapping` as m on m.login_id = d.login_id WHERE m.unit_id = '" +unitID+  "' AND m.society_id = '"+society_id+ "' AND l.member_id = '"+sUserEmail+ "' AND d.device_id <> '' ORDER BY d.id DESC LIMIT 0,1";
						HashMap<Integer, Map<String, Object>> deviceDetails = dbop_soc_root.Select(sqlDeviceDetails);
						String deviceId="",title ="",msg = "";
						AndroidPush ap = new AndroidPush();
						for(Entry<Integer, Map<String, Object>> entry2 : deviceDetails.entrySet()) 
						{
							deviceId = entry2.getValue().get("device_id").toString().trim();
						}
						title = "Visitor Notification";
						
							msg = "Visitor " +visitorName.toUpperCase()+" is on the way to Unit No "+Unit.get(i)+".";
							System.out.println("Message : " + msg);
						if(deviceId == "")
						{
						
						}
						else
						{
							
							if(check_prev_msg != msg && approve_flag != "2"){
								check_prev_msg = msg;
								ap.sendPushNotification(title,msg, deviceId, sMapID, "10", "VisitorInPage",String.valueOf(v_id)); 
							}
						}
					}
				}
				}
			
			
			 //long attend = dbop.Insert(sInsertQuery);
			
			if( result  == 0)
			{
				rows.put("success","0");
				rows2.put("message"," Please try again");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","1");
				rows2.put("message","Details Inserted");
				
				rows.put("response",rows2);
			}
			String sSelectQuery = "SELECT Entry_flag  FROM visit_approval WHERE v_id = '" + v_id + "'";
			Map<Integer, Map<String, Object>> result_of = dbop_sec.Select(sSelectQuery);

			boolean shouldInsert = false;

			if (result_of != null && !result_of.isEmpty()) {
			    for (Map<String, Object> row : result_of.values()) {
			        String entryFlag = row.get("Entry_flag").toString();
			        if (!"2".equals(entryFlag)) {
			            shouldInsert = true;
			            break;
			        }
			    }
			}

			if (shouldInsert) {
				String sUpdateQuery = "UPDATE visitorentry SET status = 'inside' WHERE id = '" + v_id + "'";
			    dbop_sec.Update(sUpdateQuery);  // Assuming Update is used for INSERTs too in your utility
			}

		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	

	
	

	
	public  HashMap<Integer, Map<String, Object>> submitWithout(int userID,String Fname,String Lname, String mobNo,String unit,int purpose,String company,String vehicle,String docNo,int VisitorEntryID,int EntryGateNo,String vNote,String socid,int EntryDoc_id, float Temp, float Oxygen,int Pulse)
//	public  HashMap<Integer, Map<String, Object>> submitWithout(int userID,String Fname,String Lname, String mobNo,String unit,int purpose,String company,String vehicle,String docNo,int VisitorEntryID,int EntryGateNo,String vNote,String socid,int EntryDoc_id) throws ClassNotFoundException
	{
		
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String new_dbname ="",visitorName="",unitNo="",memName="",purposeDetails="";
		HashMap<Integer, Map<String, Object>> db = new HashMap<Integer, Map<String, Object>>();
		String sInsertQuery = "";
		String check_prev_sent = "";
		try
		{	
			vehicle = vehicle.toUpperCase();
			docNo = docNo.toUpperCase();
			long result = 0;
			String status = null;
			String WithoutOTP="none"; 
			if(Temp > 0 && Oxygen > 0 && Pulse > 0)
			{
				sInsertQuery ="INSERT INTO `visitorentry` (`login_id`,`visitor_ID`,`visitorMobile`,`unit_id`,`purpose_id`,`company`,`vehicle`,`otpGenerated`,`Entry_Gate`,`visitor_note`,Entry_With,temp,oxygen,pulse) VALUES ('"+userID+"','"+VisitorEntryID+"' ,'"+mobNo+"', '"+unit+"','"+purpose+"','"+company+"','"+vehicle+"','"+WithoutOTP+"','"+EntryGateNo+"','"+vNote+"','"+EntryDoc_id+"','"+Temp+"','"+Oxygen+"','"+Pulse+"')";
			}
			else
			{
				sInsertQuery ="INSERT INTO `visitorentry` (`login_id`,`visitor_ID`,`visitorMobile`,`unit_id`,`purpose_id`,`company`,`vehicle`,`otpGenerated`,`Entry_Gate`,`visitor_note`,Entry_With) VALUES ('"+userID+"','"+VisitorEntryID+"' ,'"+mobNo+"', '"+unit+"','"+purpose+"','"+company+"','"+vehicle+"','"+WithoutOTP+"','"+EntryGateNo+"','"+vNote+"','"+EntryDoc_id+"')";
			}
			//System.out.println("Submit Without Otp :"+sInsertQuery); 
			dbop_sec = new DbOperations(DB_SECURITY);
			result = dbop_sec.Insert(sInsertQuery);
			List<String> Unit1 = Arrays.asList(unit.split(","));
			for (int i = 0; i < Unit1.size(); i++)
			{
			dbop_soc = new DbOperations(DB_SOCIETY);
			String sqlForunitNo = "Select u.`unit_no`, m.`owner_name` from unit as u, member_main as m where u.`unit_id` = '"+Unit1.get(i)+"' and u.society_id='"+socid+"'  and u.`unit_id` = m.`unit` and m.`status` = 'Y' and m.`ownership_status` = 1";
			HashMap<Integer, Map<String, Object>> unitMemDetails = dbop_soc.Select(sqlForunitNo);
			
			//System.out.println("unitMemDetails: "+unitMemDetails);
			for(Entry<Integer, Map<String, Object>> entry1 : unitMemDetails.entrySet()) 
			{
				unitNo = entry1.getValue().get("unit_no").toString();
				memName = entry1.getValue().get("owner_name").toString();
			}
			//}
			////////////////DOUBT???????????????????????????????
			/*dbop = new DbOperations(DB_SECURITY_ROOT);
			String sqlVisitorDetails = "Select CONCAT(`Fname`,' ',`Lname`) as name from `visitors` where visitor_id = "+VisitorEntryID+";";
			HashMap<Integer, Map<String, Object>> visitorDetails = dbop.Select(sqlVisitorDetails);
			for(Entry<Integer, Map<String, Object>> entry : visitorDetails.entrySet()) 
			{
				 visitorName = entry.getValue().get("name").toString();
			}*/
			visitorName = Fname+" "+Lname;
			dbop_sec = new DbOperations(DB_SECURITY);
			String sqlPurposeDetails = "Select purpose_name from purpose where purpose_id = "+purpose+";";
			HashMap<Integer, Map<String, Object>> purposeAllDetails = dbop_sec.Select(sqlPurposeDetails);
			for(Entry<Integer, Map<String, Object>> entry2 : purposeAllDetails.entrySet()) 
			{
				 purposeDetails = entry2.getValue().get("purpose_name").toString();
			}
			/*vehicle = vehicle.toUpperCase();
			docNo = docNo.toUpperCase();
			long result = 0;
			String status = null;
			String WithoutOTP="none"; 
			sInsertQuery ="INSERT INTO `visitorentry` (`login_id`,`visitor_ID`,`visitorMobile`,`unit_id`,`purpose_id`,`company`,`vehicle`,`otpGenerated`,`Entry_Gate`,`visitor_note`) VALUES ('"+userID+"','"+VisitorEntryID+"' ,'"+mobNo+"', '"+unit+"','"+purpose+"','"+company+"','"+vehicle+"','"+WithoutOTP+"','"+EntryGateNo+"','"+vNote+"')";
			//System.out.println("Submit Without Otp :"+sInsertQuery); 
			result = dbop.Insert(sInsertQuery);
			*/
			 //long attend = dbop.Insert(sInsertQuery);
			
			if( result  == 0)
			{
				rows.put("success","0");
				rows2.put("message"," Please try again");
				rows.put("response",rows2);
			}
			else
			{
				int loginId = 0;
				String sMapId = "";
				dbop_soc_root = new DbOperations(DB_ROOT_NAME);
				
				//String sqlLoginDetails = "Select id,login_id from mapping where unit_id = "+Unit1.get(i)+";";
				String sqlLoginDetails = "Select id,login_id,unit_id from mapping where unit_id = "+Unit1.get(i)+";";
				HashMap<Integer, Map<String, Object>> loginDetails = dbop_soc_root.Select(sqlLoginDetails);
				//System.out.println("Login details:"+loginDetails);
				for(Entry<Integer, Map<String, Object>> entry1 : loginDetails.entrySet()) 
				{    //  add condition unit id 0
					if(Integer.parseInt(entry1.getValue().get("unit_id").toString()) != 0)
					{
					sMapId = entry1.getValue().get("id").toString();
					if(Integer.parseInt(entry1.getValue().get("login_id").toString()) != 0)
					{
						loginId = Integer.parseInt(entry1.getValue().get("login_id").toString());
						String sqlDeviceDetails = "Select device_id from device_details where login_id = "+loginId+";";
						//System.out.println("Query: "+sqlDeviceDetails);
						HashMap<Integer, Map<String, Object>> deviceDetails = dbop_soc_root.Select(sqlDeviceDetails);
						//System.out.println(deviceDetails);
						String deviceId="",title ="",msg = "";
						AndroidPush ap = new AndroidPush();
						for(Entry<Integer, Map<String, Object>> entry2 : deviceDetails.entrySet()) 
						{
							deviceId = entry2.getValue().get("device_id").toString().trim();
						}
					//	System.out.println("Device Id: "+deviceId);
						title = "Visitor Notification";
						msg = memName+", Visitor "+visitorName.toUpperCase()+"  is on the way to Unit No "+unitNo+" for "+purposeDetails+" .";
						//"Visitor"+visitorName.toUpperCase()+" is on the way to Unit No "+unitNo+" for "+purpose+" ."
						if(deviceId == "")
						{
						
						}
						else
						{
							if(check_prev_sent != msg){
								
								String EntryID=String.valueOf(result);
								ap.sendPushNotification(title,msg, deviceId, sMapId, "10", "VisitorInPage", EntryID); 
								//ap.sendPushNotification(title,msg, deviceId, "0", "11", "VisitorInPage",result);
							}
						}
					}
				}
				}
				rows.put("success","1");
				rows2.put("message","Details Inserted");
				rows2.put("VisitorID",result);
				rows2.put("VisitorEntryID",VisitorEntryID);
				rows.put("response",rows2);
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	//       -------------------------  Set Gate No ----------------------------------- //
	
	public  HashMap<Integer, Map<String, Object>> setgate(int userID,int GateNo) throws ClassNotFoundException
	{
		
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//String new_dbname ="",visitorName="";
		//HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
		String sInsertQuery = "";
		long result = 0;
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			 
			sInsertQuery ="INSERT INTO `gate_entry` (`login_id`, `gate_no`) VALUES ('"+userID+"','"+GateNo+"')";
			//System.out.println("Submit Without Otp :"+sInsertQuery); 
			result = dbop_sec.Insert(sInsertQuery);
			 //long attend = dbop.Insert(sInsertQuery);
			
			if( result  == 0)
			{
				rows.put("success","0");
				rows2.put("message"," Please try again");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","1");
				rows2.put("message","Details Insert");
				rows2.put("GateID",result);
				rows2.put("GateNo",GateNo);
				rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	
	public HashMap<Integer, Map<String, Object>> ChangeOTP(int iOTPFlag_Rep,int iOTPFlag_New) throws ClassNotFoundException
	{
		
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//String new_dbname ="",visitorName="";
		//HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
		String sUpdateQuery = "";
		long result = 0;
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			 
			sUpdateQuery ="UPDATE `feature_setting` SET `OTP_Status_Rep` ='"+iOTPFlag_Rep+"',`OTP_Status_New`='"+iOTPFlag_New+"'";
			//System.out.println("Submit Without Otp :"+sInsertQuery); 
			result = dbop_sec.Update(sUpdateQuery);
			 //long attend = dbop.Insert(sInsertQuery);
			
			if( result  == 0)
			{
				rows.put("success","0");
				rows2.put("message"," Please try again");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","1");
				rows2.put("message","OTP Disable Successfully");
				rows2.put("OTPstatus",result);
				//rows2.put("GateNo",GateNo);
				rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	
	//* ---------------------------------------  New Function  ---------------------------------- *//
	public HashMap<Integer, Map<String, Object>> submit1(int loginId,String SocietyId,String unit,int purpose,int vEntryID, int VisitorRepetedId,String Mobile,String EntryGate,String Fname,String LName,String company,String Vehicle,String note,int EntryDoc_id, float Temp,float Oxygen,int Pulse) throws ClassNotFoundException
	{
		
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//String new_dbname ="",visitorName="";
		//HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
		String sUpdateQuery = "";
		long result = 0;
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			 
			if(Temp > 0 && Oxygen > 0 && Pulse > 0) sUpdateQuery ="update visitorentry set Entry_With = '"+EntryDoc_id+"' , visitor_ID = '"+VisitorRepetedId+"', unit_id='"+unit+"',purpose_id='"+purpose+"', Entry_Gate='"+EntryGate+"',company='"+company+"',vehicle='"+Vehicle+"',visitor_note='"+note+"',temp='"+Temp+"',oxygen='"+Oxygen+"',pulse='"+Pulse+"' Where id='"+vEntryID+"'";
			else sUpdateQuery ="update visitorentry set Entry_With = '"+EntryDoc_id+"' , visitor_ID = '"+VisitorRepetedId+"', unit_id='"+unit+"',purpose_id='"+purpose+"', Entry_Gate='"+EntryGate+"',company='"+company+"',vehicle='"+Vehicle+"',visitor_note='"+note+"' Where id='"+vEntryID+"'";
			result = dbop_sec.Update(sUpdateQuery);
		 
			
			
			if( result  == 0)
			{
				rows.put("success","0");
				rows2.put("message"," Please try again");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","1");
				rows2.put("VisitorEntryID",VisitorRepetedId);
				rows2.put("VisitorID",vEntryID);
				//rows2.put("GateNo",GateNo);
				rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	
	
	
	public HashMap<Integer, Map<String, Object>> submitunit(int VisitorEntryID,String unit) //throws ClassNotFoundException
	{
		
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//String new_dbname ="",visitorName="";
		//HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
		String sUpdateQuery = "";
		long result = 0;
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			 
			sUpdateQuery ="update visitorentry set unit_id = '"+unit+"' where id='"+VisitorEntryID+"'";
			result = dbop_sec.Update(sUpdateQuery);
			//System.out.println("sUpdateQuery:"+sUpdateQuery );		 
			//System.out.println("result:"+result );
			
			
			
			
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	
	public  HashMap<Integer, Map<String, Object>> addunitforapproval(int v_id,String unitID,String visitorid) throws ClassNotFoundException
	{
		
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//String new_dbname ="",visitorName="";
		//HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
		String sInsertQuery = "",visitorName="";
		long result = 0;
		String prev_sent_check = "";
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			
			sInsertQuery ="INSERT INTO `visit_approval`(`v_id`,`unit_id`) VALUES  ('"+v_id+"','"+unitID+"')";
			result = dbop_sec.Insert(sInsertQuery);
			dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
			String sqlVisitorDetails = "Select CONCAT(`Fname`,' ',`Lname`) as name from `visitors` where visitor_id = "+visitorid+";";
			HashMap<Integer, Map<String, Object>> visitorDetails = dbop_sec_root.Select(sqlVisitorDetails);
			//System.out.println("visitorDetails:"+sqlVisitorDetails);
			
			//System.out.println("visitorDetails:"+visitorDetails);
			for(Entry<Integer, Map<String, Object>> entry : visitorDetails.entrySet()) 
			{
				 visitorName = entry.getValue().get("name").toString();
				// System.out.println("name : "+ visitorName );
			}
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			String sqlLoginDetails = "Select id,login_id,unit_id from mapping where unit_id = "+unitID+";";
				HashMap<Integer, Map<String, Object>> loginDetails = dbop_soc_root.Select(sqlLoginDetails);
				//System.out.println("Login details:"+loginDetails);
				int loginID = 0;
				String sMapID="";
				for(Entry<Integer, Map<String, Object>> entry1 : loginDetails.entrySet()) 
				{
					// add condition in unit id 0
					if(Integer.parseInt(entry1.getValue().get("unit_id").toString()) != 0)
					{
						sMapID = entry1.getValue().get("id").toString();
					
					//System.out.println("MapID:"+sMapID);
					if(Integer.parseInt(entry1.getValue().get("login_id").toString()) != 0)
					{
						loginID = Integer.parseInt(entry1.getValue().get("login_id").toString());
						//System.out.println("Login " +loginID);
						String sqlDeviceDetails = "Select device_id from device_details where login_id = "+loginID+";";
						//String sqlDeviceDetails ="SELECT d.login_id, l.login_id, l.member_id, d.device_id, m.id as 'map_id' from `device_details` as d JOIN `login` as l on l.login_id = d.login_id JOIN `mapping` as m on m.login_id = d.login_id WHERE m.unit_id = '" +unitID+  "' AND m.society_id = '"+society_id+ "' AND l.member_id = '"+sUserEmail+ "' AND d.device_id <> '' ORDER BY d.id DESC LIMIT 0,1";
						//.out.println("Query: "+sqlDeviceDetails);
						HashMap<Integer, Map<String, Object>> deviceDetails = dbop_soc_root.Select(sqlDeviceDetails);
						//System.out.println(deviceDetails);
						String deviceId="",title ="",msg = "";
						AndroidPush ap = new AndroidPush();
						for(Entry<Integer, Map<String, Object>> entry2 : deviceDetails.entrySet()) 
						{
							deviceId = entry2.getValue().get("device_id").toString().trim();
						}
						//System.out.println("Device Id: "+deviceId);
						title = "Visitor Notification";
						//msg = memName+", visitor "+visitorName.toUpperCase()+" is verified and is on the way to Unit No "+unitNo+" for "+purpose+" .";
						//if(company.lenth > 0 )
						//{
							//msg = "Visitor "+visitorName.toUpperCase()+" from "+company_name+" is on the way to Unit No "+unitNo+" for "+purpose+" .";
						//}
						//else
						//{
							msg = "Visitor " +visitorName.toUpperCase()+" is waiting for approval to Unit No "+unitID+".";
						//}
						if(deviceId == "")
						{
						
						}
						else
						{
							if(prev_sent_check != msg){
								//System.out.println(msg);
								ap.sendPushNotification(title,msg, deviceId, sMapID, "10", "VisitorInPage",String.valueOf(v_id)); 
								//String sTitle, String sMessage, String sDeviceToken, String sMapID, String sPageRef, String sPageName, String sDetails
							}
						}
					}
				}
				}
			
			if( result  == 0)
			{
				rows.put("success","0");
				rows2.put("message"," Please try again");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","1");
				rows2.put("message","Details Inserted");
				
				rows.put("response",rows2);
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	
	
	public  HashMap<Integer, Map<String, Object>> checkunitapprovalstatus(int v_id,String unitID) throws ClassNotFoundException
	{
		
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//String new_dbname ="",visitorName="";
		//HashMap<Integer, Map<String, Object>> result = new HashMap<Integer, Map<String, Object>>();
		String sInsertQuery = "";
		long result = 0;
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			
			sInsertQuery ="select * from visit_approval where v_id='"+v_id+"' and unit_id='"+unitID+"'";
			//System.out.println("Submit Without Otp :"+sInsertQuery); 
			rows2 = dbop_sec.Select(sInsertQuery);
			 //long attend = dbop_sec.Insert(sInsertQuery);
			
			if( rows.size() >  0)
			{
				rows.put("success","1");
				rows.put("response",rows2);
			}
			else
			{
				rows.put("success","0");
				rows.put("response",rows2);
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return rows;
	}
	public static void main(String[] args) //throws Exception
	{
		String sToken = "Ldx0TA8sZCT3EXmuyXf5_VETtd5AWxPWRSZw-NgWtncvS_V2RZeKdsd6Dnd91fbvC8aCycU14QwGkU5Ay0OKckaapu6Stc-5zCaf6yJ1a6OZXXuivIb9VRwM1uLc_DnGKaPghvGhxA5xbEkrloYUeNtnLiOevu-I_4gy3sfauIb_CGVX7mSwiOGjR7cNjxqe";
//				+ "CawJsy_43VVSDNdBTutaa1PU2c7bo66W-OV8Y0m7D6tDH7L6lUr8-5C5O6NrWt-7MP--eGUhBhluGRWO2UpQZ-HYMNjGDWFp_NPhscYVkQekl8JK4F5GZi3-xgiTiIvsvIj0ThL4poKVa9e2LYRmEhjH4ZywL6BmXLHgeQ6dMtk92_e46dlI2xaEVJbO6gBD";

		OTP otp = new OTP(sToken);
		//int VisitorEntryID = 957; // Replace with any valid test ID
        //String unit = "247,18,347";     // Replace with your unit string

        try {
			HashMap<Integer, Map<String, Object>> objHash2 = otp.submitapprovalnewdata(2784,"13,25,64","1,2,1","", "Avaneesh", "2560");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		int iLoginID = 4;
		//String sMobile = "7972928940";
		String sMobile = "9773029129";
		String SocietyName = "Acme Society";

		//HashMap<Integer, Map<String, Object>> objHash3 = otp.submitapprovalnewdata(957, "247,18,347");	
		
		
		/*
		http://way2society.com:8080/NewSecurityManagerWeb/OTP/NewAPPsubmitWithoutOTP?
			token=XlFSRg4qELtZP-GaTc97T268Ph-lAd1IAWJZSZr5gEF1nq6ISpH6PiFYuTu1xL5sdqd2rrhpGyNSzEltCsALx-L64X6h9J65lOa1V1Iw4fwnF-e8ciwyDuRSAMG-nsb2Y0e94POWSrHJhNghYXoY0jUbSyiPQ4KmL8dgkjSKl4M
			
EntryDoc_id=1
temp=
oxygen=
pulse=
VisitorEntryID=7
vFName=&Prashant
&vLName=&Shetye
vmobNumber=9773029129&vNote=&Gate_no=2&vCompany=&
vVehicle=&purpose_id=5&vCompanyOther=		
	*/	

int userID = 275;
String Fname = "Prashant";
String Lname = "Shetye";
String mobNo = "9773029129";
String unit = "-1";
int purpose=5;
String company="MyCompany";
String vehicle="Honda";
String docNo="1";
int VisitorEntryID = 7;
int EntryGateNo = 1;
String vNote = "Note here";
String socid = "59";
int EntryDoc_id = 1;
float Temp = 30;
float Oxygen = 100;
int Pulse = 99;
//
//HashMap<Integer, Map<String, Object>> objHash = otp.submitWithout(userID, Fname, Lname,  mobNo, unit, purpose, company, vehicle, docNo, VisitorEntryID, EntryGateNo, vNote, socid, EntryDoc_id,  Temp,  Oxygen, Pulse);
//		
		//HashMap<Integer, Map<String, Object>> objHash = otp.createVisitor(iLoginID, sMobile, SocietyName);
		//HashMap<Integer, Map<String, Object>> objHash = otp.submitunit(iLoginID, sMobile);

		String otp1 = "9575";
		String mobileNo1 = "9773029129";
		int vid1 = 1473;
		int vid=1877;
		
		//System.out.println(objHash);
		
	
		//HashMap<Integer, Map<String, Object>> objHash = otp.addunitforapproval(370,"365","48");
		//System.out.println(objHash);
		//	(int LoginID, String vcompany,String vvehicle,int unitID, int purposeID,int vid,String verified,int visitorId,String Mobile,int EnryGateNo) throws ClassNotFoundException
		
		//HashMap<Integer, Map<String, Object>> o = otp.submit(169,"Ola", "mh01cb5999", 26, 3, 0, "true", 48,"9869752739",2);
		//System.out.println(o);
	}

}
