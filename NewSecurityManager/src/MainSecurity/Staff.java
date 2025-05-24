package MainSecurity;

import static MainSecurity.DbConstants.*;

import java.sql.Blob;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Map.Entry;
import java.util.TimeZone;

import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import com.sun.org.apache.xml.internal.security.utils.Base64;

import MainSecurity.TimeZoneConvertor;
import SecuriyCommonUtility.MapUtility;

import org.apache.commons.codec.binary.Base64;
public class Staff extends CommonBaseClass
{
	//ClassNotFoundException

	//ProjectConstants m_objProjectConstants;
	//private  SecuriyCommonUtility.MapUtility m_objMapUtility;
	private String m_sToken;
	
	ProjectConstants m_objProjectConstants;
//	private  TimeZoneConvertor m_Timezone;
	private static SecuriyCommonUtility.MapUtility m_objMapUtility;
	//public  DbOperations dbop;
	private  TimeZoneConvertor m_Timezone;
	public Staff(String token) //throws ClassNotFoundException
	{
		super(token);
		this.m_sToken = token;
//		DB_SOCIETY = "hostmjbt_society46";
//		DB_SECURITY = "sm_1";
//		m_Timezone = new TimeZoneConvertor();
		m_objProjectConstants = new ProjectConstants();
		m_objMapUtility = new MapUtility();
		//dbop =  new DbOperations(DB_SECURITY);
	}
	
	//ADD NEW STAFF 
	public  HashMap<Integer, Map<String, Object>> mNewAddSatff(String Fname,String Lname,String gender,String mob,String img,String job,int unit,int doc_id, String join_date,String work_hours,String Dob)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sInsertQuery = "";
		String New_join="";
		
