package com.bhe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bhe.business.IQuestionTypeManagerBusiness;
import com.bhe.controller.common.BaseController;
import com.bhe.util.common.JsonUtil;
import com.bhe.util.common.PageUtil;

/**
 * 
 * @author zhangyu
 *
 */
@Controller
public class QuestionTypeManagerController extends BaseController {

	@Autowired
	IQuestionTypeManagerBusiness iQuestionTypeManagerBusiness;

	/**
	 * @author yichuan 获取问题分类list
	 * @param model
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/selectQuestionTypeList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectQuestionTypeList(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iQuestionTypeManagerBusiness.selectQuestionTypeList(dataMap);
		
		try {
			reMap.put("maxPage", PageUtil.getAllPage(Integer.parseInt(reMap.get("totalCount").toString()),
					Integer.parseInt(dataMap.get("pageSize").toString())));
		} catch (Exception e) {
			reMap.put("maxPage", 1);
		}
		
		return reMap;
	}

	/**
	 * @author yichuan 获取问题分类单条
	 * @param model
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/selectQuestionTypeOne", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getQuestionTypeone(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iQuestionTypeManagerBusiness.selectQuestionTypeOne(dataMap);

		return reMap;
	}

	/**
	 * @author yichuan 添加问题分类
	 * @param model
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/insertQuestionType", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertQuestionType(@RequestParam Map<String, Object> dataMap) throws Exception {
		Map<String, Object> reMap = iQuestionTypeManagerBusiness.insertQuestionType(dataMap);

		return reMap;
	}

	/**
	 * @author yichuan 修改问题分类
	 * @param model
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/updateQuestionType", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateQuestionType(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iQuestionTypeManagerBusiness.updateQuestionType(dataMap);

		return reMap;
	}

	/**
	 * @author yichuan 获取问题分类对应关系 list
	 * @param model
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/selectQuestionTypeForUserList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectQuestionTypeForUserList(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iQuestionTypeManagerBusiness.selectQuestionTypeForUserList(dataMap);

		return reMap;
	}

	/**
	 * @author yichuan 获取问题分类对应关系单条
	 * @param model
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/selectQuestionTypeForUserOne", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectQuestionTypeForUserOne(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iQuestionTypeManagerBusiness.selectQuestionTypeForUserOne(dataMap);

		return reMap;
	}

	/**
	 * @author yichuan 添加问题分类对应关系
	 * @param model
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/insertQuestionTypeForUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> insertQuestionTypeForUser(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iQuestionTypeManagerBusiness.insertQuestionTypeForUser(dataMap);

		return reMap;
	}

	/**
	 * @author yichuan 修改问题分类对应关系
	 * @param model
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/updateQuestionTypeForUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateQuestionTypeForUser(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iQuestionTypeManagerBusiness.updateQuestionTypeForUser(dataMap);

		return reMap;
	}

	/**
	 * @author yichuan 通过问题小分类 查询 相关人员
	 * @param model
	 * @return 0 系统错误 1 成功 2 失败
	 */
	@RequestMapping(value = "/selectQuestionTypeToUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> selectQuestionTypeToUser(@RequestParam Map<String, Object> dataMap) {
		Map<String, Object> reMap = iQuestionTypeManagerBusiness.selectQuestionTypeToUser(dataMap);

		return reMap;
	}

}
