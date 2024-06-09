package com.tree3.filter;

import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 自定义拦截器对请求进行日志记录,todo 使用另外的线程对日志进行记录
 * todo #point后续拓展点: 和前端进行约定 完成对请求数据进行加密 todo 需要和前端约定好加密算法 对响应结果进行加密 相应的前端需要对请求结果进行解密
 */
@Component  //代表在工厂中创建对象   @configuration 配置     @Component  在工厂中创建对象   使用 logger -Token
public class LoggerGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggerGatewayFilterFactory.Config> {
    private static final Logger log = LoggerFactory.getLogger(LoggerGatewayFilterFactory.class);

    private RedisTemplate redisTemplate;

    @Autowired
    public LoggerGatewayFilterFactory(RedisTemplate redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

    //Config 参数就是基于本类中的 内部类Config的对象
    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {    // servlet service httpServletRequest  httpServletResponse 传统web springmvc   springwebflux new web模型 //filter   request response filterChain.dofilter(request,response)
            @Override
            //参数1: exchange 交换机
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                log.debug("config required token: {}", config.requiredLog);
                log.debug("config name: {}", config.name);
                log.debug("日志记录");
                if (config.requiredLog) {
                    ServerHttpRequest request = exchange.getRequest();
                    HttpHeaders headers = request.getHeaders();
                    log.info("日志记录---------->时间：{},path：{},from：{},参数:{},requestObj：{}",
                            new Date(),
                            request.getPath(),
                            headers.getOrigin(),
                            JSONUtil.toJsonStr(request.getQueryParams()),
                            JSONUtil.toJsonStr(request));
                }
                return chain.filter(exchange);
            }
        };
    }


    //用来配置将使用filter时指定值赋值给Config中哪个属性
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("requiredLog", "name");
    }


    //自定义配置类
    public static class Config {
        private boolean requiredLog;  //false
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isRequiredLog() {
            return requiredLog;
        }

        public void setRequiredLog(boolean requiredLog) {
            this.requiredLog = requiredLog;
        }
    }


}
