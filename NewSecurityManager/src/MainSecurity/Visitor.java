package MainSecurity;

import MainSecurity.DbConstants.*;
import MainSecurity.CommonBaseClass;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import SecuriyCommonUtility.MapUtility;

public class Visitor extends CommonBaseClass
{
	
/*	
	dbop_sec = new DbOperations(DB_SECURITY);
	
	System.out.println("SECURITY ROOT DB:"+DB_SECURITY_ROOT);
	dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
	
	System.out.println("DB_SOCIETY:" + DB_SOCIETY);
	dbop_soc = new DbOperations(DB_SOCIETY);
*/	
	
	ProjectConstants m_objProjectConstants;
//	private static SecuriyCommonUtility.MapUtility m_objMapUtility;
	private  TimeZoneConvertor m_Timezone;
	public Visitor(String token)
	{
		super(token);
//		System.out.println("Inside visitor ctor");
//		m_objProjectConstants = new ProjectConstants();
//		m_objMapUtility = new MapUtility();
		if(IsTokenValid())
		{
			try 
			{
				m_Timezone= new TimeZoneConvertor();
			} 
			catch (ClassNotFoundException e) 
			{
				System.out.println("Exception");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Token invalid");
		}
//		System.out.println("end of ctor");
	}
	
	public  HashMap<Integer, Map<String, Object>> getMobileFromUnit(int unit_no)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();		
		try 
		{
			String sSqlMobileNo = "";
			sSqlMobileNo = "select mob,alt_mob from member_main where unit = '"+unit_no+"' and `ownership_status` = '1'";
			dbop_soc = new DbOperations(DB_SOCIETY);
			HashMap<Integer, Map<String, Object>>  mProvideMobile = dbop_soc.Select(sSqlMobileNo);
			for(Entry<Integer, Map<String, Object>> entry : mProvideMobile .entrySet()) 
			{
				if(!entry.getValue().get("mob").equals("0") )
				{

					rows2.put("mobile_no", entry.getValue().get("mob"));
					rows.put("success", 1);
					rows.put("response",rows2);

				}
				else if (!entry.getValue().get("alt_mob").equals(""))
				{

					rows2.put("mobile_no", entry.getValue().get("alt_mob"));
					rows.put("success", 1);
					rows.put("response",rows2);

				}
				else 
				{
					rows2.put("mobile_no", "not provided");
					rows.put("success", 0);
					rows.put("response",rows2);

				}
			}
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			
		}	
	
    return  rows;
	}
	
