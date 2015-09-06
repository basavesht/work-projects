/**
 *This class is used to store the session object and 
 *retrive the same from any helper classes.
 *Note: This will work only in the current thread context.
 */
package com.tcs.ebw.common.util;

import java.util.HashMap;
import java.util.Map;

public class SessionUtil {
private static ThreadLocal sessionData = new ThreadLocal() {
protected synchronized Object initialValue() {
return new HashMap();
}
};
public static Object get(String key) {
return ((Map)sessionData.get()).get(key);
}
public static void set(String key, Object val) {
((Map)sessionData.get()).put(key,val);
}
}
