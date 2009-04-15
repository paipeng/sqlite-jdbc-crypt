//--------------------------------------
// sqlite-jdbc Project
//
// QueryTest.java
// Since: Apr 8, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.sqlite;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.BeforeClass;
import org.junit.Test;

public class QueryTest
{
    @BeforeClass
    public static void forName() throws Exception
    {
        Class.forName("org.sqlite.JDBC");
    }

    @Test
    public void createTable() throws Exception
    {
        String driver = "org.sqlite.JDBC";
        String url = "jdbc:sqlite::memory:";
        //String url = "jdbc:sqlite:file.db";

        Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS sample " + "(id INTEGER PRIMARY KEY, descr VARCHAR(40))");
        stmt.close();

        stmt = conn.createStatement();
        try
        {
            ResultSet rs = stmt.executeQuery("SELECT * FROM sample");
            rs.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        conn.close();

    }

    @Test
    public void setFloatTest() throws Exception
    {
        String driver = "org.sqlite.JDBC";
        String url = "jdbc:sqlite::memory:";

        float f = 3.141597f;
        Connection conn = DriverManager.getConnection(url);
        conn.createStatement().execute("create table sample (data NOAFFINITY)");
        conn.createStatement().execute(String.format("insert into sample values(%f)", f));

        PreparedStatement stmt = conn.prepareStatement("select * from sample where data > ?");
        stmt.setObject(1, 3.0f);
        ResultSet rs = stmt.executeQuery();
        assertTrue(rs.next());
        float f2 = rs.getFloat(1);
        assertEquals(f, f2, 0.0000001);

    }

}