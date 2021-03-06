/*******************************************************************************
 * @(#)WxSupayCoreDemo.java 2020年05月16日 09:07
 * Copyright 2020 http://supay.org.cn All rights reserved.
 *******************************************************************************/
package cn.org.supay.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.setting.dialect.Props;
import cn.org.supay.core.channel.ChannelPayService;
import cn.org.supay.core.channel.wx.WxApiType;
import cn.org.supay.core.channel.wx.WxChannelPayService;
import cn.org.supay.core.channel.wx.convert.WxPayConverter;
import cn.org.supay.core.channel.wx.data.*;
import cn.org.supay.core.config.SupayChannelConfig;
import cn.org.supay.core.config.SupayCoreConfig;
import cn.org.supay.core.context.SupayContext;
import cn.org.supay.core.enums.SupayChannelType;
import cn.org.supay.core.enums.SupayPayType;
import cn.org.supay.core.SupayCore;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * <b>Application name：</b> WxSupayCoreDemo.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2020 supay.org.cn/ 版权所有。<br>
 * <b>Company：</b> supay.org.cn/ <br>
 * <b>@Date：</b> 2020年05月16日 09:07 <br>
 * <b>@author：</b> <a href="mailto:deific@126.com"> deific </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
@Slf4j
public class WxSupayCoreDemo {

    private static Props props;
    private static  SupayChannelConfig channelConfig;

    // 初始化
    static {
        props = new Props("config/my-wx-pay.conf");
        // 初始化配置
        channelConfig = SupayChannelConfig.builder()
                .appId(props.getStr("wx.appId")).appSecret(props.getStr("wx.appSecret")).appName("微信公众号-支付")
                .mchId(props.getStr("wx.mchId")).mchSecretKey(props.getStr("wx.mchSecretKey")).mchName("微信商户")
                .channelType(SupayChannelType.WECHAT).apiBaseUrl(WxApiType.BASE_URL_CHINA1.getUrl())
                .build().register();

    }

    public static void main(String[] args) {


        String orderCode = IdUtil.fastSimpleUUID();
        // 微信支付

        // 构建支付上下文
        SupayContext cxt = WxPayUnifiedOrderRequest.builder()
                .body("测试微信支付订单")
                .outTradeNo(orderCode)
                .productId("12")
                .notifyUrl("https://www.spay.org.cn/notify")
                .totalFee("100")
                .timeStart(DateUtil.format(new Date(), "yyyyMMddHHmmss"))
                .timeExpire(DateUtil.format(DateUtil.offsetMinute(new Date(), 15), "yyyyMMddHHmmss"))
                .tradeType(SupayPayType.WX_MP_PAY.getCode())
                .openid(props.getStr("wx.openId"))
                .spbillCreateIp("127.0.0.1")
                .nonceStr(String.valueOf(System.currentTimeMillis()))
                .build().toContext(channelConfig.getAppId(), false);

        // 调用支付接口
        cxt = (SupayContext) SupayCore.pay(cxt);
        if (!cxt.hasError()) {
            cxt.getResponse();
        }
        log.debug("交易状态：{} 信息：{} 耗时：{} 接口响应数据：{}", cxt.hasError(), cxt.getMsg(), cxt.duration(), cxt.getResponse());

        // 查询支付订单
        WxPayOrderQueryRequest qReq = WxPayOrderQueryRequest.builder().outTradeNo(orderCode).build();
        SupayContext qCtx = SupayContext.buildContext(channelConfig, qReq, false);


//        SupayCore.queryPayOrder(qCtx);
        // 获取具体渠道支付服务
        ChannelPayService wxPayChannelService = SupayCore.getPayChannelService(SupayChannelType.WECHAT);
        wxPayChannelService.queryTradeInfo(qCtx);

        log.debug("查询结果：{}", qCtx.getResponse());
    }
}