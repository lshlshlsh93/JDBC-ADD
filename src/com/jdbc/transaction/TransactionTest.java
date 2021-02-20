package com.jdbc.transaction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.jdbc.util.JdbcUtils;
/*
 * 1.	什么叫数据库事务？ 一组逻辑操作单元，使数据从一种状态变换到另一种状态
 * 		->一组逻辑操作单元：一个或多个DML操作。
 * 		->
 * 	
 * 
 * 2.数据一旦提交，就不可回滚
 * 
 * 
 * 
 * 3.哪些操作会导致数据的自动提交
 * 		1.>DDL操作一旦执行，都会自动提交
 * 		2.>DML默认情况下，一旦执行，就会自动提交
 * 			>我们可以通过set autoCommit = false的方式取消DML的自动提交
 * 		3.>	，默认在关闭连接时，也会自动提交数据
 * 
 *   	 要保证上述三个同时满足不自动提交数据
 * 
 * 4.并非任意的对数据库的操作序列都是数据库事务。数据库事务拥有以下四个特性，习惯上被称之为ACID特性。
 * 
		*********面试常考*****************
		原子性(Atomicity):事务作为一个整体被执行，包含在其中的对数据库的操作要么全部被执行，要么都不执行。
		一致性(Consistency):事务应确保数据库的状态从一个一致状态转变为另一个一致状态。一致状态的含义是数据库中的数据应满足完整性约束。
		隔离性(Isolation):多个事务并发执行时，一个事务的执行不应影响其他事务的执行。
		持久性(Durability):已被提交的事务对数据库的修改应该永久保存在数据库中。
 * 
 */

public class TransactionTest {

	// *************************未考虑事务的转账操作***********************************
	/*
	 * 
	 * 对于数据表user_table来说： AA用户给BB用户转账200 update user_table set balance = balance -
	 * 200 where user = "AA"; update user_table set balance = balance + 200 where
	 * user = "BB";
	 */
	// @Test
	public void testUpdate() {
		String sql1 = "update user_table set balance = balance - 200 where user = ?; ";
		update(sql1, "AA");

		String sql2 = "update user_table set balance = balance + 200 where user = ?; ";
		update(sql2, "BB");

		System.out.println("转账成功！");
	}

	/**
	 * 
	 *
	 * @Description 通用的增删改 ---1.0版本
	 * @author LSH
	 * @date 2021-1-19 17:07:06
	 * @param sql
	 * @param args
	 * @return
	 *
	 */
	public static int update(String sql, Object... args) {// SQL语句中占位符的个数与可变参数的个数相同

		Connection connection = null;
		PreparedStatement ps = null;
		try {
			// 1 获取数据库连接
			connection = JdbcUtils.getConnection();
			// 2 预编译SQL语句，返回PreparedStatement的实例化对象
			ps = connection.prepareStatement(sql);
			// 3 填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]); // 小心参数声明错误
			}
			// 4.执行SQL
			return ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5.关闭资源
			JdbcUtils.closeSource(connection, ps);
		}
		return 0;

	}

	// *************************考虑事务后的转账操作***********************************
	// @Test
	public void testUpdateTransaction1() {
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			// System.out.println(connection.getAutoCommit()); // true
			// ***1.取消数据的自动提交
			connection.setAutoCommit(false);
			String sql1 = "update user_table set `balance` = `balance` - 200 where `user` = ?; ";
			update(connection, sql1, "AA");

			// 模拟异常
			// System.out.println(10 / 0);

			String sql2 = "update user_table set `balance` = `balance` + 200 where `user` = ?; ";
			update(connection, sql2, "BB");

			System.out.println("转账成功！");

			// 2.提交数据
			connection.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				// 3.回滚数据
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				// 修改其为自动提交数据
				// 主要针对于数据库连接池的使用
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			JdbcUtils.closeSource(connection, null);
		}
	}

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
	public static int update(Connection connection, String sql, Object... args) {// SQL语句中占位符的个数与可变参数的个数相同

		PreparedStatement ps = null;
		try {
			// 1 获取数据库连接
			connection = JdbcUtils.getConnection();
			// 2 预编译SQL语句，返回PreparedStatement的实例化对象
			ps = connection.prepareStatement(sql);
			// 3 填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]); // 小心参数声明错误
			}
			// 4.执行SQL
			return ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5.关闭资源
			JdbcUtils.closeSource(null, ps); // **不要关闭连接！！！！
		}
		return 0;

	}

	// ***********************************************************************
	@Test
	public void testTransactionSelect1() throws Exception {

		Connection connection = JdbcUtils.getConnection();

		// 获取事务的隔离级别
		System.out.println(connection.getTransactionIsolation());

		// 设置不允许自动提交数据
		connection.setAutoCommit(false);

		// 设置事务的隔离级别
		 connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		 
		String sql = "select `user`,`password`,`balance` from `user_table` where `user` = ?; ";
		User user = getInstance(connection, User.class, sql, "CC");
		System.out.println(user);

	}

	@Test
	public void testTransactionUpdate() throws Exception {
		Connection connection = JdbcUtils.getConnection();

		// 设置不允许自动提交数据
		connection.setAutoCommit(false);
		String sql = "update `user_table` set `balance` = ? where `user` = ?;";
		update(connection, sql,5000, "CC");

		Thread.sleep(15000);
		System.out.println("修改结束！");
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

	/*
	 * 针对不同表的查询方法,返回表中的一条记录 考虑事务
	 */
	public <T> T getInstance(Connection connection, Class<T> clazz, String sql, Object... args) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 获取数据库连接
			connection = JdbcUtils.getConnection();
			// 预编译SQL语句，返回PreparedStatement的实例化对象
			ps = connection.prepareStatement(sql);
			// 填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			// 执行,返回结果集对象
			rs = ps.executeQuery();

			// **获取结果集的元数据ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();

			// 通过ResultSetMetaData获取结果集中的列数
			int columnCount = rsmd.getColumnCount();

			if (rs.next()) {

				T t = clazz.newInstance();
				// 处理结果集每一行数据的每一个列
				for (int i = 0; i < columnCount; i++) {
					Object columnValue = rs.getObject(i + 1); // 参数易错

					/*
					 * 难点
					 */

					// 获取每个列的列名
					// String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1); // 参数易错
					// 给t对象指定的 columnName 属性 ，赋值为columnValue ：通过反射
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			JdbcUtils.closeSource(null, ps, rs);// **连接不关闭
		}
		return null;
	}

}
