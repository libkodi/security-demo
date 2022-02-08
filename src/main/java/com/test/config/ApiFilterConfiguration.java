package com.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.libkodi.security.CacheManager;
import io.github.libkodi.security.factory.FilterManager;
import io.github.libkodi.security.factory.TokenManager;
import io.github.libkodi.security.interfaces.Cache;

@Configuration
public class ApiFilterConfiguration {
	@Bean
	public FilterManager getAuthFilter(TokenManager tokenManager) {
		FilterManager filter = new FilterManager();
		
		filter.add("^/v2/.+", (ctx, request) -> {
			try {
				Cache cache = tokenManager.getCache(request);
				
				if (cache != null) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		});
		
//		filter.setBeforeFilter((ctx, request) -> {
//			String user = request.getParameter("username");
//			
//			return user.equals("user");
//		});
		
		filter.setAfterFilter((ctx, response, value) -> {
//			System.out.println(value);
			return value;
		});
		
		return filter;
	}
	
	@Bean
	public TokenManager getTokenFactory(CacheManager cacheManager) {
		return new TokenManager(cacheManager);
	}
	
}
