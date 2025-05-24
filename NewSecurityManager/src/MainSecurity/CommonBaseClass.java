package MainSecurity;
import MainSecurity.DbConstants.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import MainSecurity.DbOperations;
import MainSecurity.TokenAuthentication;
import MainSecurity.ProjectConstants;

public class CommonBaseClass 
{
	ProjectConstants m_objProjectConstants;
	DbOperations m_objDbOperations;
	TokenAuthentication m_objTokenAuthentication;
	private boolean m_bIsTokenValid = false;
	private HashMap m_DecryptedTokenMap = new HashMap<>();

	private String m_sToken;
	private String m_SocietyID;
	private String m_LoginID;

	public String DB_ROOT_NAME = "hostmjbt_societydb";
	public String DB_SECURITY_ROOT = "security_rootdb";
	public String DB_SOCIETY = "";
	public String DB_SECURITY = "";
	
	protected  DbOperations dbop_soc_root;		//Society root db	DB_ROOT_NAME
	protected  DbOperations dbop_soc;			//Soc db			DB_SOCIETY
	protected  DbOperations dbop_sec_root;		//Security root db	DB_SECURITY_ROOT
	protected  DbOperations dbop_sec;			//Security db 		DB_SECURITY
	
	public CommonBaseClass(String sToken/*, Boolean bIsVerifyDbDetails, String sTkey*/) //throws ClassNotFoundException 
	{
		m_bIsTokenValid = false;
		this.m_sToken = sToken;
		HashMap<String, String> map = new HashMap<>();
		HashMap rows = new HashMap<>();
		HashMap rows2 = new HashMap<>();
		m_objProjectConstants = new ProjectConstants();
		m_objTokenAuthentication = new TokenAuthentication();
		
		
		//verify token
		System.out.println("CommonBaseClass:Token : " + sToken);	
		 try
		 {
			 map = m_objTokenAuthentication.verifyToken(sToken);//, bIsVerifyDbDetails, sTkey);
			 System.out.println(map);	 
//			 if(map.get("status") == "Expired")
			 if(map.get("status") == "Valid")
			 {
				m_bIsTokenValid = true;
				 //connect to database given tkey
	
				m_SocietyID = (String) map.get("S_Code");
				m_LoginID = (String) map.get("id");
				
				DB_SECURITY = (String) map.get("S_DB");
				System.out.println("SecurityDB :" + DB_SECURITY);

				DB_SOCIETY = (String) map.get("S_DB_Soc");
				System.out.println("SocietyDB :" + DB_SOCIETY);
				rows.put("success","1");
				rows2.put("message","Operation successful");
			 }
			 else if(map.get("status") == "Expired")
			 {				 
				 System.out.println("CommonBaseClass : Token Expired");
				 rows.put("success","0");
				 map.put("message","Token Expired. Please try again");
				//rows2.put("status",map);
			 }
			 else
			 {				 
				 //token invalid
				 System.out.println("CommonBaseClass : Token Invalid");
				 rows.put("success","0");
				 map.put("message","Token Invalid. Please try again");
			 }
		 }
		 catch(Exception e){
			rows.put("success","0");
			map.put("message","Exception occured"); 
		 }
		 		
		 rows.put("response",map);
		 m_DecryptedTokenMap = rows;
	}
	
	public String getLoginID()
	{
		return 	m_LoginID;
	}
	
	public String getSocietyID()
	{
		return m_SocietyID;
	}
	
	public boolean IsTokenValid()
	{
		//return boolean value if IsTokenValid
		return m_bIsTokenValid;
	}
	
	public HashMap<String,String> getDecryptedTokenMap()
	{
		//return decrypted token map 
		return m_DecryptedTokenMap;
	}
	//
	public static void main(String[] args) //throws Exception
	{
		CommonBaseClass c = new CommonBaseClass("H3r8Ve89nBm4SEEYg82Uh5zX13YbvTWczx9-MnoCOYp5eaJqPohDg2l3Iuz00Bl51cH0n3N56SDxfcqvfWfbnuOxadtNOOiH4T0WVLKZbu0lGrQn-LcN4l3-eq4dXLNcasHwJeWJbrDpFpG1o4PQnUB-TAc3_LdmsG27zA_5QjA");
		
		System.out.println("getDecryptedTokenMap :" + c.getDecryptedTokenMap());
		System.out.println("IsTokenValid :" + c.IsTokenValid());
	}
}
