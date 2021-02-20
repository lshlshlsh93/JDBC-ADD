package com.jdbc.transaction;

import java.sql.Connection;

import org.junit.Test;

import com.jdbc.util.JdbcUtils;

public class ConnectionTest {
	
	
	@Test
	public void testGetConnection() throws Exception {
		Connection connection = JdbcUtils.getConnection();
		System.out.println(connection);
	}
}
