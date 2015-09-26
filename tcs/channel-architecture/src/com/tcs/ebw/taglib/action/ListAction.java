/*
 * Created on Oct 25, 2005
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

/**
 * @author 152820
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ListAction extends Action {
	/**
	 * Dispatch control to the "success" forward.
	 *
	 * @version $Rev: 54929 $ $Date: 2009/03/12 11:48:09 $
	 */
	    public static String SUCCESS = "success";

	    /**
	     * Dispatch control to the "success" forward.
	     */
	    public ActionForward execute(
	            ActionMapping mapping,
	            ActionForm form,
	            HttpServletRequest request,
	            HttpServletResponse response)
	            throws Exception {
	    	request.getParameterNames();
	    	
	    	ExcelForm excelform = new ExcelForm("d:\\Arun\\Model\\data1.xls","Table1");
	    	System.out.println("Came into Action Class");
	    	//request.setAttribute("searchForm1",new SearchForm1());
	    	request.setAttribute("myForm",excelform);
	    	System.out.println("Attribute set");
	    	return mapping.findForward(SUCCESS);
	    }
}
