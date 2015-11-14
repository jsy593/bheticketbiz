package com.bhe.business;

import java.util.Map;

import org.springframework.stereotype.Service;
@Service
public interface IQuickReplyBusiness {

	/**
	 * 添加快捷回复
	 * @author jsy
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> insertQuickReply(Map<String, Object> dataMap);

	/**查询快捷回复列表
	 * @author jsy
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectQuickReplyList(Map<String, Object> dataMap);

	/**查询单个快捷回复
	 * @author jsy
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> selectQuickReplyOne(Map<String, Object> dataMap);

	/**修改快捷回复
	 * @author jsy
	 * @param dataMap
	 * @return
	 */
	Map<String, Object> updateQuickReply(Map<String, Object> dataMap);


}
