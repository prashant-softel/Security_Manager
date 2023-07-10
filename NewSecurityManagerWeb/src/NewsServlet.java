
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

//import MainSecurity.CommonBaseClass;
import MainSecurity.News;

/**
 * Servlet implementation class NewsServlet
 */
@WebServlet("/News/*")
public class NewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String projectUrl = "/NewSecurityManagerWeb";
	//CommonBaseClass objCommonBase;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "GET");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
	    response.addHeader("Access-Control-Max-Age", "86400");
	    
		String sToken = request.getParameter("token").trim();
		String str = request.getRequestURI();
		JSONObject jsonObject = new JSONObject();
		PrintWriter out = response.getWriter();
		News newsObj=null;
		try 
		{
			newsObj =  new News(sToken);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		try 
		{
			//objCommonBase = new CommonBaseClass(sToken);
			if(str.equals(projectUrl+"/News/UpdateLoginDatalogout"))
			{
				String email = request.getParameter("Email").trim();
				String password = request.getParameter("Password").trim();
				HashMap objHash = newsObj.updatelogout(email, password);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				
				out.println(objStr);
			}
			if(newsObj.IsTokenValid() == true)
			{
				/*HashMap DecryptedTokenMap = objCommonBase.getDecryptedTokenMap();
				String userid =(String) ((HashMap) DecryptedTokenMap.get("response")).get("id");
				int userID=Integer.valueOf(userid);*/
				String sLoginID = newsObj.getLoginID();
				int userID=Integer.parseInt(sLoginID);
				
				
				if(str.equals(projectUrl+ "/News/insert"))
				{
					String title = "";
					String desc = "";
					title = request.getParameter("title").trim();
					desc = request.getParameter("desc").trim();
					
					HashMap objHash = newsObj.updateNews(userID,title,desc);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else if (str.equals(projectUrl+ "/News/getHome"))
				{
					HashMap objHash = newsObj.getHomePageNews();
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else if(str.equals(projectUrl+"/News/getAll"))
				{
					HashMap objHash = newsObj.getAllNews();
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else if(str.equals(projectUrl+"/News/GetContactNo"))
				{
					int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
					//System.out.println("SOcietyID"+iSocietyID );
					HashMap objHash = newsObj.getSocietyContact(iSocietyID);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else if(str.equals(projectUrl+"/News/OtpFlag"))
				{
					int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
					//System.out.println("SOcietyID"+iSocietyID );
					HashMap objHash = newsObj.getEnableOTPFlag(iSocietyID);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else if(str.equals(projectUrl+"/News/getSOS"))
				{
					//int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
					//System.out.println("SOcietyID"+iSocietyID );
					HashMap objHash = newsObj.getSOSAlert();
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else if(str.equals(projectUrl+"/News/ShowAlert"))
				{
					int iAlertID= Integer.parseInt(request.getParameter("AlertID").trim());
					//System.out.println("SOcietyID"+iSocietyID );
					HashMap objHash = newsObj.ShowAlert(iAlertID);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else if(str.equals(projectUrl+"/News/ResolvedAlert"))
				{
					int iAlertID= Integer.parseInt(request.getParameter("AlertID").trim());
					String sLoginName=request.getParameter("LoginName").trim();
					String sRole= request.getParameter("Role").trim();
					String sGate= request.getParameter("GateNo").trim();
					//int SubmitType= Integer.parseInt(request.getParameter("SubType").trim());
					//System.out.println("SOcietyID"+iSocietyID );
					HashMap objHash = newsObj.ResolvedIssues(iAlertID,sLoginName,sRole,sGate);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else if(str.equals(projectUrl+"/News/CheckResolvedStatus"))
				{
					int iAlertID= Integer.parseInt(request.getParameter("AlertID").trim());
					HashMap objHash = newsObj.CheckResolvedStatus(iAlertID);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					out.println(objStr);
				}
				else if(str.equals(projectUrl+"/News/UpdateLoginData"))
				{
					String email = request.getParameter("Email").trim();
					String password = request.getParameter("Password").trim();
					String device_id = request.getParameter("deviceId").trim();
					HashMap objHash = newsObj.updatelogin(email, password,device_id);
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					
					out.println(objStr);
				}
				
				
			}
			
			else
			{
				out.println("{\"success\":0,\"response\":{\"message\":\"Invalid Token\"}}  ");
			}
		}
		catch(Exception e)
		{
			out.println("{\"success\":0,\"response\":{\"message\":\"request failed\"}} Exception : "+e);
		}
		
		
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
