package com.czl.chatServer.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.czl.chatServer.bean.Database;
import com.czl.chatServer.utils.DBAccess.DBName;

public class JdbcUtil {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private Connection connection;
	private PreparedStatement pstmt;
	private ResultSet resultSet;
	public JdbcUtil() {
		try{
			Class.forName(DRIVER);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 获得数据库的连接
	 * @return
	 */
	public Connection getConnection(){
		return getConnection(DBName.userdb);
	}

	public Connection getConnection(DBName db){
		try {
			Database da = DBAccess.getDB(db);
			connection = DriverManager.getConnection(da.getUrl(), da.getUsername(), da.getPassword());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}


	/**
	 * 增加、删除、改
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public boolean updateByPreparedStatement(String sql, List<Object>params)throws SQLException{
		boolean flag = false;
		int result = -1;
		pstmt = connection.prepareStatement(sql);
		int index = 1;
		if(params != null && !params.isEmpty()){
			for(int i=0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		result = pstmt.executeUpdate();
		flag = result > 0 ? true : false;
		return flag;
	}



	/**
	 * 查询单条记录
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> findMoreKeyValue(String sql, List<Object> params) throws SQLException{
		Map<String, Object> map = new HashMap<String, Object>();
		int index  = 1;
		pstmt = connection.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i=0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();//返回查询结果
		ResultSetMetaData metaData = resultSet.getMetaData();
		int col_len = metaData.getColumnCount();
		while(resultSet.next()){
			for(int i=0; i<col_len; i++ ){
				String cols_name = metaData.getColumnName(i+1);
				Object cols_value = resultSet.getObject(cols_name);
				if(cols_value == null){
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
		}
		return map;
	}

	/**查询多条记录
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> findMoreResult(String sql, List<Object> params) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		while(resultSet.next()){
			Map<String, Object> map = new HashMap<String, Object>();
			for(int i=0; i<cols_len; i++){
				String cols_name = metaData.getColumnName(i+1);
				Object cols_value = resultSet.getObject(cols_name);
				if(cols_value == null){
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 查询单个值
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Object findSingleValue(String sql, List<Object> params) throws SQLException{
		int index  = 1;
		pstmt = connection.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i=0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();//返回查询结果
		while(resultSet.next()){
			return resultSet.getObject(1);
		}
		return null;
	}

	/**通过反射机制查询单条记录
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> T findSingleRefResult(String sql, List<Object> params,
									 Class<T> cls )throws Exception{
		T resultObject = null;
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData  = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		while(resultSet.next()){
			//通过反射机制创建一个实例resultObject
			resultObject = cls.newInstance();
			for(int i = 0; i<cols_len; i++){
				String cols_name = metaData.getColumnName(i+1);
				Object cols_value = resultSet.getObject(cols_name);
				if(cols_value == null){
					cols_value = "";
				}
				Field field = cls.getDeclaredField(cols_name);
				field.setAccessible(true); //打开javabean的访问权限
				field.set(resultObject, cols_value);
			}
		}
		return resultObject;

	}

	/**通过反射机制查询多条记录
	 * @param sql
	 * @param params
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findMoreRefResult(String sql, List<Object> params,
										 Class<T> cls )throws Exception {
		List<T> list = new ArrayList<T>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData  = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		while(resultSet.next()){
			//通过反射机制创建一个实例
			Constructor<T> ct = cls.getDeclaredConstructor();
			ct.setAccessible(true);
			T resultObject = (T) ct.newInstance();

			//	T resultObject = cls.newInstance();
			for(int i = 0; i<cols_len; i++){
				String cols_name = metaData.getColumnLabel(i+1);
				Object cols_value = resultSet.getObject(cols_name);
				if(cols_value == null){
					cols_value = "";
				}
				Field field = cls.getDeclaredField(cols_name);
				if(field.getType().getName().equals("int")){
					if(cols_value.equals("")) cols_value = 0;
					else if(cols_value instanceof Long)
						cols_value = Integer.parseInt(cols_value.toString());
				}
				field.setAccessible(true); //打开javabean的访问权限
				field.set(resultObject, cols_value);
			}
			list.add(resultObject);
		}
		return list;
	}

	public Map<String,String> findMoreRefResult(String sql, List<Object> params)throws Exception {
		Map<String, String> list = new HashMap<String, String>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();
		while(resultSet.next()){
			list.put(resultSet.getString(1), resultSet.getString(2));
		}
		return list;
	}

	public List<String> findMoreValue(String sql, List<Object> params)throws Exception {
		List<String> list = new ArrayList<String>();
		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();
		while(resultSet.next()){
			list.add(resultSet.getString(1));
		}
		return list;
	}

	/**查询多条记录,返回记录集
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public JSONArray findMoreRefResultSet(String sql, List<Object> params)throws Exception {

		int index = 1;
		pstmt = connection.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData  = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		JSONArray ja = new JSONArray();
		JSONObject json = new JSONObject();
		while(resultSet.next()){
			for(int i = 0; i<cols_len; i++){
				String cols_name = metaData.getColumnLabel(i+1);
				Object cols_value = resultSet.getObject(cols_name);
				if(cols_value == null) cols_value = "";
				json.put(cols_name, cols_value);
			}
			ja.add(json);
		}
		return ja;
	}


	/**
	 * 释放数据库连接
	 */
	public void releaseConn(){
		try{
			if(connection != null && !connection.isClosed()) connection.close();
			if(resultSet != null && !resultSet.isClosed()) resultSet.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
