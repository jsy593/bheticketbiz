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
public class ReplyBusinessImpl extends BaseBusiness {

	public Map<String, Object> selectOne(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();

		List<Object> params = new ArrayList<Object>();
		
		//获取systemIndex,拼装表名
		String systemIndex =dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");

		StringBuffer sql = new StringBuffer("select uuid,ticketId,userId,attachmentUrl,attachmentName,replyContent,status,createTime from ").append(TableNameUtil.getTableName(REPLY, systemIndex));

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
		
		dataMap.put("orderBy", "createTime asc");
		
		//获取systemIndex,拼装表名
		String systemIndex =dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");
		
		StringBuffer sql = new StringBuffer(" select uuid,ticketId,userId,attachmentUrl,attachmentName,replyContent,status,createTime from ").append(TableNameUtil.getTableName(REPLY, systemIndex));

		handleSql(dataMap, sql, params, null);
		
		List<Map<String, Object>> REPLYList = commonDao.selectList(sql.toString(), params);

		if (!CommonUtil.isEmpty(REPLYList) && REPLYList.size() > 0) {
			reMap.put(KEY_STATE, STATE_ONE);
			reMap.put("list", REPLYList);
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
		
		//获取systemIndex,拼装表名
		String systemIndex =dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");
		
		Integer insertManual = commonDao.insertManual(dataMap, TableNameUtil.getTableName(REPLY, systemIndex));
		
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
		
		Integer updateOne = commonDao.updateOne(dataMap, TableNameUtil.getTableName(REPLY, systemIndex));
		
		//updateOne代表返回影响的条数
		if (updateOne == 1) {
			reMap.put(KEY_STATE, STATE_ONE);
		} else {
			reMap.put(KEY_STATE, STATE_FIFTEEN);
		}

		return reMap;
	}
}
