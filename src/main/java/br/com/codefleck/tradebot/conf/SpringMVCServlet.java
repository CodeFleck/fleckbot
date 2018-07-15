package br.com.codefleck.tradebot.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Configuration
@ComponentScan
public class SpringMVCServlet extends AbstractAnnotationConfigDispatcherServletInitializer
{

    @Override
    protected Class<?>[] getRootConfigClasses()
    {
        return new Class[] { JPAConfiguration.class, SecurityConfig.class, AppWebConfiguration.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses()
    {
        return null;
    }

    @Override
    protected String[] getServletMappings()
    {
        return new String[] { "/" };
    }

}
