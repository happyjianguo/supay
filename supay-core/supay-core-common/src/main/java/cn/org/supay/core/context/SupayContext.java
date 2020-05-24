/*******************************************************************************
 * @(#)SupayContext.java 2020年05月15日 22:51
 * Copyright 2020 http://supay.org.cn All rights reserved.
 *******************************************************************************/
package cn.org.supay.core.context;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.org.supay.core.channel.filter.SupayFilter;
import cn.org.supay.core.channel.filter.SupayFilterChain;
import cn.org.supay.core.config.SupayChannelConfig;
import cn.org.supay.core.config.SupayConfig;
import cn.org.supay.core.converter.SupayConverter;
import cn.org.supay.core.data.Request;
import cn.org.supay.core.data.Response;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * <b>Application name：</b> SupayContext.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2020 supay.org.cn/ 版权所有。<br>
 * <b>Company：</b> supay.org.cn/ <br>
 * <b>@Date：</b> 2020年05月15日 22:51 <br>
 * <b>@author：</b> <a href="mailto:deific@126.com"> deific </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
public class SupayContext<R extends Request, S extends Response> extends SupayFilterChain {
    /** 交易流水号 */
    protected String tradeId;
    /** 是否沙盒 */
    protected boolean isSandBox;
    /** 支付渠道参数 */
    protected SupayChannelConfig channelConfig;
    /** 开始时间 */
    private Date startTime;
    /** 结束时间 */
    private Date endTime;
    /** 支付请求参数 */
    protected  R request;
    /** 支付结果 */
    protected S response;
    /** 交易是否成功 */
    protected boolean success = true;
    /** 交易信息 */
    protected String msg;

    /**
     * 成功
     * @param msg
     * @return
     */
    public SupayContext success(String msg) {
        this.setMsg(msg);
        this.setSuccess(true);
        return this;
    }

    /**
     * 失败
     * @param msg
     * @return
     */
    public SupayContext fail(String msg) {
        this.setMsg(msg);
        this.setSuccess(false);
        return this;
    }

    /**
     * 是否有错误
     * @return
     */
    public boolean hasError() {
        return success;
    }

    /**
     * 转换为请求字符串
     * @return
     */
    public String toRequestStr() {
        SupayConverter converter = SupayConfig.getApiParamConverter(this.channelConfig.getChannelType());
        return converter.convert(this.getRequest());
    }

    /**
     * 解析响应字符串为对象实例
     * @param respStr
     * @param targetClass
     * @return
     */
    public Response parseResponseStr(String respStr, Class<? extends Response> targetClass) {
        SupayConverter converter = SupayConfig.getApiParamConverter(this.channelConfig.getChannelType());
        this.setResponse((S) converter.convert(respStr, targetClass));
        // 检查结果
        this.getResponse().checkResult(this);
        return this.response;
    }

    /**
     * 耗时
     * @return
     */
    public long duration() {
        return this.endTime.getTime() - this.startTime.getTime();
    }

    /**
     * 构建上下文
     * @param channelConfig
     * @param request
     * @param isSandBox
     * @return
     */
    public static SupayContext buildContext(SupayChannelConfig channelConfig, Request request, boolean isSandBox, SupayFilter... filters) {
        SupayContext cxt = SupayContext.builder()
                .tradeId(IdUtil.fastUUID())
                .channelConfig(channelConfig)
                .isSandBox(isSandBox)
                .request(request)
                .filters(ListUtil.toList(filters))
                .build();
        return cxt;
    }
}