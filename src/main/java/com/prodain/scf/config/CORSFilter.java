package com.prodain.scf.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {
	
	@Value("${cors.origin}")
	private String corsOrigin;
	
	@Value("${cors.methods}")
	private String corsMethods;
	
	@Value("${cors.age}")
	private String corsAge;
	
	@Value("${cors.headers}")
	private String corsHeaders;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		response.setHeader("Access-Control-Allow-Origin", corsOrigin);
		response.setHeader("Access-Control-Allow-Methods", corsMethods);
		response.setHeader("Access-Control-Max-Age", corsAge);
		response.setHeader("Access-Control-Allow-Headers", corsHeaders);
		response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
		
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
        	chain.doFilter(request, response);
        }
	}

}
