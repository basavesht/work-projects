/*
 * Created on Oct 21, 2005
 *
 * Copyright Tata Consultancy Services. All rights reserved.
 */
package com.tcs.ebw.codegen.common;

/**
 * This class is used for Storing all the CodeGeneration Constants.
 * These constants values are used in the Excel sheet configuration.
 * 
 * @author TCS
 */
public interface CommonConstants {
    // Type of Code generation
	public final static String CODE_JSP = "JSP";
	public final static String CODE_HTML = "HTML";
	public final static String CODE_STRUTS = "STRUTS";
	
	// Different Mode configurations
	public final static String MODE_NEW = "New";
	public final static String MODE_EDIT = "Edit";
	public final static String MODE_VIEW = "View";
	
	// Retain value separator
	public final static String RETAIN_SEP="~";

	// Configuration Error message.
	public final static String CONFIG_ERR = ">>>>> ERROR IN CONFIG : ";
	
	// Configuration Resource Bundle
	public final static String CONFIG_RB = "ConfigResource";
	public final static String CODING_COMMENTS_RB = "com.tcs.ebw.codegen.struts.CodingComments";
	
	// Screen Type
	public final static String SCREEN_TYPE_VIEWONLY = "ViewOnly";

	// Service Types
	public final static String SVC_TYPE_XML = "XML";
	public final static String SVC_TYPE_DB = "DB";
	
	// Separator used in Map Key
	public final static String MAP_KEY_SEPARATOR = "~";
	
	// Screen & Service Linking constants
	public final static String OUTPUT_SCREEN = "OutputScreen";
	public final static String OUTPUT_STATE = "OutputState";
	public final static String SERVICE = "Service";
	public final static String SERVICE_MAP_ID = "ServiceMapID";
	public final static String BD_HOOK = "BusinessDelegateHook";
	public final static String TXN_PWD = "TxnPassword";
	public final static String ACTION_HOOK = "ActionHook";
	public final static String COPY_PROPERTIES = "CopyProperties";
	public final static String SESSION_SCOPE = "SessionScope";
	public final static String TAG_CASHDRAWER="CashDrawer";
	public final static String CASHDRAWER_REQ="CashDrawer";
	
	// Service Mapping constants
	//Parameter	ParameterField	ScreenField	INOUT
	public final static String SCREEN = "Screen";
	public final static String PARAMETER = "Parameter";
	public final static String PARAMETER_FIELD = "ParameterField";
	public final static String SCREEN_FIELD = "ScreenField";
	public final static String IN_OUT = "INOUT";
	
	// Service Definition Master & Details
	public final static String IMPL_CLS_NAME = "ImplClassName";
	public final static String METHOD_NAME = "MethodName";
	public final static String PARAM_NAME = "ParameterName";
	public final static String RETURN = "Return";
	
	// Service Def Details
	public final static String SUCCESS_VAL = "SuccessValue";
	public final static String FAILURE_VAL = "FailureValue";
	public final static String DEFAULT_VAL = "DefaultValue";
	public final static String DATA_TYPE = "DataType";
	public final static String TYPE = "Type";
	public final static String MANDATORY = "Mandatory";
	
	// Transfer object def.
	public final static String TO_TYPE = "TOType";
	public final static String TO_DATA_TYPE = "DataType";
	public final static String TO_DEFAULT_VAL = "DefaultValue";
	public final static String TO_FIELD_NAME = "FieldName";
	public final static String TO_FORMATTING_REQ = "FormattingRequired";//Added For WMP
	public final static String TO_CURRENCY_ATTR = "CurrencyAttribute";//Added For WMP
	
