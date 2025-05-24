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

import MainSecurity.CommonBaseClass;
import com.google.gson.Gson;


import MainSecurity.Staff;

/**
 * Servlet implementation class StaffServlet
 */
@WebServlet("/Staff/*")
public class StaffServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String projectUrl = "/NewSecurityManagerWeb"; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StaffServlet() {
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
	    
			
	    JSONObject jsonObject = new JSONObject();  	
	    PrintWriter out = response.getWriter();  	
	    String str = request.getRequestURI();
	     //	System.out.println(str);
			Staff objStaff=null;
			try
			{
				String sToken = request.getParameter("token").trim();
				//objCommonBase = new CommonBaseClass(sToken);
				objStaff = new Staff(sToken);
				
				
				if(objStaff.IsTokenValid() == true)
				{
				
					if(str.equals(projectUrl+"/Staff/fetch")) 
					{
						int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
						//send to societyID------------------------------------------------------------------------------
						HashMap objHash = objStaff.getStaffDetails(iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);					
					}
					else if(str.equals(projectUrl+"/Staff/Addtstaff"))
					{
						//System.out.println("Call To Add Staff Function");
						
						String Fname ="";
						String Lname = "";
						String gender= "";
						String mob = "";
						String img = "";
						String job= "";
						String Dob= "";
						int unit= 0;
						int doc=0;
						String join_date = "";
						String work_hours = "";
						
						
						Fname = request.getParameter("sFname").trim();
						Lname = request.getParameter("sLname").trim();
						mob = request.getParameter("sMob").trim();
						gender = request.getParameter("sGender").trim();
					    //img = request.getParameter("img").trim();
						job = request.getParameter("sJobProfile").trim();
						unit = Integer.parseInt(request.getParameter("unit_id").trim());
						doc =  Integer.parseInt(request.getParameter("document_id").trim());
						//join_date= request.getParameter("sJoin_date").trim();
						work_hours =request.getParameter("sWork_hours").trim();
						Dob =request.getParameter("sDob").trim();
						HashMap objHash = objStaff.mNewAddSatff( Fname, Lname, gender, mob, img, job, unit,doc, join_date, work_hours,Dob);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
					else if(str.equals(projectUrl+"/Staff/fetchSocietyStaff"))
					{
						//int EntryStaffID =0;
						String StaffID="";
						String ContactNo="";
						String FingerISO= "";
						int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
						//System.out.println("SOcietyID :" + iSocietyID);
						if(request.getParameter("StaffID") != "" && request.getParameter("contactNo") !="")
						{
						  StaffID="";
						  ContactNo =  request.getParameter("contactNo").trim();
						}
						else if(request.getParameter("StaffID") != "" && request.getParameter("contactNo") =="")
						{
							StaffID =  request.getParameter("StaffID").trim();
						}
						else if(request.getParameter("StaffID") == "" && request.getParameter("contactNo") =="")
						{
							FingerISO=request.getParameter("fingerISO").toString();
						}
						else
						{
							ContactNo =  request.getParameter("contactNo").trim();
						}
						
						
						HashMap objHash = objStaff.fetchStaff(ContactNo,StaffID,iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
					else if(str.equals(projectUrl+"/Staff/fetchSocietyStaff1"))
					{
						//int EntryStaffID =0;
						String StaffID="";
						String ContactNo="";
						String FingerISO= "";
						int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
						//System.out.println("SOcietyID :" + iSocietyID);
						if(request.getParameter("StaffID") != "" && request.getParameter("contactNo") !="")
						{
						  StaffID="";
						  ContactNo =  request.getParameter("contactNo").trim();
						}
						else if(request.getParameter("StaffID") != "" && request.getParameter("contactNo") =="")
						{
							StaffID =  request.getParameter("StaffID").trim();
						}
						else if(request.getParameter("StaffID") == "" && request.getParameter("contactNo") =="")
						{
							FingerISO=request.getParameter("fingerISO").toString();
						}
						else
						{
							ContactNo =  request.getParameter("contactNo").trim();
						}
						
						
						HashMap objHash = objStaff.fetchStaff1(ContactNo,StaffID,iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
					else if(str.equals(projectUrl+"/Staff/fetchStaffSyncronise"))
					{
						int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
						
						HashMap objHash = objStaff.fetchStaffSyncronise(iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr); 
					}
					
					
				else if(str.equals(projectUrl+"/Staff/fetchStaffCheckIn"))
					{
						String EntryStaffID = request.getParameter("staffId").trim();
						
						//String StaffID="";
						String ContactNo="";
						int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
						HashMap objHash = objStaff.fetchStaff1(ContactNo,EntryStaffID,iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
					else if(str.equals(projectUrl+"/Staff/AllStaffList"))
					{
						//int EntryStaffID = Integer.parseInt(request.getParameter("staffId").trim());
						int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
						String StaffID="";
						HashMap objHash = objStaff.fetchAllStaff(iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
					else if(str.equals(projectUrl+"/Staff/StaffProvider"))
					{
						//int EntryStaffID = Integer.parseInt(request.getParameter("staffId").trim());
						int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
						int iService_prd_reg_id= Integer.parseInt(request.getParameter("ProviderID").trim());
						String StaffID="";
						HashMap objHash = objStaff.fetchStaffProvider(iService_prd_reg_id,iSocietyID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
					else if(str.equals(projectUrl+"/Staff/StaffEntry"))
					{
						int ProviderID = Integer.parseInt(request.getParameter("ProviderID").trim());
						int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
						int iGateEntry= Integer.parseInt(request.getParameter("EntryGate").trim());
						
						float Temp = Float.parseFloat(request.getParameter("temp").trim());
						float Oxygen = Float.parseFloat(request.getParameter("oxygen").trim());
						int Pulse= Integer.parseInt(request.getParameter("pulse").trim());
						
						String counter = "0";
						if(request.getParameterMap().containsKey("counter"))
						{
							counter = request.getParameter("counter").toString();
						}
						String profile=request.getParameter("Profile").trim();
						String pNote="";
						pNote=request.getParameter("pNote").trim();
						HashMap objHash = objStaff.markEntry(ProviderID,iSocietyID,profile,iGateEntry,pNote,counter,Temp,Oxygen,Pulse);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
					else if(str.equals(projectUrl+"/Staff/StaffExit"))
					{
						int ProviderID = Integer.parseInt(request.getParameter("ProviderID").trim());
						int iGateExit= Integer.parseInt(request.getParameter("ExitGate").trim());
						
						HashMap objHash = objStaff.markExit(ProviderID,iGateExit);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
					else if(str.equals(projectUrl+"/Staff/fetchStaffReportDetails"))
					{
						
						int StaffID = Integer.parseInt(request.getParameter("StaffID").trim());
						String StartDate=request.getParameter("startDate").trim();
						String EndDate=request.getParameter("EndtDate").trim();
						//int Count=0 ;
						HashMap objHash = objStaff.fetchStaffReports(StaffID,StartDate,EndDate);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}else if(str.equals(projectUrl+"/Staff/fetchLatestReport"))
					{
						
						int StaffID = Integer.parseInt(request.getParameter("StaffID").trim());
						
						int Count=5;
						HashMap objHash = objStaff.fetchStaffLetestReports(StaffID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
					else if(str.equals(projectUrl+"/Staff/StaffEntryWelcome"))
					{
						
						int StaffEntryID = Integer.parseInt(request.getParameter("StaffEntryId").trim());
						int StaffID = Integer.parseInt(request.getParameter("StaffId").trim());
						HashMap objHash = objStaff.fetchWelcomeStaff(StaffEntryID,StaffID);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);	
					}
					else if(str.equals(projectUrl+"/Staff/insertFingerData"))
					{
					
						
						//byte[] fingerISO=request.getParameter("fingerISO").getBytes();
						String fingeriso=request.getParameter("fingerISO").toString();
						String StaffID =  request.getParameter("StaffID").trim();
						int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
						
						//System.out.println("fingerISO  : "+fingeriso);
						// String FingerServer=obj.getString("fingerISO");
			               // byte[] data = fingeriso.getBytes("UTF-8");
			               // byte[] fingerISO = Base64.decodeBase64(fingeriso);
			                //String text = new String(data, "UTF-8");
						//fingeriso = "["+fingeriso;
						//byte[] fingerISO=fingeriso.getBytes();
					
						//System.out.println("fingerISO1 :: "+fingerISO);
						HashMap objHash =objStaff.insertfingerdata(StaffID,iSocietyID,fingeriso);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
