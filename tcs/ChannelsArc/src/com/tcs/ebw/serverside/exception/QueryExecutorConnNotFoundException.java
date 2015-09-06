package com.tcs.ebw.serverside.exception;

import com.tcs.ebw.exception.EbwException;

public class QueryExecutorConnNotFoundException extends EbwException {

		public QueryExecutorConnNotFoundException(String str){
			super("SYS0014",str);
		}
}