	//Excel Sheet Configuraion
	public final static String EXCEL_SCREENID = "ScreenID";
	public final static String EXCEL_FIELDID = "FieldID";
	public final static String EXCEL_COLPOS = "ColPos";
	public final static String EXCEL_ROWPOS = "RowPos";
	public final static String EXCEL_MANDATORY = "Mandatory";
	public final static String EXCEL_GLOBALMESSAGES = "GlobalMessages";
	public final static String EXCEL_DISPLAYLABEL = "DisplayLabel";
	public final static String EXCEL_DISPLAYLENGTH = "DisplayLength";
	public final static String EXCEL_COMPONENTTYPE = "ComponentType";
	public final static String EXCEL_DATATYPE = "DataType";
	public final static String EXCEL_REFERENCEFIELD = "ReferenceField";
	public final static String EXCEL_REPORTGROUP = "ReportGroup";
	public final static String EXCEL_DYNAMICVALUE = "DynamicValue";
	public final static String EXCEL_DEFAULTVALUE = "DefaultValue";
	public final static String EXCEL_VARIABLE = "Variable";
	public final static String EXCEL_ACCESSKEY = "AccessKey";
	public final static String EXCEL_GROUPNAME = "GroupName";
	public final static String EXCEL_MINLENGTH = "MinLength";
	public final static String EXCEL_MAXLENGTH = "MaxLength";
	public final static String EXCEL_MASK = "Mask";
	public final static String EXCEL_VALIDATE = "Validate";
	public final static String EXCEL_COLSPAN = "ColSpan";
	public final static String EXCEL_ROWSPAN = "RowSpan";
	public final static String EXCEL_TABLECOLSTYLE = "TableColStyle";
	public final static String EXCEL_TABLESTYLE = "TableStyle";
	public final static String EXCEL_ALIGNMENT = "Alignment";
	public final static String EXCEL_FIELDATTRIBUTE = "FieldAttribute";
	public final static String EXCEL_SERVERVALIDATION = "ServerSideValidation";//Added by 163974
	public final static String EXCEL_HTMLDEFAULTVALUE = "HTMLDefaultValue";
	public final static String EXCEL_TABORDER = "TabOrder";
	public final static String EXCEL_RETAINVALUE = "RetainValue";
	public final static String EXCEL_DYNAMICLABEL = "DynamicLabel";	
	public final static String EXCEL_REPORTOPERATIONS = "ReportOperations";
	//	 GroupType-----222774(Value Can be Div or Table)
	public final static String EXCEL_GROUPTYPE="GroupType";
	public final static String EXCEL_DIVSTYLE="DivStyle";
	
	//Page Builder Constants
	public final static String PB_TEMPLATE = "template";
	public final static String PB_NAME = "name";
	public final static String PB_DIRECT = "direct";
	public final static String PB_SET_START = "<set";
	public final static String PB_CONTENT = " content";
	public final static String PB_DYNAMIC = "DYNAMIC";
	public final static String PB_GET_START = "<get";
	public final static String PB_TAG_END = "/>";
	
	// Constants for Transfer Object Creation
	public final static String TO_TYPE_DB = "DB";
	public final static String TO_TYPE_NONDB = "NONDB";
	
	// Tag Names
	public final static String TAG_GROUP = "Group";
	public final static String TAG_TEXTFIELD = "TextField";
	public final static String TAG_RADIOBUTTON = "RadioButton";
	public final static String TAG_CHECKBOX = "Checkbox";
	public final static String TAG_TEXTAREA = "TextArea";
	public final static String TAG_COMBOBOX = "Combobox";
	public final static String TAG_DYNCOMBOBOX = "DynCombobox";
	public final static String TAG_HREF = "Href";
	public final static String TAG_LABEL = "Label";
	public final static String TAG_LABELDATA = "LabelData";
	public final static String TAG_DATE = "Date";
	public final static String TAG_IMAGE = "Image";
	public final static String TAG_BROWSE = "Browse";
	public final static String TAG_HTML = "HTML";
	public final static String TAG_TABLE = "Table";
	public final static String TAG_MATRIXSTRUCTURETABLE = "MatrixStructureTable";
	public final static String TAG_SITEDEFINEDFIELDSTABLE = "SiteDefinedFieldsTable";
	public final static String TAG_COLORCODEDIMAGE = "ColorCodedImage";
	public final static String TAG_SOLUTIONSLISTTABLE = "SolutionsListTable";
	public final static String TAG_MULTILEVELDISPLAYTABLE = "MultiLevelDisplayTable";
	public final static String TAG_MULTILEVELCOMPONENTTABLE = "MultiLevelComponentTable";
	public final static String TAG_ROWLISTDISPLAYTABLE = "RowListDisplayTable";
	public final static String TAG_TEXTDYNAMICLABELTABLE = "TextDynamicLabelTable";
	public final static String TAG_GOALPLANNINGTABLE = "GoalPlanningTable";
	public final static String TAG_DSETABLE = "DSETable";
	public final static String TAG_SYSTEMPARAMETERSTABLE = "SystemParametersTable";
	public final static String TAG_PRODUCTUNIVERSETABLE= "ProductUniverseTable";
	public final static String TAG_ALLOCATIONTABLE= "AllocationTable";
	public final static String TAG_FREEPORTFOLIOTABLE= "FreePortfolioTable";
	public final static String TAG_LEGENDDISPLAYTABLE= "LegendDisplayTable";
	public final static String TAG_WORKORGANIZERTABLE= "WorkOrganizerTable";
	public final static String TAG_TREE = "Tree";
	public final static String TAG_LIST = "List";
	public final static String TAG_FILE = "File";
	public final static String TAG_PASSWORD = "Password";
	public final static String TAG_JAVASCRIPT = "JavaScript";
	public final static String TAG_HIDDEN = "Hidden";
	public final static String TAG_TITLE = "Title";
	public final static String TAG_MULTICHECKBOX = "MultiCheckBox";
	public final static String TAG_MULTIRADIOBUTTON = "MultiRadioButton";//Added by Jadumani
	public final static String TAG_MODETEXTFIELDLABEL = "ModeTextFieldLabel";
	public final static String TAG_MODECOMBOLABEL = "ModeComboLabel";
	public final static String TAG_MODECOMBONODISPLAY = "ModeComboNoDisplay";
	public final static String TAG_MODETEXTAREALABEL = "ModeTextAreaLabel";
	public final static String TAG_MODEDATELABEL = "ModeDateLabel";
	public final static String TAG_MODECOMBOTEXTFIELD = "ModeComboTextField";
	public final static String TAG_TEMPLATE = "Template";
	public final static String TAG_REPORTTABLE = "ReportTable";
	public final static String TAG_SLIDER = "Slider";
	public final static String TAG_COMBOGRID = "ComboGrid";
	public final static String TAG_QUESTIONANSWER  = "QuestionAnswer";
	public final static String TAG_SPAN="Span";
	
