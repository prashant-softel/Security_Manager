import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import MainSecurity.CommonBaseClass;
import MainSecurity.Login;


/**
 * Servlet implementation class Login
 */
//@WebServlet("/Login")
@WebServlet(urlPatterns= {"/Login/*"})
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "GET");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
	    response.addHeader("Access-Control-Max-Age", "86400");
	    
		boolean bVarified=false;
		JSONObject jsonObject = new JSONObject();
		PrintWriter out = response.getWriter();
//		String strURL = request.getRequestURI();
		Login objLogin=null;
		objLogin =  new Login();
		System.out.println("test Call");
		try 
		{
			String sToken=request.getParameter("token").trim();
			String username = request.getParameter("username").trim();
			String pass= request.getParameter("pass").trim();
			bVarified = true;
			if(sToken.length() > 0)
			{
				HashMap objHash = objLogin.refreshToken(sToken, username, pass);
				Gson objGson1 = new Gson();
				String objStr1 = objGson1.toJson(objHash);
				//out.println("{\"success\":0,\"response\":{\"message\":\"refreshToken     objStr1  : " + objStr1 + "\"}}   ");
				
				String sMessage = (String) ((HashMap) objHash.get("response")).get("message");
				String sStatus = (String) ((HashMap) objHash.get("response")).get("success");
				//out.println("{\"success\":0,\"response\":{\"message\":\"message  : " + sMessage + "\"}}   ");
				if(objHash.get("success") == "1")
				{
					Gson objGson = new GsonBuilder().disableInnerClassSerialization() .create();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
					response.setCharacterEncoding("utf-8");
					out.println(objStr);
					//return;
				}				
				//else if(objHash.get("success") == "Expired")
				//{
				//	out.println("{\"success\":0,\"response\":{\"message\":\"Invalid token or login\"}}   ");
				//	return;
				//}				
				else
				{
					out.println("{\"success\":"+ sStatus + ",\"response\":{\"message\":\"Invalid token or login  status  : " + sStatus + "\"}}   ");
				}
					
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			//call is not for refreshToken
//			out.println("{\"success\":0,\"response\":{\"message\":\"Exception: Invalid Login\"}}   ");
			
		}

		try
		{
//			objLogin =  new Login();
			
			if(bVarified == false)
			{			
				String email = request.getParameter("Email").trim();
				String password = request.getParameter("Password").trim();
//				out.println("{\"success\":0,\"response\":{\"message\":\"Test message email " + email + " password " + password + "\"}}   ");
				
				if(email.equals("")||password.equals(""))
				{
					//out.print("Vacant Field!!");
					out.println("{\"success\":0,\"response\":{\"message\":\"Vacant Field\"}}   ");
				}
				else
				{
					HashMap objHash = objLogin.fetchLoginDetails(email, password);				
					
					Gson objGson = new Gson();
					String objStr = objGson.toJson(objHash);
					response.setContentType("application/json");
//					out.println("{\"success\":0,\"response\":{\"message\":\"return from fetchLoginDetails strURL : " + strURL + "   email : " + email + "  password : " + password + "  objStr : " + objStr + "\"}}   ");
					//String sToken = (String) ((HashMap) objHash.get("response")).get("token");
					out.println(objStr);
			
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			out.println("{\"success\":0,\"response\":{\"message\":\"Exception: Invalid Login\"}}   ");
			
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
