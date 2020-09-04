package com.btson.aoes.security;

import com.btson.aoes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义Spring Security认证处理类的时候
 * 我们需要继承自WebSecurityConfigurerAdapter来完成，相关配置重写对应 方法即可。
 * */
@Configuration
public class AppSecurityConfigurer extends WebSecurityConfigurerAdapter{

	// 依赖注入用户服务类
	@Autowired
	private UserService userService;

	// 依赖注入加密接口
	@Autowired
	private PasswordEncoder passwordEncoder;

	// 依赖注入用户认证接口
	@Autowired
	private AuthenticationProvider authenticationProvider;

	// 依赖注入认证处理成功类，验证用户成功后处理不同用户跳转到不同的页面
	@Autowired
	AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;

	/*
	 *  BCryptPasswordEncoder是Spring Security提供的PasswordEncoder接口是实现类
	 *  用来创建密码的加密程序，避免明文存储密码到数据库
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// DaoAuthenticationProvider是Spring Security提供AuthenticationProvider的实现
	@Bean
	public AuthenticationProvider authenticationProvider() {
		// 创建DaoAuthenticationProvider对象
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		// 不要隐藏"用户未找到"的异常
		provider.setHideUserNotFoundExceptions(false);
		// 通过重写configure方法添加自定义的认证方式。
		provider.setUserDetailsService(userService);
		// 设置密码加密程序认证
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 加密
        String encodedPassword = passwordEncoder.encode("123456".trim());
        System.out.println(encodedPassword);
		// 设置认证方式。
		auth.authenticationProvider(authenticationProvider);

	}

	/**
	 * 设置了登录页面，而且登录页面任何人都可以访问，然后设置了登录失败地址，也设置了注销请求，注销请求也是任何人都可以访问的。
	 * permitAll表示该请求任何人都可以访问，.anyRequest().authenticated(),表示其他的请求都必须要有权限认证。
	 * */
	@Override
	protected void configure(HttpSecurity http) throws Exception {


		http.authorizeRequests()
		// spring-security 5.0 之后需要过滤静态资源
		.antMatchers("/grade/testView","/correction/auto","/notice/user/**","/logout","/","/user/toRegister","/login","/register","/css/**","/js/**","/img/**","/images/**","/assets/**","/layui/**","/paper/**","/tree/**","/guide/**","/user/**","/catalogue/getAllData","/uploadfile/**").permitAll()
		.antMatchers( "/examinees/**","/stu/**").hasRole( "STU")
		.antMatchers("/**").hasAnyRole("ADMIN","TEACHER")
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login").successHandler(appAuthenticationSuccessHandler)
		.usernameParameter("loginName").passwordParameter("password")
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/accessDenied");
		//关掉跨域攻击保护
		http.csrf().disable();

	}

    /**
     * 用户认证操作
     * */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // spring-security 5.0 之后需要密码编码器，否则会抛出异常：There is no PasswordEncoder mapped for the id "null"
        auth.inMemoryAuthentication().passwordEncoder(new MyPasswordEncoder()).withUser("fkit").password("123456").roles("STU");
        auth.inMemoryAuthentication().passwordEncoder(new MyPasswordEncoder()).withUser("admin").password("admin").roles("ADMIN","TEACHER");
    }



}
