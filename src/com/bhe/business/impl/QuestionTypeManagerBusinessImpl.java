package com.bhe.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bhe.business.BaseBusiness;
import com.bhe.business.IQuestionTypeManagerBusiness;
import com.bhe.business.base.DepartmentBusinessImpl;
import com.bhe.business.base.QuestionTypeBusinessImpl;
import com.bhe.business.base.QuestionTypeUserMapBusinessImpl;
import com.bhe.business.base.UserBusinessImpl;
import com.bhe.dao.common.impl.CommonDaoImpl;
import com.bhe.util.common.CommonUtil;
import com.bhe.util.common.UuidUtil;

/**
 * 
 * @author zhangyu
 *
 */
@Service
public class QuestionTypeManagerBusinessImpl extends BaseBusiness implements IQuestionTypeManagerBusiness {

	@Autowired
	private QuestionTypeBusinessImpl questionTypeBusinessImpl;
	@Autowired
	private QuestionTypeUserMapBusinessImpl questionTypeUserMapBusinessImpl;
	@Autowired
	private DepartmentBusinessImpl deptManagerBusinessImpl;
	@Autowired
	private UserBusinessImpl userBusinessImpl;

	// 查询该部门是否有对应的问题分类
	@Override
	public Map<String, Object> selectQueTypeByDept(Map<String, Object> deptMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> queMap = new HashMap<String, Object>();

		if (!CommonUtil.isEmpty(deptMap.get("uuid"))) {
			queMap.put("departmentId", deptMap.get("uuid"));

			try {
				// 查询该部门是否有对应的问题分类
				queMap = questionTypeBusinessImpl.selectOne(queMap);
			} catch (Exception e) {
				e.printStackTrace();
				reMap.put(KEY_STATE, STATE_ZERO);
				return reMap;
			}
		}

		return reMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> selectQuestionTypeList(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = questionTypeBusinessImpl.selectList(dataMap);

			if (STATE_ONE.equals(reMap.get("state"))) {
				List<Map<String, Object>> typeList = (List<Map<String, Object>>) reMap.get("list");

				if (!CommonUtil.isEmpty(dataMap.get("departmentId")) && !"0".equals(dataMap.get("parentId"))) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					for (Map<String, Object> map : typeList) {
						Map<String, Object> paramsMap = new HashMap<String, Object>();
						paramsMap.put("parentId", map.get("uuid"));
						List<Map<String, Object>> qtblist = (List<Map<String, Object>>) questionTypeBusinessImpl.selectList(paramsMap).get("list");
						if (!CommonUtil.isEmpty(qtblist)) {
							list.addAll(0, qtblist);
						}
					}
					reMap.put("list", list);
				} else {

					for (Map<String, Object> map : typeList) {
						Map<String, Object> deptParamsMap = new HashMap<String, Object>();
						deptParamsMap.put("uuid", (String) map.get("departmentId"));
						Map<String, Object> deptMap = (Map<String, Object>) deptManagerBusinessImpl
								.selectOne(deptParamsMap).get("data");

						if (deptMap != null) {
							map.put("deptName", deptMap.get("name"));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> selectQuestionTypeOne(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = questionTypeBusinessImpl.selectOne(dataMap);

			if (STATE_ONE.equals(reMap.get("state"))) {
				Map<String, Object> map = (Map<String, Object>) reMap.get("data");
				Map<String, Object> paramsMap = new HashMap<String, Object>();

				paramsMap.put("uuid", map.get("departmentId"));

				Map<String, Object> deptMap = (Map<String, Object>) deptManagerBusinessImpl.selectOne(paramsMap)
						.get("data");
				map.put("deptName", deptMap.get("name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@Override
	@Transactional
	public Map<String, Object> insertQuestionType(Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();

		String questionTypeUuid = UuidUtil.generateUUID();
		dataMap.put("uuid", questionTypeUuid);
		dataMap.put("createTime", new Date());
		String userId = (String) dataMap.get("userId");
		dataMap.remove("userId");

		if (!"0".equals(userId)) {
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("userId", userId);
			paramsMap.put("questionTypeId", questionTypeUuid);
			paramsMap.put("systemIndex", dataMap.get("systemIndex"));

			questionTypeUserMapBusinessImpl.insert(paramsMap);
		}
		reMap = questionTypeBusinessImpl.insert(dataMap);

		return reMap;
	}

	@Override
	public Map<String, Object> updateQuestionType(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = questionTypeBusinessImpl.update(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@Override
	public Map<String, Object> selectQuestionTypeForUserList(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = questionTypeUserMapBusinessImpl.selectList(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@Override
	public Map<String, Object> selectQuestionTypeForUserOne(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = questionTypeUserMapBusinessImpl.selectOne(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> updateQuestionTypeForUser(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> params = new HashMap<String, Object>();

		try {
			if (!CommonUtil.isEmpty(dataMap.get("uuid"))) {
				reMap = questionTypeUserMapBusinessImpl.update(dataMap);
			} else {
				params.put("userId", dataMap.get("userId"));
				params.put("questionTypeId", dataMap.get("questionTypeId"));

				Map<String, Object> questionUser = questionTypeUserMapBusinessImpl.selectOne(params);

				Map<String, Object> questionUserData = (Map<String, Object>) questionUser.get("data");

				if (!CommonUtil.isEmpty(questionUserData)) {
					params.clear();
					params.put("uuid", questionUserData.get("uuid"));
					params.put("status", dataMap.get("status"));

					reMap = questionTypeUserMapBusinessImpl.update(params);
				} else {
					reMap = questionTypeUserMapBusinessImpl.insert(dataMap);
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
	public Map<String, Object> insertQuestionTypeForUser(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		try {
			reMap = questionTypeUserMapBusinessImpl.insert(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> selectQuestionTypeToUser(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = questionTypeUserMapBusinessImpl.selectList(dataMap);

			if (STATE_ONE.equals(reMap.get("state"))) {
				List<Map<String, Object>> listMap = (List<Map<String, Object>>) reMap.get("list");

				for (Map<String, Object> map : listMap) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("uuid", map.get("userId"));

					Map<String, Object> user = (Map<String, Object>) userBusinessImpl.selectOne(params).get("data");

					map.put("realName", user.get("realName"));
				}
			}

		} catch (Exception e) {
			reMap = new HashMap<String, Object>();
			e.printStackTrace();
		}

		return reMap;
	}
}
