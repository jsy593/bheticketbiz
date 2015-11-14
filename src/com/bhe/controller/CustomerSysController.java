package com.bhe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bhe.business.ICustomerSysBusiness;
import com.bhe.business.IUserManagerBusiness;
import com.bhe.controller.common.BaseController;
import com.bhe.util.common.JsonUtil;
import com.bhe.util.common.PageUtil;

/**
 * @author yichuan
 *
 */
@Controller
public class CustomerSysController extends BaseController {

	@Autowired
	private ICustomerSysBusiness iCustomerSysBusiness;
	@Autowired
	IUserManagerBusiness iUserManagerBusiness;

	/**
	 * yichuan 客户系统提交工单
	 * 
	 * @param map
	 * @param session
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/insertTicket", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertTicket(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> result = iCustomerSysBusiness.insertTicket(dataMap);
		
		return result;
	}

	/**
	 * yichuan 客户系统查看工单 分页
	 * 
	 * @param map
	 * @param session
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/selectCustomerTicketList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectCustomerTicketList(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> customerTicketList = iCustomerSysBusiness.selectCustomerTicketList(dataMap);

		try {
			customerTicketList.put("maxPage", PageUtil.getAllPage(Integer.parseInt(customerTicketList.get("totalCount").toString()),
					Integer.parseInt(dataMap.get("pageSize").toString())));
		} catch (Exception e) {
			customerTicketList.put("maxPage", 1);
		}
		
		return customerTicketList;
	}

	/**
	 * yichuan 客户系统关闭工单
	 * 
	 * @param map
	 * @param session
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/closeTicket", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> closeTicket(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> result = iCustomerSysBusiness.closeTicket(dataMap);

		return result;
	}
}
