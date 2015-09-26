package com.tcs.ebw.integrate;

import java.sql.*;

public interface ConnectionType {
    public Connection connect();

    //public java.util.Map execute(java.util.Map parameters);
    //public java.util.Map executeQuery(java.util.Map parameters);
    public int close();
}