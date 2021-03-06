# supay
简单（super）支付系统
简单轻量级可扩展的支付系统。提供了优雅架构设计的支付工具包和完善的支付系统。
工具包可单独使用，通过整合、抽象、聚合第三方支付系统接口，对外提供简洁的使用和调用接口完成第三方支付接口（微信,支付宝,银联等等）调用，实现了扫码支付、H5支付、小程序支付等，支付与业务完全剥离，简单几行代码即可实现支付，简单快速完成支付模块的开发，可轻松嵌入到任何系统里。并且采用灵活的扩展性架构，以便实现更多的第三方支付接口。
支付系统完善的支付系统。包括商户管理，用户账户体系，支付管理，支付路由，订单管理，上下游对账，清结算等支付核心功能，目标是打造一个开源的完善的可扩展的用于生产的支付系统。

## 特性
```java
1. 极少依赖,轻量级可扩展的工具包SDK，可嵌入任何系统
2. 统一支付入口,简化调用,只使用SupayCore类即可
3. 高性能异步线程处理请求和异步通知
4. 支持多商户多应用
5. 支持调用过滤器机制,可以灵活扩展调用逻辑,第三方接口灵活切换使用55
6. 支付Context设计,统一封装请求响应参数,可以灵活控制每一笔支付交易

```
## 痛点
调用第三方支付系统通常在自身业务中中经常遇到，支付流程总的来说大差不多，但各大第三方支付系统，银行的支付接口各自不一样：
- 接口签名方法不一样
   ```java
    例如:微信和支付宝的接口签名算法不一样,有的还需要有证书,有的不需要
    ``` 
- 参数要求不一样，传输格式不一样
    ```java
        例如:同样一笔支付请求,微信和支付的支付参数要求不一样,简单点的可以几个参数,复杂的几十个参数.微信用xml传输,支付宝用json等
    ``` 
- 接口交互方式不一样
    ```java
        例如:有的接口是直接返回交易结果数据,有的接口实际返回html片段,需要在浏览器再次提交
    ``` 

这些不一样的地方造成开发人员在对接支付系统时，不得不去详细了解各个第三方支付系统自身的实现机制，还需要考虑自身业务在使用不同渠道时如何设计路由等。
更过情况下，可能会在不同项目中复制粘贴代码的方式去复用代码，容易出错，而且对于其他人员接手又需要了解支付的机制。
## 设计
基于上述痛点的考虑，supay的力图在一个统一层面消除不同支付系统接口的不一致性，对于业务暴露出统一的支付交互接口和交互流程，降低业务系统方使用支付的
难度，统一成一套流程，并且提供扩展性。
- 抽象和统一支付交易流程，屏蔽支付系统差异
- 内部架构设计支付扩展第三方支付系统

![支付](/docs/SupayCore.png)

## 示例
```java
    String orderCode = IdUtil.fastSimpleUUID();
    
    // 阿里支付调用

    // 构建支付上下文
    SupayContext cxt = AliPayPageRequest.builder()
            .outTradeNo(orderCode)
            .payType(SupayPayType.ALI_PAGE_PAY)
            .subject("测试网页支付")
            .totalAmount("1")
            .returnUrl("http://taobao.com")
            .build().toContext(channelConfig.getAppId(), false);

    // 本地模拟支付
    cxt.setLocalMock(true);
    // 调用支付接口
    cxt = (SupayContext) SupayCore.pay(cxt);
    
    log.debug("交易状态：{} 信息：{} 耗时：{} 接口响应数据：{}", cxt.hasError(),
            cxt.getMsg(), cxt.duration(), JSONUtil.toJsonStr(cxt.getResponse()));


    // 微信支付调用
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
```

## 统一支付入口SupayCore
抽象各个第三方渠道支付接口和调用过程，统一支付SupayCore提供入口，对于不同的第三方渠道支付，SupayCore的方法入参和返回值不同。并且调用过程中增加了过滤器设计，可以灵活扩展各渠道调用过程。
