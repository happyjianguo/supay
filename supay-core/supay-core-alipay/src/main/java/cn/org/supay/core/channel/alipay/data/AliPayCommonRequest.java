/*******************************************************************************
 * @(#)AliPayAppRequest.java 2020年05月29日 12:26
 * Copyright 2020 http://supay.org.cn All rights reserved.
 *******************************************************************************/
package cn.org.supay.core.channel.alipay.data;

import cn.org.supay.core.channel.data.Request;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * <b>Application name：</b> AliPayAppRequest.java <br>
 * <b>Application describing： </b>通用接口 Common  <br>
 * <b>Copyright：</b> Copyright &copy; 2020 supay.org.cn/ 版权所有。<br>
 * <b>Company：</b> supay.org.cn/ <br>
 * <b>@Date：</b> 2020年05月29日 12:26 <br>
 * <b>@author：</b> <a href="mailto:deific@126.com"> deific </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
@Data
@SuperBuilder
public class AliPayCommonRequest extends AliPayBaseRequest implements Request {
    /** 买家的支付宝唯一用户号 */
    String buyerId;
}