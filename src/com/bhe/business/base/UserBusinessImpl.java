package com.bhe.business.base;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bhe.business.BaseBusiness;
import com.bhe.business.ISystem;
import com.bhe.business.ISystemBusiness;
import com.bhe.util.common.CommonUtil;
import com.bhe.util.common.MD5Util;
import com.bhe.util.common.UuidUtil;

/**
 * 
 * @author zhangyu
 *
 */
@Service
public class UserBusinessImpl extends BaseBusiness {
	@Autowired
	private ISystem systemImpl;

	public Map<String, Object> selectOne(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();

		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(
				"select uuid,username,password,roleId,departmentId,email,mobile,phone,realName,sex,status,remark,createTime,workerNo,systemIndex from ")
						.append(USER);

		handleSql(dataMap, sql, params, null);

		Map<String, Object> map = commonDao.selectForMap(sql.toString(), params.toArray());

		if (!CommonUtil.isEmpty(map)) {
			reMap.put(KEY_STATE, STATE_ONE);
			reMap.put("data", map);
		} else {
			reMap.put(KEY_STATE, STATE_FIFTEEN);
		}

		return reMap;
	}

	// 搜索的内容字段请传content字段
	public Map<String, Object> selectList(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();

		List<Object> params = new ArrayList<Object>();
		List<Map<String, Object>> userList = null;

		StringBuffer sql = new StringBuffer(
				" select uuid,username,password,roleId,departmentId,email,mobile,phone,realName,sex,status,remark,createTime,workerNo,systemIndex from ")
						.append(USER);

		if (CommonUtil.isEmpty(dataMap.get("status"))) {
			dataMap.remove("status");
		}

		Object content = dataMap.get("content");

		if (!CommonUtil.isEmpty(content)) {// 有搜索条件
			dataMap.put("username", content);
			dataMap.remove("content");
			handleSql(dataMap, sql, params, "username");
			userList = commonDao.selectList(sql.toString(), params);
		} else {// 无搜索条件
			dataMap.remove("content");
			handleSql(dataMap, sql, params, null);
			userList = commonDao.selectList(sql.toString(), params);
		}

		if (!CommonUtil.isEmpty(userList) && userList.size() > 0) {
			reMap.put(KEY_STATE, STATE_ONE);
			reMap.put("list", userList);
			reMap.put("totalCount", commonDao.selectTotalCount(sql.toString(), params));
		} else {
			reMap.put(KEY_STATE, STATE_FIFTEEN);
		}

		return reMap;
	}

	public Map<String, Object> insert(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();

		String password = MD5Util.string2MD5(dataMap.get("password").toString());
		dataMap.put("password", password);
		dataMap.put("uuid", UuidUtil.generateUUID());
		dataMap.put("createTime", new Date());
		if (CommonUtil.isEmpty(dataMap.get("status"))) {
			dataMap.put("status", 0);
		}
		dataMap.put("workerNo", systemImpl.getNextKey("workerNo"));

		Integer insertManual = commonDao.insertManual(dataMap, USER);

		// insertManual代表返回影响的条数
		if (insertManual == 1) {
			reMap.put(KEY_STATE, STATE_ONE);
		} else {
			reMap.put(KEY_STATE, STATE_FIFTEEN);
		}

		return reMap;
	}

	public Map<String, Object> update(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();

		dataMap.put("pk", "uuid");

		Integer updateOne = commonDao.updateOne(dataMap, USER);

		// updateOne代表返回影响的条数
		if (updateOne == 1) {
			reMap.put(KEY_STATE, STATE_ONE);
		} else {
			reMap.put(KEY_STATE, STATE_FIFTEEN);
		}

		return reMap;
	}
}
