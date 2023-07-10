

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

//import MainSecurity.CommonBaseClass;
import MainSecurity.OTP;
import MainSecurity.Staff;
import MainSecurity.Visitor;

/**
 * Servlet implementation class VisitorServlet
 */
@WebServlet(urlPatterns= {"/Visitor/*"})
public class VisitorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String projectUrl = "/NewSecurityManagerWeb";
	//CommonBaseClass objCommonBase;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VisitorServlet() {
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
	    Visitor visit=null;
		JSONObject jsonObject = new JSONObject();
		PrintWriter out = response.getWriter();

		String sToken = request.getParameter("token").trim();
		
		//objCommonBase = new CommonBaseClass(sToken);
		//HashMap DecryptedTokenMap = objCommonBase.getDecryptedTokenMap();
		

		//String socid =(String) ((HashMap) DecryptedTokenMap.get("response")).get("SocietyId");
			
		//System.out.println("Society ID  :"+socid);
		
		
		try 
		{
			 visit = new Visitor(sToken);
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
		
		//String sToken = request.getParameter("visitorId").trim();
		String str = request.getRequestURI();
		
		
		try
		{
			//String id = "";
			//id = request.getParameter("scannedCode").trim();
			//int loginId = Integer.valueOf(id);
			if(str.equals(projectUrl + "/Visitor/exit")) 
			{
				String id = "";
				id = request.getParameter("scannedCode").trim();
				int loginId = Integer.valueOf(id);
				
				
				HashMap objHash = visit.markExit(loginId);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
				
			}
			
			else if(str.equals(projectUrl+"/Visitor/fetchMobNumber"))
			{
				int unit_no = Integer.parseInt( request.getParameter("unit_id").trim());
				HashMap objHash = visit.getMobileFromUnit(unit_no);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/Visitor/submit")) // otp mobno sid
			{
				OTP op=new OTP(sToken);
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
				
				int VisitorEntryID =0;
				int loginId=0;
				int iSubmitFlag = 0;
				
				float Temp = Float.parseFloat(request.getParameter("temp").trim());
				float Oxygen = Float.parseFloat(request.getParameter("oxygen").trim());
				int Pulse= Integer.parseInt(request.getParameter("pulse").trim());
				
				VisitorEntryID=Integer.parseInt(request.getParameter("VisitorEntryID").trim());  //
				vID=Integer.parseInt(request.getParameter("vID").trim());     //
				vehicle=request.getParameter("vVehicle").trim();   //
				unit=request.getParameter("unit_id").trim();      //
				approvedflag=request.getParameter("ApprovalFlag").trim();  //
				
				purpose=Integer.parseInt(request.getParameter("purpose_id").trim());  //
				verified=request.getParameter("isVerified").trim();   ///      ----------------
				Fname=request.getParameter("vFName").trim();
				Lname=request.getParameter("vLName").trim();
				vNote=request.getParameter("vNote").trim();
				Mobile=request.getParameter("vmobNumber").trim();   //
				int Doc_id=Integer.parseInt(request.getParameter("Doc_id").trim());
				int EntryGateNo=Integer.parseInt(request.getParameter("Gate_no").trim());  //
				//iSubmitFlag =Integer.parseInt(request.getParameter("SubmitFlag").trim()); 
				docNo=request.getParameter("Doc_no").trim();
				company=request.getParameter("vCompany").trim();   ///
				
				int EntryDoc_id = Integer.parseInt(request.getParameter("EntryDoc_id").trim());
				if(company.equals("other"))
				{
					company= request.getParameter("vCompanyOther").trim();   //
					HashMap objHash = visit.UpdateCompany(company,purpose);
				}
				else
				{
					company=request.getParameter("vCompany").trim();
					
				}
				//System.out.println("company :"+company);
				if(VisitorEntryID == 0)
				{
					
					HashMap<Integer, Map<String, Object>> ServiceReqID = visit.createNewVisitor(Fname,Lname,Mobile,doc_img,docNo,company,img,purpose,Doc_id,EntryDoc_id);
					Map<String, Object> mapResponse = ServiceReqID.get("response");
					String sServiceProRegID= mapResponse.get("VisitorId").toString();
					VisitorEntryID = (int) Long.parseLong(sServiceProRegID);
					 
				}
				String socid = op.getSocietyID();
				//System.out.println("Visitor ID :" +vID+ "Company :" +company+ "vehicle :" +vehicle+ "unit : " +unit+ "purpose :" +purpose+ "verified:" +verified);
				HashMap<Integer, Map<String, Object>> objHash = op.submit(socid, loginId,company,vehicle,unit,purpose,vID,VisitorEntryID,Mobile,EntryGateNo,vNote,EntryDoc_id,Temp,Oxygen,Pulse);
				//HashMap objHash = OTP.submit(company,vehicle,unit,purpose,vID,verified);
				
				
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				Map<String, Object> mapResponse = objHash.get("response");
				String visitorID= mapResponse.get("VisitorID").toString();
				
				VisitorEntryID = (int) Long.parseLong(visitorID);
				HashMap<Integer, Map<String, Object>>  objHash1 = op.submitapproval(VisitorEntryID,unit,approvedflag);
				
				//String objStr1 = objGson.toJson(objHash1);
				response.setContentType("application/json");
				out.println(objStr);
				//System.out.println(objStr);
					
				
				
			}
			
			else if(str.equals(projectUrl+"/Visitor/fetchVisitors"))
			{
//				int unit_no = Integer.parseInt( request.getParameter("unit_id").trim());
				HashMap<Integer, Map<String, Object>> objHash = visit.mfetchAllVisitors();
				
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/Visitor/fetchVisitorsDetails"))
			{
				int VisitorEntryID = Integer.parseInt( request.getParameter("visitorId").trim());
				HashMap objHash = visit.mfetchAllVisitorsDetails(VisitorEntryID);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/Visitor/ExitVisitors"))
			{   
				int VisitorID = Integer.parseInt( request.getParameter("VisitorID").trim());
				int ExitGateNo = Integer.parseInt( request.getParameter("ExitGate").trim());
				int CheckOut = Integer.parseInt( request.getParameter("UnkownOut").trim());
				
				HashMap objHash = visit.mfetchExit(VisitorID,ExitGateNo,CheckOut);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/Visitor/VisitorReport"))
			{
				//System.out.println("In visitor report");
				int VisitorID = 0;
				String StartDate=request.getParameter("startDate").trim();
				//System.out.println("StartDate : "+StartDate);
				String EndDate=request.getParameter("EndDate").trim();
				//System.out.println("EndDate : "+EndDate);
				HashMap objHash = visit.fetchVisitorsReports(StartDate,EndDate,VisitorID);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				//System.out.println("objStr : "+objStr);
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/Visitor/VisitorVisitReport"))
			{  
				int VisitorID = Integer.parseInt( request.getParameter("VisitorVisitID").trim());
				String StartDate=request.getParameter("startDate").trim();
				
				String EndDate=request.getParameter("EndtDate").trim();
				
				HashMap objHash = visit.fetchVisitorsReports(StartDate,EndDate,VisitorID);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/Visitor/LatestVisitReport"))
			{  
				int VisitorID = Integer.parseInt( request.getParameter("VisitorVisitID").trim());
				///String StartDate=request.getParameter("startDate").trim();
				
				//String EndDate=request.getParameter("EndtDate").trim();
				
				HashMap objHash = visit.fetchLatestVisitorsReports(VisitorID);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/Visitor/FetchVisitorByMobileNo"))
			{
				String ContactNo = request.getParameter("contactNo").trim();
				String EntryDoc_id = request.getParameter("EntryDoc_id").trim();
				//int purposeId = (Integer.parseInt(request.getParameter("purposeId")));
				HashMap objHash = visit.fetchVisitor(ContactNo,EntryDoc_id);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			/*else if(str.equals(projectUrl+"/Visitor/createVisitor"))
			{
				String FName = request.getParameter("vFName").trim();
				String LName = request.getParameter("vLName").trim();
				String Mobile = request.getParameter("vMobile").trim();
				String Doc_img = request.getParameter("docName").trim();
				String DocNo = request.getParameter("DocId").trim();
				String company = request.getParameter("company").trim();
				String img = request.getParameter("img").trim();
				int purposeId = (Integer.parseInt(request.getParameter("purposeId")));
				int Doc_Id = (Integer.parseInt(request.getParameter("Doc_id")));
			
				HashMap objHash = Visitor.createNewVisitor(FName, LName, Mobile, Doc_img, DocNo, company, img, purposeId,Doc_Id);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}*/
			else if(str.equals(projectUrl+"/Visitor/fetchAllStaffReport"))
			{
				Staff staff=new Staff(sToken);
				//System.out.println("In All Staff Report");
				String StartDate=request.getParameter("startDate").trim();
				//System.out.println("StartDate :"+StartDate);
				String EndDate=request.getParameter("EndDate").trim();
				//System.out.println("EndDate :"+EndDate);
				HashMap objHash = staff.fetchAllStaffReports(StartDate,EndDate);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				//System.out.println("objStr : "+objStr);
				response.setContentType("application/json");
				out.println(objStr);	
			}
			
			// * --------------------------- New Function -------------------------------- */
			else if(str.equals(projectUrl+"/Visitor/SubmitwithVisitor"))
			{
				OTP op=new OTP(sToken);
				//out.println("Inside FUnction");	
				String Fname="";
				String Lname="";
				String Mobile="";
				String img="";
				String approvedflag="";
				String unit="-1";
				String doc_img="";
				String docNo="";
				int Doc_id =0;
				int vID=0;
				int VisitorRepetedID =0;
				int loginId=0;
				int iSubmitFlag = 0;
				String vNote="";
				String vEntryGate="";
				String vCompany ="";
				String vVehicle="";
				int purpose=0;
				String vCompanyOther ="";
				
				float Temp = Float.parseFloat(request.getParameter("temp").trim());
				float Oxygen = Float.parseFloat(request.getParameter("oxygen").trim());
				int Pulse= Integer.parseInt(request.getParameter("pulse").trim());
/*				
				String sToken = request.getParameter("token").trim();
				objCommonBase = new CommonBaseClass(sToken);
				HashMap DecryptedTokenMap = objCommonBase.getDecryptedTokenMap();
				String socid =(String) ((HashMap) DecryptedTokenMap.get("response")).get("SocietyId");
				*/
				Fname=request.getParameter("Fname").trim();
				Lname=request.getParameter("Lname").trim();
				Mobile=request.getParameter("vMobile").trim();
				vEntryGate=request.getParameter("Gate_no").trim();
				VisitorRepetedID=Integer.parseInt(request.getParameter("VisitorEntryID").trim());
				vID=Integer.parseInt(request.getParameter("vID").trim());
				vNote =request.getParameter("vNote").trim();
				vVehicle =request.getParameter("vVehicle").trim();
				purpose=Integer.parseInt(request.getParameter("purpose_id").trim());
				//vCompany=request.getParameter("vCompany").trim();
				if(vCompany.equals("other"))
				{
					
					vCompany= request.getParameter("vCompanyOther").trim();
					HashMap objHash = visit.UpdateCompany(vCompany,purpose);
				}
				else
				{
					vCompany=request.getParameter("vCompany").trim();
					
				}
				int EntryDoc_id = Integer.parseInt(request.getParameter("EntryDoc_id").trim());
				
				if(VisitorRepetedID == 0)
				{
					
					HashMap<Integer, Map<String, Object>> ServiceReqID = visit.createNewVisitor(Fname,Lname,Mobile,doc_img,docNo,vCompany,img,purpose,Doc_id,EntryDoc_id);
					Map<String, Object> mapResponse = ServiceReqID.get("response");
					String sServiceProRegID= mapResponse.get("VisitorId").toString();
					VisitorRepetedID = (int) Long.parseLong(sServiceProRegID);
					 
				}
				String socid = op.getSocietyID();
				
				HashMap<Integer, Map<String, Object>> objHash = op.submit1(loginId,socid,unit,purpose,vID,VisitorRepetedID,Mobile,vEntryGate,Fname,Lname,vCompany,vVehicle,vNote,EntryDoc_id,Temp,Oxygen,Pulse);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				//System.out.println("objStr : "+objStr);
				response.setContentType("application/json");
				out.println(objStr);	
			}
			else
			{
				
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
