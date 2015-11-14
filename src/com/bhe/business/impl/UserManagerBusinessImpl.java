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
import com.bhe.business.ISystem;
import com.bhe.business.IUserManagerBusiness;
import com.bhe.business.base.AccessSystemBusinessImpl;
import com.bhe.business.base.DepartmentBusinessImpl;
import com.bhe.business.base.QuestionTypeBusinessImpl;
import com.bhe.business.base.UserBusinessImpl;
import com.bhe.business.base.UserRoleBusinessImpl;
import com.bhe.util.common.CommonUtil;
import com.bhe.util.common.MD5Util;
import com.bhe.util.common.SendMail;
import com.bhe.util.common.SendMsg_webchinese;
import com.bhe.util.common.SendSms;
import com.bhe.util.common.UuidUtil;

/**
 * 
 * @author zhangyu
 *
 */
@Service
public class UserManagerBusinessImpl extends BaseBusiness implements IUserManagerBusiness {

	@Autowired
	private ISystem systemImpl;

	@Autowired
	private AccessSystemBusinessImpl accessSystemBusinessImpl;

	@Autowired
	private UserRoleBusinessImpl userRoleBusinessImpl;

	@Autowired
	private UserBusinessImpl userBusinessImpl;

	@Autowired
	private DepartmentBusinessImpl departmentBusinessImpl;
	@Autowired
	private QuestionTypeBusinessImpl questionTypeBusinessImpl;

	// 注册
	@Override
	public Map<String, Object> insertUserInfo(Map<String, Object> user) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> usMap = new HashMap<String, Object>();

		if (CommonUtil.isEmpty(user.get("username"))) {
			reMap.put(KEY_STATE, STATE_FOUR);
			return reMap;
		}
		if (CommonUtil.isEmpty(user.get("password"))) {
			reMap.put(KEY_STATE, STATE_NINE);
			return reMap;
		}

		usMap.put("username", user.get("username"));
		Map<String, Object> userMap = this.selectUserList(usMap);

		if (!CommonUtil.isEmpty(userMap)) {
			if (userMap.get(KEY_STATE).equals(STATE_ONE)) {
				reMap.put(KEY_STATE, STATE_EIGHT);
				return reMap;
			} else if (userMap.get(KEY_STATE).equals(STATE_ZERO)) {
				reMap.put(KEY_STATE, STATE_ZERO);
				return reMap;
			} else {
				try {
					String roleId = selectRoleIdByCode(CODE_SYSTEM_ADMIN);

					user.put("roleId", roleId);

					reMap = userBusinessImpl.insert(user);
				} catch (Exception e) {
					e.printStackTrace();
					reMap.put(KEY_STATE, STATE_ZERO);
					return reMap;
				}
			}
		} else {
			reMap.put(KEY_STATE, STATE_TWO);
		}

