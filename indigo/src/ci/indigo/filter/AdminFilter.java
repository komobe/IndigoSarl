package ci.indigo.filter;

import java.io.IOException;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import ci.indigo.entities.User;

@WebFilter(filterName = "AdminFilter", urlPatterns = { "*.xhtml" })
public class AdminFilter implements Filter {

	public AdminFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {

			HttpServletRequest reqt = (HttpServletRequest) request;
			//HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession session = reqt.getSession(false);
				
			String reqURI = reqt.getRequestURI();
			if(reqURI.indexOf("/login.xhtml") < 0 && (session != null && session.getAttribute("user") != null)) {
				User user = (User) session.getAttribute("user");
				if((reqURI.contains("client") || reqURI.contains("article")) && 
						(reqURI.contains("ajouter.xhtml") || reqURI.contains("modifier.xhtml")) ) {
					if ((user.isAdmin()))
						chain.doFilter(request, response);
					else {
						RequestDispatcher dispatcher;
						//resp.sendRedirect(reqt.getContextPath() + "/errorPages/403.xhtml");		
						dispatcher = reqt.getRequestDispatcher( "/errorPages/403.xhtml");
						dispatcher.forward(request, response);
					}
				}else {
					chain.doFilter(request, response);
				}	
			}else {
				chain.doFilter(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {

	}
}