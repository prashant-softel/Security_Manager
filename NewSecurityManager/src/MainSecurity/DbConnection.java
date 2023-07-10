package MainSecurity;

import static MainSecurity.DbConstants.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class DbConnection 
{
	//public static Connection mBasesqli;
	//public Connection mMysqliRoot;
	//public Connection mMyW2Ssqli;
	//public Connection mMyW2SsqliRoot;
	//public static boolean bIsConnected = false;
	//public static String dbConnect = ""; 
	//public static String sConnErrorMsg ="";
	//boolean canConnect=false;

	/*
	public String DB_HOST = "localhost:3306/";
	private String DB_USER = "root";	
	private String DB_PASSWORD = "";
	public String DB_HOST = "localhost";
	private String DB_USER = "root";	
	private String DB_PASSWORD = "aws123";
	private String DB_ROOT_NAME = "hostmjbt_societydb";
*/
	
	protected Connection mMysqli;

	public static  HashMap<String, Connection>  mDBConnectionMap = new HashMap<>();
	
	public DbConnection(String dbName) throws Exception
	{
//		System.out.println("DbConnection::DbConnection");
		//Connection sqliConnection = null;
		if(dbName == "")
		{
			//dbName = DB_ROOT_NAME;
			System.out.println("Empty database name");
			throw new Exception("Database " + dbName + " not found.");
			//return;
		}
		//System.out.println("Database Name " + dbName);
		int No_Of_Connections = mDBConnectionMap.size();
		Class.forName("com.mysql.cj.jdbc.Driver");
		mMysqli = (Connection) mDBConnectionMap.get(dbName);
		//System.out.println("In No_Of_Connections " + No_Of_Connections);
		System.out.println("Connections " + No_Of_Connections + ". Database Name " + dbName + " Connection Object Status : " + mMysqli);
		if(mMysqli == null)
		{
			/*******connect to society database*********/
			//sConnErrorMsg +="2";
			
			try 
			{
				System.out.println(dbName);
				//mMysqli = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName + "?zeroDateTimeBehavior=convertToNull&serverTimezone=UTC",DB_USER,DB_PASSWORD);
				mMysqli = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + "/" + dbName + "?zeroDateTimeBehavior=convertToNull&serverTimezone=UTC&autoReconnect=true&tcpKeepAlive=true",DB_USER,DB_PASSWORD);
				mDBConnectionMap.put(dbName, mMysqli);
				//System.out.println("Created new dbconn " + dbName + " mMysqli db<" + mMysqli + ">");
				System.out.println("Created new dbconn " + dbName);
			} 
			catch (SQLException e) 
			{
				System.out.println("Connection failed to db<" + dbName + ">");
				//sConnErrorMsg +="4";
				e.printStackTrace();
				//sConnErrorMsg += "object not created if part";
			}
		}
		else
		{
			//System.out.println("Found existing connection to " + dbName );
		}
		//list_DB();
	}

//		@Override
		public void clear_DB() throws SQLException {
			return; //need to implement
			
			// TODO Auto-generated method stub
			//mBasesqli.close();
			//Iterate over all connection and close
			/*
			int iIndex = 0;
			System.out.println("In finalize");
			int No_Of_Connections = mDBConnectionMap.size();
			System.out.println("In No_Of_Connections " + No_Of_Connections);
			for(Object entry2 : mDBConnectionMap.entrySet()) 
			{
				System.out.println("Index " + iIndex);
				String dem[]=entry2.toString().split("=");
				System.out.println("Entry " + dem[0]);
				//get connection from the map
				
				//Connection sqliConnection =DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dem[0] + "?zeroDateTimeBehavior=convertToNull&serverTimezone=UTC",DB_USER,DB_PASSWORD);//= (Connection)entry2;		
				sqliConnection.close();
				System.out.println("closing mysql connection to " + sqliConnection.toString()); 
			
				//Pending : remove entry from the collection
				iIndex ++;
				
			}
			mDBConnectionMap.clear();
			//super.finalize();
			 
			 */
		
	}
		
	public void list_DB()  {
			// TODO Auto-generated method stub
			//mBasesqli.close();
			//Iterate over all connection and close
			int iIndex = 0;
			System.out.println("In list_db");
			int No_Of_Connections = mDBConnectionMap.size();
			System.out.println("No_Of_Connections " + No_Of_Connections);
			System.out.println("mDBConnectionMap " + mDBConnectionMap);
			for(Entry<String, Connection> entry2 : mDBConnectionMap.entrySet()) 
			{
				System.out.println("entry2 " + entry2);
				//databaseName =entry.getValue();

				//Pending : remove entry from the collection
				iIndex ++;
				
			}
		
	}
	
	
}
