package com.lizx.wechat.service;

import java.util.Map;

public interface WechatPayService {

	String wechatOrder(String goodsId, int goodsCont, String openid, String tocken);

	boolean checkPayMessage(Map<String, String> map);

}