		try
		{		
			DateFormat output = new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			//DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			//Date date = formatter.parse(join_date);
			//DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy"); 
			//System.out.println("new Date of Birth : "+date);
			New_join= output.format(date);
			//New_join= formatter.format(date);
			//System.out.println("string Date of Birth : "+New_join);
		  
			sInsertQuery = "insert into newstaff (`s_fname`,`s_lname`,`s_gender`,`dob`,`s_mob`,`img`,`jobprofile`,`unit_id`,`doc_id`,`join_date`,`working_hours`) values ";

			sInsertQuery = sInsertQuery + "('" + Fname + "','" + Lname + "','" + gender +  "','" + Dob +  "','" + mob +  "','" + img +  "','" + job + "','" + unit + "','"+doc_id+"','" + New_join + "','" + work_hours +  "')";

			//System.out.println(sInsertQuery);	
			
			long newStaffID = dbop_sec.Insert(sInsertQuery);
			//System.out.println("mAddStaff : StaffID<" + newStaffID + ">");
			
			if(newStaffID  > 0)
			{
				//add member to map
				 rows2.put("StaffID ",newStaffID );
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//member not found
				 rows2.put("StaffID ","");
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			rows2.put("exception ",e);
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println(rows);
		return rows;
	}
	
	//MARK STAFF ENTRY AND PRESENT 
	public  HashMap<Integer, Map<String, Object>> markEntry(int staff_id, int societyId,String sProfile,int iGateEntry,String pNote,String counter,float Temp,float Oxygen,int Pulse)
	{
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sInsertQuery = "", staffName="", purpose="",unitNo= "",memName="";
		String sUpdateQuery="";
		//staff_id =21;
		try
		{	
			dbop_sec = new DbOperations(DB_SECURITY);
			if(Temp > 0 && Oxygen > 0 && Pulse > 0) sInsertQuery = "Insert into staffattendance (staff_id,attendance,status,entry_profile,Entry_Gate,staff_note,staff_entry_with,temp,oxygen,pulse) values('"+staff_id+"', 'p','inside','"+sProfile+"','"+iGateEntry+"','"+pNote+"','"+counter+"','"+Temp+"','"+Oxygen+"','"+Pulse+"')"; 
			else sInsertQuery = "Insert into staffattendance (staff_id,attendance,status,entry_profile,Entry_Gate,staff_note,staff_entry_with) values('"+staff_id+"', 'p','inside','"+sProfile+"','"+iGateEntry+"','"+pNote+"','"+counter+"')";
			long attend = dbop_sec.Insert(sInsertQuery);
			//System.out.println(attend);
			
			if(attend  > 0)
			{
				dbop_soc_root = new DbOperations(DB_ROOT_NAME);
				sUpdateQuery = "Update `service_prd_reg` set security_status='1' Where service_prd_reg_id = '"+staff_id+"'";
				//System.out.println(sUpdateQuery);
				long insideEntry = dbop_soc_root.Update(sUpdateQuery);
				
				String sqlForSefrviceProviderDetails = "select spr.`full_name`,c.`cat` from cat as c, spr_cat as sc, service_prd_reg as spr where spr.`service_prd_reg_id` =  sc.`service_prd_reg_id` and sc.`cat_id` = c.`cat_id` and spr.`service_prd_reg_id` = '"+staff_id+"'";
				HashMap<Integer, Map<String, Object>> serviceProviderDetails =  dbop_soc_root.Select(sqlForSefrviceProviderDetails);
				//System.out.println("serviceProviderDetails: "+serviceProviderDetails);
				for(Entry<Integer, Map<String, Object>> entry : serviceProviderDetails .entrySet()) 
				{
					staffName = entry.getValue().get("full_name").toString();
					purpose = entry.getValue().get("cat").toString();
				}
				String sqlForUnitDetails = "Select unit_id from service_prd_units where service_prd_id = '"+staff_id+"' and society_id ='"+societyId+"'";
				HashMap<Integer, Map<String, Object>> unitDetails =  dbop_soc_root.Select(sqlForUnitDetails);
				//System.out.println("sqlForUnitDetails: "+sqlForUnitDetails);
				for(Entry<Integer, Map<String, Object>> entry : unitDetails .entrySet()) 
				{
					if(entry.getValue().get("unit_id").toString().equals("0"))
					{
						continue;
					}
					dbop_sec = new DbOperations(DB_SOCIETY);
					String sqlForunitNo = "Select u.`unit_no`, m.`owner_name` from unit as u, member_main as m where u.`unit_id` = '"+entry.getValue().get("unit_id")+"' and u.`unit_id` = m.`unit` and m.`status` = 'Y' and m.`ownership_status` = 1";
					HashMap<Integer, Map<String, Object>> unitMemDetails = dbop_sec.Select(sqlForunitNo);
					//System.out.println("unitMemDetails: "+unitMemDetails);
					for(Entry<Integer, Map<String, Object>> entry1 : unitMemDetails.entrySet()) 
					{
						unitNo = entry1.getValue().get("unit_no").toString();
						memName = entry1.getValue().get("owner_name").toString();
					}
					dbop_soc_root = new DbOperations(DB_ROOT_NAME);
					//unitDetailsForNotification = unitNoDetails.split("[");
					String sqlLoginDetails = "Select login_id from mapping where unit_id = "+entry.getValue().get("unit_id")+";";
					//System.out.println("Sql: "+sqlLoginDetails);
					HashMap<Integer, Map<String, Object>> loginDetails = dbop_soc_root.Select(sqlLoginDetails);
					//System.out.println("Login details:"+loginDetails);
					int loginId=0;
					for(Entry<Integer, Map<String, Object>> entry1 : loginDetails.entrySet()) 
					{
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
							title = "Service Provider Notification";
							//msg = memName+", service provider "+staffName+" is verified and he/she is on the way to Unit No "+unitNo+" for "+purpose+" .";
							msg = staffName+ " is verified and is on the way to Unit No "+unitNo+" for "+purpose+" .";
							//System.out.println("Device Id: "+deviceId);
							if(deviceId == "")
							{
							
							}
							else
							{
								//sendPushNotification(title, message, deviceID, sMappingID, sPageRef, sPageName, sDetails);
								//System.out.println("in else");
								String StaffID=String.valueOf(staff_id);
								//String Detils
								System.out.println("Sending notification");
								//ap.sendPushNotification(title,msg, deviceId, "0", "11", "ProviderDetailsPage", StaffID+'/'+societyId); 
							}
						}
					}
				}
				rows2.put("StaffID",staff_id);
				rows2.put("EntryID",attend);
				rows2.put("message","Staff marked present" );
				rows.put("success",1);
				rows.put("response",rows2);			 
			}
			else
			{
				//member not found
				 rows2.put("StaffID",staff_id );
				 rows2.put("message","Staff marked absent" );
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			 rows2.put("exception ",e);
			 rows.put("success",0);
			 rows.put("response",rows2);
		}
		///System.out.println("satff entry"+rows);
		return rows;
	}
	
	//MARK STAFF EXIT 
	public  HashMap<Integer, Map<String, Object>> markExit(int staff_id, int iGateExit)
	{
	
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sUpdateQuery = "";
	
		try
		{		
			dbop_sec = new DbOperations(DB_SECURITY);
			sUpdateQuery = "Update staffattendance set status='outside',outTimeStamp=now(),Exit_Gate='"+iGateExit+"' Where staff_id = '"+staff_id+"' AND status='inside' ";
			//System.out.println(sUpdateQuery);
			long exit = dbop_sec.Update(sUpdateQuery);
			
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			sUpdateQuery = "Update `service_prd_reg` set security_status='0' Where service_prd_reg_id = '"+staff_id+"'";
			//System.out.println(sUpdateQuery);
			long entry = dbop_soc_root.Update(sUpdateQuery);
			//System.out.println(exit);
			if(exit  > 0)
			{
				//add member to map
				 rows2.put("StaffID",staff_id );
				 rows2.put("message","Staff exit marked" );
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//member not found
				 rows2.put("StaffID",staff_id );
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
	
	//GET STAFF COUNT
	public  long fetchStaffDetails(int iSocietyID)
	{
		HashMap<Integer, Map<String, Object>> sCount = new HashMap<Integer, Map<String, Object>>() ;
		long count1=0 ;
		try 
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			String count =  "select count(*) as count from service_prd_reg where society_id='"+iSocietyID+"' AND status='Y'";
			//String count =  "select service_prd_reg_id as ProvderID from service_prd_reg where society_id='"+iSocietyID+"' AND status='Y'";
			sCount = dbop_soc_root.Select(count);
			//System.out.println(sCount);
			for(Entry <Integer,Map<String, Object>> entry : sCount.entrySet() )
			{
				count1 = (long)entry.getValue().get("count");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//System.out.println("size of staff hash map :"+ sCount.size());
		return count1;
	}
	
	//GET STAFF CURRENTLY INSIDE AND PRESENT
	//check
	public  HashMap<Integer, Map<String, Object>> sCurrentlyIn(String today)
	{
		HashMap<Integer, Map<String, Object>> todayStaff = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>> staffId = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "",sql1="",sql2="",MonthDate="";
		HashMap rows = new HashMap<>();
		try 
		{		
			dbop_sec = new DbOperations(DB_SECURITY);
			sSelectQuery = "Select sA.staff_id,sA.inTimeStamp,sA.entry_profile,sA.Entry_Gate from staffattendance as sA where sA.status = 'inside';";
			//System.out.println("query:"+sSelectQuery);
			staffId = dbop_sec.Select(sSelectQuery);
			//System.out.println(staffId);
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			for(Entry<Integer, Map<String, Object>> entry1 : staffId.entrySet()) 
			{
				
				if(entry1.getValue().get("inTimeStamp") != null)
				{
					MonthDate =entry1.getValue().get("inTimeStamp").toString();
					
					String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
					String fmt = "yyyy-MM-dd HH:mm a";
					DateFormat df = new SimpleDateFormat(fmt);

					Date dt = df.parse(Time);

					DateFormat tdf = new SimpleDateFormat("hh:mm a");
					DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
					String timeOnly = tdf.format(dt);
					String dateOnly = dfmt.format(dt);
					//System.out.println("InTime:"+timeOnly);
					//System.out.println("date :"+dateOnly + "<br>");
					
					entry1.getValue().put("InTime", timeOnly);
			 		entry1.getValue().put("Date", dateOnly);
			 		//entry1.getValue().put("EntryProfile",entry1.getValue().get("entry_profile"));
					//entry1.getValue().put("GateEntry",entry1.getValue().get("Entry_Gate"));
			 		//entry1.getValue().put("Date", dateOnly);
			 		//rows1.put("ID", entry.getValue().get("sr_no"));
				}
				HashMap<Integer, Map<String, Object>> servicePrbDetails = new HashMap<Integer, Map<String, Object>>();
				sql1 = "Select spr.`full_name`, spr.`cur_con_1`, spr.`cur_resd_add`, spr.`society_id`,sc.`cat_id`, c.`cat` from service_prd_reg as spr, cat c,spr_cat as sc where spr.`service_prd_reg_id` = '"+entry1.getValue().get("staff_id")+"' and sc.`service_prd_reg_id`= spr.`service_prd_reg_id` and sc.`cat_id` = c.`cat_id` and sc.`status` = 'Y'";
				servicePrbDetails = dbop_soc_root.Select(sql1);
				for(Entry<Integer, Map<String, Object>> entry2 : servicePrbDetails.entrySet()) 
				{
					entry1.getValue().put("Full_Name",entry2.getValue().get("full_name"));
					entry1.getValue().put("SocietyId",entry2.getValue().get("society_id"));
					entry1.getValue().put("CurrentAddress",entry2.getValue().get("cur_resd_add"));
					entry1.getValue().put("ContactNo",entry2.getValue().get("cur_con_1"));
					//entry1.getValue().put("JobProfile",entry2.getValue().get("cat"));
				}
				HashMap<Integer, Map<String, Object>> unitDetails = new HashMap<Integer, Map<String, Object>>();
				sql2="Select unit_no from service_prd_units where service_prd_id = '"+entry1.getValue().get("staff_id")+"';";
				unitDetails = dbop_soc_root.Select(sql2);
				if(unitDetails.size()< 0)
				{
					entry1.getValue().put("UnitDetails","");
				}
				else
				{
					entry1.getValue().put("UnitDetails",unitDetails);
				}
			}
			//System.out.println("final:"+staffId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return staffId;
	}
	
	// GET STAFF TOTAL COUNT, CURRENTLY INSIDE STAFF
	public  HashMap<Integer, Map<String, Object>> getStaffDetails(int iSocietyID )
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sql = "";
		try 
		{
			long staffCount = fetchStaffDetails(iSocietyID);
			
			Date date = new Date();  
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
            String today = dateFormat.format(date);  
            HashMap<Integer, Map<String, Object>> staffInside = sCurrentlyIn(today);
			 
			if(staffCount == 0) 
			{
				rows2.put("message ","no staff found" );
				rows.put("success",0);
				rows.put("response",rows2);
			} 
			else if(staffInside.size() < 0)
			{
				rows2.put("message ","no staff found inside society" );
				rows.put("success",0);
				rows.put("response",rows2);
			}
			else
			{
				rows2.put("getStaffDetails", MapUtility.HashMaptoList(staffInside));
				rows2.put("TotalStaffCount",staffCount);
				rows2.put("InStaffCount", staffInside.size());
//				rows2.put("currentlyInStaff",staffInside.size() );
				rows.put("success",1);
				rows.put("response",rows2);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			e.printStackTrace();
			rows2.put("exception ",e );
			rows.put("success",0);
			rows.put("response",rows2);
		}
//		System.out.println("staff details"+rows);
		return rows;
	}
	
	//GET MOTHLY ATTENDANCE OF STAFF BASED ON MONTH , YEAR, STAFF ID
	public  HashMap<Integer, Map<String, Object>> monthlyStaffAttendance(int month, int year, int s_id)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sSelectQuery = "";
		HashMap<Integer, Map<String, Object>> staffAttendance = new HashMap<Integer, Map<String, Object>>();
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			sSelectQuery = "Select * from staffattendance where year(inTimeStamp)='"+year+"' AND month(inTimeStamp)= '"+month+"' AND attendance = 'p' AND staff_id='"+s_id+"'";
			staffAttendance = dbop_sec.Select(sSelectQuery);
			//System.out.println("monthly attendance"+staffAttendance);
			
			if(staffAttendance.size() > 0)
			{
				rows2.put("attendance ",staffAttendance.size() );
				rows.put("success",1);
				rows.put("response",rows2);
			}
			else
			{
				rows2.put("message ","attendance not found for staff "+ s_id );
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
		
		return rows;
	}
	public  HashMap<Integer, Map<String, Object>> fetchStaff1(String contactNo, String servicePrdId, int iSocietyID)
	{
	  
		HashMap<Integer, Map<String, Object>>  mpStaff = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>>  resultFound = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String StatusBy="inside",sql2="",dbo="",uDbo="",MonthDate="",timeOnly="";
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			if(servicePrdId == "")
			{
				
				sSelectQuery = "Select spr.`full_name`,spr.`service_prd_reg_id`, spr.`cur_con_1`, spr.`cur_resd_add`, spr.`society_id`,sc.`cat_id`, c.`cat`,spr.`dob`,spr.photo,spr.photo_thumb,spr.since,spr.fingerISO from service_prd_reg as spr, cat c,spr_cat as sc where spr.`society_id`='"+iSocietyID+"' and spr.`cur_con_1` = '"+contactNo+"' and sc.`service_prd_reg_id`= spr.`service_prd_reg_id` and sc.`cat_id` = c.`cat_id` and sc.`status` = 'Y'";
				
			}
			else
			{
				sSelectQuery = "Select spr.`full_name`, spr.`service_prd_reg_id`,spr.`cur_con_1`, spr.`cur_resd_add`, spr.`society_id`,sc.`cat_id`, c.`cat`,spr.`dob`,spr.photo,spr.photo_thumb,spr.since,spr.fingerISO,spc.society_staff_id  from service_prd_reg as spr, cat c,spr_cat as sc,service_prd_society as spc where sc.`service_prd_reg_id`= spr.`service_prd_reg_id`  and spr.service_prd_reg_id = spc.provider_id and sc.`cat_id` = c.`cat_id` and sc.`status` = 'Y' and spr.`service_prd_reg_id`='"+servicePrdId+"' and spr.`society_id`='"+iSocietyID+"'";
				
			}
			//System.out.println("sSelectQuery: "+sSelectQuery);
			mpStaff = dbop_soc_root.Select(sSelectQuery);
			String catDetails[] = new String[mpStaff.size()];
			int c=0;
			for(Entry<Integer, Map<String, Object>> entry : mpStaff.entrySet()) 
			{
				
				//String query="select fingerISO from service_prd_reg where service_prd_reg_id='"+entry.getValue().get("service_prd_reg_id").toString()+"'";
				///byte[] fingerdata=dbop_soc_root.Selectfingerdata(query);
				// String finger = Base64.encodeBase64String(fingerdata);
				//System.out.println("fingerdata " + finger);
				//entry.getValue().put("FINGER ",finger);
				//String test = entry.getValue().get("fingerISO").toString();
				//byte[] encodedBytes = Base64.encodeBase64(test.getBytes());
				//System.out.println("encodedBytes " + new String(encodedBytes));
				//byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
				//System.out.println("decodedBytes " + new String(decodedBytes));
				//byte[] test= Base64.decodeBase64(entry.getValue().get("fingerISO").toString());
				//System.out.println("Test"+test);
				dbop_soc_root = new DbOperations(DB_ROOT_NAME);
				HashMap<Integer, Map<String, Object>> unitDetails = new HashMap<Integer, Map<String, Object>>();
				sql2="Select unit_no from service_prd_units where service_prd_id = '"+entry.getValue().get("service_prd_reg_id")+"';";
				//System.out.println("Sql"+sql2);
				dbo = entry.getValue().get("dob").toString();
				if(dbo.equals(""))
				{
					uDbo="Not Provided";
				}
				else
				{
				String[] dboArray=dbo.split("-");
				//System.out.println(dboArray);
				String[] monthName = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
				uDbo = dboArray[0]+"-"+monthName[Integer.parseInt(dboArray[1])]+"-"+dboArray[2];
				}
				//System.out.println(uDbo);
				entry.getValue().put("dob", uDbo);
				unitDetails = dbop_soc_root.Select(sql2);
				if(unitDetails.size()< 0)
				{
					entry.getValue().put("UnitDetails","");
				}
				else
				{
					entry.getValue().put("UnitDetails",unitDetails);
				}
				//System.out.println("ProviderID" +ProviderID);
				dbop_sec = new DbOperations(DB_SECURITY);
				//SELECT * FROM `staffattendance` where staff_id='"+entry.getValue().get("service_prd_reg_id")+"' and status='inside'
				String sSelectStaffAtend = "SELECT st.*,it.* FROM `staffattendance` as st left join `item_lended` as it on it.entry_id = st.sr_no where st.staff_id='"+entry.getValue().get("service_prd_reg_id")+"' and st.status='inside'" ;
				//System.out.println("sSelectStaffAtend :" +sSelectStaffAtend);
				resultFound = dbop_sec.Select(sSelectStaffAtend);
				//System.out.println("resultFound :" +resultFound);
				for(Entry<Integer, Map<String, Object>> entry1 : resultFound .entrySet()) 
				{
					if(entry1.getValue().get("inTimeStamp") != null)
					{
						MonthDate =entry1.getValue().get("inTimeStamp").toString();
						//System.out.println("MonthDate :" +MonthDate);
						String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);
						//entry.getValue().put("LetestCount", resultFound);
						Date dt = df.parse(Time);

						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
					
						timeOnly = tdf.format(dt);
						String dateOnly = dfmt.format(dt);
					
							// put staff entry in welcome array
						entry1.getValue().put("InTime", timeOnly);
						entry1.getValue().put("Date", dateOnly);
					}
					else
					{
						String OuttimeOnly = "00:00";
						String OutdateOnly = "00-00-00";
						entry1.getValue().put("InTime", OuttimeOnly);
						entry1.getValue().put("InDate", OutdateOnly);
					}
				}
				
				if(resultFound.size() > 0)
				{	
					rows2.put("ChekInExist", "Inside");
					rows2.put("InTime", resultFound);
					//rows2.put("ChekInProfile", entry.getValue().get("entry_profile").toString());
				}
				else
				{
					rows2.put("ChekInExist", "outside");
					rows2.put("InTime", "00:00");
					//rows2.put("ChekInProfile", "none");
				}
				catDetails[c] = entry.getValue().get("cat").toString();
				c=c+1;
			}
			
			
			//System.out.println(mpStaff);
			if(mpStaff.size() > 0)
			{
				//add document to map
					
				 rows2.put("visitors",MapUtility.HashMaptoList(mpStaff));
				rows2.put("Category",catDetails);
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("visitors","Not Found");
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			rows2.put("exception", e);
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println(rows);
		 return  rows;
		
	}
	public  HashMap<Integer, Map<String, Object>> fetchStaff(String contactNo, String servicePrdId, int iSocietyID)
	{
	  
		HashMap<Integer, Map<String, Object>>  mpStaff = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>>  resultFound = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String StatusBy="inside",sql2="",dbo="",uDbo="",MonthDate="",timeOnly="";
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			if(servicePrdId == "")
			{
				
				sSelectQuery = "Select spr.`full_name`,spr.`service_prd_reg_id`, spr.`cur_con_1`, spr.`cur_resd_add`, spr.`society_id`,sc.`cat_id`, c.`cat`,spr.`dob`,spr.photo,spr.photo_thumb,spr.since,spr.fingerISO from service_prd_reg as spr, cat c,spr_cat as sc where spr.`society_id`='"+iSocietyID+"' and spr.`cur_con_1` = '"+contactNo+"' and sc.`service_prd_reg_id`= spr.`service_prd_reg_id` and sc.`cat_id` = c.`cat_id` and sc.`status` = 'Y'";
				
			}
			else
			{
				sSelectQuery = "Select spr.`full_name`, spr.`service_prd_reg_id`,spr.`cur_con_1`, spr.`cur_resd_add`, spr.`society_id`,sc.`cat_id`, c.`cat`,spr.`dob`,spr.photo,spr.photo_thumb,spr.since,spr.fingerISO,spc.society_staff_id  from service_prd_reg as spr, cat c,spr_cat as sc,service_prd_society as spc where sc.`service_prd_reg_id`= spr.`service_prd_reg_id` and sc.`cat_id` = c.`cat_id` and spr.service_prd_reg_id = spc.provider_id and sc.`status` = 'Y' and spc.`society_staff_id`='"+servicePrdId+"' and spr.`society_id`='"+iSocietyID+"'";
				
			}
			//System.out.println("sSelectQuery: "+sSelectQuery);
			mpStaff = dbop_soc_root.Select(sSelectQuery);
			String catDetails[] = new String[mpStaff.size()];
			int c=0;
			for(Entry<Integer, Map<String, Object>> entry : mpStaff.entrySet()) 
			{
				
				//String query="select fingerISO from service_prd_reg where service_prd_reg_id='"+entry.getValue().get("service_prd_reg_id").toString()+"'";
				///byte[] fingerdata=dbop_soc_root.Selectfingerdata(query);
				// String finger = Base64.encodeBase64String(fingerdata);
				//System.out.println("fingerdata " + finger);
				//entry.getValue().put("FINGER ",finger);
				//String test = entry.getValue().get("fingerISO").toString();
				//byte[] encodedBytes = Base64.encodeBase64(test.getBytes());
				//System.out.println("encodedBytes " + new String(encodedBytes));
				//byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
				//System.out.println("decodedBytes " + new String(decodedBytes));
				//byte[] test= Base64.decodeBase64(entry.getValue().get("fingerISO").toString());
				//System.out.println("Test"+test);
				dbop_soc_root = new DbOperations(DB_ROOT_NAME);
				HashMap<Integer, Map<String, Object>> unitDetails = new HashMap<Integer, Map<String, Object>>();
				sql2="Select unit_no from service_prd_units where service_prd_id = '"+entry.getValue().get("service_prd_reg_id")+"';";
				//System.out.println("Sql"+sql2);
				dbo = entry.getValue().get("dob").toString();
				if(dbo.equals(""))
				{
					uDbo="Not Provided";
				}
				else
				{
				String[] dboArray=dbo.split("-");
				//System.out.println(dboArray);
				String[] monthName = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
				uDbo = dboArray[0]+"-"+monthName[Integer.parseInt(dboArray[1])]+"-"+dboArray[2];
				}
				//System.out.println(uDbo);
				entry.getValue().put("dob", uDbo);
				unitDetails = dbop_soc_root.Select(sql2);
				if(unitDetails.size()< 0)
				{
					entry.getValue().put("UnitDetails","");
				}
				else
				{
					entry.getValue().put("UnitDetails",unitDetails);
				}
				//System.out.println("ProviderID" +ProviderID);
				dbop_sec = new DbOperations(DB_SECURITY);
				String sSelectStaffAtend = "SELECT * FROM `staffattendance` where staff_id='"+entry.getValue().get("service_prd_reg_id")+"' and status='inside'" ;
				//System.out.println("sSelectStaffAtend :" +sSelectStaffAtend);
				resultFound = dbop_sec.Select(sSelectStaffAtend);
				//System.out.println("resultFound :" +resultFound);
				for(Entry<Integer, Map<String, Object>> entry1 : resultFound .entrySet()) 
				{
					if(entry1.getValue().get("inTimeStamp") != null)
					{
						MonthDate =entry1.getValue().get("inTimeStamp").toString();
						//System.out.println("MonthDate :" +MonthDate);
						String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);
						//entry.getValue().put("LetestCount", resultFound);
						Date dt = df.parse(Time);

						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
					
						timeOnly = tdf.format(dt);
						String dateOnly = dfmt.format(dt);
					
							// put staff entry in welcome array
						entry1.getValue().put("InTime", timeOnly);
						entry1.getValue().put("Date", dateOnly);
					}
					else
					{
						String OuttimeOnly = "00:00";
						String OutdateOnly = "00-00-00";
						entry1.getValue().put("InTime", OuttimeOnly);
						entry1.getValue().put("InDate", OutdateOnly);
					}
				}
			  
				if(resultFound.size() > 0)
				{	
					rows2.put("ChekInExist", "Inside");
					rows2.put("InTime", resultFound);
					//rows2.put("ChekInProfile", entry.getValue().get("entry_profile").toString());
				}
				else
				{
					rows2.put("ChekInExist", "outside");
					rows2.put("InTime", "00:00");
					//rows2.put("ChekInProfile", "none");
				}
				catDetails[c] = entry.getValue().get("cat").toString();
				c=c+1;
			}
			//System.out.println(mpStaff);
			if(mpStaff.size() > 0)
			{
				//add document to map
					
				 rows2.put("visitors",MapUtility.HashMaptoList(mpStaff));
				rows2.put("Category",catDetails);
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("visitors","Not Found");
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			rows2.put("exception", e);
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println(rows);
		 return  rows;
		
	}
	public  String getFormatedDate(String date)//input date in (yyyy-mm-dd) format returns date in (dd-MMM-yyyy) format
	{
		String uDate;
		String[] dboArray=date.split("-");
		String[] monthName = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		uDate = dboArray[2]+"-"+monthName[Integer.parseInt(dboArray[1])-1]+"-"+dboArray[0];
		return uDate;
	}
	public  HashMap<Integer, Map<String, Object>> fetchAllStaff(int iSocietyId)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sSelectQuery = "",sql2,dbo="",uDbo="",since="",updateSince="";
		String sSelectReview = "";  ////////////////
		HashMap<Integer, Map<String, Object>> AllStaff = new HashMap<Integer, Map<String, Object>>();
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			sSelectQuery = "Select spr.service_prd_reg_id,spr.`full_name`,spr.`age`,spr.`cur_con_1`,spr.`dob`,spr.`since`,spr.`ref_name`,spr.`ref_con_1`,sc.`cat_id`, c.`cat`,spr.photo,spr.photo_thumb,spr.IsBlock,spr.active,spr.security_status,spc.society_staff_id from service_prd_reg as spr, cat c,spr_cat as sc,service_prd_society as spc where spr.`society_id`='"+iSocietyId+"' and sc.`cat_id` = c.`cat_id` and spr.`service_prd_reg_id` = spc.provider_id and spr.`service_prd_reg_id` = sc.`service_prd_reg_id` and spr.`status` = 'Y' and sc.`status` = 'Y' and spr.society_id=spc.society_id";
			AllStaff = dbop_soc_root.Select(sSelectQuery);
			//System.out.println("All Staff detail:"+AllStaff);
			int ProviderID= 0;      ////////////////////
			int iCount = 0;         ////////////////////
			for(Entry<Integer, Map<String, Object>> entry : AllStaff.entrySet()) 
			{
				dbo = entry.getValue().get("dob").toString();
				if(dbo.equals(""))
				{
					uDbo="Not Provided";
				}
				else
				{
			
				uDbo = getFormatedDate(dbo);
				}
				entry.getValue().put("dob", uDbo);
				since = entry.getValue().get("since").toString();
				if(since.equals(""))
				{updateSince="Not Provided";}
				else
				{
				String[] sinceArray=since.split("-");
				String[] monthName = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
				updateSince = sinceArray[2]+"-"+monthName[Integer.parseInt(sinceArray[1])-1]+"-"+sinceArray[0];
				}
				//System.out.println(updateSince);
				entry.getValue().put("since", updateSince);
				HashMap<Integer, Map<String, Object>> unitDetails = new HashMap<Integer, Map<String, Object>>();
				sql2="Select unit_no from service_prd_units where service_prd_id = '"+entry.getValue().get("service_prd_reg_id")+"';";
				//System.out.println("Sql"+sql2);
				unitDetails = dbop_soc_root.Select(sql2);
				if(unitDetails.size()< 0)
				{
					entry.getValue().put("UnitDetails",MapUtility.HashMaptoList(unitDetails));
				}
				else
				{
					entry.getValue().put("UnitDetails",MapUtility.HashMaptoList(unitDetails));
				}
				///////////////////////////////////////////////////////////////
				if(entry.getValue().get("service_prd_reg_id") != null)
				{
					ProviderID =(Integer)  entry.getValue().get("service_prd_reg_id");
				}
				//System.out.println("ProviderId"+ProviderID);
				if(AllStaff.size() > 0 && ProviderID != 0)
				{
					sSelectReview="select ROUND( AVG(rating),1 ) as 'Rate',comment,name,add_comment_id from add_comment where service_prd_reg_id ='"+ProviderID+"'";
					HashMap<Integer, Map<String, Object>> Reviewlist =  dbop_soc_root.Select(sSelectReview);
					//entry.getValue().put("Review",Reviewlist);   ///////////////////////Use for adding the whole review array in all staff otherwise go for following code....
					for(Entry<Integer, Map<String, Object>> entry1 : Reviewlist.entrySet()) 
					{
					//System.out.println("Review:"+Reviewlist);
						entry.getValue().put("CommentEnteredBy",entry1.getValue().get("name"));
						entry.getValue().put("Comment",entry1.getValue().get("comment"));
						entry.getValue().put("Rating",entry1.getValue().get("Rate"));
					}	
				}	
			}
			if(AllStaff.size() > 0)
			{
				rows2.put("AllStaff",MapUtility.HashMaptoList(AllStaff));
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
			rows2.put("exception ",e );
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println(rows);
		return rows;
	}

	public  HashMap<Integer, Map<String, Object>> fetchStaffProvider(int iService_prd_reg_id,int iSocietyId) //throws ClassNotFoundException
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sSelectQuery = "",dbo="",uDbo="",since="",updateSince="";
		String sSelectReview = "";  ////////////////
		HashMap<Integer, Map<String, Object>> StaffProvider = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>> Reviewlist=null;
		try
		{
		dbop_soc_root = new DbOperations(DB_ROOT_NAME);
		sSelectQuery ="Select spr.service_prd_reg_id,spr.`full_name`,spr.`age`,spr.`cur_resd_add`,spr.`cur_con_1`,spr.`cur_con_2`,spr.`native_add`,spr.`native_con_1`,spr.`native_con_2`,spr.`dob`,spr.`since`,spr.`ref_name`,spr.`ref_add`,spr.`ref_con_1`,spr.`ref_con_2`,spr.security_status,spc.society_staff_id from service_prd_reg as spr,service_prd_society as spc where spr.`society_id`='"+iSocietyId+"' and spr.`service_prd_reg_id` = spc.provider_id and spr.`status` = 'Y' and spr.service_prd_reg_id='"+iService_prd_reg_id+"'";
		StaffProvider = dbop_soc_root.Select(sSelectQuery);
		//System.out.println("Staff detail:"+StaffProvider);
		int ProviderID= 0;      ////////////////////
		int iCount = 0;         ////////////////////
		for(Entry<Integer, Map<String, Object>> entry : StaffProvider.entrySet()) 
		{
			dbo = entry.getValue().get("dob").toString();
			System.out.println(entry.getValue().get("dob").toString());
				if(dbo.equals(""))
			{
				uDbo="Not Provided";
			}
			else
			{
		
			uDbo = getFormatedDate(dbo);
			}
			entry.getValue().put("dob", uDbo);
			
			since = entry.getValue().get("since").toString();
			if(since.equals(""))
			{updateSince="Not Provided";}
			else
			{
			String[] sinceArray=since.split("-");
			String[] monthName = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
			updateSince = sinceArray[2]+"-"+monthName[Integer.parseInt(sinceArray[1])-1]+"-"+sinceArray[0];
			}
			//System.out.println(updateSince);
			entry.getValue().put("since", updateSince);
			if(entry.getValue().get("service_prd_reg_id") != null)
			{
				ProviderID =(Integer)  entry.getValue().get("service_prd_reg_id");
			}
			//System.out.println("ProviderId"+ProviderID);
			if(StaffProvider.size() > 0 && ProviderID != 0)
			{
				sSelectReview="select rating as 'Rate',comment,name,add_comment_id from add_comment where service_prd_reg_id ='"+ProviderID+"'";
				Reviewlist =  dbop_soc_root.Select(sSelectReview);
				//entry.getValue().put("Review",Reviewlist);   ///////////////////////Use for adding the whole review array in all staff otherwise go for following code....
				
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();			
		}
		if(StaffProvider.size() > 0)
		{
			rows2.put("StaffProvider",MapUtility.HashMaptoList(StaffProvider));
			rows2.put("review", MapUtility.HashMaptoList(Reviewlist));
			rows.put("success",1);
			rows.put("response",rows2);
		}
		else
		{
			rows2.put("message ","");
			rows.put("success",0);
			rows.put("response",rows2);
		}
		return rows;
	}

	public  ArrayList<String> getDatesBetweenTwoDates(String str_date,String end_date)
	{
		List<Date> dates = new ArrayList<Date>();
		ArrayList<String> allDates = new ArrayList<String>();
		try
		{
			DateFormat formatter ; 
			formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date  startDate = (Date)formatter.parse(str_date); 
			Date  endDate = (Date)formatter.parse(end_date);
			long interval = 24*1000 * 60 * 60; // 1 hour in millis
			long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
			long curTime = startDate.getTime();
			while (curTime <= endTime) {
			    dates.add(new Date(curTime));
			    curTime += interval;
			}
			for(int i=0;i<dates.size();i++)
			{
			    Date lDate =(Date)dates.get(i);
			    String ds = formatter.format(lDate);
			    allDates.add(ds);
			    //System.out.println(" Date is ..." + ds);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return allDates;
	}
	public  HashMap<Integer, Map<String, Object>> fetchStaffReports(int StaffID,String StartDate,String EndDate)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//HashMap rowsFinal = new HashMap<>();
		String sSelectQuery = "";
		String sss=StartDate;
		String eee = EndDate;
		JsonObject jsonOption = new JsonObject(); 
		HashMap<Integer, Map<String, Object>> StaffReportDetails = new HashMap<Integer, Map<String, Object>>();
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			String StDate="00:01:01";
			String EnDate="23:59:59";
			if(StartDate== "" && EndDate=="" )
			{
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				StartDate= dateFormat.format(date) +" "+ StDate;
				EndDate = dateFormat.format(date)+ " " + EnDate;				
			}
			
			else
			{
				StartDate= StartDate +" "+ StDate;
				EndDate = EndDate + " " + EnDate;
			}
			//sSelectQuery = "Select * from `newstaff` where s_id = '"+StaffID+"'";
			
			sSelectQuery = "SELECT * FROM `staffattendance` where staff_id = '" + StaffID + "' AND `inTimeStamp` between '" + StartDate + "' AND '" + EndDate + "'";
			StaffReportDetails = dbop_sec.Select(sSelectQuery);
			//System.out.println("monthly attendance"+sSelectQuery);
			String MonthDate ="";
			String OutMonthDate ="";
			String attendanceDate [] = new String[StaffReportDetails.size()];
			String inTimeDetails [] = new String[StaffReportDetails.size()];
			String timeDiffDetails [] = new String[StaffReportDetails.size()];
			String outTimeDetails [] = new String[StaffReportDetails.size()];
			String outDateDetails [] = new String[StaffReportDetails.size()];
			String dateOnly="",timeOnly="";
			String[] monthName = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
			int i=0;
			for(Entry<Integer, Map<String, Object>> entry : StaffReportDetails.entrySet()) 
			{
				HashMap rows1 = new HashMap<>();
			 		//Integer iCnt = 1;
					if(entry.getValue().get("inTimeStamp") != null)
					{
						
						MonthDate =entry.getValue().get("inTimeStamp").toString();
						String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);
						Date dt = df.parse(Time);
						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yyyy");
						timeOnly = tdf.format(dt);
						dateOnly = dfmt.format(dt);
						rows1.put("InTime", timeOnly);
				 		rows1.put("Date", dateOnly);	
				 		attendanceDate[i] = dateOnly;
				 		inTimeDetails[i] = timeOnly;
					}
					
					if(entry.getValue().get("outTimeStamp") != null)
					{
						OutMonthDate =entry.getValue().get("outTimeStamp").toString();
						String OutTime = m_Timezone.convertToCurrentTimeZone(OutMonthDate);
						//System.out.println("out Time :"+OutTime);
						
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);
	
						Date dt = df.parse(OutTime);
	
						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yyyy");
	
	
						String OuttimeOnly = tdf.format(dt);
						String OutdateOnly = dfmt.format(dt);
						
						rows1.put("OutTime", OuttimeOnly);
				 		rows1.put("OutDate", OutdateOnly);
				 		outTimeDetails[i] = OuttimeOnly;
				 		outDateDetails[i] = OutdateOnly;
					//}
					    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					    Date d1 = null;
					    Date d2 = null;
					    try
					    {
					    	
					        d1 = format.parse(entry.getValue().get("inTimeStamp").toString());
					        d2 = format.parse(entry.getValue().get("outTimeStamp").toString());
					    } 
					    catch (ParseException e) 
					    {
					        e.printStackTrace();
					    }
					    long diff = d2.getTime() - d1.getTime();
					    long diffMinutes = diff / (60 * 1000) % 60;
					    long diffHours = diff / (60 * 60 * 1000);
					    String diffStr = diffHours+":"+diffMinutes;
					    rows1.put("TimeDiff",diffStr);
					    timeDiffDetails[i] = diffStr;
						entry.getValue().put("Timelist", rows1);
						i++;
						//System.out.println("\nTTTT:"+entry.getValue().get("Timelist")+"\n");
						//System.out.println("\nSSSS:"+StaffReportDetails+"\n");
					}
					else
					{
						String OuttimeOnly = "00:00";
						String OutdateOnly = "00-00-00";
						rows1.put("OutTime", OuttimeOnly);
				 		rows1.put("OutDate", OutdateOnly);
				 		String diffStr = "00";
				 		rows1.put("TimeDiff",diffStr);
				 		timeDiffDetails[i] = diffStr;
				 		outTimeDetails[i] = OuttimeOnly;
				 		outDateDetails[i] = OutdateOnly;
				 		entry.getValue().put("Timelist", rows1);
					}
				}
				int MonthDays[] = {31,28,31,30,31,30,31,31,30,31,30,31};
				String startDate2 = getFormatedDate(sss);
				//System.out.println("InTime:"+inTimeDetails[2]);
				//System.out.println("TimeDiff:"+timeDiffDetails[2]);
				
				String endDate2 =  getFormatedDate(eee);
				//System.out.println(endDate2);
				ArrayList<String> allDates = getDatesBetweenTwoDates(startDate2, endDate2); 
				String attReport[] = new String[allDates.size()];
				String inTimeReport[] = new String[allDates.size()];
				String timeDiffReport[] = new String[allDates.size()];
				String outTimeReport[] = new String[allDates.size()];
				String outDateReport[] = new String[allDates.size()];
				//System.out.println("AllDate:"+allDates);
				for(i=0;i<attReport.length;i++)
				{
					attReport[i]="A";
					inTimeReport[i] = "--";
					timeDiffReport[i] = "--";
					outTimeReport[i] = "--";
					outDateReport[i] = "00-00-00";
				}
				int m = 0;
				for(int j = 0;j<allDates.size();j++)
				{
					if(attendanceDate.length > 0)
					{
						for(i=0;i<attendanceDate.length;i++)
						{
						//System.out.println("i:"+i+"j:"+j+"\nAtt:"+attendanceDate[i]+"\nAD:"+allDates.get(j));	
						//entry1.getValue().put("Date", allDates.get(j));
							if(allDates.get(j).equalsIgnoreCase(attendanceDate[i]))
							{
								attReport[j] = "P";//entry1.getValue().put("Attendance", "P");
							//System.out.println("InTime:"+inTimeDetails[m]+"m:"+m);
							//System.out.println("TimeDiff:"+timeDiffDetails[m]+"m:"+m);
								inTimeReport[j] = inTimeDetails[m];
								timeDiffReport[j] = timeDiffDetails[m];
								outTimeReport[j] = outTimeDetails[m];
								outDateReport[j] = outDateDetails[m];
								m++;
							}
						}
					}
					else
					{
					}
				}
				for(i=0;i<attReport.length;i++)
				{
					//System.out.println("Att Report:"+attReport[i]);
					//System.out.println("inTime Report:"+inTimeReport[i]);
					//System.out.println("time diff Report:"+timeDiffReport[i]);
				//	System.out.println("out time report:"+outTimeReport[i]);
				}
				//ArrayList<String><String> attReportFinal = new ArrayList<String><String>();
				HashMap tempAtt = new HashMap();
				String attReportFinal[][] = new String[allDates.size()][6];
				int k=0;
				for(i = 0; i < allDates.size(); i++)
				{
					for(int j = 0; j < 6; j++)
					{
						if(j == 0)
						{
							attReportFinal[i][j] = allDates.get(i);
							//tempAtt.put("Date", allDates.get(i));
						}
						else if(j == 1)
						{
							attReportFinal[i][j] =  attReport[i];
						}
						else if(j == 2)
						{
							attReportFinal[i][j] = inTimeReport[i];
						}
						else if(j == 3)
						{
							attReportFinal[i][j] =  timeDiffReport[i];
						}
						else if(j == 4)
						{
							attReportFinal[i][j] =  outTimeReport[i];
						}
						else
						{
						//	System.out.println("out time report:"+outDateReport[i]);
							attReportFinal[i][j] = outDateReport[i];
						}
					}
				}
				//System.out.println("TempATT:"+tempAtt);
				if(attReportFinal.length > 0)
				{
					//rows2.put("StaffReport",MapUtility.HashMaptoList(StaffReportDetails) );
					rows2.put("AttendanceReport",attReportFinal);
					rows.put("success",1);
					rows.put("response",rows2);	
					
				}
				else
				{
					rows2.put("message ","");
					rows.put("success",0);
					rows.put("response",rows2);
					//rows.put("InDate", dateOnly);
					//rows.put("InTime",timeOnly);
				}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			rows2.put("exception ",e );
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println("Rows:"+rows);
		return rows;
	}
	
	public  HashMap<Integer, Map<String, Object>> fetchWelcomeStaff(int StaffEntryID,int StaffID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap rows3 = new HashMap<>();
		String sSelectQuery = "",sSelectQuery1="";
		String StatusBy="inside",sql1="",MonthDate="";
		String dateOnly="",timeOnly="";
		HashMap<Integer, Map<String, Object>> WelcomeStaff = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>>  resultFound = new HashMap<Integer, Map<String, Object>>();
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			sSelectQuery = "SELECT * FROM `service_prd_reg` where service_prd_reg_id='"+StaffID+"'";
			WelcomeStaff = dbop_soc_root.Select(sSelectQuery);
			//System.out.println(WelcomeStaff);
			
			dbop_sec = new DbOperations(DB_SECURITY);
			String sSelectStaffAtend = "SELECT * FROM `staffattendance` where sr_no='"+StaffEntryID+"'" ;
			resultFound = dbop_sec.Select(sSelectStaffAtend);
			String latestImg="";
			for(Entry<Integer, Map<String, Object>> entry : resultFound .entrySet()) 
			{
				MonthDate =entry.getValue().get("inTimeStamp").toString();
				String profile =entry.getValue().get("entry_profile").toString();
				
				if(entry.getValue().get("entry_image") != null)
				{
					 latestImg =entry.getValue().get("entry_image").toString();
					// OutMonthDate =entry.getValue().get("outTimeStamp").toString();
				}
				else
				{
					 latestImg="no";
				}
				String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
				String fmt = "yyyy-MM-dd HH:mm a";
				DateFormat df = new SimpleDateFormat(fmt);
				entry.getValue().put("VisitorDetails", resultFound);
				Date dt = df.parse(Time);

				DateFormat tdf = new SimpleDateFormat("hh:mm a");
				DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
				timeOnly = tdf.format(dt);
				dateOnly = dfmt.format(dt);
				//System.out.println("InTime:"+timeOnly);
				//System.out.println("date :"+dateOnly + "<br>");
			
			//rows3.put("InTime", timeOnly);
			//rows3.put("Date", dateOnly);
			
			//entry.getValue().put("InTime", timeOnly);
			//entry.getValue().put("Date", dateOnly);   
			rows3.put("Profile", profile);
			rows3.put("Img", latestImg);// put staff entry in welcome array
			rows3.put("InTime", timeOnly);
			rows3.put("Date", dateOnly);
		}
			
			
			//System.out.println(WelcomeStaff);
			if(WelcomeStaff.size() > 0)
			{
				//add document to map
					//System.out.println("inside If");
				 rows2.put("welcome",MapUtility.HashMaptoList(WelcomeStaff));
				 rows2.put("Result",rows3);
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("welcome","");
				 rows2.put("message ","No Visitor found" );
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rows;
		
	}
	public  HashMap<Integer, Map<String, Object>> fetchAllStaffReports(String StartDate,String EndDate)
	{
		
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		//HashMap rowsFinal = new HashMap<>();
		String sSelectQuery = "",sql1="";
		JsonObject jsonOption = new JsonObject();
		HashMap<Integer, Map<String, Object>> staffReportDetails = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>> staffDetails = new HashMap<Integer, Map<String, Object>>();
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			String StDate="00:01:01";
			String EnDate="23:59:59";				
			if(StartDate== "" && EndDate=="" )
			{
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				StartDate= dateFormat.format(date) +" "+ StDate;
				EndDate = dateFormat.format(date)+ " " + EnDate;
			}
			else
			{
				StartDate= StartDate +" "+ StDate;
				EndDate = EndDate + " " + EnDate;
			}
			//System.out.println("Date"+StartDate);
			dbop_sec = new DbOperations(DB_SECURITY);
			sSelectQuery = "SELECT * FROM `staffattendance` where `inTimeStamp` between '"+StartDate+"' AND '"+EndDate+"' ORDER BY `sr_no` ASC";
			staffReportDetails = dbop_sec.Select(sSelectQuery);
			//System.out.println("staffReportDetails : "+sSelectQuery);
			String MonthDate ="";
			String OutMonthDate ="";
			//Map<String, List<String>> hm = new HashMap<String, List<String>>();
			//List<String> values = new ArrayList<String>();
			String dateOnly="",timeOnly="";
			//System.out.println("visitor:"+visitorReportDetails);
			for(Entry<Integer, Map<String, Object>> entry : staffReportDetails.entrySet()) 
			{
				dbop_soc_root = new DbOperations(DB_ROOT_NAME);
				String sql2 = "Select * from service_prd_reg where service_prd_reg_id = '"+entry.getValue().get("staff_id")+"'";
				//System.out.println("sql2 : "+sql2);
				staffDetails = dbop_soc_root.Select(sql2);
				//System.out.println("staffDetails :"+staffDetails);
				//dbop = new DbOperations(DB_ROOT_NAME);
				for(Entry<Integer, Map<String, Object>> entry1 : staffDetails.entrySet()) 
				{
					entry.getValue().put("FullName", entry1.getValue().get("full_name"));
					entry.getValue().put("Mobile", entry1.getValue().get("cur_con_1"));
				}
				//dbop = new DbOperations(DB_ROOT_NAME);
				HashMap<Integer, Map<String, Object>> unitDetails = new HashMap<Integer, Map<String, Object>>();
				String sql3="Select unit_no from service_prd_units where service_prd_id = '"+entry.getValue().get("staff_id")+"';";
				//System.out.println("Sql"+sql3);
				unitDetails = dbop_soc_root.Select(sql3);
				//System.out.println("unitDetails : "+unitDetails);
				if(unitDetails.size()< 0)
				{
					entry.getValue().put("UnitDetails","");
				}
				else
				{
					entry.getValue().put("UnitDetails",unitDetails);
				}
				HashMap rows1 = new HashMap<>();
		 		//Integer iCnt = 1;
				if(entry.getValue().get("inTimeStamp") != null)
				{
					MonthDate =entry.getValue().get("inTimeStamp").toString();
					
					String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
					String fmt = "yyyy-MM-dd HH:mm a";
					DateFormat df = new SimpleDateFormat(fmt);

					Date dt = df.parse(Time);

					DateFormat tdf = new SimpleDateFormat("hh:mm a");
					DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
					timeOnly = tdf.format(dt);
					dateOnly = dfmt.format(dt);
					//System.out.println("InTime:"+timeOnly);
					//System.out.println("date :"+dateOnly + "<br>");
					
					rows1.put("InTime", timeOnly);
			 		rows1.put("Date", dateOnly);
			 		//rows1.put("ID", entry.getValue().get("sr_no"));
			 	//	
					
				}
				if(entry.getValue().get("outTimeStamp") != null)
				{
					OutMonthDate =entry.getValue().get("outTimeStamp").toString();
					String OutTime = m_Timezone.convertToCurrentTimeZone(OutMonthDate);
					//System.out.println("out Time :"+OutTime);
					
					String fmt = "yyyy-MM-dd HH:mm a";
					DateFormat df = new SimpleDateFormat(fmt);

					Date dt = df.parse(OutTime);

					DateFormat tdf = new SimpleDateFormat("hh:mm a");
					DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");


					String OuttimeOnly = tdf.format(dt);
					String OutdateOnly = dfmt.format(dt);
					
					rows1.put("OutTime", OuttimeOnly);
			 		rows1.put("OutDate", OutdateOnly);
			 		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    Date d1 = null;
				    Date d2 = null;
				    try
				    {
				        d1 = format.parse(entry.getValue().get("inTimeStamp").toString());
				        d2 = format.parse(entry.getValue().get("outTimeStamp").toString());
				    } 
				    catch (ParseException e) 
				    {
				        e.printStackTrace();
				    }
				    long diff = d2.getTime() - d1.getTime();
				    long diffMinutes = diff / (60 * 1000) % 60;
				    long diffHours = diff / (60 * 60 * 1000);
				    String diffStr = diffHours+":"+diffMinutes;
				    rows1.put("TimeDiff",diffStr);
					entry.getValue().put("Timelist", rows1);
					//System.out.println("\nTTTT:"+entry.getValue().get("Timelist")+"\n");
					//System.out.println("\nSSSS:"+StaffReportDetails+"\n");
				}
				else
				{
					String OuttimeOnly = "00:00";
					String OutdateOnly = "00-00-00";
					rows1.put("OutTime", OuttimeOnly);
			 		rows1.put("OutDate", OutdateOnly);
			 		String diffStr = "00";
			 		rows1.put("TimeDiff",diffStr);
			 		entry.getValue().put("Timelist", rows1);
				}
			}
			
			if(staffReportDetails.size() > 0)
			{
				rows2.put("StaffReport",MapUtility.HashMaptoList(staffReportDetails) );
				rows.put("success",1);
				rows.put("response",rows2);	
				
			}
			else
			{
				rows2.put("message ","");
				rows.put("success",0);
				rows.put("response",rows2);
				//rows.put("InDate", dateOnly);
				//rows.put("InTime",timeOnly);
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			rows2.put("exception ",e );
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println("Rows:"+rows);
		return rows;
	}
	
	/* --------------------------- Fetch Letast Five Record in Staff ------------------------- */
	
	public  HashMap<Integer, Map<String, Object>> fetchStaffLetestReports(int StaffID)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap rows3 = new HashMap<>();
		String sSelectQuery = "",sSelectQuery1="";
		
		String dateOnly="",timeOnly="",MonthDate="",OutMonthDate="";
		
		HashMap<Integer, Map<String, Object>>  resultFound = new HashMap<Integer, Map<String, Object>>();
		try
		{

//			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			dbop_sec = new DbOperations(DB_SECURITY);

			String sSelectStaffAtend = "SELECT * FROM `staffattendance` where staff_id='"+StaffID+"' and status='outside' ORDER BY sr_no DESC LIMIT 5" ;
			resultFound = dbop_sec.Select(sSelectStaffAtend);
			
			for(Entry<Integer, Map<String, Object>> entry : resultFound .entrySet()) 
			{
				MonthDate =entry.getValue().get("inTimeStamp").toString();
				
				String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
				String fmt = "yyyy-MM-dd HH:mm a";
				DateFormat df = new SimpleDateFormat(fmt);
				//entry.getValue().put("LetestCount", resultFound);
				Date dt = df.parse(Time);

				DateFormat tdf = new SimpleDateFormat("hh:mm a");
				DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
				
				timeOnly = tdf.format(dt);
				dateOnly = dfmt.format(dt);
				
			// put staff entry in welcome array
				entry.getValue().put("InTime", timeOnly);
				entry.getValue().put("Date", dateOnly);
				
				if(entry.getValue().get("outTimeStamp") != null)
				{
				 OutMonthDate =entry.getValue().get("outTimeStamp").toString();
				 String OutTime = m_Timezone.convertToCurrentTimeZone(OutMonthDate);
					//System.out.println("out Time :"+OutTime);
					Date dt1 = df.parse(OutTime);
				 String OuttimeOnly = tdf.format(dt1);
				 String OutdateOnly = dfmt.format(dt1);
				
				 entry.getValue().put("OutTime", OuttimeOnly);
				 entry.getValue().put("OutDate", OutdateOnly);
				
				
				}
				else
				{

					String OuttimeOnly = "00:00";
					String OutdateOnly = "00-00-00";
					entry.getValue().put("OutTime", OuttimeOnly);
					entry.getValue().put("OutDate", OutdateOnly);
				}
			}
			
			
			
			if(resultFound.size() > 0)
			{
				 rows2.put("LetestFive",MapUtility.HashMaptoList(resultFound));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				
				 rows2.put("LetestFive","");
				 rows2.put("message ","No Report Found" );
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rows;
		
	}
	public  HashMap<Integer, Map<String, Object>> insertfingerdata(String servicePrdId, int iSocietyID, String fingerISO)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sInsertQuery = "";
		try
		{	
			dbop_soc_root=new DbOperations(DB_ROOT_NAME);
			//System.out.println();
			sInsertQuery = "UPDATE `service_prd_reg`as spr, cat as c,spr_cat as sc set spr.fingerISO='"+fingerISO+"' where sc.`service_prd_reg_id`= spr.`service_prd_reg_id` and sc.`cat_id` = c.`cat_id` and sc.`status` = 'Y' and spr.`service_prd_reg_id`='"+servicePrdId+"' and spr.`society_id`='"+iSocietyID+"'";
			//System.out.println("Query"+sInsertQuery);
			long fingerID = dbop_soc_root.Update(sInsertQuery);
			if(fingerID  > 0)
			{
				 rows2.put("fingerID ",fingerID );
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				 rows2.put("fingerID ","");
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			rows2.put("exception ",e);
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println(rows);
		return rows;
	}
	public  HashMap<Integer, Map<String, Object>> fetchStaffByFinger(String fingerISO, int iSocietyID)
	{
	  
		HashMap<Integer, Map<String, Object>>  mpStaff = new HashMap<Integer, Map<String, Object>>();
		HashMap<Integer, Map<String, Object>>  resultFound = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String StatusBy="inside",sql2="",dbo="",uDbo="",MonthDate="",timeOnly="";
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			
				
				sSelectQuery = "Select spr.`full_name`,spr.`service_prd_reg_id`, spr.`cur_con_1`, spr.`cur_resd_add`, spr.`society_id`,sc.`cat_id`, c.`cat`,spr.`dob`,spr.photo,spr.photo_thumb,spr.since,spr.fingerISO from service_prd_reg as spr, cat c,spr_cat as sc where spr.`society_id`='"+iSocietyID+"' and spr.`fingerISO` = '"+fingerISO+"' and sc.`service_prd_reg_id`= spr.`service_prd_reg_id` and sc.`cat_id` = c.`cat_id` and sc.`status` = 'Y'";
				
			
			//System.out.println("sSelectQuery: "+sSelectQuery);
			mpStaff = dbop_soc_root.Select(sSelectQuery);
			String catDetails[] = new String[mpStaff.size()];
			int c=0;
			for(Entry<Integer, Map<String, Object>> entry : mpStaff.entrySet()) 
			{
				
				//String query="select fingerISO from service_prd_reg where service_prd_reg_id='"+entry.getValue().get("service_prd_reg_id").toString()+"'";
				///byte[] fingerdata=dbop_soc_root.Selectfingerdata(query);
				// String finger = Base64.encodeBase64String(fingerdata);
				//System.out.println("fingerdata " + finger);
				//entry.getValue().put("FINGER ",finger);
				//String test = entry.getValue().get("fingerISO").toString();
				//byte[] encodedBytes = Base64.encodeBase64(test.getBytes());
				//System.out.println("encodedBytes " + new String(encodedBytes));
				//byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
				//System.out.println("decodedBytes " + new String(decodedBytes));
				//byte[] test= Base64.decodeBase64(entry.getValue().get("fingerISO").toString());
				//System.out.println("Test"+test);
				dbop_soc_root = new DbOperations(DB_ROOT_NAME);
				HashMap<Integer, Map<String, Object>> unitDetails = new HashMap<Integer, Map<String, Object>>();
				sql2="Select unit_no from service_prd_units where service_prd_id = '"+entry.getValue().get("service_prd_reg_id")+"';";
				//System.out.println("Sql"+sql2);
				dbo = entry.getValue().get("dob").toString();
				if(dbo.equals(""))
				{
					uDbo="Not Provided";
				}
				else
				{
				String[] dboArray=dbo.split("-");
				//System.out.println(dboArray);
				String[] monthName = {"","Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
				uDbo = dboArray[0]+"-"+monthName[Integer.parseInt(dboArray[1])]+"-"+dboArray[2];
				}
				//System.out.println(uDbo);
				entry.getValue().put("dob", uDbo);
				unitDetails = dbop_soc_root.Select(sql2);
				if(unitDetails.size()< 0)
				{
					entry.getValue().put("UnitDetails","");
				}
				else
				{
					entry.getValue().put("UnitDetails",unitDetails);
				}
				//System.out.println("ProviderID" +ProviderID);
				dbop_sec = new DbOperations(DB_SECURITY);
				String sSelectStaffAtend = "SELECT * FROM `staffattendance` where staff_id='"+entry.getValue().get("service_prd_reg_id")+"' and status='inside'" ;
				//System.out.println("sSelectStaffAtend :" +sSelectStaffAtend);
				resultFound = dbop_sec.Select(sSelectStaffAtend);
				//System.out.println("resultFound :" +resultFound);
				for(Entry<Integer, Map<String, Object>> entry1 : resultFound .entrySet()) 
				{
					if(entry1.getValue().get("inTimeStamp") != null)
					{
						MonthDate =entry1.getValue().get("inTimeStamp").toString();
						//System.out.println("MonthDate :" +MonthDate);
						String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);
						//entry.getValue().put("LetestCount", resultFound);
						Date dt = df.parse(Time);

						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
					
						timeOnly = tdf.format(dt);
						String dateOnly = dfmt.format(dt);
					
							// put staff entry in welcome array
						entry1.getValue().put("InTime", timeOnly);
						entry1.getValue().put("Date", dateOnly);
					}
					else
					{
						String OuttimeOnly = "00:00";
						String OutdateOnly = "00-00-00";
						entry1.getValue().put("InTime", OuttimeOnly);
						entry1.getValue().put("InDate", OutdateOnly);
					}
				}
			  
				if(resultFound.size() > 0)
				{	
					rows2.put("ChekInExist", "Inside");
					rows2.put("InTime", resultFound);
					//rows2.put("ChekInProfile", entry.getValue().get("entry_profile").toString());
				}
				else
				{
					rows2.put("ChekInExist", "outside");
					rows2.put("InTime", "00:00");
					//rows2.put("ChekInProfile", "none");
				}
				catDetails[c] = entry.getValue().get("cat").toString();
				c=c+1;
			}
			//System.out.println(mpStaff);
			if(mpStaff.size() > 0)
			{
				//add document to map
					
				 rows2.put("visitors",MapUtility.HashMaptoList(mpStaff));
				rows2.put("Category",catDetails);
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("visitors","Not Found");
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			rows2.put("exception", e);
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println(rows);
		 return  rows;
		
	}
	
	public  HashMap<Integer, Map<String, Object>> fetchStaffSyncronise(int iSocietyID)
	{
	  
		HashMap<Integer, Map<String, Object>>  mpStaff = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			sSelectQuery = "Select spr.`full_name`, spr.`service_prd_reg_id`, spr.`society_id`,spr.`fingerISO` from service_prd_reg as spr, cat c,spr_cat as sc where sc.`service_prd_reg_id`= spr.`service_prd_reg_id` and sc.`cat_id` = c.`cat_id` and sc.`status` = 'Y' and spr.`status` = 'Y' and spr.`society_id`= '"+iSocietyID+"'";
			
			//System.out.println("sSelectQuery: "+sSelectQuery);
			mpStaff = dbop_soc_root.Select(sSelectQuery);
			//System.out.println("mpStaff"+mpStaff);
			if(mpStaff.size() > 0)
			{
				//add document to map
					
				 rows2.put("staffdata",MapUtility.HashMaptoList(mpStaff));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("staffdata","Not Found");
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			rows2.put("exception", e);
			rows.put("success",0);
			rows.put("response",rows2);
		}
		//System.out.println(rows);
		 return  rows;
		
	}
	
	public static void main(String[] args)// throws Exception
	{
		
		String sToken = "7OcsNHBSGr5lCZlc5Hb7rITIRR-MAhiGmRjszjyvLwDdLOar-X5orYtpfvcVXDF0WUk8wi9HL3tXgH26rnQ_qa8lsdKB6KdgP-PclmV3X9LxnXMA7U8ZH3PAYsqOwhuDdfl2Skj-_ht3gA0N_Od8oWpYoTQuS99G7a5U21fT2dxpEh0JrMhL7hVMaeZ795Vx";

		Staff st=new Staff(sToken); 

		int iStaffID = 15;
		HashMap objHash = st.fetchStaffProvider(iStaffID, 59);

		//HashMap objHash = st.getStaffDetails(59);
		//ProviderID,iSocietyID,profile,iGateEntry,pNote,counter
		//,float Temp,float Oxygen,int Pulse
		//HashMap objHash = st.markEntry(10,59,"Gardener",1,"123456wqerty","1", 30, 99, 98);
		//String contactNo, int servicePrdId, int iSocietyID
		//ArrayList<String> dates=st.getDatesBetweenTwoDates("01-Aug-2018", "01-Sep-2018");
		//markEntry(int staff_id, int societyId,String sProfile,int iGateEntry)
		//fetchStaff(String contactNo, String servicePrdId, int iSocietyID)
		//fetchStaffReports(int StaffID,String StartDate,String EndDate)
		//HashMap objHash = st.fetchStaffSyncronise(59);
		Gson objGson = new Gson();
		String objStr = objGson.toJson(objHash);
		System.out.println("entryyyyyy        "+objStr);
//		response.setContentType("application/json");
//		out.println(objStr);
//		st.monthlyStaffAttendance(7, 2018,4);
		//st.markEntry(21);
	//	st.markExit(21);

	}

	
}
