import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import MainSecurity.CommonBaseClass;
import MainSecurity.OTP;
import MainSecurity.Visitor;

/**
 * Servlet implementation class OTPServlet
 */
@WebServlet(urlPatterns= {"/OTP/send","/OTP/sendapi","/OTP/verify","/OTP/submitWithoutOTP","/OTP/submitNewWithoutOTP","/OTP/SetGateNo","/OTP/FetchGateNo","/OTP/OTPDisable","/OTP/OTPDisableNew","/OTP/NewAPPsubmitWithoutOTP","/OTP/UnitApproval","/OTP/UnitApprovalnew","/OTP/UnitApprovalData","/OTP/SubmitUnit","/OTP/checkStatus"})
public class OTPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String projectUrl = "/NewSecurityManagerWeb";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OTPServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
//		CommonBaseClass objCommonBase;
		//Integer iloginID = new Integer(0);
		response.addHeader("Access-Control-Allow-Origin", "*");
	    response.addHeader("Access-Control-Allow-Methods", "GET");
	    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
	    response.addHeader("Access-Control-Max-Age", "86400");
	    
		JSONObject jsonObject = new JSONObject();
		PrintWriter out = response.getWriter();
		
		String sToken = request.getParameter("token").trim();
		String str = request.getRequestURI();
		OTP objOTP =  new OTP(sToken);
		
		try 
		{
			//objCommonBase = new CommonBaseClass(sToken);
			
			
			
			if(objOTP.IsTokenValid() == true)
			{
				/*
				HashMap DecryptedTokenMap = objCommonBase.getDecryptedTokenMap();
				//iloginID = Integer.parseInt((String) DecryptedTokenMap.get("id"));
				String userid =(String) ((HashMap) DecryptedTokenMap.get("response")).get("id");
				int userID=Integer.valueOf(userid);
				*/
				//System.out.println("USER ID  :"+userID);
					
					//---------------sending otp -------------------------------
					if(str.equals(projectUrl+"/OTP/send"))
					{
					
							//String Fname = "";
							//String Lname = ""; 
							//String mobNo = "";
							//String SocietyName= "";
							//int docId ;
							String docNo = "";
							String Fname = request.getParameter("vFName").trim();
							String Lname = request.getParameter("vLName").trim();
							String mobNo = request.getParameter("vmobNumber").trim();
							String SocietyName=request.getParameter("SocietyName").trim();
//							docNo = request.getParameter("docNo").trim();
//							docId = Integer.parseInt((String)request.getParameter("docId").trim());
							//HashMap objHash = OTP.createVisitor(userID, Fname, Lname, mobNo);
							String sLoginID = objOTP.getLoginID();
							int userID=Integer.parseInt(sLoginID);
							//int MyID = 4;

							HashMap objHash = objOTP.createVisitor(userID, mobNo, SocietyName);
							if(Fname.equals("")|| Lname.equals("")|| mobNo.equals(""))
							{
								out.println("{\"success\":0,\"response\":{\"message\":\"Vacant Field\"}}   ");
							}
							else
							{
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
							}
					
					}
					
					//----------Send Otp via API-------------------------
					if(str.equals(projectUrl + "/OTP/sendapi"))
					{
						
							String Fname = "";
							String Lname = ""; 
							String mobNo = "";
							String SocietyName= "";
							int docId ;
							String docNo = "";
							Fname = request.getParameter("vFName").trim();
							Lname = request.getParameter("vLName").trim();
							mobNo = request.getParameter("vmobNumber").trim();
							SocietyName=request.getParameter("SocietyName").trim();
//							docNo = request.getParameter("docNo").trim();
//							docId = Integer.parseInt((String)request.getParameter("docId").trim());
							//HashMap objHash = OTP.createVisitor(userID, Fname, Lname, mobNo);
							HashMap objHash = objOTP.createVisitorapi(mobNo,SocietyName);
							if(Fname.equals("")|| Lname.equals("")|| mobNo.equals(""))
							{
								out.println("{\"success\":0,\"response\":{\"message\":\"Vacant Field\"}}   ");
							}
							else
							{
								Gson objGson = new Gson();
								String objStr = objGson.toJson(objHash);
								response.setContentType("application/json");
								out.println(objStr);
							}
					
					}
					//  ------------------Submit without otp =--------------------- 
					else if(str.equals(projectUrl+"/OTP/submitWithoutOTP"))
					{
						
						Visitor visit=new Visitor(sToken);
						String company = "";
						String vehicle="";
						String verified="";
						String Fname = "";
						String Lname = ""; 
						String mobNo = "";
						String docNo = "";
						String vNote = "";
						String unit="";
						String approvedflag="";
						int purpose=0;
						int VisitorEntryID =0;
						VisitorEntryID=Integer.parseInt(request.getParameter("VisitorEntryID").trim());
						Fname = request.getParameter("vFName").trim();
						Lname = request.getParameter("vLName").trim();
						mobNo = request.getParameter("vmobNumber").trim();
						vNote = request.getParameter("vNote").trim();
						int EntryGateNo=Integer.parseInt(request.getParameter("Gate_no").trim());
						
						//vID=Integer.parseInt(request.getParameter("vID").trim());
						company=request.getParameter("vCompany").trim();
						vehicle=request.getParameter("vVehicle").trim();
						unit=request.getParameter("unit_id").trim();
						approvedflag=request.getParameter("ApprovalFlag").trim();
					//	System.out.println("hello");
						//unit=Integer.parseInt(request.getParameter("unit_id").trim());
						purpose=Integer.parseInt(request.getParameter("purpose_id").trim());
					//	verified=request.getParameter("isVerified").trim();
					
						int iSubmitFlag =0;//Integer.parseInt(request.getParameter("SubmitFlag").trim()); 
		
						
						//{
							
						if(company.equals("other"))
						{
							
							company= request.getParameter("vCompanyOther").trim();
							HashMap objHash = visit.UpdateCompany(company,purpose);
						}
						else
						{
							company=request.getParameter("vCompany").trim();
							
						}
						//System.out.println("company :"+company);
						//System.out.println( "Reuslt Of WIthout Otp userID :" +userID+ "Fname :" +Fname+ "Lname : " +Lname+ "mobNo :" +mobNo+ "company:" +company+ "vehicle:" +vehicle+ "unit:" +unit+ "purpose:" +purpose);
						int EntryDoc_id = Integer.parseInt(request.getParameter("EntryDoc_id").trim());

/*
						objCommonBase = new CommonBaseClass(sToken);
						
						String socid =(String) ((HashMap) DecryptedTokenMap.get("response")).get("SocietyId");
						*/
						String socid = objOTP.getSocietyID();
						String sLoginID = objOTP.getLoginID();
						int userID=Integer.parseInt(sLoginID);

						//System.out.println("userID  :"+userID);
						float Temp=0;
						float Oxygen=0;
						int Pulse=0;
						
						HashMap<Integer, Map<String, Object>>  objHash = objOTP.submitWithout(userID,Fname,Lname,mobNo,unit,purpose,company,vehicle,docNo,VisitorEntryID,EntryGateNo,vNote,socid,EntryDoc_id,Temp,Oxygen,Pulse);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						Map<String, Object> mapResponse = objHash.get("response");
						String visitorID= mapResponse.get("VisitorID").toString();
						
						VisitorEntryID = (int) Long.parseLong(visitorID);
						HashMap<Integer, Map<String, Object>>  objHash1 = objOTP.submitapproval(VisitorEntryID,unit,approvedflag);
						
						//String objStr1 = objGson.toJson(objHash1);
						response.setContentType("application/json");
						out.println(objStr);
						//System.out.println(objStr);					
					}
					else if(str.equals(projectUrl+"/OTP/submitNewWithoutOTP"))
					{
						Visitor visit=new Visitor(sToken);
						String Fname="";
						String Lname="";
						String Mobile="";
						String doc_img="";
						String docNo="";
						String company = "";
						String vehicle="";
						String verified="";
						String img="";
						String vNote="";
						String unit="";
						String approvedflag="";
						int purpose=0;
						int vID=0;
						//int Doc_id=0;
						int VisitorEntryID =0;
						
						float Temp = Float.parseFloat(request.getParameter("temp").trim());
						float Oxygen = Float.parseFloat(request.getParameter("oxygen").trim());
						int Pulse= Integer.parseInt(request.getParameter("pulse").trim());
						
						int EntryGateNo=Integer.parseInt(request.getParameter("Gate_no").trim());
						VisitorEntryID=Integer.parseInt(request.getParameter("VisitorEntryID").trim());
						vID=Integer.parseInt(request.getParameter("vID").trim());
						company=request.getParameter("vCompany").trim();
						vehicle=request.getParameter("vVehicle").trim();
						unit=request.getParameter("unit_id").trim();
						approvedflag=request.getParameter("ApprovalFlag").trim();
						//unit=Integer.parseInt(request.getParameter("unit_id").trim());
						purpose=Integer.parseInt(request.getParameter("purpose_id").trim());
						//verified=request.getParameter("isVerified").trim();
						Fname=request.getParameter("vFName").trim();
						Lname=request.getParameter("vLName").trim();
						Mobile=request.getParameter("vmobNumber").trim();
						vNote=request.getParameter("vNote").trim();
						int Doc_id=Integer.parseInt(request.getParameter("Doc_id").trim());
						docNo=request.getParameter("Doc_no").trim();
						//int iSubmitFlag =Integer.parseInt(request.getParameter("SubmitFlag").trim()); 
						if(company.equals("other"))
						{
							company= request.getParameter("vCompanyOther").trim();
							HashMap objHash = visit.UpdateCompany(company,purpose);
						}
						else
						{
							company=request.getParameter("vCompany").trim();
							
						}
						//System.out.println("company :"+company);
						int EntryDoc_id = Integer.parseInt(request.getParameter("EntryDoc_id").trim());

						if(VisitorEntryID == 0)
						{
							
							HashMap<Integer, Map<String, Object>> ServiceReqID = visit.createNewVisitor(Fname,Lname,Mobile,doc_img,docNo,company,img,purpose,Doc_id,EntryDoc_id);
							Map<String, Object> mapResponse = ServiceReqID.get("response");
							String sServiceProRegID= mapResponse.get("VisitorId").toString();
							VisitorEntryID = (int) Long.parseLong(sServiceProRegID);
							 
						}
						//System.out.println("VisitorEntryID"+VisitorEntryID);
						//System.out.println("Visitor ID :" +vID+ "Company :" +company+ "vehicle :" +vehicle+ "unit : " +unit+ "purpose :" +purpose+ "verified:" +verified);
						
						String socid  = visit.getLoginID();
						String userID = objOTP.getLoginID();
						int iLoginID = Integer.parseInt(userID);
						HashMap<Integer, Map<String, Object>> objHash = objOTP.submit(socid,iLoginID,company,vehicle,unit,purpose,VisitorEntryID,vID,Mobile,EntryGateNo,vNote,EntryDoc_id,Temp,Oxygen,Pulse);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						Map<String, Object> mapResponse = objHash.get("response");
						String visitorID= mapResponse.get("VisitorID").toString();
						
						VisitorEntryID = (int) Long.parseLong(visitorID);
						HashMap<Integer, Map<String, Object>>  objHash1 = objOTP.submitapproval(VisitorEntryID,unit,approvedflag);
						
						//String objStr1 = objGson.toJson(objHash1);
						response.setContentType("application/json");
						out.println(objStr);
						///System.out.println(objStr);
							
					
					}
					//---------------verifying otp-------------------------------
					else if(str.equals(projectUrl+"/OTP/verify")) // otp mobno sid
					{
						String otp = "";
						String number = "";
						int vID=0;
						otp = request.getParameter("vEnteredOtp").trim();
						number = request.getParameter("vmobNumber").trim();
						vID=Integer.parseInt(request.getParameter("vID").trim());
						
						HashMap objHash = objOTP.verifyOtp(otp,number,vID);
						if(otp.equals("")|| number.equals(""))
						{
							out.println("{\"success\":0,\"response\":{\"message\":\"Vacant Field\"}}   ");
						}
						else
						{
							Gson objGson = new Gson();
							String objStr = objGson.toJson(objHash);
							response.setContentType("application/json");
							out.println(objStr);
						}
					}
					
					// ---------------------  Get No Set -------------------------------- //
					else if(str.equals(projectUrl+"/OTP/SetGateNo"))
					{
	
						int GateNo=0;
						GateNo=Integer.parseInt(request.getParameter("gateNo").trim());
						String sLoginID = objOTP.getLoginID();
						int userID = Integer.parseInt(sLoginID);

						HashMap objHash = objOTP.setgate(userID,GateNo);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);
					}
					//  ----------------------------------  new app implement function ---------------------/
					else if(str.equals(projectUrl+"/OTP/NewAPPsubmitWithoutOTP"))
					{
//						System.out.println("My Test string");
						Visitor visit=new Visitor(sToken);
						String company = "";
						String vehicle="";
						String verified="";
						String Fname = "";
						String Lname = ""; 
						String mobNo = "";
						String docNo = "";
						String vNote = "";
						String doc_img="";
						String img= "";
						String unit="-1";
						String approvedflag="";
						int Doc_id =0;
						int purpose=0;
						int VisitorEntryID =0;
					
						float Temp = Float.parseFloat(request.getParameter("temp").trim());
						float Oxygen = Float.parseFloat(request.getParameter("oxygen").trim());
						int Pulse= Integer.parseInt(request.getParameter("pulse").trim());
						
						VisitorEntryID=Integer.parseInt(request.getParameter("VisitorEntryID").trim());
						Fname = request.getParameter("vFName").trim();
						Lname = request.getParameter("vLName").trim();
						mobNo = request.getParameter("vmobNumber").trim();
						vNote = request.getParameter("vNote").trim();
						int EntryGateNo=Integer.parseInt(request.getParameter("Gate_no").trim());
						company=request.getParameter("vCompany").trim();
						vehicle=request.getParameter("vVehicle").trim();
						purpose=Integer.parseInt(request.getParameter("purpose_id").trim());
						int iSubmitFlag =0;
						//System.out.println("Inside Function");
						if(company.equals("other"))
						{
							
							company= request.getParameter("vCompanyOther").trim();
							HashMap objHash = visit.UpdateCompany(company,purpose);
						}
						else
						{
							company=request.getParameter("vCompany").trim();
							
						}
						int EntryDoc_id = Integer.parseInt(request.getParameter("EntryDoc_id").trim());
						
						if(purpose == 6)
						{
							
							company= request.getParameter("vPurposeOther").trim();
							//HashMap objHash = visit.UpdateCompany(company,vpurpose);
						}
						//System.out.println("company :"+company);
						if(VisitorEntryID == 0)
						{
							
							HashMap<Integer, Map<String, Object>> ServiceReqID = visit.createNewVisitor(Fname,Lname,mobNo,doc_img,docNo,company,img,purpose,Doc_id,EntryDoc_id);
							Map<String, Object> mapResponse = ServiceReqID.get("response");
							String sServiceProRegID= mapResponse.get("VisitorId").toString();
							VisitorEntryID = (int) Long.parseLong(sServiceProRegID);
							 
						}
						String socid = objOTP.getSocietyID();
						String sLoginID = objOTP.getLoginID();
						int userID = Integer.parseInt(sLoginID);
								
						HashMap<Integer, Map<String, Object>>  objHash = objOTP.submitWithout(userID,Fname,Lname,mobNo,unit,purpose,company,vehicle,docNo,VisitorEntryID,EntryGateNo,vNote,socid,EntryDoc_id,Temp,Oxygen,Pulse);
							
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);
						//System.out.println(objStr);
	
					
					}
					else if(str.equals(projectUrl+"/OTP/UnitApproval"))
					{
						//System.out.println("OTP Status");
						int VisitorEntryID=Integer.parseInt(request.getParameter("VisitorEntryID").trim());
						String unit=request.getParameter("unit_id").trim();
						String approvedflag=request.getParameter("ApprovalFlag").trim();
						HashMap<Integer, Map<String, Object>>  objHash2 = objOTP.submitunit(VisitorEntryID,unit);
						
						HashMap<Integer, Map<String, Object>>  objHash1 = objOTP.submitapproval(VisitorEntryID,unit,approvedflag);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash1);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(str.equals(projectUrl+"/OTP/UnitApprovalnew"))
					{
						//System.out.println("OTP Status");
						int VisitorEntryID=Integer.parseInt(request.getParameter("VisitorEntryID").trim());
						String unit=request.getParameter("unit_id").trim();
						//String approvedflag=request.getParameter("ApprovalFlag").trim();
						HashMap<Integer, Map<String, Object>>  objHash2 = objOTP.submitunit(VisitorEntryID,unit);
						Gson objGson = new Gson();
						if(request.getParameter("approve").trim().equals(""))
						{
							
							String objStr = objGson.toJson(objHash2);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else
						{
						String unitapprove=request.getParameter("unit").trim();
						String approveflag=request.getParameter("approve").trim();
						String id=request.getParameter("id").trim();
						String name=request.getParameter("name").trim();
						
						HashMap<Integer, Map<String, Object>>  objHash1 = objOTP.submitapprovalnew(VisitorEntryID,unitapprove,approveflag,id,name);
					
						String objStr = objGson.toJson(objHash1);
						response.setContentType("application/json");
						out.println(objStr);
						}
					
					}
					else if(str.equals(projectUrl+"/OTP/UnitApprovalData"))
					{
						//System.out.println("OTP Status");
						int VisitorEntryID=Integer.parseInt(request.getParameter("VisitorEntryID").trim());
						String unit=request.getParameter("unit_id").trim();
						//String approvedflag=request.getParameter("ApprovalFlag").trim();
						HashMap<Integer, Map<String, Object>>  objHash2 = objOTP.submitunit(VisitorEntryID,unit);
						Gson objGson = new Gson();
						if(request.getParameter("approve").trim().equals(""))
						{
							
							String objStr = objGson.toJson(objHash2);
							response.setContentType("application/json");
							out.println(objStr);
						}
						else
						{
						String unitapprove=request.getParameter("unit").trim();
						String approveflag=request.getParameter("approve").trim();
						String id=request.getParameter("id").trim();
						String name=request.getParameter("name").trim();
						String visitorid = request.getParameter("visitorid");
						HashMap<Integer, Map<String, Object>>  objHash1 = objOTP.submitapprovalnewdata(VisitorEntryID,unitapprove,approveflag,id,name,visitorid);
					
						String objStr = objGson.toJson(objHash1);
						response.setContentType("application/json");
						out.println(objStr);
						}
					
					}
					else if(str.equals(projectUrl+"/OTP/SubmitUnit"))
					{
						//System.out.println("OTP Status");
						int VisitorEntryID=Integer.parseInt(request.getParameter("VisitorEntryID").trim());
						String unit=request.getParameter("unit_id").trim();
						
						String visitorid=request.getParameter("visitorid").trim();
						
						HashMap<Integer, Map<String, Object>>  objHash1 = objOTP.addunitforapproval(VisitorEntryID,unit,visitorid);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash1);
						response.setContentType("application/json");
						out.println(objStr);
					}
					else if(str.equals(projectUrl+"/OTP/checkStatus"))
					{
						//System.out.println("OTP Status");
						int VisitorEntryID=Integer.parseInt(request.getParameter("VisitorEntryID").trim());
						String unit=request.getParameter("unit_id").trim();
						
						HashMap<Integer, Map<String, Object>>  objHash1 = objOTP.checkunitapprovalstatus(VisitorEntryID,unit);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash1);
						response.setContentType("application/json");
						out.println(objStr);
					}
					/*else if(str.equals("/NewSecurityManagerWeb/OTP/OTPDisableNew"))
					{
						System.out.println("OTP Status");
						//int iSocietyID= Integer.parseInt(request.getParameter("SocietyID").trim());
						int iOTPFlag= Integer.parseInt(request.getParameter("OptStatusNew").trim());
						System.out.println("iOTPFlag" +iOTPFlag);
						HashMap objHash = objOTP.ChangeOTPNew(iOTPFlag);
						Gson objGson = new Gson();
						String objStr = objGson.toJson(objHash);
						response.setContentType("application/json");
						out.println(objStr);
					}*/
					
				}
			else
			{
				out.println("login:");
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