		return reMap;
	}

	public Map<String, Object> insertSysInfo(Map<String, Object> systemMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		if (!CommonUtil.isEmpty(systemMap.get("systemName"))) {
			Map<String, Object> sysMap = selectSysOne(systemMap);

			if (CommonUtil.isEmpty(sysMap)) {
				String uuid = UuidUtil.generateUUID();
				String appId = UuidUtil.generateUUID();
				String appKey = UuidUtil.generateUUID();

				systemMap.put("uuid", uuid);
				systemMap.put("appId", appId);
				systemMap.put("appKey", appKey);
				systemMap.put("uuid", uuid);
				systemMap.put("status", 0);
				systemMap.put("createTime", new Date());

				try {
					commonDao.insertManual(systemMap, ACCESS_SYSTEM);
					reMap.put(KEY_STATE, STATE_ONE);
				} catch (Exception e) {
					log.error("添加数据失败", e);
					reMap.put(KEY_STATE, STATE_TWO);
				}
			} else {
				reMap.put(KEY_STATE, STATE_ELEVEN);
			}
		} else {
			reMap.put(KEY_STATE, STATE_TEN);

			reMap.put(KEY_STATE, STATE_TWO);
		}
		return reMap;
	}

	public Map<String, Object> selectSysOne(Map<String, Object> dataMap) {
		Map<String, Object> reMap = null;

		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(
				"select uuid, systemName, appId, appKey, userId, status, remark, createTime from ")
						.append(ACCESS_SYSTEM);

		try {
			handleSql(dataMap, sql, params, null);

			reMap = commonDao.selectForMap(sql.toString(), params.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reMap;
	}

	@Override
	public Map<String, Object> selectUserOne(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = userBusinessImpl.selectOne(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	public Map<String, Object> selectUserList(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = userBusinessImpl.selectList(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;

	}

	public Map<String, Object> selectRoleOne(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = userRoleBusinessImpl.selectOne(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	public List<Map<String, Object>> selectRoleList(Map<String, Object> dataMap) {
		StringBuffer sql = new StringBuffer(
				" select uuid,username,password,roleId,departmentId,email,mobile,phone,realName,sex,status,remark,createTime,workerNo,systemIndex from ")
						.append(USER);

		List<Map<String, Object>> reList = null;
		List<Object> params = new ArrayList<Object>();

		try {
			handleSql(dataMap, sql, params, null);
			reList = commonDao.selectList(sql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reList;
	}

	// 查询admin
	public Map<String, Object> selectAdminList(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> rMap = new HashMap<String, Object>();

		rMap.put("code", CODE_SYSTEM_ADMIN);

		try {
			Map<String, Object> roleMap = userRoleBusinessImpl.selectOne(rMap);

			if (STATE_ONE.equals(roleMap.get(KEY_STATE))) {
				@SuppressWarnings("unchecked")
				Map<String, Object> resultMap = (Map<String, Object>) roleMap.get("data");
				Object roleId = resultMap.get("uuid");
				dataMap.put("roleId", roleId);

				reMap = userBusinessImpl.selectList(dataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	// 查询admin
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectDeptAdminList(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> rMap = new HashMap<String, Object>();

		rMap.put("code", CODE_DEPARTMENT_ADMIN);

		try {
			Map<String, Object> roleMap = userRoleBusinessImpl.selectOne(rMap);

			if (STATE_ONE.equals(roleMap.get(KEY_STATE))) {
				@SuppressWarnings("unchecked")
				Map<String, Object> resultMap = (Map<String, Object>) roleMap.get("data");
				Object roleId = resultMap.get("uuid");
				dataMap.put("roleId", roleId);

				reMap = userBusinessImpl.selectList(dataMap);

				if (STATE_ONE.equals(reMap.get("state"))) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) reMap.get("list");

					for (Map<String, Object> map : list) {
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("uuid", map.get("departmentId"));

						Map<String, Object> dept = departmentBusinessImpl.selectOne(params);

						if (STATE_ONE.equals(dept.get("state"))) {
							Map<String, Object> deptData = (Map<String, Object>) dept.get("data");
							map.put("deptName", deptData.get("name"));
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

	// 查询admin
	@Override
	public Map<String, Object> selectStaffList(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> rMap = new HashMap<String, Object>();

		rMap.put("code", CODE_STAFF);

		try {
			Map<String, Object> roleMap = userRoleBusinessImpl.selectOne(rMap);

			if (STATE_ONE.equals(roleMap.get(KEY_STATE))) {
				@SuppressWarnings("unchecked")
				Map<String, Object> resultMap = (Map<String, Object>) roleMap.get("data");
				Object roleId = resultMap.get("uuid");
				dataMap.put("roleId", roleId);

				reMap = userBusinessImpl.selectList(dataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> login(Map<String, Object> map) {
		Map<String, Object> usernameMap = new HashMap<String, Object>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Map<String, Object> reMap = new HashMap<String, Object>();

		// 0 系统错误 1 成功 4 用户名不存在 5 密码错误 6 账号禁止登陆 7 账号异常
		String userName = map.get("username").toString();
		String password = MD5Util.string2MD5(map.get("password").toString());

		usernameMap.put("username", userName);

		paramsMap.put("username", userName);
		paramsMap.put("password", password);

		// 单个用户
		Map<String, Object> userMap = this.selectUserList(usernameMap);

		try {

			if (!CommonUtil.isEmpty(userMap)) {
				String state = userMap.get("state").toString();

				if ("1".equals(state)) {
					// 用户存在
					List<Map<String, Object>> userList = (List<Map<String, Object>>) userMap.get("list");

					if (!CommonUtil.isEmpty(userList) && userList.size() > 0 && userList.size() < 2) {
						Map<String, Object> userMapOne = (Map<String, Object>) this.selectUserOne(paramsMap)
								.get("data");

						if (!CommonUtil.isEmpty(userMapOne)) {

							if (userMapOne.containsKey("status")) {
								if ("1".equals(userMapOne.get("status").toString())) {
									Map<String, Object> roleParamsMap = new HashMap<String, Object>();
									Map<String, Object> sysParamsMap = new HashMap<String, Object>();
									Map<String, Object> departmentParamsMap = new HashMap<String, Object>();

									// 角色
									roleParamsMap.put("uuid", userMapOne.get("roleId"));

									Map<String, Object> roleMap = (Map<String, Object>) this
											.selectRoleOne(roleParamsMap).get("data");

									if (CODE_SUPER_ADMIN.equals(roleMap.get("code"))
											|| CODE_SYSTEM_ADMIN.equals(roleMap.get("code"))) {
										reMap.put("state", STATE_ONE);
										reMap.put("userInfo", userMapOne);
										reMap.put("roleInfo", roleMap);

										return reMap;
									}

									// 系统
									sysParamsMap.put("systemIndex", userMapOne.get("systemIndex"));

									Map<String, Object> sysMap = (Map<String, Object>) accessSystemBusinessImpl.selectOne(sysParamsMap).get("data");

									if (!CommonUtil.isEmpty(sysMap) && STATE_ONE.equals(sysMap.get("status"))) {
									} else {
										reMap.put("state", STATE_SIX);
										return reMap;
									}

									// 部门
									departmentParamsMap.put("uuid", userMapOne.get("departmentId"));

									Map<String, Object> departmentMap = (Map<String, Object>) departmentBusinessImpl
											.selectOne(departmentParamsMap).get("data");

									if (STATE_ONE.equals(departmentMap.get("status"))) {
										reMap.put("state", STATE_ONE);
										reMap.put("userInfo", userMapOne);
										reMap.put("roleInfo", roleMap);
										reMap.put("departmentInfo", departmentMap);
										reMap.put("sysInfo", sysMap);
									} else {
										reMap.put("state", STATE_SIX);
									}

								} else {
									reMap.put("state", STATE_SIX);
								}
							}
						} else {
							reMap.put("state", STATE_FIVE);
						}
					} else if (!CommonUtil.isEmpty(userList) && userList.size() > 1) {
						reMap.put("state", STATE_SEVEN);
					} else {
						reMap.put("state", STATE_FOUR);
					}
				} else {
					reMap.put("state", STATE_FOUR);
				}
			}
		} catch (Exception e) {
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	@Override
	public Map<String, Object> updateUserOne(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		// 账户status为正常状态
		if (!CommonUtil.isEmpty(dataMap.get("uuid")) && "1".equals(dataMap.get("status").toString())) {
			try {
				dataMap.remove("status");
				reMap = userBusinessImpl.update(dataMap);
			} catch (Exception e) {
				e.printStackTrace();
				reMap.put(KEY_STATE, STATE_ZERO);
				return reMap;
			}
		} else {// 部门管理员异常
			reMap.put(KEY_STATE, STATE_SEVEN);
		}

		return reMap;
	}

	public Map<String, Object> selectQuestionTypeOne(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			reMap = questionTypeBusinessImpl.selectOne(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
			return reMap;
		}

		return reMap;
	}

	// 审核管理员
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> checkUser(Map<String, Object> userMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		String reason = "";

		if (!CommonUtil.isEmpty(userMap.get("reason"))) {
			reason = userMap.get("reason").toString();
			userMap.remove("reason");
		}

		if (!CommonUtil.isEmpty(userMap.get("status")) && !CommonUtil.isEmpty(userMap.get("uuid"))) {
			try {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("uuid", userMap.get("uuid"));

				Map<String, Object> map = userBusinessImpl.selectOne(tempMap);

				reMap = userBusinessImpl.update(userMap);// 审核管理员

				// FIXME
				// 查询该管理员是不是第一次接受审核
				if (!CommonUtil.isEmpty(map.get("data"))) {
					Map<String, Object> temp = (Map<String, Object>) map.get("data");
					String status = temp.get("status").toString();
					String email = "";
					if (!CommonUtil.isEmpty(temp.get("emali"))) {
						email = temp.get("email").toString();
					}
					// 第一次审核
					if ("0".equals(status)) {
						// 判断审核是否通过
						Map<String, Object> resultMap = userBusinessImpl.selectOne(tempMap);

						if (!CommonUtil.isEmpty(resultMap.get("data"))) {
							Map<String, Object> result = (Map<String, Object>) resultMap.get("data");
							String resultStatus = result.get("status").toString();
							try {
								if ("1".equals(resultStatus)) {
									// 发送审核成功邮件
									SendMail.send(email, "百居易电子商务有限公司",
											"<h1>百居易电子商务有限公司</h1><font>您的账号已审核成功，请点击登录链接。</font><a href='//http://localhost:8080/bheticketview/'>登录</a>");
								} else if ("2".equals(resultStatus)) {
									// 发送审核失败邮件
									SendMail.send(email, "百居易电子商务有限公司", "<h1>百居易电子商务有限公司</h1><font>对不起您的账号审核失败了，原因是:"
											+ reason
											+ "。<br/>请点击链接重新注册。</font><a href='//http://localhost:8080/bheticketview/toRegist'>注册</a>");
								}
							} catch (Exception e) {
							}
						}
					}
				}
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
	public Map<String, Object> insertUser(Map<String, Object> userMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> roleMap = new HashMap<String, Object>();

		Object username = userMap.get("username");

		try {
			if (!CommonUtil.isEmpty(username)) {
				Map<String, Object> usMap = new HashMap<String, Object>();
				usMap.put("username", username);

				Map<String, Object> useMap = this.selectUserList(usMap);

				// 判断添加用户角色
				if (!CommonUtil.isEmpty(userMap.get("code"))) {
					roleMap.put("code", userMap.get("code"));

					roleMap = (Map<String, Object>) userRoleBusinessImpl.selectOne(roleMap).get("data");

					if (!CommonUtil.isEmpty(roleMap)) {
						userMap.put("roleId", roleMap.get("uuid"));
						userMap.remove("code");
					} else {
						reMap.put(KEY_STATE, STATE_SIXTEEN);// 角色不存在
						return reMap;
					}
				}

				if (!CommonUtil.isEmpty(useMap)) {
					if (useMap.get(KEY_STATE).equals(STATE_ONE)) {// 该用户存在
						reMap.put(KEY_STATE, STATE_EIGHT);
					} else {
						// 加密
						String password = MD5Util.string2MD5(userMap.get("password").toString());
						userMap.put("password", password);

						String uuid = UuidUtil.generateUUID();
						userMap.put("uuid", uuid);
						userMap.put("status", 1);
						userMap.put("createtime", new Date());
						userMap.put("workerNo", systemImpl.getNextKey("workerNo"));

						commonDao.insertManual(userMap, USER);
						reMap.put(KEY_STATE, STATE_ONE);

					}
				}

			} else {
				reMap.put(KEY_STATE, STATE_TEN);
			}
		} catch (Exception e) {
			log.error("添加数据失败", e);
			reMap.put(KEY_STATE, STATE_TWO);
		}

		return reMap;
	}

	// 将status置为删除状态
	@Override
	public Map<String, Object> deleteDeptAdmin(Map<String, Object> userMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		if (!CommonUtil.isEmpty(userMap.get("uuid"))) {
			userMap.put("status", 2);

			try {
				reMap = userBusinessImpl.update(userMap);
			} catch (Exception e) {
				e.printStackTrace();
				reMap.put(KEY_STATE, STATE_ZERO);
				return reMap;
			}

		}

		return reMap;
	}

	@Override
	@Transactional
	public Map<String, Object> becomeDeptAdmin(Map<String, Object> userMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!CommonUtil.isEmpty(userMap.get("uuid"))) {
			List<Object> params = new ArrayList<Object>();
			userMap.put("status", 1);

			StringBuffer sql = new StringBuffer(" select uuid from ").append(USER);

			try {
				Map<String, Object> deptMap = new HashMap<String, Object>();
				Map<String, Object> staffMap = new HashMap<String, Object>();
				Map<String, Object> handleMap = new HashMap<String, Object>();
				Map<String, Object> deptAdminMap = new HashMap<String, Object>();
				// 得到部门管理员的roleId
				String deptRoleId = selectRoleIdByCode(CODE_DEPARTMENT_ADMIN);

				// 得到普通职员的roleId
				String staffRoleId = selectRoleIdByCode(CODE_STAFF);

				// 查询当前部门的管理者
				handleMap.put("roleId", deptRoleId);// roleId为部门管理员的id
				handleMap.put("departmentId", userMap.get("departmentId"));
				handleMap.put("status", 1);
				handleSql(handleMap, sql, params, null);

				deptAdminMap = commonDao.selectForMap(sql.toString(), params.toArray());
				// 得到当前部门管理者的uuid
				String uuid = deptAdminMap.get("uuid").toString();
				handleMap.clear();
				// 将当前部门的管理者降为普通职员
				handleMap.put("roleId", staffRoleId);
				handleMap.put("uuid", uuid);
				handleMap.put("pk", "uuid");

				commonDao.updateOne(handleMap, USER);
				// 将当前职员或者删除了的部门管理者变成部门管理者
				userMap.put("pk", "uuid");
				userMap.put("status", 1);
				userMap.put("roleId", deptRoleId);

				commonDao.updateOne(userMap, USER);
			} catch (Exception e) {
				resultMap.put(KEY_STATE, STATE_TWO);
				e.printStackTrace();
			}

			resultMap.put(KEY_STATE, STATE_ONE);

		}

		return resultMap;
	}

	public String selectRoleIdByCode(String code) {
		String roleId = null;
		Map<String, Object> roleMap = new HashMap<String, Object>();
		List<Object> Params = new ArrayList<Object>();
		Map<String, Object> roleIdMap = null;

		roleMap.put("code", code);

		StringBuffer Sql = new StringBuffer(" select uuid from ").append(USER_ROLE);

		try {
			handleSql(roleMap, Sql, Params, null);

			roleIdMap = commonDao.selectForMap(Sql.toString(), Params.toArray());

			roleId = roleIdMap.get("uuid").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return roleId;
	}

	@Override
	public Map<String, Object> selectUserBydept(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer(
				"select uuid,username,password,roleId,departmentId,email,mobile,phone,realName,sex,status,remark,createTime,workerNo,systemIndex from ")
						.append(USER);

		try {
			// 根据departmentId和systemId查询user表
			handleSql(dataMap, sql, params, null);
			List<Map<String, Object>> list = commonDao.selectList(sql.toString(), params);

			if (!CommonUtil.isEmpty(list) && list.size() > 0) {
				reMap.put("list", list);
				reMap.put(KEY_STATE, STATE_ONE);
			} else {
				reMap.put(KEY_STATE, STATE_FIFTEEN);
			}

		} catch (Exception e) {
			reMap.put(KEY_STATE, STATE_TWO);
			return reMap;
		}

		return reMap;
	}

	@Override
	public Map<String, Object> recoverStaff(Map<String, Object> dataMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (!CommonUtil.isEmpty(dataMap.get("uuid"))) {
			dataMap.put("status", 1);

			try {
				dataMap.put("pk", "uuid");
				commonDao.updateOne(dataMap, USER);
				resultMap.put(KEY_STATE, STATE_ONE);
			} catch (Exception e) {
				resultMap.put(KEY_STATE, STATE_TWO);
				log.error("添加数据失败", e);
			}

		}

		return resultMap;
	}

	@Override
	public Map<String, Object> selectDeptAdmin(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		try {
			List<Object> paramsList = new ArrayList<Object>();
			StringBuffer deptSql = new StringBuffer(" select uuid,roleName,createTime,level,remark,code from ")
					.append(USER_ROLE).append(" where code=? ");
			StringBuffer userSql = new StringBuffer(
					" select uuid,username,password,roleId,departmentId,email,mobile,phone,realName,sex,status,remark,createTime,workerNo,systemIndex from ")
							.append(USER);

			// 查询部门管理员roleId
			Map<String, Object> deptMap = commonDao.selectForMap(deptSql.toString(), CODE_DEPARTMENT_ADMIN);

			// 查询部门管理员信息
			dataMap.put("roleId", deptMap.get("uuid"));
			handleSql(dataMap, userSql, paramsList, null);

			Map<String, Object> user = commonDao.selectForMap(userSql.toString(), paramsList.toArray());

			if (!CommonUtil.isEmpty(user)) {
				reMap.put("data", user);
				reMap.put(KEY_STATE, STATE_ONE);
			} else {
				reMap.put(KEY_STATE, STATE_FIFTEEN);
			}
		} catch (Exception e) {
			reMap.put(KEY_STATE, STATE_TWO);
			return reMap;
		}

		return reMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> selectDeptUser(Map<String, Object> dataMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> userMap = new HashMap<String, Object>();

		// 根据uuid查询当前人的信息
		userMap = selectUserOne(dataMap);

		if ("1".equals(userMap.get(KEY_STATE))) {
			Map<String, Object> tempMap = (Map<String, Object>) userMap.get("data");
			Object departmentId = tempMap.get("departmentId");
			tempMap.clear();

			// 查询状态正常的人，才能处理工单
			tempMap.put("departmentId", departmentId);
			tempMap.put("status", 1);

			reMap = selectUserList(tempMap);
		}

		return reMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> sendCode(Map<String, Object> userMap) {
		Map<String, Object> reMap = new HashMap<String, Object>();

		Map<String, Object> uMap;

		try {
			uMap = userBusinessImpl.selectOne(userMap);

			if (!CommonUtil.isEmpty(uMap.get("data"))) {
				// 发送短信
				reMap = (Map<String, Object>) uMap.get("data");

				String phone = reMap.get("phone").toString();
				String uuid = reMap.get("uuid").toString();
				Long random = (long) (Math.random() * 99999999); // String code
																	// =
																	// UuidUtil.generateUUID().substring(0,
																	// 6);
				String code = random.toString().substring(0, 6);
				String content = "验证码:" + code + ",请勿向任何人提供验证码，谨防诈骗";

				System.out.println(code);
				SendSms sms = new SendSms();
				sms.setPhone(phone);
				sms.setContent(content);

				Boolean isSend = SendMsg_webchinese.sendSms(sms);

				if (isSend) {
					reMap.put("code", code);
					reMap.put("uuid", uuid);
				} else {
					reMap.put(KEY_STATE, STATE_TWO);
				}

			} else {
				reMap.put(KEY_STATE, STATE_TWO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reMap.put(KEY_STATE, STATE_ZERO);
		}

		return reMap;
	}
}
