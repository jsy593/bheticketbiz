package com.bhe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bhe.business.IUserManagerBusiness;
import com.bhe.controller.common.BaseController;
import com.bhe.util.common.CommonUtil;
import com.bhe.util.common.JsonUtil;
import com.bhe.util.common.MD5Util;
import com.bhe.util.common.PageUtil;

/**
 * 
 * @author zhangyu
 *
 */
@Controller
public class UserManagerController extends BaseController {

	@Autowired
	private IUserManagerBusiness iUserManagerBusiness;

	/**
	 * @author yichuan
	 * @param model
	 * @return 0 系统错误 1 成功 4 用户名不存在 5 密码错误 6 账号禁止登陆 7 账号异常
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(@RequestParam Map<String, Object> map, HttpSession session) {
		Map<String, Object> reMap = iUserManagerBusiness.login(map);

		return reMap;
	}

	/**
	 * 获取用户信息
	 * 
	 * @author yichuan
	 * @param model
	 * @return 1 成功 2 失败
	 */
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getUserInfo(@RequestParam Map<String, Object> map) {
		Map<String, Object> reMap = iUserManagerBusiness.selectUserOne(map);
		
		return reMap;
	}
	/**
	 * 修改用户信息
	 * 
	 * @author yichuan
	 * @param model
	 * @return 1 成功 2 失败
	 */
	@RequestMapping(value = "/updataUserIngo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updataUserIngo(@RequestParam Map<String, Object> map) {
		Map<String, Object> reMap = iUserManagerBusiness.updateUserOne(map);
		
		return reMap;
	}

	/**
	 * 注册
	 * 
	 * @author jsy
	 * @param map
	 * @return 0 服务器错误 1 成功 2 失败 4 用户名空 9 密码不存在 8 用户名已存在
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> regist(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> resultMap = iUserManagerBusiness.insertUserInfo(dataMap);

		return resultMap;
	}

	/**
	 * 审核管理员
	 * 
	 * @author jsy
	 * @param dataMap
	 * @return 1 成功 2 失败
	 */
	@RequestMapping(value = "/checkUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkUser(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> resultMap = iUserManagerBusiness.checkUser(dataMap);

		return resultMap;
	}


	/**
	 * 新增部门管理者或者普通职员
	 * 
	 * @author jsy
	 * @param dataMap
	 * @return 1 成功
	 */
	@RequestMapping(value = "/selectUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectUser(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> resultMap = iUserManagerBusiness.insertUser(dataMap);

		return resultMap;
	}

	/**
	 * 替换或恢复部门管理者
	 * 
	 * @author jsy
	 * @param dataMap
	 * @return 1 成功
	 */
	@RequestMapping(value = "/becomeDeptAdmin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> becomeDeptAdmin(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> resultMap = iUserManagerBusiness.becomeDeptAdmin(dataMap);

		return resultMap;
	}

