package com.bhe.business;

import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 
 * @author yichuan
 *
 */
@Service
public interface ITicketManagerBusiness {

	/**
	 * 获取当前用户下的 工单list
	 * 
	 * @param dateMap
	 * @return
	 */
	Map<String, Object> selectUserTicketList(Map<String, Object> dataMap);

	/**
	 * 获取当前用户下的 工单 单条
	 * 
	 * @param dateMap
	 * @return
	 */
	Map<String, Object> selectUserTicketOne(Map<String, Object> dataMap);

	/**
	 * yichuan 获取工单回复
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectTicketReply(Map<String, Object> dataMap);

	/**
	 * yichuan 客户或客服 回复工单
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> insertTicketReply(Map<String, Object> dataMap);

	/**
	 * 易川 添加工单流转
	 * 
	 * @param dataMap
	 * @return
	 * @throws Exception 
	 */
	Map<String, Object> insertTicketMove(Map<String, Object> dataMap) throws Exception;

	/**
	 * 易川 工单审核
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> updateTicketMove(Map<String, Object> dataMap) throws Exception;

	/**
	 * 查看工单流转
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectTicketMoveList(Map<String, Object> dataMap);

	/**
	 * 查看工单流转One
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectTicketMoveOne(Map<String, Object> dataMap);


	/**
	 * 查询工单列表
	 * 
	 * @param dataMap
	 */
	Map<String, Object> selectTicketList(Map<String, Object> dataMap);

	/**
	 * 驳回工单流转 yc
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> rejectTicketMove(Map<String, Object> dataMap);

	/**
	 * 分配 工单流转 yc
	 * 
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> distributionTicketMove(Map<String, Object> dataMap)  throws Exception ;

	/**
	 * 查询单条工单
	 * 
	 * @param dataMap
	 */
	Map<String, Object> selectTicketOne(Map<String, Object> dataMap);

	/**
	 * 工单修改
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> updateTicket(Map<String, Object> dataMap);
	/**
	 * 工单分配方式
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> updateAllotType(Map<String, Object> dataMap);

}
