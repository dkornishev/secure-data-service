package org.slc.sli.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import org.slc.sli.manager.PortalWSManager;
import org.slc.sli.web.controller.ErrorController.ErrorDescriptor;

/**
 * Simple Mapping Exception Resolver to log the exception and display user-friendly message.
 * @author vummalaneni
 *
 */
public class DashboardExceptionResolver extends SimpleMappingExceptionResolver {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected PortalWSManager portalWSManager;

    public DashboardExceptionResolver() {
        super();
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        ModelAndView mv = super.resolveException(request, response, handler, ex);
        if (mv == null) {
            mv = new ModelAndView("error");
        }

        // Leverage ErrorController to display custom error page with Dashboard application exception details
        if ((ex instanceof DashboardException) && (ex.getMessage() != null)) {
            ErrorDescriptor error = ErrorDescriptor.DEFAULT;
            mv.getModelMap().addAttribute(Constants.ATTR_ERROR_HEADING, error.getHeading());
            mv.getModelMap().addAttribute(Constants.ATTR_ERROR_CONTENT, ex.getMessage());
        } else {

            // If not Dashboard application exception, then provide developer exception details
            String stackTrace = getStackTrace(ex);
            logger.error(stackTrace);
            if (logger.isDebugEnabled()) {
                ErrorDescriptor error = ErrorDescriptor.EXCEPTION;
                mv.getModelMap().addAttribute(Constants.ATTR_ERROR_HEADING, error.getHeading());
                mv.getModelMap().addAttribute(Constants.ATTR_ERROR_CONTENT, ex.getMessage());

                // If debug is enabled, then provide developer exception stack trace
                mv.getModelMap().addAttribute(Constants.ATTR_ERROR_DETAILS_ENABLED, true);
                mv.getModelMap().addAttribute(Constants.ATTR_ERROR_DETAILS, stackTrace);
            }
        }

        response.setStatus(500);

        setContextPath(mv.getModelMap(), request);
        addHeaderFooter(mv.getModelMap());

        return mv;
    }

    /**
     * This method is converts the stack trace to a string
     * @param t, Throwable
     * @return returns a String of exception stack trace
     */
    public String getStackTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        t.printStackTrace(printWriter);
        printWriter.flush();
        stringWriter.flush();
        return stringWriter.toString();
    }

    protected void addHeaderFooter(ModelMap model) {
        String token = SecurityUtil.getToken();
        String header = portalWSManager.getHeader(token);
        if (header != null) {
            header = header.replace("[$USER_NAME$]", SecurityUtil.getUsername());
            model.addAttribute(Constants.ATTR_HEADER_STRING, header);
            model.addAttribute(Constants.ATTR_FOOTER_STRING, portalWSManager.getFooter(token));
        }
    }

    protected void setContextPath(ModelMap model, HttpServletRequest request) {
        model.addAttribute(Constants.CONTEXT_ROOT_PATH, request.getContextPath());
        model.addAttribute(Constants.CONTEXT_PREVIOUS_PATH,  "javascript:history.go(-1)");
    }

    @Autowired
    public void setPortalWSManager(PortalWSManager portalWSManager) {
        this.portalWSManager = portalWSManager;
    }
}

