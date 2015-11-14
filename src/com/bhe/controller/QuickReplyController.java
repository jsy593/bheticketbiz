package com.bhe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bhe.business.IQuickReplyBusiness;
import com.bhe.util.common.PageUtil;

@Controller
public class QuickReplyController {

	@Autowired
	private IQuickReplyBusiness iQuickReplyBusiness;

	/**
	 * 添加快捷回复
	 * @author jsy
	 * @param map
	 * @return 0 系统错误 1 成功
	 */
	@RequestMapping(value = "/insertQuickReply", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addQuickReply(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> result = iQuickReplyBusiness.insertQuickReply(dataMap);
		
		return 	result;
	}

	/**
	 *  查询快捷回复列表
	 * @author jsy
	 * 
	 * @param map
	 * @param session
	 * @return 0 系统错误 1 成功 2 失败 list
	 */
	@RequestMapping(value = "/selectQuickReplyList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getQuickReply(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> result = iQuickReplyBusiness.selectQuickReplyList(dataMap);
		
		try {
			result.put("maxPage", PageUtil.getAllPage(Integer.parseInt(result.get("totalCount").toString()),
					Integer.parseInt(dataMap.get("pageSize").toString())));
		} catch (Exception e) {
			result.put("maxPage", 1);
		}

		return result;
	}
	
	/**
	 *	查询单条快捷回复
	 * @author jsy
	 * @param map
	 * @param session
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/selectQuickReplyOne", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getQuickReplyOne(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> result = iQuickReplyBusiness.selectQuickReplyOne(dataMap);
		
		return result;
	}

	/**
	 * 修改快捷回复
	 * @author jsy
	 * @param map
	 * @param session
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/updateQuickReply", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateQuickReply(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> result = iQuickReplyBusiness.updateQuickReply(dataMap);

		return result;
	}
	
}
