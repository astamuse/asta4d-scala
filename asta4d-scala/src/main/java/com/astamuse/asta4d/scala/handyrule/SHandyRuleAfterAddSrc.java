package com.astamuse.asta4d.scala.handyrule;

import com.astamuse.asta4d.web.dispatch.mapping.UrlMappingRule;
import com.astamuse.asta4d.web.dispatch.mapping.handy.HandyRuleAfterAddSrc;
import com.astamuse.asta4d.web.dispatch.mapping.handy.HandyRuleAfterAttr;
import com.astamuse.asta4d.web.dispatch.mapping.handy.HandyRuleAfterHandler;

@SuppressWarnings({ "unchecked" })
public class SHandyRuleAfterAddSrc extends HandyRuleAfterAddSrc<SHandyRuleAfterAddSrc, HandyRuleAfterAttr<?, ?>, HandyRuleAfterHandler<?>>
        implements SHandyRuleBuilder {

    public SHandyRuleAfterAddSrc(UrlMappingRule rule) {
        super(rule);
    }

}
