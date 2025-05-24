package MainSecurity;

import javax.crypto.spec.SecretKeySpec;

import SecurityecryptDecryptAlgos.KeyAuthentication;

import java.util.HashMap;

public class TokenAuthentication //extends KeyAuthentication
{
	static KeyAuthentication m_objKeyAuthentication;
	
	public TokenAuthentication()
	{
		m_objKeyAuthentication = new KeyAuthentication();
	}
	
	public HashMap<String,String> verifyToken(String sToken)//,Boolean bIsVerifyDbDetails,String sTkey)
	{
		/*
		HashMap<String,String> map = new HashMap<>();
		map = KeyAuthentication.ValidateToken(sToken, true, null);	
		return map;
		*/
		return KeyAuthentication.ValidateToken(sToken, true, null);	
	}
	
	public HashMap<String,String> verifyDbDetails(String sToken,SecretKeySpec sPkey)
	{
		HashMap<String,String> map = new HashMap<>();
		map = KeyAuthentication.ValidateToken(sToken, false, sPkey);
		
		return map;
	}
}
