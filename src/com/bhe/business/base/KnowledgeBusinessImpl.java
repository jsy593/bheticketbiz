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
import com.bhe.util.common.TableNameUtil;
import com.bhe.util.common.UuidUtil;

/**
 * 
 * @author zhangyu
 *
 */
@Service
public class KnowledgeBusinessImpl extends BaseBusiness {

	public Map<String, Object> selectOne(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		List<Object> params = new ArrayList<Object>();
		
		//获取systemIndex,拼装表名
		String systemIndex = dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");
		
		StringBuffer sql = new StringBuffer(" select uuid,userId,question,answer,attatmentUrl,attatmentName,questionTypeId,createTime,status,rank from ").append(TableNameUtil.getTableName(KNOWLEDGE, systemIndex));

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
		List<Map<String, Object>> userList = null;
		
		//获取systemIndex,拼装表名
		String systemIndex = dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");
		
		StringBuffer sql = new StringBuffer(" select uuid,userId,question,answer,attatmentUrl,attatmentName,questionTypeId,createTime,status,rank from ").append(TableNameUtil.getTableName(KNOWLEDGE, systemIndex));
		
		dataMap.put("orderBy", "rank");
		
		if(CommonUtil.isEmpty(dataMap.get("status"))){
			dataMap.remove("status");
		}
		
		//得到搜索条件
		Object content = dataMap.get("content");
		
		if (!CommonUtil.isEmpty(content)) {//有搜索条件
			dataMap.put("question", content);
			dataMap.remove("content");
			handleSql(dataMap, sql, params, "question");
			 userList = commonDao.selectList(sql.toString(), params);
		} else {//无搜索条件
			dataMap.remove("content");
			handleSql(dataMap, sql, params, null);
			userList = commonDao.selectList(sql.toString(), params);
		}

		if (!CommonUtil.isEmpty(userList) && userList.size() > 0) {
			reMap.put(KEY_STATE, STATE_ONE);
			reMap.put("list", userList);
		} else {
			reMap.put(KEY_STATE, STATE_FIFTEEN);
		}
		
		return reMap;
		
	}

	public Map<String, Object> insert(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		dataMap.put("uuid", UuidUtil.generateUUID());
		dataMap.put("createTime", new Date());
		
		//获取systemIndex,拼装表名
		String systemIndex =dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");
		
		Integer insertManual = commonDao.insertManual(dataMap, TableNameUtil.getTableName(KNOWLEDGE, systemIndex));
		
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
		
		//获取systemIndex,拼装表名
		String systemIndex =dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");
		
		Integer updateOne = commonDao.updateOne(dataMap, TableNameUtil.getTableName(KNOWLEDGE, systemIndex));
		
		//updateOne代表返回影响的条数
		if (updateOne == 1) {
			reMap.put(KEY_STATE, STATE_ONE);
		} else {
			reMap.put(KEY_STATE, STATE_FIFTEEN);
		}
		
		return reMap;
	}
}
