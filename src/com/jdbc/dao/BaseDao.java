package com.jdbc.dao;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.jdbc.util.JdbcUtils;

/**
 * DAO(data/date access object)
 * 
 * @Description 封装了通用的操作 --- 增删改查    
 * @author LSH QQ:1945773750
 * @version
 * @date 2021-1-19 18:12:14
 *
 */
public abstract class BaseDao {

	/**
	 * 
	 *
	 * @Description 通用的增删改 ---2.0版本 ----考虑事务
	 * @author LSH
	 * @date 2021-1-19 17:07:47
	 * @param connection
	 * @param sql
	 * @param args
	 * @return
	 *
	 */
	public int update(Connection connection, String sql, Object... args) {// SQL语句中占位符的个数与可变参数的个数相同

		PreparedStatement ps = null;
		try {
			connection = JdbcUtils.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.closeSource(null, ps);
		}
		return 0;

	}

	/**
	 * 
	 *
	 * @Description 针对不同表的通用查询方法,返回表中的一条记录， 考虑事务
	 * @author LSH
	 * @date 2021-1-18 12:49:21
	 * @param <T>
	 * @param clazz 传入的类
	 * @param sql   SQL语句
	 * @param args  填充可变参数也就是占位符
	 * @return
	 *
	 */
	public <T> T getInstance(Connection connection, Class<T> clazz, String sql, Object... args) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			if (rs.next()) {
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) {
					Object columnValue = rs.getObject(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.closeSource(null, ps, rs);
		}
		return null;
	}

	/**
	 * 
	 * 
	 * @Description 对多表的查询 针对不同表的通用查询方法,返回表中的多条记录， 考虑事务
	 * @author LSH
	 * @date 2021-1-18 13:14:03
	 * @param <T>
	 * @param clazz 传入的类
	 * @param sql   SQL语句
	 * @param args  填充可变参数也就是占位符
	 * @return
	 *
	 */
	public <T> List<T> getForList(Connection connection, Class<T> clazz, String sql, Object... args) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = JdbcUtils.getConnection();
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			ArrayList<T> list = new ArrayList<T>();
			while (rs.next()) {
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) {
					Object columnValue = rs.getObject(i + 1); 
					String columnLabel = rsmd.getColumnLabel(i + 1);
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.closeSource(null, ps, rs);
		}
		return null;

	}

	/**
	 * 
	 *
	 * @Description 用于查询特殊值的通用方法
	 * @author LSH
	 * @date 2021-1-19 18:30:57
	 * @param <E>        泛型方法
	 * @param connection 数据库连接
	 * @param sql        SQL语句
	 * @param args       占位符
	 * @return
	 *
	 */
	public <E> E getValue(Connection connection, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				return (E) rs.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JdbcUtils.closeSource(null, ps, rs);
		return null;
	}

}
