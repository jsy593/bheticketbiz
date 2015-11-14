package com.bhe.business;

import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 
 * @author zhangyu
 *
 */
@Service
public interface IQuestionTypeManagerBusiness {

	/**
	 * jsy 根据部门uuid查询该部门是否有对应的问题分类 2015-9-20
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectQueTypeByDept(Map<String, Object> dataMap);

	/**
	 * yichuan 获取问题分类 list
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectQuestionTypeList(Map<String, Object> dataMap);

	/**
	 * 易川 添加问题分类
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> insertQuestionType(Map<String, Object> dataMap) throws Exception;

	/**
	 * yicahun 修改问题分类
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> updateQuestionType(Map<String, Object> dataMap);

	/**
	 * yichuan 获取问题分类 单条
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectQuestionTypeOne(Map<String, Object> dataMap);

	/**
	 * 获取问题分类对应关系 list
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectQuestionTypeForUserList(Map<String, Object> dataMap);

	/**
	 * 获取问题分类对应关系 单条
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectQuestionTypeForUserOne(Map<String, Object> dataMap);

	/**
	 * 修改问题分类对应关系
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> updateQuestionTypeForUser(Map<String, Object> dataMap);

	/**
	 * 添加问题分类对应关系
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> insertQuestionTypeForUser(Map<String, Object> dataMap);

	/**
	 * 通过问题小分类 查询 相关人员
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectQuestionTypeToUser(Map<String, Object> dataMap);

}
