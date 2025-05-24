package MainSecurity;

import static MainSecurity.DbConstants.*;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
public class DbOperations  extends  DbConnection 
{
	public boolean bShowTraces = true;
	//Connection mMysqli = null;
	//public String sDBName = "";
//	static Connection mMysqli;
	public DbOperations(String dbName) throws Exception 
	{
		super(dbName);
		//System.out.println("Database Name " + dbName);
//		this.mMysqli = super.mMysqli;
		//System.out.println("Database name " + sDBName); 
		//mMysqli = super.getDbConnection(sDBName);

		if(this.mMysqli == null)
		{
			System.out.println("mysql connection is null for " + dbName); 
		}
		else
		{
			System.out.println("mysql connected to " + dbName); 
		}
		
	}
	
	private void finalise() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();

	}
	
	public HashMap<Integer, Map<String, Object>> Select(String Sql)
	{
		if(bShowTraces == true)
		{
			System.out.println("Select SQL:" + Sql);
		}
		if(Sql == null || Sql.isEmpty())
		{
			//sql query is empty or null
			System.out.println("Problem In SQL Query!, Your sql query seems wrong. Please check your SQL query.");			
			return null;
		}
		
		HashMap<Integer, Map<String, Object>> rows = new HashMap<Integer, Map<String, Object>>();
		try
		{
			Statement stmt = mMysqli.createStatement();
			ResultSet rs = stmt.executeQuery(Sql);
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			int count = 0;
			
			//add fetched record to map
		    while (rs.next())
		    {
		    	//use object for all types of database table columns datatype  i.e, character,int,float,varchar
			   Map<String, Object> row = new HashMap<String, Object>(columns);        
		       for(int i = 1; i <= columns; ++i)
		       {      
		    	   row.put(md.getColumnLabel(i), rs.getObject(i));        
		       }
		       
		       rows.put(count,row);      
		       count++;
		    }    
		    rs.close();
		    stmt.close();
		}
		catch(Exception e)
		{
			//exception occured 
			Map<String, Object> row = new HashMap<String, Object>(0);    
			row.put("0",e.getMessage());
			rows.put(0,row); 
		
		}
	 return rows;
	}

	public byte[] Selectfingerdata(String Sql) throws SQLException
	{
		byte[] b = null;
		if(bShowTraces == true)
		{
			System.out.println("Select SQL:" + Sql);
		}
		if(Sql == null || Sql.isEmpty())
		{
			//sql query is empty or null
			System.out.println("Problem In SQL Query!, Your sql query seems wrong. Please check your SQL query.");			
			return null;
		}
		
		HashMap<Integer, Map<String, Blob>> rows = new HashMap<Integer, Map<String, Blob>>();
		
			Statement stmt = mMysqli.createStatement();
			ResultSet rs = stmt.executeQuery(Sql);
			ResultSetMetaData md = rs.getMetaData();
			int columns = md.getColumnCount();
			int count = 0;
			//byte[] b;
			//add fetched record to map
		    while (rs.next())
		    {
		    	//use object for all types of database table columns datatype  i.e, character,int,float,varchar
			   Map<String, Blob> row = new HashMap<String, Blob>(columns);        
		       for(int i = 1; i <= columns; ++i)
		       {      
		    	   //row.put(md.getColumnLabel(i), rs.getBlob(i));    
		    	   b=rs.getBytes(i);
		       }
		       
		       rows.put(count,row);      
		       count++;
		    }    
		    rs.close();
		    stmt.close();
		
	 return b;
	}

	
	public long Insert(String Sql) {
		if(bShowTraces == true)
		{
			System.out.println("Insert SQL:" + Sql);
		}
		// TODO Auto-generated method stub
		long rs = 0;
		//HashMap<Integer, Map<String, Object>> rows = new HashMap<Integer, Map<String, Object>>();
		if(Sql == null || Sql.isEmpty())
		{
			//sql query is empty or null
			System.out.println("Problem In SQL Query!,Your sql query seems wrong. Please check your SQL query.");
		}
		else
		{
			
			try
			{
				Statement stmt = mMysqli.createStatement();
				
				rs = stmt.executeUpdate(Sql,Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmt.getGeneratedKeys();
		        if (rs1.next()){
		        	rs=rs1.getInt(1);
		        }
		        rs1.close();

			    stmt.close();
			}
			catch(Exception e)
			{
				System.out.println(e);
				//exception occured 
			}
		}
		 return rs;
			
		}

	public long Update(String Sql) {
		// TODO Auto-generated method stub
		if(bShowTraces == true)
		{
			System.out.println("Update SQL:" + Sql);
		}
		long result = 0;
		//HashMap<Integer, Map<String, Object>> rows = new HashMap<Integer, Map<String, Object>>();
		if(Sql == null || Sql.isEmpty())
		{
			//sql query is empty or null
			System.out.println("Problem In SQL Query!,Your sql query seems wrong. Please check your SQL query.");
		}
		else
		{
			
			try
			{
				Statement stmt = mMysqli.createStatement();
				result = stmt.executeUpdate(Sql,Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmt.getGeneratedKeys();
		        if (rs1.next()){
		        	result=rs1.getInt(1);
		        }
		        rs1.close();

			    stmt.close();
			}
			catch(Exception e)
			{
				System.out.println(e);
				//exception occured 
			}
		}
		 return result;
	}
	
	public void BeginTransaction() throws SQLException {
		mMysqli.setAutoCommit(false);
	}
	
	public void EndTransaction() throws SQLException {
		mMysqli.commit();
	}
	
	public void RollbackTransaction() throws SQLException {
		mMysqli.rollback();
	}

	public void Clear_DB()  throws Throwable {
		super.clear_DB();
		
	}
	
	public static void main(String[] args) throws Exception
	{
		//System.out.println("Problem In SQL Query!,Your sql query seems wrong. Please check your SQL query.");
		//DbOperations obj1 = new DbOperations("");	
		/*
		DbOperations obj2 = new DbOperations(DB_ROOT_NAME);
		System.out.println("New connection "); 
		DbOperations obj3 = new DbOperations(DB_SECURITY_ROOT);
		DbOperations obj4 = new DbOperations(DB_SOCIETY);
		DbOperations obj5 = new DbOperations(DB_SECURITY);
		DbOperations obj21 = new DbOperations(DB_ROOT_NAME);
		
		try
		{
			System.out.println("Before DB clear "); 
			obj21.Clear_DB();
		}
		catch (Throwable e)
		{
			System.out.println("In catch "); 
		
		}
		*/
		System.out.println("Done ! "); 
		//HashMap rows = obj.fetchLoginDetails("prsh3006@yahoo.com", "acmeamay123");
		
		//HashMap rows = obj.refreshToken("eQYLAsJuUQjDhT9-AxH7fU9t34Ea5Ngwqh4JvDhPzkZthG3N6Yg1RucQStquSwo4bkO6FBM911voCAtXQPb4OwvWOgGIuiqzUYrKaFJO46VF5V7ZOw8hhRIM1QfVEM6hWmE-_EXcEkl_YvQ4f27oGQ");
		//System.out.println(rows);
	
	}

}
