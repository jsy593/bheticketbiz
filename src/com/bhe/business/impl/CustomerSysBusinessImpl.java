package com.bhe.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhe.business.BaseBusiness;
import com.bhe.business.ICustomerSysBusiness;
import com.bhe.business.IUserManagerBusiness;
import com.bhe.business.base.AccessSystemBusinessImpl;
import com.bhe.business.base.DepartmentBusinessImpl;
import com.bhe.business.base.QuestionTypeUserMapBusinessImpl;
import com.bhe.business.base.TicketBusinessImpl;
import com.bhe.util.common.CommonUtil;
import com.bhe.util.common.TableNameUtil;

/**
 * 
 * @author yichuan
 *
 */
@Service
public class CustomerSysBusinessImpl extends BaseBusiness implements ICustomerSysBusiness {

	@Autowired
	IUserManagerBusiness iUserManagerBusiness;
	@Autowired
	private TicketBusinessImpl ticketBusinessImpl;
	@Autowired
	private AccessSystemBusinessImpl accessSystemBusinessImpl;
	@Autowired
	private DepartmentBusinessImpl departmentBusinessImpl;
	@Autowired
	private QuestionTypeUserMapBusinessImpl questionTypeUserMapBusinessImpl;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> selectCustomerTicketList(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		Map<String, Object> sysParamsMap = new HashMap<String, Object>();
		Map<String, Object> accSysMap = null;
		
		sysParamsMap.put("appId", dataMap.get("appId"));
		sysParamsMap.put("appKey", dataMap.get("appKey"));
		dataMap.remove("appId");
		dataMap.remove("appKey");


		try {
			accSysMap = (Map<String, Object>) accessSystemBusinessImpl.selectOne(sysParamsMap).get("data");

			if (CommonUtil.isEmpty(accSysMap)) {
				reMap.put(KEY_STATE, STATE_SIX);
			} else {
				dataMap.put("systemIndex", accSysMap.get("systemIndex"));
				reMap = ticketBusinessImpl.selectList(dataMap);
				reMap.put("systemIndex", accSysMap.get("systemIndex"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reMap;
	}


	@Override
	public Map<String, Object> closeTicket(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		dataMap.put("status", 3);
		
		try {
			reMap = ticketBusinessImpl.update(dataMap);
		} catch (Exception e) {
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}
		
		return reMap;

	}

	// selectUserByQuestionType
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> insertTicket(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		HashMap<String, Object> deptId = new HashMap<String, Object>();
		Map<String, Object> userMap = new HashMap<String, Object>();
		
		try {
			Map<String, Object> questionMap = getDeptIdByQuestionId(dataMap);
			deptId.put("uuid", questionMap.get("departmentId"));
			
			// 获取分配方式
			Map<String, Object> deptMap = (Map<String, Object>) departmentBusinessImpl.selectOne(deptId).get("data");
			Integer allotType = (Integer) deptMap.get("allotType");
			
			switch (allotType) {
			case 0:// 手动分配
				userMap = selectUserByQuestionType(deptId.get("uuid").toString());
				break;
			case 1:// 自动分配
				userMap = selectUserByTicketNumber(dataMap);
				break;
			case 2:// 自动分配

				break;
			case 3:// 自动分配

				break;
			}
			
			if (!CommonUtil.isEmpty(userMap)) {
				dataMap.put("ownerId", userMap.get("userId"));
				reMap = ticketBusinessImpl.insert(dataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}
		
		return reMap;
	}

	/**
	 * 自动分配方式1 分配给手中工单数最少的员工
	 * 
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> selectUserByTicketNumber(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		Map<String, Object> questionMap = new HashMap<String, Object>();
		try {
			// 通过问题子分类获取 分类对应的员工
			questionMap.put("questionTypeId", dataMap.get("questionTypeId"));
			Map<String, Object> questionToUser = questionTypeUserMapBusinessImpl.selectList(questionMap);
			
			List<Map<String, Object>> questionUserList = (List<Map<String, Object>>) questionToUser.get("list");
			for (Map<String, Object> map : questionUserList) {
				map.put("count", getTicketNumber(map));
			}
			
			reMap = questionUserList.get(0);
			
			for (int i = 1; i < questionUserList.size(); i++) {
				Integer lastNum = Integer.parseInt(reMap.get("count").toString());
				Integer nextNum = Integer.parseInt(questionUserList.get(i).get("count").toString());
				
				if (nextNum < lastNum) {
					reMap = questionUserList.get(i);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reMap;
	}

	/**
	 * 获取当前用户 下 未处理 正在处理 正在流转的工单总数
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String getTicketNumber(Map<String, Object> map) throws Exception {
		String systemIndex = map.get("systemIndex").toString();
		StringBuffer sql = new StringBuffer("select count(*) as count from ").append(TableNameUtil.getTableName(TICKET, systemIndex)
			+ " where ownerId = ? and status in ('0','1','2','5')");
		
		Map<String, Object> selectForMap = commonDao.selectForMap(sql.toString(), map.get("userId"));
		
		return selectForMap.get("count").toString();
	}

	/**
	 * 通过问题分类id 查询 部门 id
	 * 
	 * @param dataMap
	 * @return
	 */
	public Map<String, Object> getDeptIdByQuestionId(Map<String, Object> dataMap) {
		Map<String, Object> questionIdMap = new HashMap<String, Object>();
		
		try {
			// 通过问题分类id 查询 部门 id
			Map<String, Object> questionParamsMap = new HashMap<String, Object>();
			questionParamsMap.put("uuid", dataMap.get("questionTypeId"));

			StringBuffer questionSql = new StringBuffer(" select parentId,departmentId from ").append(QUESTION_TYPE);
			List<Object> questionParams = new ArrayList<Object>();

			handleSql(questionParamsMap, questionSql, questionParams, null);
			questionIdMap = commonDao.selectForMap(questionSql.toString(), questionParams.toArray());
			
			// 判断该问题分类是否是子分类
			if (!"0".equals(questionIdMap.get("parentId")) || null != questionIdMap.get("parentId")) {
				questionParamsMap.clear();
				questionParamsMap.put("uuid", questionIdMap.get("parentId"));
				questionParams = new ArrayList<Object>();
				
				handleSql(questionParamsMap, new StringBuffer(), questionParams, null);
				questionIdMap = commonDao.selectForMap(questionSql.toString(), questionParams.toArray());
			}
		} catch (Exception e) {
			questionIdMap = new HashMap<String, Object>();
		}
		
		return questionIdMap;
	}

	/**
	 * 部门Id + 角色Id 查询 管理员Id、
	 * 
	 * @param deptId
	 * @return
	 */
	public Map<String, Object> selectUserByQuestionType(String deptId) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			List<Object> roleParams = new ArrayList<Object>();
			Map<String, Object> roleParamsMap = new HashMap<String, Object>();
			List<Object> userParams = new ArrayList<Object>();
			Map<String, Object> userParamsMap = new HashMap<String, Object>();
			
			roleParamsMap.put("code", CODE_DEPARTMENT_ADMIN);

			// 查询管理员的角色 id
			StringBuffer roleSql = new StringBuffer(" select uuid from ").append(USER_ROLE);

			handleSql(roleParamsMap, roleSql, roleParams, null);
			Map<String, Object> roleIdMap = commonDao.selectForMap(roleSql.toString(), roleParams.toArray());

			// 通过 部门Id 及 角色Id 查询 用户
			StringBuffer userSql = new StringBuffer(" select uuid from ").append(USER);

			userParamsMap.put("roleId", roleIdMap.get("uuid"));
			userParamsMap.put("departmentId", deptId);

			handleSql(userParamsMap, userSql, userParams, null);
			Map<String, Object> userIdMap = commonDao.selectForMap(userSql.toString(), userParams.toArray());

			reMap.put("userId", userIdMap.get("uuid"));

		} catch (Exception e) {
			reMap.put(KEY_STATE, STATE_TWO);
			return reMap;
		}

		return reMap;
	}

}
