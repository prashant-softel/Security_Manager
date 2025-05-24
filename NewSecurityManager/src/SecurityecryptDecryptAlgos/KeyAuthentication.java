package SecurityecryptDecryptAlgos;

import javax.crypto.spec.SecretKeySpec;

import java.sql.Timestamp;
import java.util.HashMap;

import MainSecurity.ProjectConstants;
import SecuriyCommonUtility.MapUtility;


public class KeyAuthentication extends ecryptDecrypt
{

	ProjectConstants m_objProjectConstants;
	static SecuriyCommonUtility.MapUtility m_objMapUtility;
	static private String m_SocietyID;
	static private String m_SecurityDB;
	
	public KeyAuthentication() 
	{
		m_objMapUtility = new MapUtility();
	}
	
	
	public static HashMap<String,String> ValidateToken(String key,boolean bUseDefaultConstants,SecretKeySpec Spkey)
	{
		//using default constants
		if(bUseDefaultConstants)
		{	
			Spkey = ENCRYPT_SPKEY;
		}
		
		//decrypting  token
		String decrptedKey = decrypt(Spkey,ENCRYPT_INIT_VECTOR, key);
		System.out.println("decrptedKey  : " + decrptedKey );
		//return 0;
		 HashMap<String,String> map = new HashMap<>();  
		 //MapUtility objComm = new MapUtility();
		 
		 //convert decryptedkey to Hasmap
		 map = MapUtility.stringToHashMap(decrptedKey);
		 
		// map.put("chkVal", decrptedKey);
		 if(map.size() > 0)
		 {
			 String value = map.get("tt");
			 m_SocietyID = map.get("SocietyId");
			 //m_SocietyCode = map.get("S_Code");			 			 
			 m_SecurityDB = map.get("S_DB");			  
			 System.out.println("m_SocietyID  : " + m_SocietyID );
			 System.out.println("m_SecurityDB : " + m_SecurityDB );
			 
			 //check if token timestamp expired
			 boolean bIsTokenExpired = bIsTokenExpired(value);
			 if(bIsTokenExpired)
			 {
				 //return "Invalid";
				 map.put("status", "Expired");
				 System.out.println("Token Expired!");
			 }
			 else
			 {
				 map.put("status", "Valid");
				 System.out.println("Token valid");
				 //return "Valid";
			 } 
		 }
		 else
		 {
			//return "Invalid";
			map.put("status", "Invalid");
			System.out.println("Token Invalid");			 
		 }
		 return map;
	}
	
	
		
	
	public static boolean bIsTokenExpired(String ts)
	{
		// token timestamp with TOKEN_EXPIRATION_TIME_INMILISECONDS for token expiration 
		 Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
		 Timestamp ts2 = Timestamp.valueOf(ts);
		 long tsTime1 = currentTimeStamp.getTime();
		 long tsTime2 = ts2.getTime();
		 long tsdiff = tsTime1 - tsTime2;
		 
		 if (tsdiff > TOKEN_EXPIRATION_TIME_INMILISECONDS) 
		 {
			 //token expired
		     return true;
		 }
		 else
		 {
			 //token valid
			 return false;
			 
		 }
	}
	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		String test = "OEJZdPPGBeNdz2v97A56GOZ3LJJfj9wcoBlE4ouijIRL5kNkgC3vVjsEdRuXnE9LWngrDjuDHscjO/xrD02z2A==";
		HashMap<String,String> map = ValidateToken(test, true,null);
		System.out.println(map );
		
		//System.out.println(map);
	}

}
