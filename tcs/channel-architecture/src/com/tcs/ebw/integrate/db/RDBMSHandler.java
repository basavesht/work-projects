/*
 *Created On NOV 15,2005
 *
 *This code is used to connect to the database of External System
 *by retrieving details from local Database.
 */
/**
 * @author 163974
 *This code is used to connect to the database of External System
 *by retrieving details from local Database.
 *
 */
package com.tcs.ebw.integrate.db;

import com.tcs.ebw.integrate.ConnectionType;

import java.sql.*;

import java.util.*;


abstract class RDBMSHandler implements ConnectionType {
    protected Connection connectionobj;
    protected Connection localconnobj;
    public ResourceBundle prop;
    String driver_class;
    String driver_url;
    String portNo;
    String dsnName;
    String Userid;
    String Pwd;

    protected Connection localConnect() {
        prop = ResourceBundle.getBundle("com.tcs.connection.ebw");

        try {
            Class.forName(prop.getString("DriverClass"));
        } catch (Exception s) {
            s.printStackTrace();
        }

        try {
            localconnobj = DriverManager.getConnection(prop.getString(
                        "DriverURL"), prop.getString("Userid"),
                    prop.getString("Password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return localconnobj;
    }

    public Connection connect() {
        try {
            Connection localConnection = localConnect();
            Statement st = localConnection.createStatement();
            ResultSet rs = st.executeQuery(
                    "select * from external_system where External_System_Type='RDBMS'");
            rs.next();
            driver_class = rs.getString("Driver_Class");
            driver_url = rs.getString("Driver_URL");
            Userid = rs.getString("User_ID");
            Pwd = rs.getString("Password");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(driver_class);
        } catch (ClassNotFoundException sw) {
            sw.printStackTrace();
        }

        try {
            connectionobj = DriverManager.getConnection(driver_url, Userid, Pwd);
        } catch (SQLException nn) {
            nn.printStackTrace();
        }

        return connectionobj;
    }

    /*public java.util.Map execute(java.util.Map parameters);
    public java.util.Map executeQuery(java.util.Map parameters);*/
    public int close() {
        try {
            connectionobj.close();
        } catch (SQLException v) {
            v.printStackTrace();
        }

        return 0;
    }
}
