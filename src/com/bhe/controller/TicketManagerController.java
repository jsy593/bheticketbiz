package com.bhe.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bhe.business.IQuickReplyBusiness;
import com.bhe.business.ITicketManagerBusiness;
import com.bhe.business.IUserManagerBusiness;
import com.bhe.controller.common.BaseController;
import com.bhe.util.common.CommonUtil;
import com.bhe.util.common.JsonUtil;
import com.bhe.util.common.PageUtil;

/**
 * @author yichuan
 *
 */
@Controller
public class TicketManagerController extends BaseController {

	@Autowired
	private ITicketManagerBusiness iTicketManagerBusiness;
	@Autowired
	private IQuickReplyBusiness iQuickReplyBusiness;
	@Autowired
	private IUserManagerBusiness iUserManagerBusiness;

	/**
	 * @author yichuan 查看当前用户下未处理的工单 list
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 */
	@RequestMapping(value = "/selectUserTicketList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectUserTicketList(@RequestParam Map<String, Object> dataMap, HttpSession session) {
		Map<String, Object> reMap = iTicketManagerBusiness.selectUserTicketList(dataMap);

		return reMap;
	}

	/**
	 * @author yichuan 查看当前用户下未处理的工单 单条
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 */
	@RequestMapping(value = "/selectUserTicketOne", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectUserTicketOne(@RequestParam Map<String, Object> dataMap, HttpSession session) {
		Map<String, Object> reMap = iTicketManagerBusiness.selectUserTicketOne(dataMap);
		
		return reMap;
	}

	/**
	 * 查询工单列表
	 * 
	 * @author jsy
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/selectTicketList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectTicketList(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iTicketManagerBusiness.selectTicketList(dataMap);

		try {
			reMap.put("maxPage", PageUtil.getAllPage(Integer.parseInt(reMap.get("totalCount").toString()),
					Integer.parseInt(dataMap.get("pageSize").toString())));
		} catch (Exception e) {
			reMap.put("maxPage", 1);
		}
		
		return reMap;
	}
	/**
	 * 修改工单状态
	 * 
	 * @author jsy
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateTicket", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateTicket(@RequestParam Map<String, Object> dataMap) {
		dataMap.put("status", 1);
		Map<String, Object> reMap = iTicketManagerBusiness.updateTicket(dataMap);
		return reMap;
	}

	/**
	 * 查询工单详情
	 * 
	 * @author jsy
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/selectTicketOne", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectTicketOne(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iTicketManagerBusiness.selectTicketOne(dataMap);
		
		return reMap;
	}

	/**
	 * 查看工单回复
	 * 
	 * @author yichuan
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 */
	@RequestMapping(value = "/selectTicketReply", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectTicketReply(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iTicketManagerBusiness.selectTicketReply(dataMap);
		
		return reMap;
	}

	/**
	 * 添加工单回复
	 * 
	 * @author jsy
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 */
	@RequestMapping(value = "/insertTicketReply", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertTicketReply(@RequestParam Map<String, Object> dataMap) {

		Map<String, Object> reMap = iTicketManagerBusiness.insertTicketReply(dataMap);
		return reMap;
	}

	/**
	 * @author yichuan 添加工单流转
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insertTicketMove", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertTicketMove(@RequestParam Map<String, Object> dataMap) throws Exception {
		Map<String, Object> userMap = new HashMap<String, Object>();
		// 获取审核人的id
		userMap.put("departmentId", dataMap.get("departmentId"));
		dataMap.remove("departmentId");
		userMap.put("status", "1");
		Map<String, Object> usermap = iUserManagerBusiness.selectDeptAdmin(userMap);
		if (!CommonUtil.isEmpty(usermap)) {
			Map<String, Object> user = (Map<String, Object>) usermap.get("data");
			dataMap.put("audtId", user.get("uuid"));
		}
		Map<String, Object> reMap = iTicketManagerBusiness.insertTicketMove(dataMap);
		return reMap;
	}

	/**
	 * @author yichuan 查看工单流转 list
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 */
	@RequestMapping(value = "/selectTicketMoveList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectTicketMoveList(@RequestParam Map<String, Object> dataMap) {

		Map<String, Object> reMap = iTicketManagerBusiness.selectTicketMoveList(dataMap);
		try {
			reMap.put("maxPage", PageUtil.getAllPage(Integer.parseInt(reMap.get("totalCount").toString()),
					Integer.parseInt(dataMap.get("pageSize").toString())));
		} catch (Exception e) {
			reMap.put("maxPage", 1);
		}
		return reMap;
	}

	/**
	 * @author yichuan 查看工单流转 one
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 */
	@RequestMapping(value = "/selectTicketMoveOne", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectTicketMoveOne(@RequestParam Map<String, Object> dataMap) {

		Map<String, Object> reMap = iTicketManagerBusiness.selectTicketMoveOne(dataMap);
		return reMap;
	}

	/**
	 * @author yichuan 工单审核
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 */
	@RequestMapping(value = "/checkTicketMove", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkTicketMove(@RequestParam Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = iTicketManagerBusiness.updateTicketMove(dataMap);
		
		return reMap;
	}

	/**
	 * @author yichuan 工单分配
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 */
	@RequestMapping(value = "/ticketDistribution", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> ticketDistribution(@RequestParam Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = iTicketManagerBusiness.updateTicket(dataMap);
		
		return reMap;
	}

	/**
	 * @author yichuan 工单驳回
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 */
	@RequestMapping(value = "/rejectTicketMove", method = RequestMethod.POST)
	public Map<String, Object> rejectTicketMove(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iTicketManagerBusiness.rejectTicketMove(dataMap);
		
		return reMap;
	}

	/**
	 * @author yichuan 流转工单分配
	 * @param model
	 * @return 0 系统错误 1 成功 2 失敗
	 */
	@RequestMapping(value = "/distributionTicketMove", method = RequestMethod.POST)
	public Map<String, Object> distributionTicketMove(@RequestParam Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = iTicketManagerBusiness.distributionTicketMove(dataMap);
		
		return reMap;
	}
	/**
	 * 工单分配方式
	 * @author jsy
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/updateAllotType", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateAllotType(@RequestParam Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = iTicketManagerBusiness.updateAllotType(dataMap);
		
		return reMap;
	}

}
