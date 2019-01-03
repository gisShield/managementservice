package me.nvliu.management.web.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UrlMetadataSourceService implements FilterInvocationSecurityMetadataSource {
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        final HttpServletRequest request = ((FilterInvocation) o).getRequest();
        Set<ConfigAttribute> allAttributes = new HashSet<>();
        ConfigAttribute configAttribute = new UrlConfigAttribute(request);
        allAttributes.add(configAttribute);
        return allAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
