/*
 *Created On NOV 15,2005
 *This class is used for returning implementation class based on the type of
 *external system.It will get the information on the external system     
 *from local configuration table.
 */

/**
 * @author 163974
 * 
 * This class is used for returning implementation class based on the type of
 * external system.It will get the information on the external system from local
 * configuration table.
 *  
 */

package com.tcs.ebw.integrate;

import com.tcs.ebw.integrate.db.AccessHandler;
import java.sql.*;
import java.util.*;

class BaseConnectionFactory {
    Connection localConnection;

    String external_System_Type;

    String external_System_Impltype;

    public ConnectionType create() {
        localConnect();
        try {
            Statement st = localConnection.createStatement();
            ResultSet rs = st.executeQuery("select * from external_system");
            rs.next();

            external_System_Type = rs.getString("External_System_Type");
            external_System_Impltype = rs.getString("System_Impl_Type");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (external_System_Type.equalsIgnoreCase("RDBMS")) {
            if (external_System_Impltype.equalsIgnoreCase("Access")) {
                return new AccessHandler();
            }
        }
        return new AccessHandler();
    }

    /*
     * else if(external_System_Impltype.equalsIgnoreCase("Oracle")) return new
     * OracleHandler(); } else
     * if(external_System_Type.equalsIgnoreCase("Messaging")) {
     * if(external_System_Impltype.equalsIgnoreCase("JBossMQ")) return new
     * JBossMQHandler(); else
     * if(external_System_Impltype.equalsIgnoreCase("MSMQ")) return new
     * MSMQHandler(); } else
     * if(external_System_Type.equalsIgnoreCase("Services")) {
     * if(external_System_Impltype.equalsIgnoreCase("Ex1")) return new
     * Ex1Handler(); else if(external_System_Impltype.equalsIgnoreCase("Ex2"))
     * return new Ex2Handler(); } }
     */
    private void localConnect() {
        ResourceBundle prop;
        prop = ResourceBundle.getBundle("com.tcs.connection.ebw");
        try {
            Class.forName(prop.getString("DriverClass"));
        } catch (Exception s) {
            s.printStackTrace();
        }
        try {
            localConnection = DriverManager.getConnection(prop
                    .getString("DriverURL"), prop.getString("Userid"), prop
                    .getString("Password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        BaseConnectionFactory bcf = new BaseConnectionFactory();
        bcf.create();
    }
}