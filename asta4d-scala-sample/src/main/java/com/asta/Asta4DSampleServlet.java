/*
 * Copyright 2014 astamuse company,Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.asta;

import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;

import com.asta.interceptor.SamplePageInterceptor;
import com.asta.interceptor.SampleSnippetInterceptor;
import com.astamuse.asta4d.snippet.DefaultSnippetInvoker;
import com.astamuse.asta4d.util.i18n.LocalizeUtil;
import com.astamuse.asta4d.web.WebApplicationConfiguration;
import com.astamuse.asta4d.web.WebApplicationContext;
import com.astamuse.asta4d.web.servlet.Asta4dServlet;

public class Asta4DSampleServlet extends Asta4dServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected WebApplicationConfiguration createConfiguration() {
        WebApplicationConfiguration conf = super.createConfiguration();

        conf.getPageInterceptorList().add(new SamplePageInterceptor());

        DefaultSnippetInvoker snippetInvoker = ((DefaultSnippetInvoker) conf.getSnippetInvoker());
        snippetInvoker.getSnippetInterceptorList().add(new SampleSnippetInterceptor());

        boolean debug = Boolean.getBoolean("asta4d.sample.debug");
        if (debug) {
            conf.setCacheEnable(false);
            conf.setSaveCallstackInfoOnRendererCreation(true);
        }

        return conf;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // for a international application, we use root as default locale
        Locale.setDefault(Locale.ROOT);
        super.init(config);
    }

    @Override
    protected void service() throws Exception {
        // resolve the locale of current request
        // for a formal web application, the locale should be resolved from the head sent by client browser,
        // but as a sample project, we use a simple way to decide the locale to simplify the logic

        WebApplicationContext context = WebApplicationContext.getCurrentThreadWebApplicationContext();
        String locale = context.getRequest().getParameter("locale");
        if (StringUtils.isNotEmpty(locale)) {
            context.setCurrentLocale(LocalizeUtil.getLocale(locale));
        }
        super.service();
    }

}
