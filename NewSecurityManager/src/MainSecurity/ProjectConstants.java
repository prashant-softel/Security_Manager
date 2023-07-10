package MainSecurity;

import java.util.concurrent.TimeUnit;

import javax.crypto.spec.SecretKeySpec;

import SecurityecryptDecryptAlgos.*;

public class ProjectConstants extends EncryptionConstants
{
	/***************
	 * Set projects constant for AES Encryption Decryption
	 */
	
	protected  static  String ENCRYPT_KEY = "WaySocApk**1234*";
	protected static SecretKeySpec ENCRYPT_SPKEY = null;
	protected static  String ENCRYPT_INIT_VECTOR = "WaySocInitVector";
	protected static  float TOKEN_EXPIRATION_TIME_INMILISECONDS = TimeUnit.DAYS.toMillis(1);     // 1 day to milliseconds.
	//protected static  float TOKEN_EXPIRATION_TIME_INMILISECONDS =TimeUnit.MINUTES.toMillis(15);
	
	public ProjectConstants() 
	{
		//set project constants
       EncryptionConstants.setEncryptParameters(ENCRYPT_KEY, ENCRYPT_INIT_VECTOR, TOKEN_EXPIRATION_TIME_INMILISECONDS);
       ENCRYPT_SPKEY = SecurityecryptDecryptAlgos.ecryptDecrypt.generate128BitAesKey(ENCRYPT_KEY);
	}

	
	public static String sTestToken = "TEexg0nIGdYqwR6Xkt1mTzcgauM_LrMi6SgPesb_7Y4QDGFe-vX7r3OLbAe4sLWBH_tbw4kWCg87PQtmt4_5tkfmKUE3x-NbXbntnsuSvPMX7qirvmEhPZKgw-cuL9NJRfCFbg0PTfuTfj-5Adni8Y4WyO3-di3XQIlQ7DLZ2FIRHWPLbWTaoVd4YjukO16B";
	
	
	

}


