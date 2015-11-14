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
public class TicketMoveBusinessImpl extends BaseBusiness {

	public Map<String, Object> selectOne(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		List<Object> params = new ArrayList<Object>();
		
		//获取systemIndex,拼装表名
		String systemIndex = dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");
		
		StringBuffer sql = new StringBuffer("select uuid,toUserId,fromUserId,audtId,ticketId,createTime,reason,status,refuseReason from ").append(TableNameUtil.getTableName(TICKET_MOVE, systemIndex));

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
		
		//获取systemIndex,拼装表名
		String systemIndex = dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");
		
		StringBuffer sql = new StringBuffer(" select uuid,toUserId,fromUserId,audtId,ticketId,createTime,reason,status,refuseReason from ").append(TableNameUtil.getTableName(TICKET_MOVE, systemIndex));


		handleSql(dataMap, sql, params, null);

		List<Map<String, Object>> TICKET_MOVEList = commonDao.selectList(sql.toString(), params);

		if (!CommonUtil.isEmpty(TICKET_MOVEList) && TICKET_MOVEList.size() > 0) {
			reMap.put(KEY_STATE, STATE_ONE);
			reMap.put("list", TICKET_MOVEList);
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
		String systemIndex = dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");
		
		Integer insertManual = commonDao.insertManual(dataMap, TableNameUtil.getTableName(TICKET_MOVE, systemIndex));
		
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
		String systemIndex = dataMap.get("systemIndex").toString();
		dataMap.remove("systemIndex");
		
		Integer updateOne = commonDao.updateOne(dataMap, TableNameUtil.getTableName(TICKET_MOVE, systemIndex));
		
		//updateOne代表返回影响的条数
		if (updateOne == 1) {
			reMap.put(KEY_STATE, STATE_ONE);
		} else {
			reMap.put(KEY_STATE, STATE_FIFTEEN);
		}
		
		return reMap;
	}
}
