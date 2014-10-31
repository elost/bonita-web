/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.test.toolkit.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Nicolas Chabanoles
 */
public class MockHttpServletRequest implements HttpServletRequest {

    HttpSession session = null;

    Map<String, Object> parametersMap = null;

    Map<String, Object> attributesMap = null;

    HttpServletRequest req = null;

    String pathInfo = null;

    public MockHttpServletRequest(final HttpServletRequest req) {
        this.req = req;
        parametersMap = req.getParameterMap();
    }

    public MockHttpServletRequest() {
        attributesMap = new HashMap<String, Object>();
        parametersMap = new HashMap<String, Object>();
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getAuthType()
     */
    @Override
    public String getAuthType() {
        if (req != null) {
            return req.getAuthType();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     */
    @Override
    public String getContextPath() {
        if (req != null) {
            return req.getContextPath();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    @Override
    public Cookie[] getCookies() {
        if (req != null) {
            return req.getCookies();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
     */
    @Override
    public long getDateHeader(final String anName) {
        if (req != null) {
            return req.getDateHeader(anName);
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
     */
    @Override
    public String getHeader(final String anName) {
        if (req != null) {
            return req.getHeader(anName);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
     */
    @Override
    public Enumeration<?> getHeaderNames() {
        if (req != null) {
            return req.getHeaderNames();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
     */
    @Override
    public Enumeration<?> getHeaders(final String anName) {
        if (req != null) {
            return req.getHeaders(anName);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
     */
    @Override
    public int getIntHeader(final String anName) {
        if (req != null) {
            return req.getIntHeader(anName);
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    @Override
    public String getMethod() {
        if (req != null) {
            return req.getMethod();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    @Override
    public String getPathInfo() {
        if (pathInfo != null) {
            return pathInfo;
        } else if (req != null) {
            return req.getPathInfo();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
     */
    @Override
    public String getPathTranslated() {
        if (req != null) {
            return req.getPathTranslated();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
    @Override
    public String getQueryString() {
        if (req != null) {
            return req.getQueryString();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
     */
    @Override
    public String getRemoteUser() {
        if (req != null) {
            return req.getRemoteUser();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRequestURI()
     */
    @Override
    public String getRequestURI() {
        if (req != null) {
            return req.getRequestURI();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRequestURL()
     */
    @Override
    public StringBuffer getRequestURL() {
        if (req != null) {
            return req.getRequestURL();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
     */
    @Override
    public String getRequestedSessionId() {
        if (req != null) {
            return req.getRequestedSessionId();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
     */
    @Override
    public String getServletPath() {
        if (req != null) {
            return req.getRequestedSessionId();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getSession()
     */
    @Override
    public HttpSession getSession() {
        if (req != null) {
            session = req.getSession();
        }
        if (session == null) {
            session = new MockHttpSession();
        }
        return session;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
     */
    @Override
    public HttpSession getSession(final boolean anCreate) {

        if (anCreate) {
            if (req != null) {
                session = req.getSession(anCreate);
            } else {
                session = new MockHttpSession();
            }
        }
        return session;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
     */
    @Override
    public Principal getUserPrincipal() {
        if (req != null) {
            return req.getUserPrincipal();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
     */
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        if (req != null) {
            return req.isRequestedSessionIdFromCookie();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
     */
    @Override
    public boolean isRequestedSessionIdFromURL() {
        if (req != null) {
            return req.isRequestedSessionIdFromURL();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
     */
    @Override
    @Deprecated
    public boolean isRequestedSessionIdFromUrl() {
        if (req != null) {
            return req.isRequestedSessionIdFromUrl();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
     */
    @Override
    public boolean isRequestedSessionIdValid() {
        if (req != null) {
            return req.isRequestedSessionIdValid();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
     */
    @Override
    public boolean isUserInRole(final String anRole) {
        if (req != null) {
            return req.isUserInRole(anRole);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(final String aName) {
        if (req != null) {
            return req.getAttribute(aName);
        }
        return attributesMap.get(aName);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    @Override
    public Enumeration<?> getAttributeNames() {
        if (req != null) {
            return req.getAttributeNames();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    @Override
    public String getCharacterEncoding() {
        if (req != null) {
            return req.getCharacterEncoding();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    @Override
    public int getContentLength() {
        if (req != null) {
            return req.getContentLength();
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getContentType()
     */
    @Override
    public String getContentType() {
        if (req != null) {
            return req.getContentType();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (req != null) {
            return req.getInputStream();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocalAddr()
     */
    @Override
    public String getLocalAddr() {
        if (req != null) {
            return req.getLocalAddr();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocalName()
     */
    @Override
    public String getLocalName() {
        if (req != null) {
            return req.getLocalName();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocalPort()
     */
    @Override
    public int getLocalPort() {
        if (req != null) {
            return req.getLocalPort();
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocale()
     */
    @Override
    public Locale getLocale() {
        if (req != null) {
            return req.getLocale();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocales()
     */
    @Override
    public Enumeration<?> getLocales() {
        if (req != null) {
            return req.getLocales();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(final String anName) {
        return (String) parametersMap.get(anName);
    }

    public void setParameter(final String anName, final Object value) {
        parametersMap.put(anName, value);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getParameterMap() {
        if (req == null) {
            return parametersMap;
        }
        return req.getParameterMap();
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    @Override
    public Enumeration<?> getParameterNames() {
        if (req != null) {
            return req.getParameterNames();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    @Override
    public String[] getParameterValues(final String anName) {
        if (req != null) {
            return req.getParameterValues(anName);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    @Override
    public String getProtocol() {
        if (req != null) {
            return req.getProtocol();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getReader()
     */
    @Override
    public BufferedReader getReader() throws IOException {
        if (req != null) {
            return req.getReader();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
     */
    @Override
    @Deprecated
    public String getRealPath(final String anPath) {
        if (req != null) {
            return req.getRealPath(anPath);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    @Override
    public String getRemoteAddr() {
        if (req != null) {
            return req.getRemoteAddr();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    @Override
    public String getRemoteHost() {
        if (req != null) {
            return req.getRemoteHost();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRemotePort()
     */
    @Override
    public int getRemotePort() {
        if (req != null) {
            return req.getRemotePort();
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
     */
    @Override
    public RequestDispatcher getRequestDispatcher(final String anPath) {
        if (req != null) {
            return req.getRequestDispatcher(anPath);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getScheme()
     */
    @Override
    public String getScheme() {
        if (req != null) {
            return req.getScheme();
        }
        return "http";
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getServerName()
     */
    @Override
    public String getServerName() {
        if (req != null) {
            return req.getServerName();
        }
        return "localhost";
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#getServerPort()
     */
    @Override
    public int getServerPort() {
        if (req != null) {
            return req.getServerPort();
        }
        return 8080;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#isSecure()
     */
    @Override
    public boolean isSecure() {
        if (req != null) {
            return req.isSecure();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(final String anName) {
        if (req != null) {
            req.removeAttribute(anName);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttribute(final String aName, final Object aValue) {
        if (req != null) {
            req.setAttribute(aName, aValue);
        } else {
            attributesMap.put(aName, aValue);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
     */
    @Override
    public void setCharacterEncoding(final String anEnv) throws UnsupportedEncodingException {
        if (req != null) {
            req.setCharacterEncoding(anEnv);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
     */
    public void setPathInfo(final String pathInfo) {
        this.pathInfo = pathInfo;
    }

}
