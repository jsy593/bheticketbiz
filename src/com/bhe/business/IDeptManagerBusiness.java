package com.bhe.business;

import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 
 * @author zhangyu
 *
 */
@Service
public interface IDeptManagerBusiness {

	/**
	 * yc 添加部门
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> insertDept(Map<String, Object> dataMap) throws Exception;
	/**
	 * jsy 维护部门(开启或者关闭该部门)
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> manageDept(Map<String, Object> dataMap);

	/**
	 * jsy 修改部门
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> updateDept(Map<String, Object> dataMap);
	
	/**
	 * 查询部门 list
	 * 
	 * @param dataMap
	 * @return
	 */
	public Map<String, Object> selectDept(Map<String, Object> dataMap);
	
	
	/**
	 * 查询部门one
	 * 
	 * @param dataMap
	 * @return
	 */
	public Map<String, Object> selectDeptOne(Map<String, Object> dataMap);

	/**
	 * 根据用户uuid或者username查询部门
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectDeptByUser(Map<String, Object> dataMap);
	/**
	 * 添加部门
	 * 
	 * @param dept
	 * @return
	 */
	public Map<String, Object> insertDeptInfo(Map<String, Object> deptMap);
}
