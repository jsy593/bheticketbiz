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
import com.bhe.util.common.CommonUtil;
import com.bhe.util.common.UuidUtil;

/**
 * 
 * @author zhangyu
 *
 */
@Service
public class QuestionTypeUserMapBusinessImpl extends BaseBusiness {

	public Map<String, Object> selectOne(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer sql = new StringBuffer("select uuid,userId,questionTypeId,createTime,status,systemIndex from ").append(QUESTION_TYPE_USER);

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

	public Map<String, Object> selectList(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(" select uuid,userId,questionTypeId,createTime,status,systemIndex from ").append(QUESTION_TYPE_USER);
		
		handleSql(dataMap, sql, params, null);
		
		List<Map<String, Object>> QUESTION_TYPE_USERList = commonDao.selectList(sql.toString(), params);

		if (!CommonUtil.isEmpty(QUESTION_TYPE_USERList) && QUESTION_TYPE_USERList.size() > 0) {
			reMap.put(KEY_STATE, STATE_ONE);
			reMap.put("list", QUESTION_TYPE_USERList);
		} else {
			reMap.put(KEY_STATE, STATE_FIFTEEN);
		}
		
		return reMap;
	}

	public Map<String, Object> insert(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		dataMap.put("uuid", UuidUtil.generateUUID());
		dataMap.put("createTime", new Date());
		dataMap.put("status", 1);
		
		Integer insertManual = commonDao.insertManual(dataMap, QUESTION_TYPE_USER);
		
		//insertManual代表返回影响的条数
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
		
		Integer updateOne = commonDao.updateOne(dataMap, QUESTION_TYPE_USER);
		
		//updateOne代表返回影响的条数
		if (updateOne == 1) {
			reMap.put(KEY_STATE, STATE_ONE);
		} else {
			reMap.put(KEY_STATE, STATE_FIFTEEN);
		}
		
		return reMap;
	}
}
