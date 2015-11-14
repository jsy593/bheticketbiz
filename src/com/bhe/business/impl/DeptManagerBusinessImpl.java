package com.bhe.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bhe.business.BaseBusiness;
import com.bhe.business.IDeptManagerBusiness;
import com.bhe.business.IQuestionTypeManagerBusiness;
import com.bhe.business.IUserManagerBusiness;
import com.bhe.business.base.DepartmentBusinessImpl;
import com.bhe.business.base.UserBusinessImpl;
import com.bhe.business.base.UserRoleBusinessImpl;
import com.bhe.util.common.CommonUtil;
import com.bhe.util.common.UuidUtil;

/**
 * 
 * @author zhangyu
 *
 */
@Service
public class DeptManagerBusinessImpl extends BaseBusiness implements IDeptManagerBusiness {
	@Autowired
	private IQuestionTypeManagerBusiness iQuestionTypeManagerBusiness;
	@Autowired
	private IUserManagerBusiness iUserManagerBusiness;
	@Autowired
	private UserBusinessImpl userBusinessImpl;
	@Autowired
	private UserRoleBusinessImpl userRoleBusinessImpl;
	@Autowired
	private DepartmentBusinessImpl departmentBusinessImpl;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> insertDept(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		Map<String, Object> userMap = new HashMap<String, Object>();
		Map<String, Object> userRoleMap = new HashMap<String, Object>();
		Map<String, Object> deptMap = new HashMap<String, Object>();

		// 添加部门
		String deptUuid = UuidUtil.generateUUID();
		deptMap.put("uuid", deptUuid);
		deptMap.put("name", dataMap.get("deptName"));
		deptMap.put("systemIndex", dataMap.get("systemIndex"));
		deptMap.put("allotType", 0);
		
		departmentBusinessImpl.insert(deptMap);

		// 查询部门管理员 uuid
		String roleId = null;
		userRoleMap.put("code", CODE_DEPARTMENT_ADMIN);
		
		Map<String, Object> roleOne = userRoleBusinessImpl.selectOne(userRoleMap);
		Map<String, Object> roleData = (Map<String, Object>) roleOne.get("data");
		roleId = (String) roleData.get("uuid");

		// 添加管理员
		userMap.put("roleId", roleId);
		userMap.put("departmentId", deptUuid);
		userMap.put("username", dataMap.get("code"));
		userMap.put("password", dataMap.get("password"));
		userMap.put("realName", dataMap.get("adminName"));
		userMap.put("systemIndex", dataMap.get("systemIndex"));
		userMap.put("status", 1);
		userBusinessImpl.insert(userMap);

		return reMap;
	}
	// 维护部门
	@Override
	public Map<String, Object> manageDept(Map<String, Object> deptMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		Map<String, Object> queMap = new HashMap<String, Object>();
		
		if (!CommonUtil.isEmpty(deptMap.get("status")) && !CommonUtil.isEmpty(deptMap.get("uuid"))) {
			try {
				//查询该部门是否有对应的问题分类
				queMap = iQuestionTypeManagerBusiness.selectQueTypeByDept(deptMap);
				
				if (!CommonUtil.isEmpty(queMap) && deptMap.get("status").equals(0)) {//有：关闭部门时,提醒管理员
					reMap = departmentBusinessImpl.update(deptMap);
					reMap.put(KEY_STATE, STATE_TEWELVE);//提示管理员该部门有对应分类
				} else {//无
					reMap = departmentBusinessImpl.update(deptMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				reMap.put(KEY_STATE, STATE_ZERO);
				return reMap;
			}
			
		}

		return reMap;
	}

	@Override
	public Map<String, Object> updateDept(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		try {
			reMap = departmentBusinessImpl.update(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}
		
		return reMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> selectDept(Map<String, Object> dataMap) {
		dataMap.put("orderBy", "createTime desc");
		StringBuffer sql = new StringBuffer(" select uuid,name,createTime,description,status,systemIndex,allotType from ").append(DEPARTMENT);

		Map<String, Object> reMap = new HashMap<String, Object>();
		List<Object> params = new ArrayList<Object>();

		try {
			handleSql(dataMap, sql, params, null);

			List<Map<String, Object>> List = commonDao.selectList(sql.toString(), params);
			if (!CommonUtil.isEmpty(List) && List.size() > 0) {
				
				for (Map<String, Object> map : List) {
					Map<String, Object> newMap = new HashMap<String, Object>();
					newMap.put("departmentId", map.get("uuid"));
					
					Map<String, Object> selectDeptAdmin = iUserManagerBusiness.selectDeptAdmin(newMap);
					
					if ("1".equals(selectDeptAdmin.get("state").toString())) {
						Map<String, Object> object = (Map<String, Object>) selectDeptAdmin.get("data");
						map.put("adminUser", object.get("realName"));
					}
				}
				reMap.put(KEY_STATE, STATE_ONE);
				reMap.put("list", List);
			} else {
				reMap.put(KEY_STATE, STATE_FIFTEEN);
			}
		} catch (Exception e) {
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}
		
		return reMap;
	}

	@Override
	public Map<String, Object> selectDeptOne(Map<String, Object> dataMap) {
		
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			reMap = departmentBusinessImpl.selectOne(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_TWO);
			return reMap;
		}

		return reMap;
	}
	
	@Override
	public Map<String, Object> selectDeptByUser(Map<String, Object> dataMap) {
		StringBuffer sql = new StringBuffer("select * from" + DEPARTMENT);
		Map<String, Object> reMap = new HashMap<String, Object>();
		Map<String, Object> userMap = new HashMap<String, Object>();

		// 查询出部门的uuid
		userMap = iUserManagerBusiness.selectUserOne(dataMap);
		Object departmentId = userMap.get("departmentId");
		userMap.clear();
		userMap.put("uuid", departmentId);
		// 查询部门表
		List<Object> params = new ArrayList<Object>();
		try {
			handleSql(userMap, sql, params, null);
			reMap = commonDao.selectForMap(sql.toString(), params.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reMap;
	}
	
	@Override
	public Map<String, Object> insertDeptInfo(Map<String, Object> deptMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		Map<String, Object> dptMap = new HashMap<String, Object>();
		
		if (CommonUtil.isEmpty(deptMap.get("name"))) {
			resultMap.put(KEY_STATE, STATE_TEN);
		} else {
			dptMap.put("name", deptMap.get("name"));
			
			if (!CommonUtil.isEmpty(this.selectDeptOne(deptMap))) {
				resultMap.put(KEY_STATE, STATE_ELEVEN);
			} else {
				try {
					commonDao.insertManual(dptMap, DEPARTMENT);
					resultMap.put(KEY_STATE, STATE_ONE);
				} catch (Exception e) {
					resultMap.put(KEY_STATE, STATE_TWO);
					log.error("添加数据失败", e);
				}
			}
		}
		return resultMap;
	}
}
