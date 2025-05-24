
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;

import org.json.JSONObject;

import com.google.gson.Gson;
import MainSecurity.CommonBaseClass;
import MainSecurity.CheckPost;

@WebServlet("/CheckPost/*")
public class CheckPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String projectUrl = "/NewSecurityManagerWeb";
	//CommonBaseClass objCommonBase;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckPostServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	 System.out.println("Call FUnction");

		response.addHeader("Access-Control-Allow-Origin", "*");  	
		response.addHeader("Access-Control-Allow-Methods", "GET");  	
		response.addHeader("Access-Control-Allow-Headers", "Content-Type");  	
		response.addHeader("Access-Control-Max-Age", "86400");  
		
		JSONObject jsonObject = new JSONObject();  	
	    PrintWriter out = response.getWriter();  	
	    String str = request.getRequestURI();
	     System.out.println(str);
	      CheckPost objsecurity=null;
			try
			{
				String sToken = request.getParameter("token").trim();
				//objCommonBase = new CommonBaseClass(sToken);
				objsecurity = new CheckPost(sToken);
				
				
				if(objsecurity.IsTokenValid() == true)
				{
				
				      if(str.equals(projectUrl+"/CheckPost/AddSecurityRound"))
					{
						System.out.println("Call To Add Staff Function");
						int society_id = 0;
						int schedule_id=0;
						int round_id=0;
						String create_by = "";
						String logdata="";
						int status = 1;
						society_id = Integer.parseInt(request.getParameter("societyid").trim());
						schedule_id = Integer.parseInt(request.getParameter("schedule_id").trim());
						create_by = request.getParameter("username").trim();
						round_id = Integer.parseInt(request.getParameter("round_id").trim());
						logdata = request.getParameter("Data").trim();
						//status = Integer.parseInt(request.getParameter("status").trim());
						HashMap objHash = objsecurity.AddSecurityRound(society_id, create_by, status,schedule_id,round_id,logdata);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
				    else if(str.equals(projectUrl+"/CheckPost/GetRoundList"))
					{
							
							HashMap objHash = objsecurity.GetRoundList();
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);	
					}
			     else if(str.equals(projectUrl+"/CheckPost/AddCheckPost"))
 				{
 					//System.out.println("Call To Add Staff Function");
 					int sround_id = 0;
 					int post_no = 0;
 					int gate_no = 0;
 					int check_post_status = 0;
 					int schedule_id =0;
 					String login_id="";
 					String code = "";
 					login_id = request.getParameter("login_id").trim();
 					schedule_id = Integer.parseInt(request.getParameter("schedule_id").trim());
 					sround_id = Integer.parseInt(request.getParameter("sround_id").trim());
 					post_no = Integer.parseInt(request.getParameter("post_no").trim());
 					gate_no = Integer.parseInt(request.getParameter("gate_no").trim());
 					check_post_status = Integer.parseInt(request.getParameter("check_post_status").trim());
 					code = request.getParameter("code").trim();
 					HashMap objHash = objsecurity.AddCheckPost(login_id,sround_id,post_no,schedule_id,gate_no, check_post_status,code);
 					Gson objGson = new Gson();
 					String objStr = objGson.toJson(objHash);
 					response.setContentType("application/json");
 					out.println(objStr);	
 				}
				      
				      // old function modification of data listing 
			    /*else if(str.equals(projectUrl+"/CheckPost/GetCheckpost"))
 				{
 					System.out.println("Call To Add Staff Function");
			    	int cType = 0;  // 0 = upcomming and 1-ongoing 
			    	int checkpostID = 0;
			    	cType = Integer.parseInt(request.getParameter("type").trim());
			    	checkpostID = Integer.parseInt(request.getParameter("checkpostID").trim());
			    	System.out.println("Type"+cType);
 					HashMap objHash = objsecurity.GetCheckpost(cType,checkpostID);
 					Gson objGson = new Gson();
 					String objStr = objGson.toJson(objHash);
 					response.setContentType("application/json");
 					out.println(objStr);	
 				}*/
			    else if(str.equals(projectUrl+"/CheckPost/GetCheckpost"))
 				{
 					//System.out.println("Call To Add Staff Function");
			    	int cType = 0;  // 0 = upcomming and 1-ongoing 
			    	int checkpostID = 0;
			    	cType = Integer.parseInt(request.getParameter("type").trim());
			    	//checkpostID = Integer.parseInt(request.getParameter("checkpostID").trim());
			    	System.out.println("Type"+cType);
 					HashMap objHash = objsecurity.GetCheckpost(cType);
 					Gson objGson = new Gson();
 					String objStr = objGson.toJson(objHash);
 					response.setContentType("application/json");
 					out.println(objStr);	
 				}
			     else if(str.equals(projectUrl+"/CheckPost/AddScheduleRound"))
	 			{
	 					//System.out.println("Call To Add Staff Function");
	 					int master_id = 0;
	 					String round_type ="";
	 					int status  = 0;
	 					
	 					master_id = Integer.parseInt(request.getParameter("master_id").trim());
	 					round_type = request.getParameter("round_type").trim();
	 					status = Integer.parseInt(request.getParameter("status").trim());
	 					HashMap objHash = objsecurity.AddScheduleRound(master_id,round_type,status);
	 					Gson objGson = new Gson();
	 					String objStr = objGson.toJson(objHash);
	 					response.setContentType("application/json");
	 					out.println(objStr);	
	 				}
				     else if(str.equals(projectUrl+"/CheckPost/GetScheduleRound"))
	 				{
	 					//System.out.println("Call To Add Staff Function");
						
	 					HashMap objHash = objsecurity.GetScheduleRound();
	 					Gson objGson = new Gson();
	 					String objStr = objGson.toJson(objHash);
	 					response.setContentType("application/json");
	 					out.println(objStr);	
	 				}
				    else if(str.equals(projectUrl+"/CheckPost/GetMasterData"))
		 			{
		 				HashMap objHash = objsecurity.getGateMaster();
		 				Gson objGson = new Gson();
		 				String objStr = objGson.toJson(objHash);
		 				response.setContentType("application/json");
		 				out.println(objStr);	
		 			}
				    else if(str.equals(projectUrl+"/CheckPost/GetCheckPostDetails"))
		 			{
				    	int ScheduleId = 0;
				    	int RoundId = 0;
				    	ScheduleId = Integer.parseInt(request.getParameter("ScheduleID").trim());
				    	RoundId = Integer.parseInt(request.getParameter("RoundID").trim());
		 				HashMap objHash = objsecurity.GetCheckpostDetails(ScheduleId,RoundId);
		 				Gson objGson = new Gson();
		 				String objStr = objGson.toJson(objHash);
		 				response.setContentType("application/json");
		 				out.println(objStr);	
		 			} 
				    else if(str.equals(projectUrl+"/CheckPost/GetScheduleList"))
		 			{
				    	//int checkpostID = 0;
				    	//checkpostID = Integer.parseInt(request.getParameter("CheckpostID").trim());
		 				HashMap objHash = objsecurity.GetScheduleList();
		 				Gson objGson = new Gson();
		 				String objStr = objGson.toJson(objHash);
		 				response.setContentType("application/json");
		 				out.println(objStr);	
		 			} 
				    else if(str.equals(projectUrl+"/CheckPost/QRCodeMatch"))
		 			{
				    	String QRCode ="";
				    	int CheckpostID =0;
				    	QRCode = request.getParameter("QRCode").trim();
				    	//CheckpostID = request.getParameter("CheckpostId").trim();
				    	CheckpostID = Integer.parseInt(request.getParameter("CheckpostId").trim());
				    	//RoundId = Integer.parseInt(request.getParameter("RoundID").trim());
		 				HashMap objHash = objsecurity.GetQRCodeMatched(QRCode,CheckpostID);
		 				Gson objGson = new Gson();
		 				String objStr = objGson.toJson(objHash);
		 				response.setContentType("application/json");
		 				out.println(objStr);	
		 			}   
				   
				   
		      }   
				else
				{
					
					out.println("invalid token");
				}
			
		}
			
			
			catch(Exception e)
			{
				out.println("{\"success\":0,\"response\":{\"message\":\"request failed\"}} Exception : "+e);
			}
	
	}

    
    
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}

}
