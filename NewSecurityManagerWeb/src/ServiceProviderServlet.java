

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

import MainSecurity.Login;
import MainSecurity.OTP;
import MainSecurity.ServiceProvider;

/**
 * Servlet implementation class ServiceProviderServlet
 */
@WebServlet(urlPatterns= {"/ServiceProvider/*"})
public class ServiceProviderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String projectUrl = "/NewSecurityManagerWeb";

	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceProviderServlet() {
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
		
	    ServiceProvider objServProvider=null;		
		try 
		{
			String str = request.getRequestURI();
			String sToken = request.getParameter("token").trim();	
			objServProvider =  new ServiceProvider(sToken);

			if(str.equals(projectUrl+ "/ServiceProvider/fetchDocuments"))
			{
				HashMap objHash = objServProvider.mfetchDocument();
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if (str.equals(projectUrl+ "/ServiceProvider/fetchPurpose"))
			{
				HashMap objHash = objServProvider.mPurposeList();
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/ServiceProvider/fetchUnits"))
			{
				int unitId = Integer.parseInt(request.getParameter("unit_id"));

				HashMap objHash = objServProvider.mUnitList(unitId);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/ServiceProvider/fetchWing"))
			{
				HashMap objHash = objServProvider.mWingList();
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/ServiceProvider/fetchCompany"))
			{
				int iCat_id = Integer.parseInt(request.getParameter("porpose").trim());
				HashMap objHash = objServProvider.mFetchCompany(iCat_id);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/ServiceProvider/fetchExpVisitor"))
			{
				int iSocietyID = Integer.parseInt(request.getParameter("societyID").trim());
				HashMap objHash = objServProvider.mFetchExpVisitor(iSocietyID);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/ServiceProvider/fetchCategory"))
			{
				//int iSocietyID = Integer.parseInt(request.getParameter("societyID").trim());
				HashMap objHash = objServProvider.mFetchCategory();
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			////////new addition
//			else if(str.equals(projectUrl+"/ServiceProvider/GetStaffDetails"))
//			{
//				int iSocietyID = Integer.parseInt(request.getParameter("societyID").trim());
//				String soc_staffid = request.getParameter("soc_staffid");
//				String contact = request.getParameter("contact");
//				HashMap objHash = objServProvider.sGetStaffDetails(iSocietyID, soc_staffid, contact);
//				Gson objGson = new Gson();
//				String objStr = objGson.toJson(objHash);
//				response.setContentType("application/json");
//				out.println(objStr);
//			}
			else if(str.equals(projectUrl+"/ServiceProvider/AddStaffPersonal"))
			{
				int iSocietyID = Integer.parseInt(request.getParameter("societyID").trim());
				String sFullName=request.getParameter("fullname").trim();
				String soc_staffid = request.getParameter("soc_staffid");
				String sDOb = request.getParameter("dob");
				String sWorkingSince= request.getParameter("workingSince");
				String sMarritalstatus= request.getParameter("marry");
				String sGenderstatus= request.getParameter("gender");
				String sAadhaar= request.getParameter("adhar_card_no");
				String sCategory =request.getParameter("category");
				String curContact1 = request.getParameter("curContact1");
				HashMap objHash = objServProvider.sAddStaffPersonal(iSocietyID,sFullName,sDOb,sWorkingSince,sMarritalstatus,sCategory,curContact1,soc_staffid, sGenderstatus, sAadhaar);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/ServiceProvider/AddStaffContact"))
			{
				int iSocietyID = Integer.parseInt(request.getParameter("societyID").trim());
				int ProviderId = Integer.parseInt(request.getParameter("ProviderId").trim());
				String sCurentAdd=request.getParameter("curAddress");
				//String sCurContact1 = request.getParameter("curContact1");
				String sCurContact2= request.getParameter("curContact2");
				String sRefName= request.getParameter("refName");
				String sRefAdd =request.getParameter("refAdd");
				String RefContact =request.getParameter("refContact");
				
				HashMap objHash = objServProvider.sAddStaffContactDetails(iSocietyID,ProviderId,sCurentAdd,sCurContact2,sRefName,sRefAdd,RefContact);
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/ServiceProvider/AddStaffUnit"))
			{
				int iSocietyID = Integer.parseInt(request.getParameter("SocietyId"));
				int iProviderId= Integer.parseInt(request.getParameter("ProviderId"));
				String unit_id = request.getParameter("unitid");
				String unit_no = request.getParameter("unitno");
				String owner_name = request.getParameter("ownername");
				HashMap objHash = objServProvider.sAddStaffUnitDetails(iSocietyID,iProviderId,unit_no,unit_id,owner_name);
	
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/ServiceProvider/fetchDocList"))
			{
				int iSocietyID = Integer.parseInt(request.getParameter("SocietyID"));
				
				HashMap objHash = objServProvider.sFetchDocumentList();
	
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/ServiceProvider/CheckStaffIdStatus"))
			{
				int iSocietyID = Integer.parseInt(request.getParameter("SocietyId"));
				String soc_staffid = request.getParameter("ProviderId");
				HashMap objHash = objServProvider.fetchstaffidstatus(iSocietyID,soc_staffid);
	
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
			else if(str.equals(projectUrl+"/ServiceProvider/GetStaffIdStatus"))
			{
				int iSocietyID = Integer.parseInt(request.getParameter("SocietyId"));
				 HashMap objHash = objServProvider.getStaffStatus(iSocietyID);
	
				Gson objGson = new Gson();
				String objStr = objGson.toJson(objHash);
				response.setContentType("application/json");
				out.println(objStr);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
