/*******************************************************************************
 * @(#)AliPayRefundRequest.java 2020年05月29日 12:26
 * Copyright 2020 supay.org.cn. All rights reserved.
 *******************************************************************************/
package cn.org.supay.core.channel.alipay.data;

import cn.hutool.json.JSONUtil;
import cn.org.supay.core.channel.data.Response;
import com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse;
import com.aliyun.tea.TeaModel;
import lombok.Data;

/**
 * <b>Application name：</b> AliPayRefundResponse.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2020 supay.org.cn/ 版权所有。<br>
 * <b>Company：</b> supay.org.cn/ <br>
 * <b>@Date：</b> 2020年05月29日 12:26 <br>
 * <b>@author：</b> <a href="mailto:deific@126.com"> deific </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
@Data
public class AliPayRefundResponse extends AlipayTradeRefundResponse implements Response {

    public static AliPayRefundResponse build(java.util.Map<String, ?> map) throws Exception {
        AliPayRefundResponse self = new AliPayRefundResponse();
        return TeaModel.toModel(map, self);
    }

    @Override
    public String toString() {
        try {
            return JSONUtil.toJsonStr(this.toMap());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}