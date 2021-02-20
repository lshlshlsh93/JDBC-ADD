 package com.jdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.jdbc.bean.Customer;

public class CustomerDaoImpl extends BaseDao implements CustomerDao {

	@Override
	public void insert(Connection connection, Customer customer) {
		String sql = "insert into `customer`(`name`,`email`,`birth`) values(?,?,?);";
		update(connection, sql, customer.getName(),customer.getEmail(),customer.getBirth());	
	}

	@Override
	public void deleteById(Connection connection, int id) {
		String sql = "delete from customer where `id` = ?";
		update(connection, sql, id);
	}

	@Override
	public void update(Connection connection, Customer customer) {
		String sql = "update customer set `name` = ? `email` = ? `birth` = ? where `id` = ?";
		update(connection, sql, customer.getName(),customer.getEmail(),customer.getBirth(),customer.getId());
	}

	@Override
	public Customer getCostomerById(Connection connection, int id) {
		String sql = "select `id`,`name`,`email`,`birth` from customer where `id` = ?;";
		Customer customer = getInstance(connection, Customer.class, sql, id);
		return customer;
	}
 
	@Override
	public List<Customer> getAll(Connection connection) {
		String sql = "select `id`,`name`,`email`,`birth` from customer;";
		List<Customer> list = getForList(connection, Customer.class, sql);
		return list;
	}

	@Override
	public Long getCount(Connection connection) {
		String sql = "select count(*) from customeer";
		return getValue(connection, sql);
	}

	@Override
	public Date getMaxBirth(Connection connection) {
		String sql = "select max(birth) from customer";
		return  getValue(connection, sql);	
	}
	

}
