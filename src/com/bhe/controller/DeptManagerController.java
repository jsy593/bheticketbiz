package com.bhe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bhe.business.IDeptManagerBusiness;
import com.bhe.util.common.JsonUtil;
import com.bhe.util.common.PageUtil;

@Controller
public class DeptManagerController {
	@Autowired
	private IDeptManagerBusiness iDeptManagerBusiness;
	/**
	 * @author yichuan 添加部门
	 * @param model
	 * @return 0 系统错误 1 成功 4 用户名不存在 5 密码错误 6 账号禁止登陆 7 账号异常
	 */
	@RequestMapping(value = "/insertDept", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addDept(@RequestParam Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = iDeptManagerBusiness.insertDept(dataMap);

		return reMap;
	}
	/**
	 * 维护部门
	 * 
	 * @author jsy
	 * @param dataMap
	 * @return 1 成功 2 失败 13 提示管理员部门有对应分类
	 */
	@RequestMapping(value = "/manageDept", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> manageDept(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> resultMap = iDeptManagerBusiness.manageDept(dataMap);
		
		return resultMap;
	}
	
	/**
	 * 修改部门
	 * 
	 * @author jsy
	 * @param dataMap
	 * @return 1 成功 2 失败 13 提示管理员部门有对应分类
	 */
	@RequestMapping(value = "/updateManageDept", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateManageDept(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> resultMap = iDeptManagerBusiness.updateDept(dataMap);
		
		return resultMap;
	}
	
	/**
	 * 查询部门 list
	 * 
	 * @author yc
	 * @param roleMap
	 * @return json字符串
	 */
	@RequestMapping(value = "/selectDept", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectDeptr(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> resultMap = iDeptManagerBusiness.selectDept(dataMap);
		
		try {
			resultMap.put("maxPage", PageUtil.getAllPage(Integer.parseInt(resultMap.get("totalCount").toString()),
					Integer.parseInt(dataMap.get("pageSize").toString())));
		} catch (Exception e) {
			resultMap.put("maxPage", 1);
		}
		
		return resultMap;
	}
	
	/**
	 * 查询部门 one
	 * 
	 * @author yc
	 * @param roleMap
	 * @return json字符串
	 */
	@RequestMapping(value = "/selectDeptOne", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectDeptOne(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> resultMap = iDeptManagerBusiness.selectDeptOne(dataMap);

		return resultMap;
	}
	

	/**
	 * 根据用户uuid或者username查询部门
	 * 
	 * @author jsy
	 * @param roleMap
	 * @return json字符串
	 */
	@RequestMapping(value = "/selectDeptByUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectDeptByUser(@RequestParam Map<String, Object> userMap) {
		Map<String, Object> resultMap = iDeptManagerBusiness.selectDeptByUser(userMap);

		return resultMap;
	}
	
	/**
	 * 添加部门
	 * 
	 * @author jsy
	 * @param dataMap
	 * @return 1 成功 2 失败 10 部门名为空 11 部门名数据库中已存在
	 */
	@RequestMapping(value = "/addDepartment", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addDepartment(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> resultMap = iDeptManagerBusiness.insertDeptInfo(dataMap);

		return resultMap;
	}
	
}
