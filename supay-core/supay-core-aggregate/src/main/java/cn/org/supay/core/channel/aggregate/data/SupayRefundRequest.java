/*******************************************************************************
 * @(#)SupayRefundRequest.java 2020年05月29日 12:26
 * Copyright 2020 supay.org.cn. All rights reserved.
 *******************************************************************************/
package cn.org.supay.core.channel.aggregate.data;

import cn.org.supay.core.channel.data.Request;
import lombok.Data;

/**
 * <b>Application name：</b> SupayRefundRequest.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2020 supay.org.cn/ 版权所有。<br>
 * <b>Company：</b> supay.org.cn/ <br>
 * <b>@Date：</b> 2020年05月29日 12:26 <br>
 * <b>@author：</b> <a href="mailto:deific@126.com"> deific </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
@Data
public class SupayRefundRequest implements Request {
    /**  业务订单号 */
    protected String outTradeNo;
    /**  金额 */
    protected String totalAmount;
}