/*******************************************************************************
 * @(#)PayService.java 2020年05月15日 22:37
 * Copyright 2020 http://codegarden.com All rights reserved.
 *******************************************************************************/
package com.spay.core.pay;

import com.spay.core.context.SpayContext;
import com.spay.core.data.SpayResponse;

/**
 * <b>Application name：</b> PayService.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2020 codegarden.com/ 版权所有。<br>
 * <b>Company：</b> codegarden.com/ <br>
 * <b>@Date：</b> 2020年05月15日 22:37 <br>
 * <b>@author：</b> <a href="mailto:deific@126.com"> deific </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
public interface PayService {

    /**
     * 直接支付
     * @param spayContext
     * @return
     */
    <T extends SpayContext> T pay(SpayContext<T> spayContext);

    /**
     * 确认支付
     * @param spayContext
     * @return
     */
    default SpayContext confirm(SpayContext spayContext) {
        return null;
    }

    /**
     * 退款
     * @param spayContext
     * @return
     */
    SpayContext refund(SpayContext spayContext);

    /**
     * 批量查询支付状态
     * @param spayContext
     * @return
     */
    SpayResponse queryTradeInfo(SpayContext spayContext);

    /**
     * 发送红包
     * @param spayContext
     * @return
     */
    default SpayContext sendRedPackage(SpayContext spayContext) {
        return null;
    }
}