package com.jcohao.gateway.filter;

import com.jcohao.authCommon.utils.JwtUtils;
import com.jcohao.common.utils.CookieUtils;
import com.jcohao.gateway.config.FilterProperties;
import com.jcohao.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {
    @Autowired
    private JwtProperties prop;

    @Autowired
    private FilterProperties filterProp;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取 request
        HttpServletRequest request = ctx.getRequest();
        // 获取路径
        String requestURI = request.getRequestURI();
        // 判断是否在白名单内
        return !isAllowPath(requestURI);
    }

    private boolean isAllowPath(String requestURL) {
        for (String path : filterProp.getAllowPaths()) {
            if (requestURL.startsWith(path)) return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取 request
        HttpServletRequest request = ctx.getRequest();
        // 获取 token
        String token = CookieUtils.getCookieValue(request, prop.getCookieName());
        // 校验
        try {
            // 校验通过则放行
            JwtUtils.getInfoFormToken(token, prop.getPublicKey());
        } catch (Exception e) {
            // 校验不通过，返回 403
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
        }
        return null;
    }
}
