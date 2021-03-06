/*******************************************************************************
 * @(#)AliPayFaceResponse.java 2020年05月29日 12:26
 * Copyright 2020 http://supay.org.cn All rights reserved.
 *******************************************************************************/
package cn.org.supay.core.channel.alipay.data;

import cn.org.supay.core.channel.data.Response;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePayResponse;
import com.aliyun.tea.TeaModel;
import lombok.Data;
import lombok.ToString;

/**
 * <b>Application name：</b> AliPayFaceResponse.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2020 supay.org.cn/ 版权所有。<br>
 * <b>Company：</b> supay.org.cn/ <br>
 * <b>@Date：</b> 2020年05月29日 12:26 <br>
 * <b>@author：</b> <a href="mailto:deific@126.com"> deific </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
@Data
@ToString
public class AliPayFaceResponse extends AlipayTradePayResponse implements Response {

    public static AliPayFaceResponse build(java.util.Map<String, ?> map) throws Exception {
        AliPayFaceResponse self = new AliPayFaceResponse();
        return TeaModel.toModel(map, self);
    }
}