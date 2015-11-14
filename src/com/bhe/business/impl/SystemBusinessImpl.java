package com.bhe.business.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bhe.business.BaseBusiness;
import com.bhe.business.ISystem;
import com.bhe.business.ISystemBusiness;
import com.bhe.business.base.AccessSystemBusinessImpl;
import com.bhe.business.base.UserBusinessImpl;
import com.bhe.util.common.CommonUtil;
import com.bhe.util.common.SendMail;
import com.bhe.util.common.UuidUtil;

@Service
public class SystemBusinessImpl extends BaseBusiness implements ISystemBusiness {
	@Autowired
	private AccessSystemBusinessImpl systemBusinessImpl;

	@Autowired
	private ISystem systemImpl;
	@Autowired
	private UserBusinessImpl userBusinessImpl;

	/**
	 * 添加系统
	 * 
	 * @author jsy
	 * @param systemMap
	 * @return
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> insertSystem(Map<String, Object> systemMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> sysMap = new HashMap<String, Object>();

		if (!CommonUtil.isEmpty(systemMap.get("systemName"))) {
			sysMap.put("systemName", systemMap.get("systemName"));

			try {
				Map<String, Object> map = (Map<String, Object>) systemBusinessImpl.selectOne(sysMap).get("data");

				if (CommonUtil.isEmpty(map)) {// 数据库中不存在该系统名
					String uuid = UuidUtil.generateUUID();
					systemMap.put("uuid", uuid);
					systemMap.put("status", 0);
					systemMap.put("createTime", new Date());

					reMap = systemBusinessImpl.insert(systemMap);
				} else {
					reMap.put(KEY_STATE, STATE_ELEVEN);// 数据库中已存在该系统名
				}
			} catch (Exception e) {
				e.printStackTrace();
				reMap.put(KEY_STATE, STATE_ZERO);
				return reMap;
			}
		}

		return reMap;
	}

	/**
	 * 查询系统列表
	 * 
	 * @author jsy
	 * @param sysMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> selectSystem(Map<String, Object> sysMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> temp = new HashMap<String, Object>();
		Map<String, Object> userMap = new HashMap<String, Object>();
		Map<String, Object> user = new HashMap<String, Object>();
		try {
			reMap = systemBusinessImpl.selectList(sysMap);

			if (!CommonUtil.isEmpty(reMap.get("list"))) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) reMap.get("list");

				for (Map<String, Object> map : list) {
					String userId = map.get("userId").toString();
					temp.put("uuid", userId);

					userMap = userBusinessImpl.selectOne(temp);
					if (!CommonUtil.isEmpty(userMap.get("data"))) {
						user = (Map<String, Object>) userMap.get("data");
						map.put("username", user.get("username"));
					}
					temp.clear();
					userMap.clear();
					user.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@Override
	public Map<String, Object> selectSystemOne(Map<String, Object> sysMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = systemBusinessImpl.selectOne(sysMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	// 审核接入系统
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> checkSystem(Map<String, Object> sysMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		String email = null;
		String userId = null;

		try {
			// 查询userId
			Map<String, Object> tMap = new HashMap<String, Object>();
			tMap.put("uuid", sysMap.get("uuid"));

			Map<String, Object> systemMap = systemBusinessImpl.selectOne(tMap);

			if (!CommonUtil.isEmpty(systemMap.get("data"))) {
				Map<String, Object> resultMap = (Map<String, Object>) systemMap.get("data");
				userId = resultMap.get("userId").toString();
			}

			// 根据userId查询用户email
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("uuid", userId);

			Map<String, Object> tempMap = userBusinessImpl.selectOne(userMap);

			if (!CommonUtil.isEmpty(tempMap.get("data"))) {
				Map<String, Object> resultMap = (Map<String, Object>) tempMap.get("data");
				email = resultMap.get("email").toString();
			}

			String appId = UuidUtil.generateUUID();
			String appKey = UuidUtil.generateUUID();
			sysMap.put("appId", appId);
			sysMap.put("appKey", appKey);

			if (!CommonUtil.isEmpty(sysMap.get("status"))) {
				String status = sysMap.get("status").toString();

				if ("1".equals(status)) {
					Long systemIndex = systemImpl.createTableWithSysName(sysMap.get("systemName").toString());

					reMap.put("systemIndex", systemIndex);
					reMap = systemBusinessImpl.update(sysMap);
					try {
						if (!CommonUtil.isEmpty(email)) {
							// 发送审核成功邮件
							SendMail.send(email, "百居易电子商务有限公司",
									"<h1>百居易电子商务有限公司</h1><font>您申请接入的系统已审核成功，您的<br/>appId:" + appId + ",<br/>appKey:"
											+ appKey
											+ "。请点击链接登录。</font><a href='http://localhost:8080/bheticketview/'>登录</a>");
						}
					} catch (Exception e) {

					}
				} else if ("3".equals(status)) {
					reMap = systemBusinessImpl.update(sysMap);
					try {
						if (!CommonUtil.isEmpty(email)) {
							// 发送审核失败邮件
							SendMail.send(email, "百居易电子商务有限公司",
									"<h1>百居易电子商务有限公司</h1><font>您申请接入的系统审核失败，请点击登录链接重新申请。</font><a href='http://localhost:8080/bheticketview/'>登录</a>");
						}
					} catch (Exception e) {

					}
				} else {
					reMap = systemBusinessImpl.update(sysMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;

	}

	// 维护接入系统
	@Override
	public Map<String, Object> manageSystem(Map<String, Object> sysMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			systemBusinessImpl.update(sysMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@Override
	public Map<String, Object> updateSystemStatus(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = systemBusinessImpl.update(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@Override
	public Map<String, Object> selectSystemIndex(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = systemBusinessImpl.selectOne(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

}
