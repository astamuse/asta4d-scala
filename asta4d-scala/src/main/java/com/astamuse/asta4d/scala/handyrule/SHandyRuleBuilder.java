package com.astamuse.asta4d.scala.handyrule;

import com.astamuse.asta4d.web.dispatch.mapping.UrlMappingRule;
import com.astamuse.asta4d.web.dispatch.mapping.handy.HandyRuleBuilder;

@SuppressWarnings("unchecked")
public interface SHandyRuleBuilder extends HandyRuleBuilder {

    @Override
    default SHandyRuleAfterAddSrc buildHandyRuleAfterAddSrc(UrlMappingRule rule) {
        return new SHandyRuleAfterAddSrc(rule);
    }

    @Override
    default SHandyRuleAfterAddSrcAndTarget buildHandyRuleAfterAddSrcAndTarget(UrlMappingRule rule) {
        return new SHandyRuleAfterAddSrcAndTarget(rule);
    }

}
