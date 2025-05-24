package MainSecurity;
import java.lang.*;

public final class DbConstants
{
	//All Database related constants
	
	public DbConstants() 
	{
        
	}
	
	public static final String DB_HOST = "localhost:3306";
	public static final String DB_USER = "root";	
	public static final String DB_PASSWORD = "aws123";	//for deployment
    //public static final String DB_PASSWORD = "";		//For local testing
	
	public static final String DB_ROOT_NAME = "hostmjbt_societydb";
//	public String DB_SECURITY_ROOT = "security_rootdb";
	public static String DB_SECURITY_ROOT = "security_rootdb";

//	public String DB_SECURITY = "";

	/*
	public String DB_HOST = "http://way2society.com";
	public final String DB_PASSWORD = "aws123";

	public String DB_ROOT_NAME = "hostmjbt_societydb";
	public String DB_SOCIETY = "";
	public String DB_SECURITY = "";
	public String DB_SECURITY_ROOT = "security_rootdb";
*/	

	public static final String APP_VERSION = "1.0,20200923";
}
