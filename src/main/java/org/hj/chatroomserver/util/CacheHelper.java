package org.hj.chatroomserver.util;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.stereotype.Component;

@Component
public class CacheHelper {
    private final CacheManager cacheManager;
    private final Cache<String, Integer> signUpCache;
    private final Cache<String, String> resetPassword;

    public CacheHelper() {
        this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();
        this.signUpCache = this.cacheManager.createCache("sign_up", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Integer.class, ResourcePoolsBuilder.heap(100)));
        this.resetPassword = this.cacheManager.createCache("reset_password", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(100)));
    }

    public void putSignUpCache(String key, Integer value){
        this.signUpCache.put(key,value);
    }

    public void deleteFromSignUpCache(String key){
        this.signUpCache.remove(key);
    }

    public Integer getFromSignUpCache(String key){
        return this.signUpCache.get(key);
    }

    public Cache<String, Integer> getSignUpCache() {
        return signUpCache;
    }

    public Cache<String, String> getResetPassword() {
        return resetPassword;
    }
}