	//MARK VISITOR EXIT 
		public  HashMap<Integer, Map<String, Object>> markExit(int visitor_id)
		{
		
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String sUpdateQuery = "";
		
			try
			{
				dbop_sec = new DbOperations(DB_SECURITY);
				sUpdateQuery = "Update visitordetails set status='outside',outTimeStamp=now() Where id = '"+visitor_id+"' AND status='inside' ";

				long exit = dbop_sec.Update(sUpdateQuery);

				if(exit  > 0)
				{
					//add member to map
					 rows2.put("VisitorID ",visitor_id );
					 rows2.put("message ","Visitor exit marked" );
					 rows.put("success",1);
					 rows.put("response",rows2);			 
				}
				else
				{
					//member not found
					 rows2.put("VisitorID ",visitor_id );
					 rows2.put("message ","No Visitor Inside Entry found" );
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
	
		
		public  HashMap<Integer, Map<String, Object>> mfetchAllVisitors()
		{
			System.out.println("Inside mfetchAllVisitors");
			
			HashMap<Integer, Map<String, Object>>  visitorEntryDetails = new HashMap<Integer, Map<String, Object>>();
			//HashMap<Integer, Map<String, Object>>  mpVisitors = new HashMap<Integer, Map<String, Object>>();
			String sSelectQuery = "",sql2="";
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String StatusBy="inside",sql1="",MonthDate="";
			String dateOnly="",timeOnly="";
			String sqlflag;
			
			try
			{
				System.out.println("SECURITY DB:"+DB_SECURITY);
				dbop_sec = new DbOperations(DB_SECURITY);
				
				System.out.println("SECURITY ROOT DB:"+DB_SECURITY_ROOT);
				dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
				
				System.out.println("DB_SOCIETY:" + DB_SOCIETY);
				dbop_soc = new DbOperations(DB_SOCIETY);

				//sql1="Select * from visitorentry where status='inside' AND `outTimeStamp` ='0000-00-00 00:00:00' AND purpose_id NOT IN(0) AND visitor_ID NOT IN(0) ORDER BY id DESC";
//				sql1="Select v.*, p.purpose_name,p.time_limit from `visitorentry` as v join `purpose` as p on p.`purpose_id`= v.`purpose_id` where v.status='inside' AND v.`outTimeStamp` ='0000-00-00 00:00:00' AND v.purpose_id NOT IN(0) AND v.visitor_ID NOT IN(0) ORDER BY id DESC";
				sql1 = "SELECT v.*, p.purpose_name, p.time_limit FROM visitorentry v JOIN (SELECT visitor_ID, MAX(id) AS max_id FROM visitorentry WHERE status='inside' AND outTimeStamp='0000-00-00 00:00:00' GROUP BY visitor_ID) latest ON v.visitor_ID = latest.visitor_ID AND v.id = latest.max_id JOIN purpose p ON p.purpose_id = v.purpose_id WHERE v.purpose_id NOT IN(0) AND v.visitor_ID NOT IN(0) ORDER BY v.id DESC";
				System.out.println("sqlvisitor : " + sql1);
				HashMap<Integer, Map<String, Object>>  flagDetails = new HashMap<Integer, Map<String, Object>>();
				dbop_sec = new DbOperations(DB_SECURITY);
				visitorEntryDetails = dbop_sec.Select(sql1);
				System.out.println("visitorEntry count :"+visitorEntryDetails.size());
				System.out.println("visitorEntryDetails :"+visitorEntryDetails);
				for(Entry<Integer, Map<String, Object>> entry : visitorEntryDetails .entrySet()) 
				{	
					System.out.println("Entry : " + entry.toString());
					//System.out.println("Mobile " + entry.getValue().get("visitorMobile"));
					//String mob =(String) entry.getValue().get("visitorMobile");
					//System.out.println(mob);
					//if(mob.equals("7972928940")) 
						//System.out.println(mob);
					
					dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
					sSelectQuery = "Select CONCAT(v.Fname,' ',v.Lname) as FullName,v.Mobile,v.img,v.Doc_No,v.Doc_Type_ID,v.Doc_img from visitors as v where v.visitor_id = '"+entry.getValue().get("visitor_ID")+"'";
					//sSelectQuery = "Select CONCAT(v.Fname,' ',v.Lname) as FullName,v.Mobile,v.Doc_img,v.Doc_No,v.Doc_Type_ID,d.document from visitors as v, documents as d where v.Doc_Type_ID = d.id and v.visitor_id = '"+entry.getValue().get("visitor_ID")+"'";
					
					//System.out.println("My visitor: " + sSelectQuery);
					HashMap<Integer, Map<String, Object>>  mpVisitors = dbop_sec_root.Select(sSelectQuery);
					System.out.println("My visitor: " + sSelectQuery);
					MonthDate =entry.getValue().get("otpGtimestamp").toString();
					String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
					String fmt = "yyyy-MM-dd HH:mm a";
					DateFormat df = new SimpleDateFormat(fmt);
					System.out.println("My visitor: " + mpVisitors);
					entry.getValue().put("VisitorDetails", mpVisitors);
					Date dt = df.parse(Time);

					DateFormat tdf = new SimpleDateFormat("hh:mm a");
					DateFormat dfmt  = new SimpleDateFormat("dd-MMM");
					timeOnly = tdf.format(dt);
					dateOnly = dfmt.format(dt);
					//System.out.println("Time only : "+ timeOnly);
					
					DateFormat desiredFormat = new SimpleDateFormat("MMM dd, h:mm:ss a");
					String formattedTimestamp = desiredFormat.format(dt);
					entry.getValue().put("otpGtimestamp", formattedTimestamp);

					
					
					String time_limit=entry.getValue().get("time_limit").toString();
					//System.out.println("Hello : " +time_limit);
					
					DateFormat tdf1 = new SimpleDateFormat("HH:mm a");
					String timeOnly1 = tdf1.format(dt);
					String[] split = timeOnly1.split(":");
					String[] split1=split[1].split(" ");
					String[] split2=time_limit.split(":");
					int hours = Integer.valueOf(split[0]);
					int minutes = Integer.valueOf(split1[0]);
					int hours1 = Integer.valueOf(split2[0]);
					int minutes1 = Integer.valueOf(split2[1]);

					//System.out.println("Hours : " +hours);
					//System.out.println("Minutes : " +minutes);
					//System.out.println("Hours : " +hours1);
					//System.out.println("Minutes : " +minutes1);
					
					int houradd=hours+hours1;
					int minuteadd=minutes+minutes1;
					if(minuteadd > 60 )
					{
						houradd = houradd+1;
						minuteadd=minuteadd-60;
					}

					SimpleDateFormat time_formatter1 = new SimpleDateFormat("dd/MM/yy");
					String currentdate = time_formatter1.format(System.currentTimeMillis());
					String date1=time_formatter1.format(dt);
					
					//System.out.println("date : "+currentdate);
					//System.out.println("DATAbase " + date1);
					//System.out.println("Hoursadd : " +houradd + " MinuteAdd : "+ minuteadd);
					
					SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm");
					String current_time_str = time_formatter.format(System.currentTimeMillis());
					String[] split5=current_time_str.split(":");
					int systemhour=	 Integer.valueOf(split5[0]);
					int systemmin=	 Integer.valueOf(split5[1]);

					//System.out.println("systemhour :" +systemhour);
					//System.out.println("systemmin :" +systemmin);
					
					String[] dsplit=currentdate.split("/");
					int ds=Integer.valueOf(dsplit[0]);
					int ms=Integer.valueOf(dsplit[1]);
					int ys=Integer.valueOf(dsplit[2]);
					
					String[] dsplit1=date1.split("/");
					int d=Integer.valueOf(dsplit1[0]);
					int m=Integer.valueOf(dsplit1[1]);
					int y=Integer.valueOf(dsplit1[2]);
					
					//System.out.println("Day :" +d);
					//System.out.println("DaySystem :" +ds);

					//System.out.println("Month :" +m);
					//System.out.println("MonthSystem :" +ms);
					

					//System.out.println("Year :" +y);
					//System.out.println("YearSystem :" +ys);
					
					int status;
					
					if(d<=ds && m<=ms && y<=ys)
					{
						if(d<ds && m==ms && y==ys)
						{
							status=1;
						}
						else
						{
							if(houradd == systemhour)
							{ 
								if(minuteadd>=systemmin)
								{
									status=0;
								}
								else
								{
									status=1;
								}
					
							}
							else if(houradd>systemhour)
							{
								status=0;
							}	
							else
							{
								status=1;
							}
						}
					}
					else
					{
						status=1;
					}

					//System.out.println("Status :" +status);
					
					//System.out.println("time :" +Time);
					//System.out.println("Time only : "+ timeOnly);
					
					entry.getValue().put("InTime", timeOnly);
					entry.getValue().put("Date", dateOnly);
					String sUnit_Id = (String)entry.getValue().get("unit_id").toString();
					System.out.println("Unit ids : "+sUnit_Id);
					List<String> Unit1 = Arrays.asList(sUnit_Id.split(","));
					System.out.println("Unit : "+Unit1);
					String unitNo[][]=new String[Unit1.size()][4];
					System.out.println("Unit Size : "+Unit1.size());
					int j=0,k=0;
					for (int i = 0; i < Unit1.size(); i++)
					{
						String flag="",login_id="",login_name="",approvewith="",approvemsg="";
						Integer iUnitID = Integer.parseInt(Unit1.get(i));
						dbop_sec = new DbOperations(DB_SECURITY);
						sqlflag="select Entry_flag,login_id,login_name,approvewith,approvemsg from visit_approval where unit_id='"+Unit1.get(i)+"' and v_id='"+entry.getValue().get("id")+"'";
						flagDetails=dbop_sec.Select(sqlflag);
						for(Entry<Integer, Map<String, Object>> entryflag : flagDetails.entrySet())
						{
							flag = entryflag.getValue().get("Entry_flag").toString();
						}
						
						//System.out.println("iUnitID :"+iUnitID);
						if(iUnitID > 0)	
						{
							
							HashMap<Integer, Map<String, Object>>  unitDetails = new HashMap<Integer, Map<String, Object>>();
							sql1="Select u.`unit_no`, mm.`primary_owner_name`,w.wing from unit u, member_main mm,wing w where u.`unit_id` ='"+iUnitID+"' and u.unit_id = mm.`unit` and w.wing_id  = u.wing_id  and mm.`status` = 'Y' and mm.`ownership_status` = '1'";
						   // System.out.println("Select Unit :" +sql1 );
						    dbop_soc = new DbOperations(DB_SOCIETY);
							unitDetails = dbop_soc.Select(sql1);
							System.out.println("Unit details : "+unitDetails);
							if(unitDetails.size() < 0)
							{
								entry.getValue().put("UnitNo","");
								entry.getValue().put("OwnerName","");
								entry.getValue().put("wing","");
								
							}
							else
							{
						
								for(Entry<Integer, Map<String, Object>> entry1 : unitDetails.entrySet())
								{
									unitNo[j][k] = entry1.getValue().get("unit_no").toString();
									//System.out.println("in for j :"+j+" k:"+k);
									k = k + 1;
									unitNo[j][k] = entry1.getValue().get("primary_owner_name").toString();
									//System.out.println("in for j :"+j+" k:"+k);
									k=k+1;
									unitNo[j][k] = flag;
									k=k+1;
									unitNo[j][k] = entry1.getValue().get("wing").toString(); 
									
									
									//System.out.println("in for j :"+j+" k:"+k);
								}
								j = j+1;
								k = 0;
							}
						}
					
					else
					{
						
						unitNo[j][k] = "S-Office";

						//System.out.println("Unit id if 0 :" + iUnitID);
						//System.out.println("in for j :"+j+" k:"+k);
						k = k + 1;
						unitNo[j][k] =  "Society Office";
						//System.out.println(unitNo[j][k]);
						//System.out.println("k: "+ k);
						//System.out.println("in for j :"+j+" k:"+k);
						k=k+1;
						unitNo[j][k] = flag;
						k=k+1;
						unitNo[j][k] = "S-Office"; 
						//System.out.println(unitNo[j][k]);
						//System.out.println("k: "+ k);
						j=j+1;
						k=0;
					}
						
					entry.getValue().put("UnitNo",unitNo);
					
				}
					entry.getValue().put("time_status", status);	
				//extra
			}
				
				int noOfVisitors = visitorEntryDetails.size();
				System.out.println("No of visitors : " + noOfVisitors);
				
				if(visitorEntryDetails.size() > 0)
				{
					//add document to map
						
					 rows2.put("visitors",MapUtility.HashMaptoList(visitorEntryDetails));
					 rows.put("success",1);
					 rows.put("response",rows2);			 
				}
				else
				{
					//documents not found
					 rows2.put("visitors","");
					 rows2.put("message ","No Visitor found" );
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
		public  HashMap<Integer, Map<String, Object>> mfetchAllVisitorsDetails(int VisitorID)
		{
			HashMap<Integer, Map<String, Object>>  visitorEntryDetails = new HashMap<Integer, Map<String, Object>>();
			HashMap<Integer, Map<String, Object>>  mpVisitors = new HashMap<Integer, Map<String, Object>>();
			String sSelectQuery = "",sql2="";
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String StatusBy="inside",sql1="",MonthDate="",OutMonthDate="";
			System.out.println("VisitorID:    " + VisitorID);

			String sqlflag="";
			String dateOnly="",timeOnly="";
			try
			{
		
				dbop_sec = new DbOperations(DB_SECURITY);
				
				sql1="Select v.id,v.visitor_ID,v.Entry_With,v.visitorMobile,v.unit_id,v.purpose_id,v.vehicle,v.otpGenerated,v.otpGtimestamp,v.otpStatus,v.status,v.outTimeStamp,v.Entry_Gate,v.Exit_Gate,p.purpose_name,v.company,v.visitor_note from visitorentry as v join `purpose` as p on p.purpose_id=v.purpose_id where v.id='"+VisitorID+"'";
				
				visitorEntryDetails = dbop_sec.Select(sql1);
				
				for(Entry<Integer, Map<String, Object>> entry : visitorEntryDetails .entrySet()) 
				{
					
					dbop_sec_root= new DbOperations(DB_SECURITY_ROOT);
					
					//sSelectQuery = "Select CONCAT(v.Fname,' ',v.Lname) as FullName,v.Mobile,v.Doc_img,v.Doc_No,v.Doc_Type_ID,d.document,v.img,v.Company from visitors as v, documents as d where v.Doc_Type_ID = d.id and v.visitor_id = '"+entry.getValue().get("visitor_ID")+"'";
					sSelectQuery = "Select CONCAT(v.Fname,' ',v.Lname) as FullName,v.Mobile, v.img,v.Doc_img from visitors as v where  v.visitor_id = '"+entry.getValue().get("visitor_ID")+"'";
				
					mpVisitors = dbop_sec_root.Select(sSelectQuery);
					for(Entry<Integer, Map<String, Object>> entry1 : mpVisitors .entrySet()) 
					{
						if(entry1.getValue().get("Doc_img") == null)
						{
							entry1.getValue().put("Doc_img", "");
						}
					}
					MonthDate =entry.getValue().get("otpGtimestamp").toString();
					String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
					String fmt = "yyyy-MM-dd HH:mm a";
					DateFormat df = new SimpleDateFormat(fmt);
					entry.getValue().put("VisitorDetails", mpVisitors);
					Date dt = df.parse(Time);

					DateFormat tdf = new SimpleDateFormat("hh:mm a");
					DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
					timeOnly = tdf.format(dt);
					dateOnly = dfmt.format(dt);
					
					entry.getValue().put("InTime", timeOnly);
					entry.getValue().put("Date", dateOnly);
					/*----- out Time   ---*/
					if(entry.getValue().get("outTimeStamp") != null)
					{
						OutMonthDate =entry.getValue().get("outTimeStamp").toString();
						String OutTime = m_Timezone.convertToCurrentTimeZone(OutMonthDate);
						
						
						String fmt1 = "yyyy-MM-dd HH:mm a";
						DateFormat df1 = new SimpleDateFormat(fmt);

						Date dt1 = df1.parse(OutTime);

						DateFormat tdf1 = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt1  = new SimpleDateFormat("dd-MMM-yy");


						String OuttimeOnly = tdf1.format(dt1);
						String OutdateOnly = dfmt1.format(dt1);
						
						entry.getValue().put("OutTime", OuttimeOnly);
						entry.getValue().put("OutDate", OutdateOnly);
					} 
					else
					{
						entry.getValue().put("OutTime", "0");
						entry.getValue().put("OutDate", "00");
					}
					String sUnit_Id = (String)entry.getValue().get("unit_id").toString();
					List<String> Unit1 = Arrays.asList(sUnit_Id.split(","));				
					String unitNo[][]=new String[Unit1.size()][8];
					//System.out.println("Unit Size : "+Unit1.size());
					int j=0,k=0;
					
					HashMap<Integer, Map<String, Object>>  unitDetails = new HashMap<Integer, Map<String, Object>>();
					HashMap<Integer, Map<String, Object>>  flagDetails = new HashMap<Integer, Map<String, Object>>();
					for (int i = 0; i < Unit1.size(); i++)
					{
						String flag="",login_id="",login_name="",approvewith="",approvemsg="";
						Integer iUnitID = Integer.parseInt(Unit1.get(i));
						System.out.println("umfetchAllVisitorsDetailsnitid:    " + iUnitID);
						dbop_sec = new DbOperations(DB_SECURITY);
						sqlflag="select Entry_flag,login_id,login_name,approvewith,approvemsg from visit_approval where unit_id='"+Unit1.get(i)+"' and v_id='"+VisitorID+"'";
						//System.out.println("SQL " + sqlflag);
						flagDetails=dbop_sec.Select(sqlflag);
						for(Entry<Integer, Map<String, Object>> entryflag : flagDetails.entrySet())
						{
							flag = entryflag.getValue().get("Entry_flag").toString();
							login_id=entryflag.getValue().get("login_id").toString();
							login_name=entryflag.getValue().get("login_name").toString();
							approvewith=entryflag.getValue().get("approvewith").toString();
							if(entryflag.getValue().get("approvemsg") != null)
							{
								approvemsg=entryflag.getValue().get("approvemsg").toString();
							}
							
						}
						if(iUnitID > 0)	
						{
						
					
						//sql1="Select u.`unit_no`, mm.`primary_owner_name` from unit u, member_main mm where u.`unit_id` = '"+entry.getValue().get("unit_id")+"' and u.unit_id = mm.`unit` and mm.`status` = 'Y'";
						sql1="Select u.`unit_no`, mm.`primary_owner_name`,w.wing from unit u, member_main mm,wing w where u.`unit_id` ='"+iUnitID+"' and u.unit_id = mm.`unit` and w.wing_id  = u.wing_id  and mm.`status` = 'Y' and mm.`ownership_status` = '1'";//System.out.print("SqlQUery : "+sql1 );
						dbop_soc = new DbOperations(DB_SOCIETY);
						unitDetails = dbop_soc.Select(sql1);
						System.out.print("Result  : "+unitDetails );
						if(unitDetails.size() < 0)
						{
							entry.getValue().put("UnitNo","");
							entry.getValue().put("OwnerName","");
							entry.getValue().put("wing","");
						}
						else
						{
						
							for(Entry<Integer, Map<String, Object>> entry1 : unitDetails.entrySet())
							{
								unitNo[j][k] = entry1.getValue().get("unit_no").toString();
								//System.out.println("in for j :"+j+" k:"+k);
								k = k + 1;
								unitNo[j][k] = entry1.getValue().get("primary_owner_name").toString();
								//System.out.println("in for j :"+j+" k:"+k);
								k = k + 1;
								unitNo[j][k] = flag;
								k=k+1;
								unitNo[j][k] = login_id;
								k=k+1;
								unitNo[j][k] = login_name;
								k=k+1;
								unitNo[j][k] = approvewith;
								k=k+1;
								unitNo[j][k] = approvemsg;
								k=k+1;
								unitNo[j][k] = entry1.getValue().get("wing").toString(); 
								
							}
							j = j+1;
							k = 0;
						}
						
					}
					else
					{
						unitNo[j][k] = "S-Office";
						//System.out.println("in for j :"+j+" k:"+k);
						k = k + 1;
						unitNo[j][k] =  "Society Office";
						k = k + 1;
						unitNo[j][k] = flag;
						k=k+1;
						unitNo[j][k] = login_id;
						k=k+1;
						unitNo[j][k] = login_name;
						k=k+1;
						unitNo[j][k] = approvewith;
						k=k+1;
						unitNo[j][k] = approvemsg;
						k=k+1;
						unitNo[j][k] = "S-Office";
						
						//System.out.println("in for j :"+j+" k:"+k);
						j=j+1;
						k=0;
					}
					entry.getValue().put("UnitNo",unitNo);
					
				}
			}
			HashMap<Integer, Map<String, Object>>  fetchvisitor = new HashMap<Integer, Map<String, Object>>();
			dbop_sec = new DbOperations(DB_SECURITY);
			String sqlfetch  = "select * from item_lended where entry_id = '"+VisitorID+"' and entry_for = '0'";
			fetchvisitor = dbop_sec.Select(sqlfetch);
				if(fetchvisitor.size() > 0)
				{
					 rows2.put("visitoritems",MapUtility.HashMaptoList(fetchvisitor));
				}
				else
				{
					 rows2.put("visitoritems","");	
				}
				if(visitorEntryDetails.size() > 0)
				{
					//add document to map
						
					 rows2.put("visitors",MapUtility.HashMaptoList(visitorEntryDetails));
					 rows.put("success",1);
					 rows.put("response",rows2);			 
				}
				else
				{
					//documents not found
					 rows2.put("visitors","");
					 rows2.put("message ","No Visitor found" );
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
		/*public static HashMap<Integer, Map<String, Object>> mfetchAllVisitorsDetails(int VisitorID)
		{
		
			HashMap<Integer, Map<String, Object>>  visitorEntryDetails = new HashMap<Integer, Map<String, Object>>();
			HashMap<Integer, Map<String, Object>>  mpVisitors = new HashMap<Integer, Map<String, Object>>();
			String sSelectQuery = "",sql1="",MonthDate="",timeOnly="",dateOnly ="",OutMonthDate;
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			//String StatusBy="inside";
		
			try
			{
				dbop = new DbOperations(DB_SECURITY);
				sql1="Select * from visitorentry where id= '"+VisitorID+"'";
				visitorEntryDetails = dbop.Select(sql1);
				for(Entry<Integer, Map<String, Object>> entry : visitorEntryDetails .entrySet()) 
				{
					dbop = new DbOperations(DB_SECURITY_ROOT);
				}
				//System.out.println(sSelectQuery);
				for(Entry<Integer, Map<String, Object>> entry : mpVisitors.entrySet())
				{
					if(entry.getValue().get("otpGtimestamp") != null)
					{
						MonthDate =entry.getValue().get("otpGtimestamp").toString();
						
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
						
						entry.getValue().put("InTime", timeOnly);
				 		entry.getValue().put("Date", dateOnly);
				 		//rows1.put("ID", entry.getValue().get("sr_no"));
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
						
						entry.getValue().put("OutTime", OuttimeOnly);
				 		entry.getValue().put("OutDate", OutdateOnly);
					}
					dbop = new DbOperations(DB_SOCIETY);
					HashMap<Integer, Map<String, Object>>  unitDetails = new HashMap<Integer, Map<String, Object>>();
					sql1="Select u.`unit_no`, mm.`primary_owner_name` from unit u, member_main mm where u.`unit_id` = '"+entry.getValue().get("unit_id")+"' and u.unit_id = mm.`unit` and mm.`status` = 'Y'";
					unitDetails = dbop.Select(sql1);
					System.out.println(unitDetails);
					if(unitDetails.size() < 0)
					{
						entry.getValue().put("UnitNo","");
						entry.getValue().put("OwnerName","");
					}
					else
					{
						for(Entry<Integer, Map<String, Object>> entry1 : unitDetails.entrySet())
						{
							entry.getValue().put("UnitNo",entry1.getValue().get("unit_no"));
							entry.getValue().put("OwnerName",entry1.getValue().get("primary_owner_name"));
						}
					}
					
				}
				if(mpVisitors.size() > 0)
				{
					//add document to map
						
					 rows2.put("visitors",MapUtility.HashMaptoList(mpVisitors));
					// rows2.put("message ","No Visitor found" );
					 rows.put("success",1);
					 rows.put("response",rows2);			 
				}
				else
				{
					//documents not found
					 rows2.put("visitors","");
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
			System.out.println(rows);
			 return  rows;
			
		}*/
		
		public  HashMap<Integer, Map<String, Object>> mfetchExit(int VisitorEntryID,int ExitGateNo,int CheckOut)
		{
		
			HashMap<Integer, Map<String, Object>>  mpVisitors = new HashMap<Integer, Map<String, Object>>();
			String sUpdateQuery = "";
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			
		
			try
			{
				dbop_sec = new DbOperations(DB_SECURITY);
				sUpdateQuery ="UPDATE `visitorentry` SET outTimeStamp=now(),status='outside',Exit_Gate='"+ExitGateNo+"',checkOut = '"+CheckOut+"' WHERE `id` = '"+VisitorEntryID+"'  AND status='inside'";
				long ExitID = dbop_sec.Update(sUpdateQuery);

				
				if(ExitID  > 0)
				{
					//add member to map
					 rows2.put("VisitorEntryID",VisitorEntryID );
					 rows2.put("message ","Visitor exit marked" );
					 rows.put("success",1);
					 rows.put("response",rows2);			 
				}
				else
				{
					//member not found
					 rows2.put("VisitorEntryID",VisitorEntryID );
					 rows2.put("message ","No Visitor Inside Entry found" );
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
		
			 return  rows;
			
		}
		
		public  HashMap<Integer, Map<String, Object>> fetchVisitorsReports(String StartDate,String EndDate,int iVisitorId)
		{
			
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String sqlflag="";
			String sSelectQuery = "",sql1="";
			JsonObject jsonOption = new JsonObject();
			List<Map<String, Object>> unitDetailsList = new ArrayList<>();
			HashMap<Integer, Map<String, Object>> visitorReportDetails = new HashMap<Integer, Map<String, Object>>();
			HashMap<Integer, Map<String, Object>> visitorDetails = new HashMap<Integer, Map<String, Object>>();
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
				
				dbop_sec = new DbOperations(DB_SECURITY);
				if(iVisitorId == 0)
				{
					sSelectQuery = "SELECT v.`id`, v.unit_id,v.`company`,v.Entry_With,v.`vehicle`, v.`otpGtimestamp`,v.`outTimeStamp`,v.`visitor_ID`,v.checkOut,v.`Entry_Gate`,v.`Exit_Gate`,p.`purpose_name` FROM `visitorentry` as v,purpose as p where v.`purpose_id` = p.`purpose_id` and v.`otpGtimestamp` between '"+StartDate+"' AND '"+EndDate+"' ORDER BY `v`.`id` desc";
					//sSelectQuery = "SELECT v.`id`, v.unit_id,v.`company`,v.`vehicle`, v.`otpGtimestamp`,v.`outTimeStamp`,v.`visitor_ID`,v.`Entry_Gate`,v.`Exit_Gate`,p.`purpose_name` FROM `visitorentry` as v, purpose as p where v.`purpose_id` = p.`purpose_id` and v.`otpGtimestamp` between '"+StartDate+"' AND '"+EndDate+"' ORDER BY `v`.`id` ASC";
				}
				else
				{
					sSelectQuery = "SELECT v.`id`, v.unit_id,v.`company`,v.`vehicle`,v.Entry_With, v.`otpGtimestamp`,v.`outTimeStamp`,v.`visitor_ID`,v.checkOut,v.`Entry_Gate`,v.`Exit_Gate` FROM `visitorentry` as v where v.`visitor_ID` = '"+iVisitorId+"' AND v.`otpGtimestamp` between '"+StartDate+"' AND '"+EndDate+"' ORDER BY `v`.`id` desc";
				}
				//System.out.println("monthly attendance "+sSelectQuery);
				visitorReportDetails = dbop_sec.Select(sSelectQuery);
				String MonthDate ="";
				String OutMonthDate ="";
				
				String dateOnly="",timeOnly="";
				
				for(Entry<Integer, Map<String, Object>> entry : visitorReportDetails.entrySet()) 
				{
					dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
					sSelectQuery = "Select CONCAT(v.Fname,' ',v.Lname) as FullName,v.Mobile,v.Doc_img,v.Doc_No,v.Doc_Type_ID,v.img from visitors as v where  v.visitor_id = '"+entry.getValue().get("visitor_ID")+"'";
					visitorDetails = dbop_sec_root.Select(sSelectQuery);
					
					for(Entry<Integer, Map<String, Object>> entry1 : visitorDetails.entrySet()) 
					{
						entry.getValue().put("FullName", entry1.getValue().get("FullName"));
						entry.getValue().put("Mobile", entry1.getValue().get("Mobile"));
						entry.getValue().put("Doc_No", entry1.getValue().get("Doc_No"));
						entry.getValue().put("Doc_Type_ID", entry1.getValue().get("Doc_Type_ID"));
						entry.getValue().put("Doc_img", entry1.getValue().get("Doc_img"));
						//entry.getValue().put("document", entry1.getValue().get("document"));
						entry.getValue().put("img", entry1.getValue().get("img"));
					
					}
					//System.out.println("entry  :"+entry);
					String sUnit_Id = (String)entry.getValue().get("unit_id").toString();

					HashMap<Integer, Map<String, Object>>  flagDetails = new HashMap<Integer, Map<String, Object>>();
					List<String> Unit1 = Arrays.asList(sUnit_Id.split(","));
					String unitNo[][]=new String[Unit1.size()][4];
					//System.out.println("Unit Size : "+Unit1.size());
					int j=0,k=0;
					
					//Integer iUnitID = Integer.parseInt(sUnit_Id);
					for (int i = 0; i < Unit1.size(); i++)
					{
						String flag="";
						Integer iUnitID = Integer.parseInt(Unit1.get(i));
						dbop_sec = new DbOperations(DB_SECURITY);
						sqlflag="select Entry_flag from visit_approval where unit_id='"+Unit1.get(i)+"' and v_id='"+entry.getValue().get("id")+"'";
						flagDetails=dbop_sec.Select(sqlflag);
						for(Entry<Integer, Map<String, Object>> entryflag : flagDetails.entrySet())
						{
							flag = entryflag.getValue().get("Entry_flag").toString();
							
						}
						//System.out.println("iUnitID :"+iUnitID);
						if(iUnitID > 0)	
						{
						dbop_soc = new DbOperations(DB_SOCIETY);
						HashMap<Integer, Map<String, Object>>  unitDetails = new HashMap<Integer, Map<String, Object>>();
						sql1="Select u.`unit_no`, mm.`primary_owner_name`,w.wing from unit u, member_main mm,wing w where u.`unit_id` ='"+iUnitID+"' and u.unit_id = mm.`unit` and w.wing_id  = u.wing_id  and mm.`status` = 'Y' and mm.`ownership_status` = '1'";
						unitDetails = dbop_soc.Select(sql1);
						
						if(unitDetails.size() < 0)
						{
							entry.getValue().put("UnitNo","");
							entry.getValue().put("OwnerName","");
							entry.getValue().put("wing","");
						}
						else
						{
							

							for (Map.Entry<Integer, Map<String, Object>> entry1 : unitDetails.entrySet()) {
							    Map<String, Object> unitInfo = new LinkedHashMap<>();
							    
							    unitInfo.put("wing", entry1.getValue().get("wing"));
							    unitInfo.put("unit_no", entry1.getValue().get("unit_no"));
							    unitInfo.put("resident_name", entry1.getValue().get("primary_owner_name"));
							    unitInfo.put("entry_flag", flag);
							    
							   

							    unitDetailsList.add(unitInfo);
							    // Add each row as a map
							}
							
						}
						entry.getValue().put("UnitNo",unitDetailsList);
					}
				else
				{
					

					Map<String, Object> unitInfo = new LinkedHashMap<>();
					unitInfo.put("wing_name", "S-Office");
					unitInfo.put("unit_no", "S-Office");
					unitInfo.put("resident_name", "Society Office");
					unitInfo.put("entry_flag", flag); // Use camelCase for keys if you're consistent in JSON, otherwise keep as-is

					entry.getValue().put("unitDetails", unitInfo); // Changed "UnitNo" to "unitDetails" for clarity

					//System.out.println("in for j :"+j+" k:"+k);
				}
				//entry.getValue().put("UnitNo",unitNo);
				}
					HashMap rows1 = new HashMap<>();
			 		
					if(entry.getValue().get("otpGtimestamp") != null)
					{
						MonthDate =entry.getValue().get("otpGtimestamp").toString();
						
						String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);

						Date dt = df.parse(Time);

						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
						timeOnly = tdf.format(dt);
						dateOnly = dfmt.format(dt);
					
						
						rows1.put("InTime", timeOnly);
				 		rows1.put("Date", dateOnly);
				 	
						
					}
					if(entry.getValue().get("outTimeStamp") != null)
					{
						OutMonthDate =entry.getValue().get("outTimeStamp").toString();
						String OutTime = m_Timezone.convertToCurrentTimeZone(OutMonthDate);
						
						
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
					        d1 = format.parse(entry.getValue().get("otpGtimestamp").toString());
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
				
				if(visitorReportDetails.size() > 0)
				{
					rows2.put("VisitorReport",MapUtility.HashMaptoList(visitorReportDetails) );
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
			
			return rows;
		}
		//   ----------------------------   Fetch letest visite ------------------------------------- //
		
		public  HashMap<Integer, Map<String, Object>> fetchLatestVisitorsReports(int iVisitorId)
		{
			
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String sqlflag="";
			String sSelectQuery = "",sql1="";
			JsonObject jsonOption = new JsonObject();
			List<Map<String, Object>> unitList = new ArrayList<>();
			HashMap<Integer, Map<String, Object>> visitorReportDetails = new HashMap<Integer, Map<String, Object>>();
			HashMap<Integer, Map<String, Object>> visitorDetails = new HashMap<Integer, Map<String, Object>>();
			try
			{

				dbop_sec = new DbOperations(DB_SECURITY);
				
					sSelectQuery = "SELECT v.`id`,v.Entry_With, v.unit_id,v.`company`,v.`vehicle`, v.`otpGtimestamp`,v.`outTimeStamp`,v.`visitor_ID`,v.checkOut,v.`Entry_Gate`,v.`Exit_Gate`,p.`purpose_name` FROM `visitorentry` as v,purpose as p where v.`purpose_id` = p.`purpose_id`  and v.`visitor_ID`='"+iVisitorId+"' ORDER BY `v`.`id` DESC LIMIT 5";
					//sSelectQuery = "SELECT v.`id`, v.unit_id,v.`company`,v.`vehicle`, v.`otpGtimestamp`,v.`outTimeStamp`,v.`visitor_ID`,v.`Entry_Gate`,v.`Exit_Gate`,p.`purpose_name` FROM `visitorentry` as v, purpose as p where v.`purpose_id` = p.`purpose_id` and v.`otpGtimestamp` between '"+StartDate+"' AND '"+EndDate+"' ORDER BY `v`.`id` ASC";
				
				//System.out.println("monthly attendance "+sSelectQuery);
				visitorReportDetails = dbop_sec.Select(sSelectQuery);
				String MonthDate ="";
				String OutMonthDate ="";
				
				String dateOnly="",timeOnly="";
				
				for(Entry<Integer, Map<String, Object>> entry : visitorReportDetails.entrySet()) 
				{
					dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
					sSelectQuery = "Select CONCAT(v.Fname,' ',v.Lname) as FullName,v.Mobile,v.Doc_img,v.Doc_No,v.Doc_Type_ID,v.img from visitors as v where v.visitor_id = '"+entry.getValue().get("visitor_ID")+"'";
					visitorDetails = dbop_sec_root.Select(sSelectQuery);
					
					for(Entry<Integer, Map<String, Object>> entry1 : visitorDetails.entrySet()) 
					{
						entry.getValue().put("FullName", entry1.getValue().get("FullName"));
						entry.getValue().put("Mobile", entry1.getValue().get("Mobile"));
						//entry.getValue().put("Doc_No", entry1.getValue().get("Doc_No"));
						//entry.getValue().put("Doc_Type_ID", entry1.getValue().get("Doc_Type_ID"));
						//entry.getValue().put("Doc_img", entry1.getValue().get("Doc_img"));
						//entry.getValue().put("document", entry1.getValue().get("document"));
						//entry.getValue().put("img", entry1.getValue().get("img"));
					
					}
					//System.out.println("entry  :"+entry);
					String sUnit_Id = (String)entry.getValue().get("unit_id").toString();

					HashMap<Integer, Map<String, Object>>  flagDetails = new HashMap<Integer, Map<String, Object>>();
					List<String> Unit1 = Arrays.asList(sUnit_Id.split(","));
					String unitNo[][]=new String[Unit1.size()][4];
					//System.out.println("Unit Size : "+Unit1.size());
					int j=0,k=0;
					
					//Integer iUnitID = Integer.parseInt(sUnit_Id);
					for (int i = 0; i < Unit1.size(); i++)
					{
						String flag="";
						Integer iUnitID = Integer.parseInt(Unit1.get(i));
						dbop_sec = new DbOperations(DB_SECURITY);
						sqlflag="select Entry_flag from visit_approval where unit_id='"+Unit1.get(i)+"' and v_id='"+entry.getValue().get("id")+"'";
						flagDetails=dbop_sec.Select(sqlflag);
						for(Entry<Integer, Map<String, Object>> entryflag : flagDetails.entrySet())
						{
							flag = entryflag.getValue().get("Entry_flag").toString();
							
						}
						//System.out.println("iUnitID :"+iUnitID);
						if(iUnitID > 0)	
						{
						dbop_soc = new DbOperations(DB_SOCIETY);
						HashMap<Integer, Map<String, Object>>  unitDetails = new HashMap<Integer, Map<String, Object>>();
						sql1="Select u.`unit_no`, mm.`primary_owner_name`,w.wing from unit u, member_main mm,wing w where u.`unit_id` ='"+iUnitID+"' and u.unit_id = mm.`unit` and w.wing_id  = u.wing_id  and mm.`status` = 'Y' and mm.`ownership_status` = '1'";
						unitDetails = dbop_soc.Select(sql1);
						
						if(unitDetails.size() < 0)
						{
							entry.getValue().put("UnitNo","");
							entry.getValue().put("OwnerName","");
							entry.getValue().put("wing","");
						}
						else
						{
							
							
							for (Entry<Integer, Map<String, Object>> entry1 : unitDetails.entrySet()) {
							{
								 Map<String, Object> unitMap = new LinkedHashMap<>();

								    unitMap.put("wing_name", entry1.getValue().get("wing"));
								    unitMap.put("unit_no", entry1.getValue().get("unit_no"));
								    unitMap.put("resident_name", entry1.getValue().get("primary_owner_name"));
								    unitMap.put("entry_flag", flag);

								    unitList.add(unitMap);
						}
					
							
						}
							entry.getValue().put("UnitNo", unitList);
						
					}
						}
				else
				{

					Map<String, Object> unitDetails = new LinkedHashMap<>();
					
					unitDetails.put("wing_name", "S-Office");
					unitDetails.put("unit_no", "S-Office");
					unitDetails.put("resident_name", "Society Office");
					unitDetails.put("entry_flag", flag);
					
					
					
					entry.getValue().put("UnitNo", unitDetails);
					//System.out.println("in for j :"+j+" k:"+k);
				}
						
				
				}
					HashMap rows1 = new HashMap<>();
			 		
					if(entry.getValue().get("otpGtimestamp") != null)
					{
						MonthDate =entry.getValue().get("otpGtimestamp").toString();
						
						String Time = m_Timezone.convertToCurrentTimeZone(MonthDate);
						String fmt = "yyyy-MM-dd HH:mm a";
						DateFormat df = new SimpleDateFormat(fmt);

						Date dt = df.parse(Time);

						DateFormat tdf = new SimpleDateFormat("hh:mm a");
						DateFormat dfmt  = new SimpleDateFormat("dd-MMM-yy");
						timeOnly = tdf.format(dt);
						dateOnly = dfmt.format(dt);
					
						
						rows1.put("InTime", timeOnly);
				 		rows1.put("Date", dateOnly);
				 	
						
					}
					if(entry.getValue().get("outTimeStamp") != null)
					{
						OutMonthDate =entry.getValue().get("outTimeStamp").toString();
						String OutTime = m_Timezone.convertToCurrentTimeZone(OutMonthDate);
						
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
					        d1 = format.parse(entry.getValue().get("otpGtimestamp").toString());
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
				
				if(visitorReportDetails.size() > 0)
				{
					rows2.put("VisitorReport",MapUtility.HashMaptoList(visitorReportDetails) );
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
			
			return rows;
		}
		
		
		public  HashMap<Integer, Map<String, Object>> fetchVisitor(String mobileNo,String EntryDoc_id) throws ClassNotFoundException, JSONException
		{
			Timestamp updateTimeStamp = new Timestamp(System.currentTimeMillis());
			String d=new SimpleDateFormat("yyyy-MM-dd").format(updateTimeStamp);
			//System.out.println(d);
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String sSelectQuery = "";
			int ex_flag=0,v_id=0;
			
			try
			{			
				
			/*
			dbop_sec = new DbOperations(DB_SECURITY);
			
			System.out.println("SECURITY ROOT DB:"+DB_SECURITY_ROOT);
			dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
			
			System.out.println("DB_SOCIETY:" + DB_SOCIETY);
			dbop_soc = new DbOperations(DB_SOCIETY);
			*/
			dbop_sec=new DbOperations(DB_SECURITY);
			//sInsertQuery = "Select `fname`,`lname`,`img_src`,`unit`,mobile FROM `expected_visitor` where mobile='"+mobileNo+"' and expected_date='"+d+"'";
			sSelectQuery="Select ev.`fname`,ev.`lname`,ev.`img_src`,ev.`unit`,ev.`mobile`,ev.`purpose_id`,p.purpose_name,ev.note FROM `expected_visitor` as ev join `purpose` as p on p.purpose_id=ev.purpose_id  where mobile='"+mobileNo+"' and expected_date='"+d+"'";
			HashMap<Integer, Map<String, Object>>  visitorDetails1 = new HashMap<Integer, Map<String, Object>>();
			System.out.println(sSelectQuery);
			visitorDetails1 = dbop_sec.Select(sSelectQuery);
			System.out.println(visitorDetails1);
				if(visitorDetails1.size() > 0)
				{
					ex_flag=1;
					for(Entry<Integer, Map<String, Object>> entry : visitorDetails1.entrySet())
					{
						//System.out.println(visitorDetails1);
						dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
						sSelectQuery = "Select v.`visitor_id`,v.`Fname`,v.Lname,v.Mobile,v.img from `visitors` as v where v.Mobile='"+entry.getValue().get("mobile")+"'";
						HashMap<Integer, Map<String, Object>>  visitorDetails = new HashMap<Integer, Map<String, Object>>();
						visitorDetails = dbop_sec_root.Select(sSelectQuery);
						//System.out.println(visitorDetails);
					
						int unit_id=Integer.parseInt(entry.getValue().get("unit").toString());
						//System.out.println("Unit : " + unit_id);
						dbop_soc=new DbOperations(DB_SOCIETY);
						//System.out.println(dbop_soc);
						//String sql="SELECT u.unit_no,w.wing,w.wing_id from unit as u Inner join wing as w on u.wing_id=w.wing_id and unit_id='"+unit_id+"'";
						String sql="SELECT u.unit_no,w.wing,w.wing_id,mm.owner_name from unit as u join wing as w on u.wing_id=w.wing_id join `member_main` as mm on mm.unit=u.unit_id where unit_id='"+unit_id+"' and mm.ownership_status='1'";

						HashMap<Integer, Map<String, Object>>  wingdetails = new HashMap<Integer, Map<String, Object>>();
						//System.out.println("sql : " + sql);
						wingdetails = dbop_soc.Select(sql);
						//System.out.println("sql : " + sql);
						String wing="",unit_no="",wing_id="";
						if(wingdetails.size()>0)
						{
							for(Entry<Integer, Map<String, Object>> entry1 : wingdetails.entrySet())
							{
								entry.getValue().put("wing", entry1.getValue().get("wing").toString());
								entry.getValue().put("wing_id", entry1.getValue().get("wing_id").toString());
								entry.getValue().put("unit_no", entry1.getValue().get("unit_no").toString());
								entry.getValue().put("OwnerName", entry1.getValue().get("owner_name").toString());
							}
						}
						else
						{
							entry.getValue().put("wing", "Wing not present in society");
							entry.getValue().put("wing_id", "Wing not present in society");
							entry.getValue().put("unit_no", "UnitNo not present in society");
							entry.getValue().put("OwnerName", "OwnerName not present in society");
						}
						if(visitorDetails.size() > 0)
						{
							for(Entry<Integer, Map<String, Object>> entry1 : visitorDetails.entrySet())
							{
								v_id=Integer.parseInt(entry1.getValue().get("visitor_id").toString());
								//System.out.println(v_id);
								entry.getValue().put("visitor_id", v_id);
								entry.getValue().put("ex_flag", ex_flag);
								entry.getValue().put("img", entry1.getValue().get("img"));
							}
						}
					
						else
						{
							//Visitor v=new Visitor();
							String FName=entry.getValue().get("fname").toString();
							String LName=entry.getValue().get("lname").toString();
							String Mobile=entry.getValue().get("mobile").toString();
							String img="";
							String doc_img="";
							String docNo="";
							String company="";
							int Doc_id=1;
							int PurposeId=0;
							int Entry_WithDoc = 1;
							HashMap<Integer, Map<String, Object>> visit = createNewVisitor(FName, LName, Mobile, doc_img, docNo, company, img, PurposeId, Doc_id,Entry_WithDoc);
							//System.out.println(visit);
							Gson objGson1 = new Gson();
							//JSONObject objStr = new JSONObject();
							String objStr1=objGson1.toJson(visit);
							//System.out.println(objStr1);
							JSONObject jsonco = null;
							jsonco=new JSONObject(objStr1);
							String jsonco1=jsonco.get("response").toString();
							JSONObject jsonco2=new JSONObject(jsonco1);
							v_id=Integer.parseInt(jsonco2.get("VisitorId").toString());
							entry.getValue().put("visitor_id",v_id);
							entry.getValue().put("ex_flag", ex_flag);
						}
				
						//if(visitorDetails1.size() > 0)
						//{
							rows2.put("visitorDetails",MapUtility.HashMaptoList(visitorDetails1));
							//rows2.put("ex_flag", ex_flag);
							rows.put("success",1);
							rows.put("response",rows2);	
						
						/*}
						else
						{
							rows2.put("message ","Visitor Not Found");
							rows.put("success",0);
							rows.put("response",rows2);
						
						}*/
					}
				}
				else
				{
					dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
					sSelectQuery = "SELECT v.`visitor_id`, v.`Fname`, v.`Lname`, v.`Mobile`, v.`Doc_img`, v.`Doc_No`, v.`Company`, v.`img`, v.`Entry_With` FROM `visitors` AS v " +
				               "WHERE v.Mobile = '" + mobileNo + "' AND v.Entry_With = '" + EntryDoc_id + "'";

				visitorDetails1 = new HashMap<Integer, Map<String, Object>>();
				visitorDetails1 = dbop_sec_root.Select(sSelectQuery);

				for (Map.Entry<Integer, Map<String, Object>> entry : visitorDetails1.entrySet()) {
				    int index = entry.getKey();
				    Map<String, Object> currentData = entry.getValue();

				    // Get visitor_id
				     v_id = Integer.parseInt(currentData.get("visitor_id").toString());

				    // Prepare SQL Query to fetch additional data
				     sSelectQuery = "SELECT v.*, p.* FROM `visitorentry` AS v " +
				               "JOIN `visit_approval` AS va ON va.v_id = v.id " +
				               "JOIN `purpose` AS p ON p.purpose_id = v.purpose_id " +
				               "WHERE v.visitor_ID = '" + v_id + "'";


				    // Run the query
				    Map<Integer, Map<String, Object>> result = dbop_sec.Select(sSelectQuery);

				    if (result != null && !result.isEmpty()) {
				        // Get the new data
				        Map<String, Object> fetchedData = result.entrySet().iterator().next().getValue();

				        // Merge into current data
				        currentData.putAll(fetchedData);

				        // Re-insert ex_flag if needed
				        currentData.put("ex_flag", entry.getValue().get("ex_flag"));
				        
				        if (fetchedData.containsKey("unit_id")) {
				            String unit_id = fetchedData.get("unit_id").toString();
				            dbop_soc = new DbOperations(DB_SOCIETY);
				            String ownerQuery = "SELECT owner_name FROM member_main WHERE unit = '" + unit_id + "'";
				            Map<Integer, Map<String, Object>> ownerResult = dbop_soc.Select(ownerQuery);

				            if (ownerResult != null && !ownerResult.isEmpty()) {
				                Map<String, Object> ownerData = ownerResult.entrySet().iterator().next().getValue();
				                if (ownerData.containsKey("owner_name")) {
				                    String ownerName = ownerData.get("owner_name").toString();
				                    currentData.put("owner_name", ownerName);  // ✅ Append owner_name
				                }
				            }
				        }
				        // Save back the merged map into the same index
				        visitorDetails1.put(index, currentData);
				    }
				}


					
					if(visitorDetails1.size() > 0)
					{
						rows2.put("visitorDetails",MapUtility.HashMaptoList(visitorDetails1));
						rows2.put("ex_flag", ex_flag);
						rows.put("success",1);
						rows.put("response",rows2);	
						
					}
					else
					{
						rows2.put("message ","Visitor Not Found");
						rows.put("success",0);
						rows.put("response",rows2);
						
					}
				}
				dbop_sec = new DbOperations(DB_SECURITY);
				
				//String sSelectVisitorAtend ="SELECT * FROM `visitorentry` where visitor_ID='"+entry.getValue().get("visitor_id")+"' and status='inside' and `outTimeStamp`='0000-00-00 00:00:00'";
				String sSelectVisitorAtend ="SELECT v.* FROM `visitorentry` as v JOIN `visit_approval` as va on va.v_id= v.id where v.visitor_ID='"+v_id+"' and v.status='inside' and v.`outTimeStamp`='0000-00-00 00:00:00' and va.Entry_flag NOT IN('2')";
				//System.out.println("sSelectStaffAtend :" +sSelectVisitorAtend);
				HashMap<Integer, Map<String, Object>>  resultFound = new HashMap<Integer, Map<String, Object>>();
				resultFound = dbop_sec.Select(sSelectVisitorAtend);
				if(resultFound.size() > 0 )
				{	
					rows2.put("ChekInExist", "Inside");
					
				}
				else
				{
					rows2.put("ChekInExist", "Outside");
					
				}

			}
			catch (Exception e)
			{
				rows2.put("message ","Exception " + e.getMessage());
				rows.put("success",0);
				rows.put("response",rows2);

			}
			return rows;
		
		}
		public  HashMap<Integer, Map<String, Object>> createNewVisitor(String FName, String LName, String Mobile, String doc_img, String docNo, String company, String img, int PurposeId,int Doc_id,int Entry_WithDoc)
		{
		
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String sInsertQuery = "";
			
			try
			{		
				docNo = docNo.toUpperCase();
				
					sInsertQuery = "INSERT INTO `visitors` (`Fname`, `Lname`,`Mobile`,`Doc_img`,`Doc_No`,`Doc_Type_ID`,`Company`,`img`,Entry_With) VALUES ('"+FName+"','"+LName+"','"+Mobile+"' ,'"+doc_img+"', '"+docNo+"','"+Doc_id+"','"+company+"','"+img+"','"+Entry_WithDoc+"')";
				
				dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
				long attend = dbop_sec_root.Insert(sInsertQuery);
				
				if(attend  > 0)
				{
					//add member to map
					 rows2.put("VisitorId",attend );
					 rows2.put("message ","Visitor added" );
					 rows.put("success",1);
					 rows.put("response",rows2);			 
				}
				else
				{
					//member not found
					 rows2.put("VisitorId",attend );
					 rows2.put("message ","Unable to add visitor." );
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
			
			return rows;
		}
		public  HashMap<Integer, Map<String, Object>> UpdateCompany(String company,int purpose)
		{
		
			HashMap rows = new HashMap<>();
			HashMap rows2 = new HashMap<>();
			String sExistingQuery = "";
			String sInsertQuery = "";
			String sLogo = "";
			long mpCompany = 0;
			
		
			try
			{
				dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
				sExistingQuery = "SELECT `cat_id` from `visitor_company` WHERE `c_name` = '"+company+"'";
				HashMap<Integer, Map<String, Object>>  cat_id = dbop_sec_root.Select(sExistingQuery);
				if(cat_id == null || cat_id.isEmpty()){
					sInsertQuery = "INSERT INTO `visitor_company` (`cat_id`, `c_name`,`logo`) VALUES ('"+purpose+"','"+company+"','"+sLogo+"')";
					 mpCompany = dbop_sec_root.Insert(sInsertQuery);
				}
				

				
				if(mpCompany  > 0)
				{
					//add member to map
					 rows2.put("Company ",mpCompany );
					 rows2.put("message ","Added successfully" );
					 rows.put("success",1);
					 rows.put("response",rows2);			 
				}
				else
				{
					//member not found
					 rows2.put("Company ",mpCompany );
					 rows2.put("message ","Unable to add company" );
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
		
			 return  rows;
			
		}
		
		
		
	public static void main(String[] args)
	{
	/*	
		String sToken = "VYpZt-Dp4WvFFTfOmIArWKgikFCqP-mwd1SDQA4YZvCrTLoh8M1i4q9m3byCoNh0AuydC_7viPme4LP7GmNCdXD3sL_gRN2FuCVkRXX7_KHoVsbgg7_zvf6ZUwFACsfN4_SaYYZHKsnINjzubS_fkqozD6OiN-qN6EgNuLPTTuk";

		CommonBaseClass objCommonBase = new CommonBaseClass(sToken);
		boolean bb = objCommonBase.IsTokenValid();
		System.out.println("Toekn valid " + bb);
		if(objCommonBase.IsTokenValid())
		{
			//System.out.println(objStr1);
			//out.print(objStr);
		}
		else
		{
			//objStr = objGson.toJson(objCommonBase.getDecryptedTokenMap());
			//out.println(objStr);
		}
		System.out.println("My results: " + bb);

		return;
		*/

//		String sToken = "DaY1sA1Dd7fmBajZ-hqC2Yf7ohtf2ZO1w-SLgmvsoeeqAvE2igzUTMxK-379cQ9h2EtsW8dxRJ9OUDiCgztZQEIFsNcv6uGZkVxoMOfpICkwGvuIOyqnjft4-vgXhv_YAZ13fkMg2lrB2WX3X4t7EqGrh8yUKOS6yxEcwwYWV62rH-HQPOSmu8Mn9gEzpC8f";
		String sToken = "Ldx0TA8sZCT3EXmuyXf5_UDTzb7-SpB68bzd1remVKahV7c4wfX5zsvNiINEI13WBG-I_E8rckxj5tsfRpd_C5yzzEP42ratfVpkAoklxHU087sF9AHSwFiWiG0rnN1vIRgEeHU3BmMnmNowKculfyE7AZcRiUL-vcZS-zQj7M4NTwUQxKNF6MpPfTjv_1KM";
		
			
		//		"WqErfW3eSh5cVm4q14AgSbFIJAnAaoZrSS0aTXPS1fK18LQrBMVlbdxc1S5YH8UGtFgOYKHfd_MrjnHpV9psLkeCIg41_EuEd0X93uaKKeHhyDystIrEAmGavUGonDfj_uMRhabf-3LeSLRB6Stdrx-KqSdeiQLH_S2jUYre5kGy9aNbFrGFTYMpWArNjjKu"
		Visitor v = new Visitor(sToken); //security
		
		
//		Boolean bb = v.IsTokenValid();
//		System.out.println("My results: " + bb.toString());

		try
		{
			int Entry_with = 1;
		
		//HashMap objHash=v.mfetchAllVisitors();
//		String StartDate = "2018-10-19";
//		String EndDate = "2018-11-19";
//		int iVisitorId = 0;
		//HashMap objHash=v.mfetchAllVisitors();
		//HashMap objHash = 
		//System.out.println("My results: " + objHash);
//		Gson objGson = new Gson();
//		String objStr = objGson.toJson(objHash);
		HashMap objHash=v.mfetchAllVisitorsDetails(2784);
		
		
		
		  if (objHash != null) {
              Gson gson = new GsonBuilder().setPrettyPrinting().create();
              String json = gson.toJson(objHash);
              System.out.println(json);
          } else {
              System.out.println("No data found for the given ID.");
          }
			
			String contactNo = "8808042620";      // Replace with the number you want to test
	        String entryDocId = "1";              // Replace with the EntryDoc_id value
	 // Assuming VisitHandler is the class with fetchVisitor()
//	        HashMap objHash = v.fetchVisitor(contactNo, entryDocId);
//			
//			  if (objHash != null) {
//	              Gson gson = new GsonBuilder().setPrettyPrinting().create();
//	              String json = gson.toJson(objHash);
//	              System.out.println(json);
//	          } else {
//	              System.out.println("No data found for the given ID.");
//	          }
	        
	        
	        
		//HashMap objHash=
//			HashMap<Integer, Map<String, Object>> objHash = v.getMobileFromUnit(24);
		//HashMap<Integer, Map<String, Object>> objHash = v.markExit(77);
			int VisitorEntryID = 971;
			int ExitGateNo = 2;
			int CheckOut = 1;
		//HashMap<Integer, Map<String, Object>> objHash = v.mfetchExit(VisitorEntryID, ExitGateNo, CheckOut);
			String StartDate = "2020-05-01";
			String EndDate = "2020-08-01";
			int iVisitorId = 7;
//		HashMap<Integer, Map<String, Object>> objHash1 = v.fetchVisitorsReports( StartDate, EndDate, iVisitorId);
		
		Gson objGson1 = new Gson();
		String objStr1 = objGson1.toJson(objHash);
		//System.out.println(objStr);
		System.out.println(objStr1);
		}
		catch (Exception e)
		{
		}
	}
	
}
