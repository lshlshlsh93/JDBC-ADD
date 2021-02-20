package com.jdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.jdbc.bean.Customer;

/**
 * 
 * @Description 此接口用于规范customer表的常用操作
 * @author LSH QQ:1945773750
 * @version
 * @date 2021-1-19 18:38:58
 *
 */
public interface CustomerDao {

	/**
	 * 
	 *
	 * @Description 将customer对象添加到数据库中
	 * @author LSH
	 * @date 2021-1-19 18:42:27
	 * @param connection
	 * @param customer
	 *
	 */
	void insert(Connection connection, Customer customer);

	/**
	 * 
	 *
	 * @Description 针对指定的Id,删除表中的一条数据
	 * @author LSH
	 * @date 2021-1-19 18:44:48
	 * @param connection
	 * @param id
	 *
	 */
	void deleteById(Connection connection, int id);

	/**
	 * 
	 *
	 * @Description 针对内层中的customer对象,来修改表中的指定的数据
	 * @author LSH
	 * @date 2021-1-19 18:44:51
	 * @param connection
	 * @param customer
	 *
	 */
	void update(Connection connection, Customer customer);

	/**
	 * 
	 *
	 * @Description 针对指定的id查询得到对应的Customer对象
	 * @author LSH
	 * @date 2021-1-19 18:47:22
	 *
	 */
	Customer getCostomerById(Connection connection, int id);

	/**
	 * 
	 *
	 * @Description 查询表中的所有记录
	 * @author LSH
	 * @date 2021-1-19 18:49:45
	 * @param connection
	 * @return 记录封装在集合中
	 *
	 */
	List<Customer> getAll(Connection connection);
	
	
	/**
	 * 
	 *
	 * @Description 返回数据表中的数据的条数 
	 * @author LSH
	 * @date 2021-1-19 18:51:14
	 * @param connection
	 * @return 数据表中的数据的条数 
	 *
	 */
	Long getCount(Connection connection);
	
	
	/**
	 * 
	 *
	 * @Description 返回数据表中的最大生日
	 * @author LSH
	 * @date 2021-1-19 18:52:02
	 * @return 返回数据表中的最大生日
	 *
	 */
	Date getMaxBirth(Connection connection);
	
	
	
}
