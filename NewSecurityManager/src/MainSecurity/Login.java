package MainSecurity;

import static MainSecurity.DbConstants.*;
import static MainSecurity.ProjectConstants.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.spec.SecretKeySpec;

import com.google.gson.Gson;

import SecurityecryptDecryptAlgos.ecryptDecrypt;
import SecuriyCommonUtility.MapUtility;

public class Login 
{
	ProjectConstants m_objProjectConstants;
	private static SecuriyCommonUtility.MapUtility m_objMapUtility;

  	private String DB_ROOT_NAME = "hostmjbt_societydb";
 
	private String m_sSocietyDB = "";
	private String m_sSecurityDB = "";
	private int m_iSocietyId = 0;
	//private String m_DB_SECURITY_ROOT = "security_rootdb";
	
	private boolean m_bLoginOk = false;
	public Login()
	{
		m_objProjectConstants = new ProjectConstants();
		m_objMapUtility = new MapUtility();
	}
	
	
	public  int fetchGateNo(int userID) throws ClassNotFoundException
	{
		int iGateNo = 0;
		if(m_bLoginOk == false)
		{
			return iGateNo;
		}
		String sSelectQuery = "";
		try
		{
			DbOperations dbop = new DbOperations(m_sSecurityDB);
			sSelectQuery ="SELECT * FROM `gate_entry` WHERE login_id = '" + userID + "' AND timestamp = ( SELECT MAX( timestamp ) FROM `gate_entry` ) LIMIT 1";
			HashMap<Integer, Map<String, Object>> GateEntry = dbop.Select(sSelectQuery);
			if(GateEntry.size() > 0)
			{
				for(Entry<Integer, Map<String, Object>> entry2 : GateEntry.entrySet()) 
				{
					iGateNo = (int) entry2.getValue().get("gate_no");
				}
			}			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return iGateNo;
	}	
	
	public   HashMap<Integer, Map<String, Object>> fetchLoginDetails(String sEmail, String sPassword)//, String sFBId, String sGPId)
	{
		//System.out.println("Inside fetchLoginDetails");

		m_bLoginOk = false;
		String sMessage = "";
		String sToken = "";
		int iLoginID = 0;
//		int iSocietyId = 0;
		String sSocietyCode = "";
		String sLoginName ="";
		String sMemberID = "";
		String sRole = "";
		String sSocietyName="";
		String loggedinstatus= "";
		String device_id = "";
		int sMapping = 0;
		int iGateNo = 0;
		String sql2, sql3;
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap rows3 = new HashMap<>();
		//String sMyNewMessage = "";
//		sMyNewMessage = sMyNewMessage + "Start `member_id` = '" + sEmail + "' and password = '" +sPassword+  "  rootdb" + DB_ROOT_NAME + " ... ";
		
		try
		{
			String sql1 = "Select * from `login` where `member_id` = '" + sEmail + "' and password = '" +sPassword+ "' and status = 'Y';";
			DbOperations dbop = new DbOperations(DB_ROOT_NAME);
			//sMyNewMessage = sMyNewMessage + "sql1  " + sql1 + " ... ";

			HashMap<Integer, Map<String, Object>> loginDetails =dbop.Select(sql1);

			//sMyNewMessage = sMyNewMessage + "loginDetails count : " + loginDetails.size() + " ... ";
			//sMyNewMessage = sMyNewMessage + "loginDetails : " + loginDetails.toString() + " ... ";
			//System.out.println("sMyNewMessage :" + sMyNewMessage);
			System.out.println("sql1 :" + sql1);
			System.out.println("loginDetails :" + loginDetails);
			if(loginDetails.size() == 1)
			{
				for(Entry<Integer, Map<String, Object>> entry2 : loginDetails.entrySet()) 
				{
					iLoginID = (int) entry2.getValue().get("login_id");
					sLoginName = (String) entry2.getValue().get("name");
					sMemberID = (String) entry2.getValue().get("member_id");
					device_id = entry2.getValue().get("device_id").toString();
					loggedinstatus = entry2.getValue().get("login_status").toString();
					sMapping = (int) entry2.getValue().get("current_mapping"); 
					//sMyNewMessage = sMyNewMessage + "Found login ... ";
					break;
				}
				System.out.println("sMapping :" + sMapping);
	
				if(sMapping > 0)
				{
					sql2 = "Select * from mapping where id = '" + sMapping + "';";
					System.out.println("Test 2 :");
					HashMap<Integer, Map<String, Object>> mappingDetails = dbop.Select(sql2);	
					for(Entry<Integer, Map<String, Object>> entry1 : mappingDetails.entrySet()) 
					{
						m_iSocietyId = (int) entry1.getValue().get("society_id");
						sRole =  (String) entry1.getValue().get("role");
					}
					sql3 = "Select * from society where society_id = '"+ m_iSocietyId +"' and status = 'Y';";
					System.out.println("sql3 :" + sql3);
					HashMap<Integer, Map<String, Object>> societyDetails = dbop.Select(sql3);
					for(Entry<Integer, Map<String, Object>> entry1 : societyDetails.entrySet()) 
					{
						sSocietyName = (String)entry1.getValue().get("society_name");
						sSocietyCode = (String)entry1.getValue().get("society_code");
						m_sSecurityDB = (String)entry1.getValue().get("security_dbname");
						m_sSocietyDB = (String)entry1.getValue().get("dbname");
						System.out.println("m_sSecurityDB : " + m_sSecurityDB);
						//sMyNewMessage = sMyNewMessage + "Found sec db" + m_sSecurityDB + " ... ";
						//DB_SOCIETY = m_sSocietyDB;
						//System.out.println("DB_SOCIETY : " + DB_SOCIETY);
					}
					
					
					if(m_sSecurityDB != null && m_sSecurityDB.length() > 0)
					{
						//System.out.println("test 3 :");
//						sMyNewMessage = sMyNewMessage + "Inside m_sSecurityDB " + m_sSecurityDB + " ... ";
						
						if(iLoginID > 0 && sLoginName != "")
						{
//							sMyNewMessage = sMyNewMessage + "Inside iLoginID " + iLoginID + " ... ";
							
							Map<String, Object> encrptionMap = new HashMap<String, Object>();
						 	encrptionMap.put("name", sLoginName);
						 	encrptionMap.put("id", iLoginID);
						 	encrptionMap.put("email", sMemberID);
						 	encrptionMap.put("S_DB_Soc", m_sSocietyDB);
						 	encrptionMap.put("S_DB", m_sSecurityDB);
						 	encrptionMap.put("S_Code", sSocietyCode);
						 	encrptionMap.put("SocietyId", m_iSocietyId);
						 	
						 	Calendar calendar = Calendar.getInstance();
					        Timestamp ourJavaTimestampObject = new Timestamp(System.currentTimeMillis());
					        
					        encrptionMap.put("tt",ourJavaTimestampObject);
					        sToken = ecryptDecrypt.encrypt(ENCRYPT_SPKEY,ENCRYPT_INIT_VECTOR,encrptionMap.toString());	
							System.out.println("sToken! : " + sToken);
//							sMyNewMessage = sMyNewMessage + "Got token " + sToken + " ... ";
					        
					        if(sToken.length() > 0)
					        {	
					        	if(authDetails(iLoginID, sToken) == true)
					        	{
					        		m_bLoginOk = true;
//									sMyNewMessage = sMyNewMessage + "Login Successful " + sToken + " ... ";
					        		sMessage = "Login Successful";
					        		iGateNo = fetchGateNo(iLoginID);	
					        	}
					        	else
					        	{
					        		sMessage = "Test Auth failed";
					        	}
					        	
					        }
					        else
					        {
					        	sMessage = "Login Failed. No token";
					        }
						}
						else
						{
							rows.put("success","0");
							rows2.put("message","Invalid Username or Password.");
							rows.put("response",rows2);
						}
					}
					else
					{
						sMessage = "Security db doesnt exist";
					}

				}
				else
				{
					sMessage = "Invalid mapping";
				}

			}
			else
			{
//				sMyNewMessage = sMyNewMessage + "login details not found... ";
				sMessage = "login details not found... ";
			}

			
        	if(m_bLoginOk)
        	{
        		rows.put("success","1");
        		rows.put("version", DbConstants.APP_VERSION);
	        	rows2.put("token",sToken);
	        	rows2.put("name", sLoginName);
	        	rows2.put("SocietyName", sSocietyName);
	        	rows2.put("SocietyId", m_iSocietyId);
	        	rows2.put("SocietyCode", sSocietyCode);
	        	rows2.put("SocietyDB", m_sSocietyDB);
	        	rows2.put("SecurityDB", m_sSecurityDB);
	        	rows2.put("GateNo", iGateNo);
	        	rows2.put("role", sRole);
	        	rows2.put("login_id",iLoginID);
	        	rows2.put("loggedinstatus",loggedinstatus);
	        	rows2.put("device_id",device_id);
	        	
        	}
        	else
        	{        		
        		rows.put("success","0");
        		rows.put("message","Invalid login");
        		//rows.put("sMyNewMessage",sMyNewMessage);
        		
        		rows.put("version", DbConstants.APP_VERSION);
        	}
        		
			rows2.put("message", sMessage);
			rows.put("response",rows2);
			
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			rows.put("success","0");
			rows.put("version", DbConstants.APP_VERSION);
			rows2.put("UserID", sEmail);
			rows2.put("message", e);
    		//rows.put("sMyNewMessage",sMyNewMessage);
			rows.put("response",rows2);			
		}
		
	    return rows;
	}
	
	//CREATE AUTHENTICATION TABLE
	public boolean authDetails(int iLoginID, String sToken)
	{
		boolean bReturn = false;
		if(m_bLoginOk == false)
		{
//			return bReturn;
		}
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();		
	
		try
		{		
			String sInsertQuery="INSERT into `auth` (`loginID`, `authToken`) VALUES ";
			sInsertQuery+="('" + iLoginID + "','" + sToken +"')";
			DbOperations dbop = new DbOperations(m_sSecurityDB);
			long lAuthID = dbop.Insert(sInsertQuery);
			if(lAuthID  > 0)
			{
				System.out.println("lAuthID : " + lAuthID);
				bReturn = true;
			}
		//}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Exception : " + e.getMessage());
		}
	    return bReturn;
		
	}
	
	//Validate if token exist in the database
	public boolean validateToken(String sToken)
	{
		boolean bReturn = false;
		//Write validation code here
		try
		{
			String sql1 = "Select * from `auth` where authToken = '" + sToken + "' and IsActive = 1;";
			DbOperations dbop = new DbOperations(m_sSecurityDB);
			HashMap<Integer, Map<String, Object>> loginDetails = dbop.Select(sql1);
		
			
			if(loginDetails.size() == 1)
			{
				for(Entry<Integer, Map<String, Object>> loginEntry : loginDetails.entrySet()) 
				{
					int iLoginID = (int) loginEntry.getValue().get("loginID");
					
				}
				bReturn = true;				 
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return bReturn;
	}
	
	public HashMap<Integer, Map<String, Object>> refreshToken(String sToken,String username,String pass) throws ClassNotFoundException
	{
		Login login=new Login();
		//refreshing token process
		HashMap tokenMap = new HashMap<>();
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		HashMap rowsFinal = new HashMap<>();
		String sLoginName = "";
		boolean bTokenOK = false;
		String sMessage = "";
	 	String sMemberID = "";
	 	String sSocietyCode = "";
	 	String sSocietyId = "";
	 	int iLoginID = 0, iGateNo = 0;
		
		//String sMyNewMessage = "";
		//sMyNewMessage = sMyNewMessage + "Start `member_id` = '" + username + "' and password = '" +pass+  "  sToken" + sToken + " ... ";
	
	 	try
	 	{
		if(sToken.length() > 0 )
		{
			//decryting old token 
			String decrptedKey = ecryptDecrypt.decrypt(ENCRYPT_SPKEY,ENCRYPT_INIT_VECTOR,sToken);
			tokenMap = m_objMapUtility.stringToHashMap(decrptedKey);
			System.out.println("Descrypted token :" + tokenMap);
			//sMyNewMessage = sMyNewMessage + "tokenMap = '" + tokenMap.toString() + "... ";
			if(tokenMap.size() > 0)
			{
				//old token decrypted and fetching login details from old token passed
				iLoginID = Integer.parseInt((String)tokenMap.get("id"));
				sLoginName = (String)tokenMap.get("name");
			 	sMemberID = (String)tokenMap.get("email");
			 	m_sSecurityDB = (String)tokenMap.get("S_DB");
			 	sSocietyId = (String)tokenMap.get("SocietyId");
			 	sSocietyCode = (String)tokenMap.get("SocietyCode");
			 	m_sSocietyDB = (String)tokenMap.get("S_DB_Soc");
			 	//sMyNewMessage = sMyNewMessage + "Token encrypted sLoginName = '" + sLoginName + "' and m_sSecurityDB = '" +m_sSecurityDB + " ... ";
			 				 				 	
				System.out.println("m_sSecurityDB : " + m_sSecurityDB);
				
				//DB_SOCIETY = sSocietyDB;
				//System.out.println("DB_SOCIETY : " + DB_SOCIETY);

				String sql1 = "Select * from `auth` where authToken = '" + sToken + "' and IsActive = 1;";
			 	//sMyNewMessage = sMyNewMessage + "validateToken sql = '" + sql1 + "' and m_sSecurityDB = '" +m_sSecurityDB + " ... ";

				DbOperations dbop = new DbOperations(m_sSecurityDB);
				HashMap<Integer, Map<String, Object>> loginDetails = dbop.Select(sql1);			
			 	//sMyNewMessage = sMyNewMessage + "loginDetails = '" + loginDetails.toString() + "... ";
				
				if(loginDetails.size() == 1)
				{
					for(Entry<Integer, Map<String, Object>> loginEntry : loginDetails.entrySet()) 
					{
						int iLoginID1 = (int) loginEntry.getValue().get("loginID");
					 	//sMyNewMessage = sMyNewMessage + "iLoginID = '" + iLoginID1  + "... ";
						
					}
				}

				if(validateToken(sToken))
				{				
				 	//sMyNewMessage = sMyNewMessage + "Token validated sLoginName = '" + sLoginName + "' and m_sSecurityDB = '" +m_sSecurityDB + " ... ";
			 	
				 	Map<String, Object> encrptionMap = new HashMap<String, Object>();
				 	encrptionMap.put("name", sLoginName);
				 	encrptionMap.put("id", iLoginID);
				 	encrptionMap.put("email", sMemberID);
				 	encrptionMap.put("S_DB_Soc", m_sSocietyDB);
				 	encrptionMap.put("S_DB", m_sSecurityDB);
				 	encrptionMap.put("S_Code", sSocietyCode);				 	
				 	encrptionMap.put("SocietyId", sSocietyId);
				 	
				 	
				 	Calendar calendar = Calendar.getInstance();
			        Timestamp ourJavaTimestampObject = new Timestamp(System.currentTimeMillis());
			        
			        encrptionMap.put("tt",ourJavaTimestampObject);
			        
			        //generate encrypted token
			        String sNewToken = ecryptDecrypt.encrypt(ENCRYPT_SPKEY,ENCRYPT_INIT_VECTOR,encrptionMap.toString());	
			       // System.out.println("sNewToken token :" + sNewToken);
				 
					//generating new token
			        
					if(authDetails(iLoginID, sNewToken) == true)
					{
						bTokenOK = true;
						sMessage = "Validation Successful";
						iGateNo = login.fetchGateNo(iLoginID);	
		        		//System.out.println("GateNo:"+iGateNo);
					}
					else
					{
						sMessage = "Auth failed";
					}
				}
				else
				{
					sMessage = "Token doesn't exist";				
				 	//sMyNewMessage = sMyNewMessage + "Token doesnt exist..... ";
				}
			}
			else
			{
				//Decoded Token Empty.
				sMessage = "Decoded Token Empty.";
				//sMyNewMessage = sMyNewMessage + "Decoded Token Empty..... ";
			}
		}
		else
		{
			sMessage = "Empty Token";
			//sMyNewMessage = sMyNewMessage + "Empty Token..... ";
		}
	 	}
	 	catch (Exception e)
	 	{
	 		
	 		sMessage = "Exception : " + e.getMessage();
	 		
	 	}
		
		if(bTokenOK == true)
		{
			rows2.put("token",sToken);
        	rows2.put("name", sLoginName);
        	rows2.put("message",sMessage);
        	//rows2.put("sMyNewMessage",sMyNewMessage);
        	rows2.put("SecurityDB", m_sSecurityDB);
        	rows2.put("SocietyCode", sSocietyCode);
        	rows2.put("GateNo", iGateNo);
        	rows.put("version", DbConstants.APP_VERSION);
			////System.out.println(rows);
        	rows.put("success","1");
        	rows.put("response",rows2);
		}
		else
		{
			//Given Token Is Empty.
			
			rows.put("success", "4");
			rows2.put("message", sMessage);
        	//rows2.put("sMyNewMessage",sMyNewMessage);
			rows.put("response",rows2);
			rows.put("version", DbConstants.APP_VERSION);
		}
		
		//System.out.println(rows);
		
		return rows;
	}

	public static void main(String[] args) throws Exception
	{
		
		
		Login obj = new Login();
		HashMap objHash = obj.fetchLoginDetails("mag_sec@gmail.com", "mag123");
		System.out.println(objHash);

/*		
		Gson objGson = new Gson();
		String objStr = objGson.toJson(objHash);
//		response.setContentType("application/json");
		String sToken = (String) ((HashMap) objHash.get("response")).get("token");
		CommonBaseClass objCommonBase;
		try 
		{
			objCommonBase = new CommonBaseClass(sToken);
			if(objCommonBase.IsTokenValid())
			{
				System.out.print(objStr);
			}
			else
			{
				objStr = objGson.toJson(objCommonBase.getDecryptedTokenMap());
				System.out.println(objStr);
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/		
		
		/*
		String sToken = "WqErfW3eSh5cVm4q14AgSaYDmS0ZEas4LkF3LL22C4tRi0DEI6ILCy2Wrx04taJuY_H9hLGSOhuvcw7wyvwfuxrMmerx9Eb2oOS_4jYmCj-19kaJsnvD7fuTOmFFwRr-xnW8mREAr3qrchyBc6mbr235Q0qQr73vpmGpCAT8OZ7t9ovr5gJq8EwLo8DHEznL";

		HashMap rows = obj.refreshToken(sToken, "security", "s123");
		
		//HashMap rows = obj.refreshToken("fugbFvnijX6K30ycP6Yfia2QSLS-qA8Wcyffo1wlIIcRvUm7ScjNodyTAFWTt42M3zEqDAuGU0Bt7_UbqU7F6mmKB3f7X-sjLmnYR9qSIbI");

		System.out.println(rows);
*/
	}
	
}
