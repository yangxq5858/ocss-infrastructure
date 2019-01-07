package com.ecmp.core.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/11/9 12:13
 */
public class CoreGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = 2672469983497772121L;

    private final String authority;
    private final Map<String, Map<String, String>> features;

    @JsonCreator
    public CoreGrantedAuthority(@JsonProperty("authority") String authority,
                                @JsonProperty("features") Map<String, Map<String, String>> features) {
        Assert.notNull(authority, "A granted authority textual representation is required");
        this.authority = authority;
        this.features = features;
    }

    public Map<String, Map<String, String>> getFeatures() {
        return features;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoreGrantedAuthority)) {
            return false;
        }
        CoreGrantedAuthority that = (CoreGrantedAuthority) o;
        return getAuthority().equals(that.getAuthority()) &&
                Objects.equals(getFeatures(), that.getFeatures());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthority(), getFeatures());
    }
}
