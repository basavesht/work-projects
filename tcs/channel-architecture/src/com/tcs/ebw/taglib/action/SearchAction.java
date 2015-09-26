/*
 * Created on Oct 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tcs.ebw.taglib.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tcs.ebw.codegen.beans.ExcelForm;
import com.tcs.ebw.codegen.beans.SearchForm1;


/**
 * @author 152820
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SearchAction extends Action{
	public static String SUCCESS = "success";
	public static String FAILURE = "failure";

    /**
     * Dispatch control to the "success" forward.
     */
    public ActionForward execute(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
    	
    	SearchForm1 sf1=null;
    	if(form!=null){
    		sf1= (SearchForm1)form;
    		System.out.println(sf1.toString());
    		System.out.println(sf1.getPayeeId());
    		System.out.println(sf1.getLocalAddress());
    		System.out.println(sf1.getLocalName1());
    		System.out.println(sf1.getLocalName2());
    		String queryStr = request.getQueryString();
    		System.out.println("QueryString is "+queryStr);
    		ExcelForm excelform =null;
    		
    		/*if(queryStr.indexOf("cancel")>0 || queryStr.indexOf("Cancel")>0)
    			excelform  = new ExcelForm("D:\\Arun\\Model\\data1.xls","Table1");
    		else
    			excelform  = new ExcelForm(queryStr,"D:\\Arun\\Model\\data1.xls","Table1");
    			*/
    		
	    	System.out.println("Came into Action Class"+((SearchForm1)form).getPayeeId());
	    	request.setAttribute("myForm",excelform);
	    	//request.setAttribute("searchForm1",new SearchForm1());
	    	return mapping.findForward(SUCCESS);
    	}else
    		return mapping.findForward(FAILURE);

    }
    

}
