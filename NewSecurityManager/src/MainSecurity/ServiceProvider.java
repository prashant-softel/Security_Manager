package MainSecurity;

import static MainSecurity.DbConstants.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import SecuriyCommonUtility.MapUtility;



public class ServiceProvider extends CommonBaseClass
{

	//ProjectConstants m_objProjectConstants;
	//private  SecuriyCommonUtility.MapUtility m_objMapUtility;
	//public  DbOperations dbop;
	
	//ProjectConstants m_objProjectConstants;
	//private static SecuriyCommonUtility.MapUtility m_objMapUtility;
	//public  DbOperations dbop;
	public ServiceProvider(String token) 
	{
		super(token);
//		DB_SOCIETY = "hostmjbt_society46";
//		DB_SECURITY = "sm_1";
//		m_objProjectConstants = new ProjectConstants();
//		m_objMapUtility = new MapUtility();
		try
		{
			//dbop = new DbOperations(DB_ROOT_NAME);
		}
		catch(Exception e)
		{
			
		}
	}
	
	// GET DOCUMENT LIST
	public  HashMap<Integer, Map<String, Object>> mfetchDocument()
	{
	
		HashMap<Integer, Map<String, Object>>  mpStaffDocument = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	
		try
		{
			dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
			sSelectQuery = "select `id`,`document` from `documents` where status = 'Y' ORDER BY id";
			mpStaffDocument = dbop_sec_root.Select(sSelectQuery);
			//System.out.println(mpStaffDocument);
			if(mpStaffDocument.size() > 0)
			{
				//add document to map
				 rows2.put("document",MapUtility.HashMaptoList(mpStaffDocument));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("document","");
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
	
	//GET PURPOSE LIST
	public  HashMap<Integer, Map<String, Object>> mPurposeList()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sSelectUnit = "";
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			sSelectUnit = "select distinct purpose_id, purpose_name,purpose_image from purpose where pstatus ='Y'"; // where status = 
			HashMap<Integer, Map<String, Object>>  pList = dbop_sec.Select(sSelectUnit);
			if(pList.size() > 0)
			{
				//add member to map
				 rows2.put("purpose",MapUtility.HashMaptoList(pList));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//member not found
				 rows2.put("purpose","");
				 rows.put("success",0);
				 rows.put("response",rows2);
			}	
		}
		catch(Exception e)
		{
			 e.printStackTrace();
			 rows2.put("exception",e);
			 rows.put("success",0);
			 rows.put("response",rows2);	
		}
		
		//System.out.println(rows);
		return  rows;
	}
	public HashMap<Integer, Map<String, Object>> mMobileUdser(){
		DbOperations dbop = null;
		try {
			dbop = new DbOperations(DB_ROOT_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String squery = "SELECT m.unit_id, m.desc,m.society_id FROM device_details AS dd " +
	                    "JOIN login AS lg ON dd.login_id = lg.login_id " +
	                    "JOIN mapping AS m ON lg.login_id = m.login_id " +
	                    "WHERE dd.device_id != '' " +
	                    "GROUP BY m.unit_id " +
	                    "ORDER BY m.society_id";
	    HashMap<Integer, Map<String, Object>> mMemberList = dbop.Select(squery);
	    return mMemberList;
	}
	//GET UNIT LIST
	//public static HashMap<Integer, Map<String, Object>> mUnitList(int iWingID)
	public  HashMap<Integer, Map<String, Object>> mUnitList()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap<Integer, Map<String, Object>> mobileUsersMap = mMobileUdser();
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			dbop_soc =new DbOperations(DB_SOCIETY);
			String sSqlMemberMain = "";	
			String dnd_fetchdata  = "";
			//sSqlMemberMain = "select unit.`unit_no`,unit.`unit_id`,mem.owner_name,mem.mob from `unit` JOIN `member_main` as mem on unit.unit_id=mem.unit where unit.wing_id='"+iWingID+"' and mem.ownership_status = '1'";
//			sSqlMemberMain = "select distinct mem.owner_name,unit.`unit_no`,unit.`unit_id`,unit.`wing_id`,mem.mob,concat_ws(' ',tm.tenant_name,tm.tenant_LName) as tenant_name,tm.mobile_no,tm.status,IF(tm.tenant_name != '','1','0') 'has_tenant',IF(tm.end_date >  CURDATE(), '1', '0') as tenant_active from `unit`  left join tenant_module as tm on  tm.unit_id = unit.unit_id left JOIN `member_main` as mem on unit.unit_id=mem.unit where mem.ownership_status = '1' ORDER BY `unit`.`sort_order`  ASC";
			sSqlMemberMain = "SELECT unit.unit_no,unit.society_id, unit.unit_id, unit.wing_id, mem.owner_name, mem.mob, CONCAT_WS(' ', tm.tenant_name, tm.tenant_LName) AS tenant_name, tm.mobile_no, tm.status, IF(tm.tenant_name != '', '1', '0') AS has_tenant, IF(tm.end_date > CURDATE(), '1', '0') AS tenant_active FROM unit LEFT JOIN (SELECT t1.* FROM tenant_module t1 INNER JOIN (SELECT unit_id, MAX(end_date) AS max_end FROM tenant_module GROUP BY unit_id) t2 ON t1.unit_id = t2.unit_id AND t1.end_date = t2.max_end) tm ON tm.unit_id = unit.unit_id LEFT JOIN member_main mem ON unit.unit_id = mem.unit WHERE mem.ownership_status = '1' ORDER BY unit.sort_order ASC";
			HashMap<Integer, Map<String, Object>> mMemberList = dbop_soc.Select(sSqlMemberMain);
			dnd_fetchdata = "SELECT dnd_id, unit_no, dnd_type, unit_id, dnd_msg from dnd_status where status=1";
			HashMap<Integer, Map<String, Object>> dnd_data = dbop_sec.Select(dnd_fetchdata);
			for(Entry<Integer, Map<String, Object>> entry1 : mMemberList.entrySet()) 
			{
				String sUnit_Id = entry1.getValue().get("unit_id").toString();
				String sSociety_Id = entry1.getValue().get("society_id").toString();

				int targetSocietyId = Integer.parseInt(sSociety_Id.trim());
				List<String> visitorUnits = Arrays.asList(sUnit_Id.split(","));
				boolean mobileUserExists = false;

				for (Map.Entry<Integer, Map<String, Object>> mobileEntry : mobileUsersMap.entrySet()) {
				    Map<String, Object> mobileUserData = mobileEntry.getValue();

				    Object mobileUnitIdObj = mobileUserData.get("unit_id");
				    Object mobileSocietyIdObj = mobileUserData.get("society_id");

				    if (mobileUnitIdObj != null && mobileSocietyIdObj != null) {
				        int mobileUnitId = Integer.parseInt(mobileUnitIdObj.toString().trim());
				        int mobileSocietyId = Integer.parseInt(mobileSocietyIdObj.toString().trim());

				        for (String visitorUnitStr : visitorUnits) {
				            try {
				                int visitorUnitId = Integer.parseInt(visitorUnitStr.trim());

				                if (visitorUnitId == mobileUnitId && targetSocietyId == mobileSocietyId) {
				                    mobileUserExists = true;
				                    break;
				                }
				            } catch (NumberFormatException e) {
				                System.err.println("Invalid unit_id format: " + visitorUnitStr);
				            }
				        }
				    }

				    if (mobileUserExists) {
				        break;
				    }
				}
				entry1.getValue().put("MobileUserExists", mobileUserExists ? 1 : 0);

				if(entry1.getValue().get("has_tenant").equals("1") && entry1.getValue().get("tenant_active").equals("1"))
				{
					entry1.getValue().put("name", entry1.getValue().get("tenant_name"));
					if(entry1.getValue().containsKey("mobile_no"))
					{
						if(entry1.getValue().get("mobile_no").equals(""))
						{
							entry1.getValue().put("mobile_no", "0");
						}
						else
						{
							entry1.getValue().put("mobile_no", entry1.getValue().get("mobile_no"));
						}
					}
					else
					{
						entry1.getValue().put("mobile_no", "0");
					}
				}
				else if(entry1.getValue().get("has_tenant").equals("0") && (entry1.getValue().get("status") == null || entry1.getValue().get("status").equals("Y") ))
				{
					entry1.getValue().put("name", entry1.getValue().get("owner_name"));
					
					if(entry1.getValue().containsKey("mob"))
					{
						if(entry1.getValue().get("mob").equals(""))
						{
							entry1.getValue().put("mobile_no", "0");
						
						}
						else
						{
							entry1.getValue().put("mobile_no", entry1.getValue().get("mob"));	
							
						}
					}
					else
					{
						entry1.getValue().put("mobile_no", "0");
					}					
				}
				else
				{
					entry1.getValue().put("name", entry1.getValue().get("tenant_name"));
				}
				
			}
			if(mMemberList.size() > 0)
			{
				//add member to map
				 rows2.put("member",MapUtility.HashMaptoList(mMemberList));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//member not found
				 rows2.put("member", MapUtility.HashMaptoList(mMemberList));
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
			if(dnd_data.size() > 0)
			{
				//fetch DND_data
				 rows2.put("DND_record",MapUtility.HashMaptoList(dnd_data));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//DND_data not found
				 rows2.put("DND_record", MapUtility.HashMaptoList(dnd_data));
				 rows.put("success",0);
				 rows.put("response",rows2);
			}	 
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			 rows2.put("exception",e);
			 rows.put("success",0);
			 rows.put("response",rows2);
		}
	    return rows;
	}	
	// Get Wing List
	public  HashMap<Integer, Map<String, Object>> mWingList()
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		
		try
		{
			dbop_soc =new DbOperations(DB_SOCIETY);
			String sSqlSocietyWing = "";
			sSqlSocietyWing = "SELECT * FROM `wing`";
			
			HashMap<Integer, Map<String, Object>> mWingList = dbop_soc.Select(sSqlSocietyWing);
			//System.out.println(mMemberList);
			if(mWingList.size() > 0)
			{
				//add member to map
				 rows2.put("Wing",MapUtility.HashMaptoList(mWingList));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//member not found
				 rows2.put("Wing","");
				 rows.put("success",0);
				 rows.put("response",rows2);
			}	 
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			 rows2.put("exception",e);
			 rows.put("success",0);
			 rows.put("response",rows2);
		}
	    return rows;
	}	
	public  HashMap<Integer, Map<String, Object>> mFetchCompany(int iCat_id)
	{
	
		HashMap<Integer, Map<String, Object>>  mpCompany = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String default_url = "https://way2society.com/images/mobileservice/visitor.png";
	
		try
		{
			dbop_sec_root = new DbOperations(DB_SECURITY_ROOT);
			sSelectQuery = "select * from `visitor_company` where cat_id= '"+iCat_id+"'";
			mpCompany = dbop_sec_root.Select(sSelectQuery);
			//System.out.println(mpStaffDocument);
			if(mpCompany.size() > 0)
			{
				for (Map<String, Object> company : mpCompany.values()) {
				    Object logo = company.get("logo");
				    if (logo == null || logo.toString().trim().equals("") || logo.toString().equalsIgnoreCase("null")) {
				        company.put("logo", default_url);
				    }
				}
				//add document to map
				 rows2.put("CompanyName",MapUtility.HashMaptoList(mpCompany));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("CompanyName","");
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
	
	public  HashMap<Integer, Map<String, Object>> mFetchExpVisitor(int iSocietyID)
	{
		
		//String DB_SOCIETY = "hostmjbt_society46";
		//String DB_SECURITY = "sm_1";

	
		HashMap<Integer, Map<String, Object>>  mpExpVisitor = new HashMap<Integer, Map<String, Object>>();
		
		HashMap<Integer, Map<String, Object>>  mpUnits = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "",sSelectQuery1 = "",sSelectQuery2 = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	
		try
		{
			dbop_sec = new DbOperations(DB_SECURITY);
			Timestamp updateTimeStamp = new Timestamp(System.currentTimeMillis());
			String d=new SimpleDateFormat("yyyy-MM-dd").format(updateTimeStamp);
			sSelectQuery = "select ex.`fname`,ex.`lname`,ex.`mobile`,ex.`expected_date`,ex.`expected_time`,ex.`unit`,p.purpose_name from `expected_visitor` as ex join purpose as p on p.purpose_id=ex.purpose_id where ex.expected_date='"+ d +"'";
			
			System.out.println("sSelectQuery : " + sSelectQuery);
			mpExpVisitor = dbop_sec.Select(sSelectQuery);
			//System.out.println(mpExpVisitor);
			String ExpContact="";
			for(Entry<Integer, Map<String, Object>> entry : mpExpVisitor .entrySet()) 
			{
				//dbop = new DbOperations(DB_SECURITY);
				HashMap<Integer, Map<String, Object>>  mpVisitors = new HashMap<Integer, Map<String, Object>>();
				sSelectQuery1 = "select * from visitorentry where visitorMobile = '"+entry.getValue().get("mobile")+"' and status='inside'";
				mpVisitors = dbop_sec.Select(sSelectQuery1);
				System.out.println(mpVisitors);
				
				if(mpVisitors.size() > 0)
				{
					entry.getValue().put("currentIn","1");
					
			 
					
				}
				else
				{
					entry.getValue().put("currentIn","0");
				}
				
				dbop_soc = new DbOperations(DB_SOCIETY);
				System.out.println("DB_SOCIETY: "+DB_SOCIETY);
				System.out.println("DB_SECURITY: "+DB_SECURITY);
				sSelectQuery2 = "SELECT u.unit_no,m.owner_name,w.wing FROM `unit` as u join wing as w on w.wing_id=u.wing_id join member_main as m on m.unit=u.unit_id where u.unit_id='"+entry.getValue().get("unit")+"' and m.ownership_status='1'";
				mpUnits = dbop_soc.Select(sSelectQuery2);
				for(Entry<Integer, Map<String, Object>> entry1 : mpUnits .entrySet()) 
				{
				if(mpUnits.size() > 0 )
				{
					entry.getValue().put("Wing",entry1.getValue().get("wing"));
					entry.getValue().put("UnitNo",entry1.getValue().get("unit_no"));
					entry.getValue().put("OwnerName",entry1.getValue().get("owner_name"));
				}
				else
				{
					entry.getValue().put("Wing","0");
					entry.getValue().put("UnitNo","0");
					entry.getValue().put("OwnerName","");
				}
			}
		}
		
			if(mpExpVisitor.size() > 0)
			{
				 rows2.put("TotalExpected",MapUtility.HashMaptoList(mpExpVisitor));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{

				 rows2.put("TotalExpected","");
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
	/*  Fewtch Staff Category */
	public  HashMap<Integer, Map<String, Object>> mFetchCategory()
	{
	
		HashMap<Integer, Map<String, Object>>  mpStaffCategory = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			sSelectQuery = "SELECT * FROM `cat` where `status`='Y' ORDER BY cat";
			mpStaffCategory = dbop_soc_root.Select(sSelectQuery);
			//System.out.println(mpStaffDocument);
			if(mpStaffCategory.size() > 0)
			{
				//add document to map
				 rows2.put("categoryList",MapUtility.HashMaptoList(mpStaffCategory));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("categoryList","");
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
	/*  Add Staff Personal Details   */
	public  HashMap<Integer, Map<String, Object>> sAddStaffPersonal(int iSocietyID,String sFullName,String sDOb,String sWorkingSince,String sMarritalstatus,String sCategory,String curContact1,String soc_staffid, String sGender, String sAadhaar)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sInsertQuery = "";
		long lServiceProviderID=0;
		String stcount="";
		try
		{		
			///DateFormat output = new SimpleDateFormat("yyyy-MM-dd");
			//Date date=new Date();
			//New_join= output.format(date);
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			String sqlForServiceProviderDetails = "Select service_prd_reg_id from service_prd_reg where cur_con_1 = '"+curContact1+"' and status = 'Y'";
			HashMap<Integer, Map<String, Object>> servicePrdDetails =  dbop_soc_root.Select(sqlForServiceProviderDetails);
			System.out.println("servicePrdDetails:   "+servicePrdDetails  + "servicePrdDetails.size()"+servicePrdDetails.size());
			if(servicePrdDetails.size() > 0)
			{
				for(Entry<Integer, Map<String, Object>> entry1 : servicePrdDetails.entrySet()) 
				{
					lServiceProviderID = Long.valueOf(entry1.getValue().get("service_prd_reg_id").toString());
					System.out.println("service_prd_reg_id : " + lServiceProviderID);
					stcount = "0";
					
				}
			}
			else
			{
				//dbop = new DbOperations(DB_ROOT_NAME);
				sInsertQuery = "insert into service_prd_reg (`society_id`,`full_name`,`dob`,`since`,`marry`,`photo`,`photo_thumb`,`cur_con_1`,`gender`,`adhar_card_no`) values ";
	
				sInsertQuery = sInsertQuery + "('"+iSocietyID+"','"+sFullName+"','" + sDOb +  "','"+sWorkingSince+"','"+sMarritalstatus+"','staffImage.jpg','staffImage.jpg','"+curContact1+"','"+sGender+"','"+sAadhaar+"' )";
				lServiceProviderID = dbop_soc_root.Insert(sInsertQuery);
				System.out.println("lServiceProviderID new:  "+lServiceProviderID);
				stcount = "1";
				List<String> Cat = Arrays.asList(sCategory.split(","));
				System.out.println("Category list: "+Cat+"  Category size:  "+ Cat.size());
				for (int i = 0; i < Cat.size(); i++)
				{
				String insertCat= "insert into 	spr_cat (`service_prd_reg_id`,`cat_id`) values ('"+lServiceProviderID+"','"+Cat.get(i)+"')";
				long addCategory= dbop_soc_root.Insert(insertCat);
				System.out.println("addCategory new:  "+addCategory);
				}
			}
			
			
			String insertQuery = "insert into service_prd_society(`provider_id`, `society_id`, `society_staff_id`) values ('"+lServiceProviderID+"','"+iSocietyID+"','"+soc_staffid+"')";
			long lstaffid = dbop_soc_root.Insert(insertQuery);
			System.out.println("lstaffid new:  "+lstaffid);
			if(lServiceProviderID  > 0)
			{
				//System.out.println("add member to map");
				 rows2.put("StaffID ",lServiceProviderID);
				 rows2.put("staff_count", stcount);
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//System.out.println("member not found");
				 rows2.put("StaffID ","");
				 rows2.put("staff_count", "");
				 rows.put("success",0);
				 rows.put("response",rows2);
			}
		}
		catch(Exception e)
		{
			System.out.println("error:  "+e);
			e.printStackTrace();
			rows2.put("exception ",e);
			rows.put("success",0);
			rows.put("response",rows2);
		}
		System.out.println(rows);
		return rows;
	} 
	/* Add Contact Details */
	public  HashMap<Integer, Map<String, Object>> sAddStaffContactDetails(int iSocietyID,int ProviderId,String sCurentAdd,String sCurContact2,String sRefName,String sRefAdd,String RefContact)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String sUpdateQuery = "";
		
		try
		{		
			
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			sUpdateQuery = "Update service_prd_reg set cur_resd_add	='"+sCurentAdd+"',cur_con_2='"+sCurContact2+"',ref_name='"+sRefName+"',ref_add='"+sRefAdd+"',ref_con_1='"+RefContact+"' Where service_prd_reg_id = '"+ProviderId+"' ";
			
			long UpdateStaffID = dbop_soc_root.Update(sUpdateQuery);
			
			if(UpdateStaffID  > 0)
			{
				//add member to map
				 rows2.put("StaffID ",UpdateStaffID );
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
	/* Add Document Details */
	public  HashMap<Integer, Map<String, Object>> sAddStaffUnitDetails(int iSocietyID,int ProviderId,String unit_no,String unit_id,String owner_name)
	{
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		String InsertQUery = "";
		long InsertUnit = 0;
		
		try
		{	
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			List<String> UnitID = Arrays.asList(unit_id.split(","));
			List<String> UnitNo = Arrays.asList(unit_no.split(","));
			List<String> OwnerName = Arrays.asList(owner_name.split(","));
					
					for (int i = 0; i < UnitID.size(); i++)
					{
						String unit = UnitNo.get(i) + "[" + OwnerName.get(i) + "]";
						InsertQUery = "insert into `service_prd_units`(`service_prd_id`,`unit_id`,`unit_no`,`society_id`) value('"+ProviderId+"','"+UnitID.get(i)+"','"+unit+"','"+iSocietyID+"')";
						
						InsertUnit = dbop_soc_root.Insert(InsertQUery);
					}

			
			
			
			if(InsertUnit  > 0)
			{
				//add member to map
				 rows2.put("StaffID ",InsertUnit );
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
	
	public  HashMap<Integer, Map<String, Object>> sFetchDocumentList()
	{
		HashMap<Integer, Map<String, Object>>  mpStaffDoc = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	
		try
		{
			dbop_soc = new DbOperations(DB_SOCIETY);
			sSelectQuery = "SELECT * FROM `document` where `status`='Y'";
			mpStaffDoc = dbop_soc.Select(sSelectQuery);
			//System.out.println(mpStaffDocument);
			if(mpStaffDoc.size() > 0)
			{
				//add document to map
				 rows2.put("DocList",MapUtility.HashMaptoList(mpStaffDoc));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("DocList","");
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
	
	
	public  HashMap<Integer, Map<String, Object>> fetchstaffidstatus(int iSocietyID,String soc_staffid)
	{
		HashMap<Integer, Map<String, Object>>  mpStaffID = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			sSelectQuery = "SELECT * FROM `service_prd_society` where `status`='Y' and society_id = '"+iSocietyID+"' and society_staff_id = '"+soc_staffid+"'";
			mpStaffID = dbop_soc_root.Select(sSelectQuery);
			//System.out.println(mpStaffDocument);
			if(mpStaffID.size() > 0)
			{
				//add document to map
				 rows2.put("StaffID",MapUtility.HashMaptoList(mpStaffID));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("StaffID","");
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
	public  HashMap<Integer, Map<String, Object>> getStaffStatus(int iSocietyID)
	{
		HashMap<Integer, Map<String, Object>>  mpStaffID = new HashMap<Integer, Map<String, Object>>();
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
	
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			sSelectQuery = "SELECT society_staff_id as StaffIDL FROM `service_prd_society` where status ='Y' and society_id = '"+iSocietyID+"' order by sp_id desc LIMIT 1";
			mpStaffID = dbop_soc_root.Select(sSelectQuery);
			//System.out.println(mpStaffDocument);
			if(mpStaffID.size() > 0)
			{
				//add document to map
				 rows2.put("StaffID",MapUtility.HashMaptoList(mpStaffID));
				 rows.put("success",1);
				 rows.put("response",rows2);			 
			}
			else
			{
				//documents not found
				 rows2.put("StaffID","");
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
	///new additions
	public  HashMap<Integer, Map<String, Object>> sGetStaffDetails(int iSocietyID,String soc_staffid, String contact)
	{
		String sSelectQuery = "";
		HashMap rows = new HashMap<>();
		Map<String, Object> serviceprovider = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		long lServiceProviderID=0;
		String sqlForServiceProviderDetails = "";
	
		try
		{
			dbop_soc_root = new DbOperations(DB_ROOT_NAME);
			if(contact.length() != 0){
				sqlForServiceProviderDetails = "Select reg.* from `service_prd_reg` as reg JOIN `service_prd_society` as soc on reg.service_prd_reg_id = soc.provider_id where reg.cur_con_1 = '"+contact+"' and reg.status = 'Y' and soc.society_id = '"+iSocietyID+"'";
			}
			else{
				sqlForServiceProviderDetails = "Select reg.* FROM `service_prd_reg` as reg JOIN `service_prd_society` as soc on reg.service_prd_reg_id = soc.provider_id where soc.society_staff_id = '"+soc_staffid+"' and reg.status = 'Y' and soc.society_id = '"+iSocietyID+"'";
				//SELECT v.* FROM `visitorentry` as v JOIN `visit_approval` as va on va.v_id= v.id where v.visitor_ID='"+v_id+"'
			}
			System.out.println("sqlForServiceProviderDetails: "+sqlForServiceProviderDetails);
			HashMap<Integer, Map<String, Object>> servicePrdDetails =  dbop_soc_root.Select(sqlForServiceProviderDetails);
			System.out.println("\nservicePrdDetails:   "+servicePrdDetails  + "\nservicePrdDetails.size: "+servicePrdDetails.size());
			if(servicePrdDetails.size() > 0)
			{
				//System.out.println("service provider: "+serviceprovider);
					//add document to map
					 rows2.put("StaffDetails",MapUtility.HashMaptoList(servicePrdDetails));
					 rows.put("success",1);
					 rows.put("response",rows2);			 
			
				
			}
			else{
				//documents not found
				 rows2.put("StaffDetails","");
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
	///
	public static void main(String[] args) //throws Exception
	{
		String sToken = "HvMdMAcmKrk3U_YEwzXPTUZqe5M2BQVH38as6nX__kPVMj_xQBWscEDVUpe42XTxW5-zj_S7kWLDlMQmQbsDNeBDT_xDL8iovWvquh1NeL5dxHjqmk7rf7oO7dYk5MSe7n7KLdzdI44CcPP8z7fK1igwWWrzBsXZCAeOn_pUC-5Y3gce_cQXilMX89QFapplmHgsrli40ow0Z2xpkQh3vg";		
		ServiceProvider sp = new ServiceProvider(sToken);

		 
//		HashMap objHash = sp.mFetchExpVisitor(59);
		int unit_id = 16;
//		HashMap objHash = sp.mFetchCompany(1);
		//HashMap objHash=sp.mFetchExpVisitor(59);
		HashMap objHash=sp.mPurposeList();
		//sp.mUnitList();
		
		//HashMap objHash=sp.fetchstaffidstatus(59, "D656");
		//sAddStaffPersonal(iSocietyID,sFullName,sDOb,sWorkingSince,sMarritalstatus,sCategory,curContact1,soc_staffid, sGenderstatus, sAadhaar)
		//HashMap objHash=sp.sAddStaffPersonal(59,"Chords Deck", "14-06-1984", "2020-06-14", "No", "32","7072028970","995","male", "1234123412341234");
//		societyID=59, soc_staffid=994, fullname=Godzilla, dob=16-06-1989, workingSince=2020-06-16, marry=No, gender=male, adhar_card_no=123412341234,
//				 category=23, curContact1=7972958040
		//HashMap objHash=sp.sAddStaffPersonal(59,"Godzilla 2", "16-06-1989", "2020-06-16", "No", "23","7972958140","994","male", "123412341234");
		//HashMap objHash=sp.sGetStaffDetails(59, "", "5451234567");
		
		Gson objGson = new Gson();
		String objStr = objGson.toJson(objHash);
		System.out.println(objStr);
		
	
	
	
	
	}
	
}
