package com.bhe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bhe.business.IKnowledgeBaseBusiness;
import com.bhe.controller.common.BaseController;
import com.bhe.util.common.PageUtil;

/**
 * 
 * @author yichuan
 *
 */
@Controller
public class KnowledgeBaseController extends BaseController {

	@Autowired
	private IKnowledgeBaseBusiness iKnowledgeBaseBusiness;

	/**
	 * yichuan 添加知识库
	 * 
	 * @param map
	 * @param session
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/insertKnowledgeBase", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertKnowledgeBase(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> result = iKnowledgeBaseBusiness.insertKnowledgeBase(dataMap);
		
		return 	result;
	}

	/**
	 * yichuan 分页查询知识库 List
	 * 
	 * @param map
	 * @param session
	 * @return 0 系统错误 1 成功 2 失败 list
	 */
	@RequestMapping(value = "/selectKnowledgeBaseList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectKnowledgeBaseList(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> result = iKnowledgeBaseBusiness.selectKnowledgeBaseList(dataMap);
		
		try {
			result.put("maxPage", PageUtil.getAllPage(Integer.parseInt(result.get("totalCount").toString()),
					Integer.parseInt(dataMap.get("pageSize").toString())));
		} catch (Exception e) {
			result.put("maxPage", 1);
		}
		
		return result;
	}
	
	/**
	 * yichuan 分页查询知识库 One
	 * 
	 * @param map
	 * @param session
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/selectKnowledgeBaseOne", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getKnowledgeBaseOne(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> result = iKnowledgeBaseBusiness.selectKnowledgeBaseOne(dataMap);
		
		return result;
	}

	/**
	 * yichuan 修改知识库
	 * 
	 * @param map
	 * @param session
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/updateKnowledgeBase", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateKnowledgeBase(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> result = iKnowledgeBaseBusiness.updateKnowledgeBase(dataMap);

		return result;
	}
}
