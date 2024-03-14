package com.csu.backend.config;

import com.baomidou.mybatisplus.autoconfigure.DdlApplicationRunner;
import com.csu.backend.entity.DTO.Account;
import com.csu.backend.entity.RestBean;
import com.csu.backend.entity.VO.response.AuthorizeVO;
import com.csu.backend.filter.JwtAuthorizeFilter;
import com.csu.backend.service.AccountService;
import com.csu.backend.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils utils;

    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;

    @Resource
    AccountService accountService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                // 未登录或无权限异常处理
                .exceptionHandling(conf->conf
                        .authenticationEntryPoint(this::onUnAuthorized)
                        .accessDeniedHandler(this::onAccessDeny)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 当用户成功认证后调用此方法。
     * 它设置响应类型和编码，获取已认证用户的详细信息，
     * 为用户生成JWT令牌，并发送包含用户详细信息和令牌的成功响应。
     *
     * @param request 代表客户端请求的HttpServletRequest对象
     * @param response 用于生成服务器响应的HttpServletResponse对象
     * @param authentication 包含已认证用户详细信息的Authentication对象
     * @throws IOException 如果发生输入或输出异常
     * @throws ServletException 如果发生servlet异常
     */
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json");
        // 设置编码，防止乱码
        response.setCharacterEncoding("utf-8");
        User user = (User) authentication.getPrincipal();
        Account account = accountService.findAccountByNameOrEmail(user.getUsername());
        String token = utils.createJwt(user,1,"小明");
        AuthorizeVO vo = new AuthorizeVO();
        BeanUtils.copyProperties(account,vo);
        vo.setExpire(utils.expireTime());
        vo.setToken(token);
        response.getWriter().write(RestBean.success(vo).asJsonString());
    }

    /**
     * 当用户认证失败时调用此方法。
     * 它设置响应类型和编码，然后发送一个包含错误信息的JSON响应。
     *
     * @param request 代表客户端请求的HttpServletRequest对象
     * @param response 用于生成服务器响应的HttpServletResponse对象
     * @param exception AuthenticationException对象，包含了导致认证失败的原因
     * @throws IOException 如果在获取响应的Writer或者写入响应时发生IO错误，会抛出此异常
     */
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setContentType("application/json");
        // 设置编码，防止乱码
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(RestBean.unAuthorized(exception.getMessage()).asJsonString());
    }

    /**
     * 当用户成功登出时调用此方法。
     * 它设置响应类型和编码，然后根据JWT令牌的有效性发送一个JSON响应。
     * 如果令牌无效，则发送一个表示成功的响应，否则发送一个包含错误信息的响应。
     *
     * @param request 代表客户端请求的HttpServletRequest对象
     * @param response 用于生成服务器响应的HttpServletResponse对象
     * @param authentication 包含已认证用户详细信息的Authentication对象
     * @throws IOException 如果在获取响应的Writer或者写入响应时发生IO错误，会抛出此异常
     * @throws ServletException 如果发生servlet异常
     */
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if (utils.invalidateJwt(authorization)) {
            writer.write(RestBean.success().asJsonString());
        } else {
            writer.write(RestBean.failure(400,"退出登录失败").asJsonString());
        }
    }

    /**
     * 当用户访问被拒绝时调用此方法。
     * 它设置响应类型和编码，然后发送一个包含错误信息的JSON响应。
     *
     * @param request 代表客户端请求的HttpServletRequest对象
     * @param response 用于生成服务器响应的HttpServletResponse对象
     * @param accessDeniedException AccessDeniedException对象，包含了导致访问被拒绝的原因
     * @throws IOException 如果在获取响应的Writer或者写入响应时发生IO错误，会抛出此异常
     * @throws ServletException 如果发生servlet异常
     */
    public void onAccessDeny(HttpServletRequest request,
                             HttpServletResponse response,
                             AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(RestBean.forbidden(accessDeniedException.getMessage()).asJsonString());
    }

    /**
     * 当用户未授权时调用此方法，返回一个包含错误信息的JSON响应。
     *
     * @param request  HttpServletRequest对象，代表客户端请求
     * @param response HttpServletResponse对象，用于生成服务器的响应
     * @param exception AuthenticationException对象，包含了导致未授权的原因
     * @throws IOException 如果在获取响应的Writer或者写入响应时发生IO错误，会抛出此异常
     * @see RestBean#unAuthorized(String) 生成错误信息的JSON响应
     */
    public void onUnAuthorized(HttpServletRequest request,
                               HttpServletResponse response,
                               AuthenticationException exception) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(RestBean.unAuthorized(exception.getMessage()).asJsonString());
    }
}
