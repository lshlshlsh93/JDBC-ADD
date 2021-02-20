package com.jdbc.dao.junit;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import com.jdbc.bean.Customer;
import com.jdbc.dao.CustomerDaoImpl;
import com.jdbc.util.JdbcUtils;

public class CustomerDaoImplTest {
	
	private CustomerDaoImpl dao = new CustomerDaoImpl();
	@Test
	public void testInsert()  {
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			Customer customer = new Customer(1,"李华","lh@126.com",new Date(45641515156L));
			dao.insert(connection, customer);
			System.out.println("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.closeSource(connection, null);			
		}	
	}

	@Test
	public void testDeleteById() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = JdbcUtils.getConnection();
			String sql = "delete from customer where id = ?";
			ps = connection.prepareStatement(sql);
			dao.deleteById(connection,1);
		}  catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.closeSource(connection, ps);
		}
	}

	@Test
	public void testUpdateConnectionCustomer()  {
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			String sql = "update customer set `name` = ? where `id` = ?";
			dao.update(connection, sql,"李依涵",5);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.closeSource(connection, null);
		}
	}

	@Test
	public void testGetCostomerById() {
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			Customer costomerById = dao.getCostomerById(connection, 2);
			System.out.println(costomerById);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.closeSource(connection, null);
		}
	}

	@Test
	public void testGetAll() {
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			List<Customer> list = dao.getAll(connection);
			list.forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.closeSource(connection, null);
		}
	}

	@Test
	public void testGetCount() {
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			Long count = dao.getCount(connection);
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtils.closeSource(connection, null);
		}
	}

	@Test
	public void testGetMaxBirth() {
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			Date maxBirth = dao.getMaxBirth(connection);
			System.out.println(maxBirth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			JdbcUtils.closeSource(connection, null);
		}
	}
	
	

}
