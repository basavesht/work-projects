/*
 *Created On NOV 15,2005
 *
 *This code is used to connect and execute Queries on
 * the database of External System.
 */

/**
 * @author 163974
 * 
 * This code is used to connect and execute Queries on the database of External
 * System
 */
package com.tcs.ebw.integrate.db;

import java.sql.*;

public class AccessHandler extends RDBMSHandler {
    public AccessHandler() {
        retrievedata();
    }

    public Connection Connect() {
        return super.connect();
    }

    public void retrievedata() {
        try {
            Connection con = Connect();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from data");
            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.print(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /*
     * public java.util.Map execute(java.util.Map parameters) {
     * super.localConnect(); } public java.util.Map executeQuery(java.util.Map
     * parameters) { super.localConnect(); }
     */
}