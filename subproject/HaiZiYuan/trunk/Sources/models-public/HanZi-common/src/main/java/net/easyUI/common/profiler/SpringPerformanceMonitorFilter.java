package net.easyUI.common.profiler;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

public class SpringPerformanceMonitorFilter extends GenericFilterBean implements
		Filter {

	/**
	 * 缺省监测值为100毫秒，超过这个值的request请求将被记录
	 */
	private int threshold = 100;

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (TimeProfiler.isProfileEnable()) {
			doFilterWithProfile(request, response, chain);
		} else {
			chain.doFilter(request, response);
		}
	}
	
	private void doFilterWithProfile(ServletRequest request,
			ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest hs = (HttpServletRequest) request;
		// hs.setCharacterEncoding(encoding);
		String profilerId = getProfilerId(hs);
		TimeProfiler profiler = TimeProfiler.start(profilerId, threshold);
		try {
			chain.doFilter(request, response);
		} finally {
			profiler.release(getProfilerName(hs));
		}
	}

	private String getProfilerId(HttpServletRequest hs) {
		StringBuffer id = hs.getRequestURL();
		return id.toString();
	}

	@SuppressWarnings("unchecked")
	private String getProfilerName(HttpServletRequest hs) {
		StringBuffer id = hs.getRequestURL();
		id.append(' ');
		Enumeration enumer = hs.getParameterNames();
		while (enumer.hasMoreElements()) {
			String name = enumer.nextElement().toString();
			String value = hs.getParameter(name);
			id.append('[').append(name).append('=').append(value).append("] ");
		}
		return id.toString();
	}

}
