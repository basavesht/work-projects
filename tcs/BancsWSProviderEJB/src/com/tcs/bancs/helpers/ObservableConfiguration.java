package com.tcs.bancs.helpers;

import java.io.File;
import java.io.FileNotFoundException;

import com.tcs.bancs.helpers.xml.ConfigXMLParsingException;

public abstract class ObservableConfiguration 
{
	private long lastModificationTime = 0;
	private String configFileName = null;

	private boolean isNewer(String configFileName){		
		if(configFileName == null) {
			return true;
		}
		File file = new File(configFileName);
		long newModifucationTime = file.lastModified();
		return newModifucationTime > lastModificationTime ;
	}

	public final void parse(String passedConfigFileName) throws FileNotFoundException, ConfigXMLParsingException {
		if(passedConfigFileName == null){
			throw new FileNotFoundException("Configuration file name is null");
		}
		this.configFileName = passedConfigFileName;
		File file = new File(configFileName);
		parseImpl(configFileName);
		lastModificationTime = file.lastModified();
	}

	public final void checkNew() throws FileNotFoundException, ConfigXMLParsingException {
		if (configFileName != null ) {
			if ( isNewer(configFileName)) {
				parse(configFileName);
			}
		}
	}

	public abstract void parseImpl(String configFileName) throws FileNotFoundException, ConfigXMLParsingException;
}