	/**
	 * 查询所有的部门管理者（根据roleId查询）
	 * 
	 * @author jsy
	 * @param roleMap
	 * @return json字符串（list集合）
	 */
	@RequestMapping(value = "/SelectDeptAdmin", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> SelectDeptAdmin(@RequestParam Map<String, Object> userMap) {
		List<Map<String, Object>> resultList = iUserManagerBusiness.selectRoleList(userMap);

		return resultList;
	}

	/**
	 * 恢复职员
	 * 
	 * @author jsy
	 * @param roleMap
	 * @return json字符串
	 */
	@RequestMapping(value = "/recoverStaff", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> recoverStaff(@RequestParam Map<String, Object> userMap) {
		Map<String, Object> resultMap = iUserManagerBusiness.recoverStaff(userMap);

		return resultMap;
	}


	/**
	 * 
	 * 
	 * @author jsy
	 * @param roleMap
	 * @return json字符串
	 */
	@RequestMapping(value = "/selectUserBydept", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectUserBydept(@RequestParam Map<String, Object> userMap) {
		Map<String, Object> result = iUserManagerBusiness.selectUserBydept(userMap);

		return result;
	}

	/**
	 * 查询admin列表（可传入status,搜索字段前台名字为content）
	 * 
	 * @author jsy
	 * @param roleMap
	 * @return json字符串
	 */
	@RequestMapping(value = "/selectAdminList")
	@ResponseBody
	public Map<String, Object> selectAdminList(@RequestParam Map<String, Object> userMap) {

		if (CommonUtil.isEmpty(userMap.get("content"))) {
			userMap.remove("content");
		}

		Map<String, Object> resultMap = iUserManagerBusiness.selectAdminList(userMap);
		
		try {
			resultMap.put("maxPage", PageUtil.getAllPage(Integer.parseInt(resultMap.get("totalCount").toString()),
					Integer.parseInt(userMap.get("pageSize").toString())));
		} catch (Exception e) {
			resultMap.put("maxPage", 1);
		}
		
		return resultMap;
	}

	/**
	 * 查询admin列表（可传入status,搜索字段前台名字为content）
	 * 
	 * @author jsy
	 * @param roleMap
	 * @return json字符串
	 */
	@RequestMapping(value = "/selectDeptAdminList")
	@ResponseBody
	public Map<String, Object> selectDeptAdminList(@RequestParam Map<String, Object> userMap) {

		if (CommonUtil.isEmpty(userMap.get("content"))) {
			userMap.remove("content");
		}

		Map<String, Object> resultMap = iUserManagerBusiness.selectDeptAdminList(userMap);

		return resultMap;
	}

	/**
	 * 查询用户列表（可传入status,搜索字段前台名字为content）
	 * 
	 * @author yc
	 * @param roleMap
	 * @return json字符串
	 */
	@RequestMapping(value = "/selectStaffList")
	@ResponseBody
	public Map<String, Object> selectUserList(@RequestParam Map<String, Object> userMap) {

		if (CommonUtil.isEmpty(userMap.get("content"))) {
			userMap.remove("content");
		}

		Map<String, Object> resultMap = iUserManagerBusiness.selectStaffList(userMap);
		
		try {
			resultMap.put("maxPage", PageUtil.getAllPage(Integer.parseInt(resultMap.get("totalCount").toString()),
					Integer.parseInt(userMap.get("pageSize").toString())));
		} catch (Exception e) {
			resultMap.put("maxPage", 1);
		}
		
		return resultMap;
	}

	@RequestMapping(value = "/selectUserOne", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectAdminOne(@RequestParam Map<String, Object> userMap) {
		Map<String, Object> resultMap = iUserManagerBusiness.selectUserOne(userMap);

		return resultMap;
	}

	/**
	 * 查询deptadmin列表
	 * 
	 * @author yc
	 * @param roleMap
	 * @return json字符串
	 */
	@RequestMapping(value = "/selectDeptAdmin", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectDeptAdmin(@RequestParam Map<String, Object> userMap) {
		Map<String, Object> resultMap = iUserManagerBusiness.selectDeptAdmin(userMap);

		return resultMap;
	}

	/**
	 * 查询该部门下的所有人员
	 * 
	 * @author yc
	 * @param roleMap
	 * @return json字符串
	 */
	@RequestMapping(value = "/selectDeptUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectDeptUser(@RequestParam Map<String, Object> userMap) {
		Map<String, Object> resultMap = iUserManagerBusiness.selectDeptUser(userMap);

		return resultMap;
	}
	/**
	 * 修改密码
	 * 
	 * @author jsy
	 * @param roleMap
	 * @return 
	 */
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateUser(@RequestParam Map<String, Object> userMap) {
		Map<String, Object> resultMap = iUserManagerBusiness.updateUserOne(userMap);
		
		return resultMap;
	}
	/**
	 * 发送验证码
	 * 
	 * @author jsy
	 * @param roleMap
	 * @return 
	 */
	@RequestMapping(value = "/sendCode", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> sendCode(@RequestParam Map<String, Object> userMap) {
		Map<String, Object> resultMap = iUserManagerBusiness.sendCode(userMap);
		
		return resultMap;
	}
	
}