	// Action Items
	public final static String TAG_ACTIONITEM = "ActionItem";
	public final static String TAG_ACTIONITEM_SUBMIT = "Submit";
	public final static String TAG_ACTIONITEM_BUTTON = "Button";
	public final static String TAG_ACTIONITEM_CANCEL = "Cancel";
	public final static String TAG_ACTIONITEM_RESET  = "Reset";
	public final static String TAG_ACTIONITEM_REPORT = "Report";
	public final static String TAG_ACTIONITEM_CHART = "Chart";
	public final static String TAG_ACTIONITEM_JSBUTTON = "JSButton";
	
	// Button Styles
	public final static String BTN_STYLE_BUTTON  = "Button";
	public final static String BTN_STYLE_LINK  = "Link";
	public final static String BTN_STYLE_IMAGE  = "Image";

	// Other Constants
	public final static String COMP_TYPE= "CompType";
	public final static String COMP_VALUE= "CompValue";
	public final static String DEF_VAL= "DefValue";
	public final static String REF_FIELD= "RefField";
	public final static String TABLE_CONFIG = "TableConfig";
	public final static String REPORT_GROUP = "ReportGroup";
	public final static String LEGEND = "Legend";
	public final static String DYN_VAL = "DynamicValue";
	public final static String GROUP_NAME = "GroupName";
	public final static String INIT_VALUE = "InitialValue";//added by 163974
	public final static String FIELD_ATTRIBUTE = "FieldAttributes";//added by 163974
	
	// Validation Related Constants.
	public final static String VAL_REQUIRED = "required";
	public final static String VAL_VALIDWHEN= "validwhen";
	public final static String VAL_TEST = "test";
	public final static String VAL_MINLEN = "minlength";
	public final static String VAL_MAXLEN = "maxlength";
	public final static String VAL_MASK = "mask";
	public final static String VAL_DATATYPE = "DataType";
	public final static String VAL_VALID = "Validate";
	public final static String VAL_SEPARATOR = ",";
	public final static String VAL_MSG_COMP = "ValMsg";
	public final static String VAL_ERR_MSG = "ValErrMsg";
	public final static String VAL_MSG_KEY = "ValMsgKey";
	
	// Export/Report Constants
	public final static String EXP_REPORT = "Report";
	public final static String EXP_PDF = "PDFReport";
	public final static String EXP_XLS = "XLSReport";
	public final static String EXP_CVS = "CVSReport";
	
	// Yes/No values
	public final static String YES = "Y";
	public final static String NO = "N";
	
	// Customization Fields
	public final static String CUST_IS_COMP_AVAIL = "IsComponentAvailable";
	public final static String CUST_IS_VAL_AVAIL = "IsValidationAvailable";
	public final static String CUST_FIELD_ATTR = "IsFieldAttribute";
}
