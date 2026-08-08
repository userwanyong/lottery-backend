package com.lottery.trigger.config;

import com.lottery.infrastructure.dao.SysConfigMapper;
import com.lottery.infrastructure.dao.po.SysConfig;
import com.lottery.types.annotation.DCCValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 动态配置中心（轻量版：基于数据库 sys_config 表，去 Zookeeper）
 * <p>
 * BeanPostProcessor 扫描 @DCCValue 字段并注入默认值；应用启动后从数据库加载实际值；
 * DCCController.update 触发时写库并反射更新内存字段，实现运行时热更新。
 */
@Slf4j
@Component
public class ConfigService implements BeanPostProcessor, ApplicationRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final Map<String, List<FieldRef>> fieldMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Object targetBean = bean;
        if (AopUtils.isAopProxy(bean)) {
            targetClass = AopUtils.getTargetClass(bean);
            targetBean = AopProxyUtils.getSingletonTarget(bean);
            if (targetBean == null) {
                targetBean = bean;
            }
        }
        for (Field field : targetClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(DCCValue.class)) {
                continue;
            }
            DCCValue dccValue = field.getAnnotation(DCCValue.class);
            String value = dccValue.value();
            String[] splits = value.split(":");
            String key = splits[0];
            String defaultValue = splits.length == 2 ? splits[1] : null;
            setField(targetBean, field, defaultValue);
            fieldMap.computeIfAbsent(key, k -> new ArrayList<>()).add(new FieldRef(targetBean, field));
        }
        return bean;
    }

    @Override
    public void run(ApplicationArguments args) {
        SysConfigMapper mapper = applicationContext.getBean(SysConfigMapper.class);
        for (Map.Entry<String, List<FieldRef>> entry : fieldMap.entrySet()) {
            SysConfig config = mapper.selectById(entry.getKey());
            if (config != null && config.getConfigValue() != null) {
                for (FieldRef ref : entry.getValue()) {
                    setField(ref.bean, ref.field, config.getConfigValue());
                }
            }
        }
        log.info("[ConfigService]DCC 配置从数据库加载完成 keys:{}", fieldMap.keySet());
    }

    /**
     * 更新配置：写库 + 反射更新内存字段（运行时热更新）
     */
    public void update(String key, String value) {
        SysConfigMapper mapper = applicationContext.getBean(SysConfigMapper.class);
        SysConfig config = new SysConfig();
        config.setConfigKey(key);
        config.setConfigValue(value);
        config.setUpdateTime(new Date());
        mapper.upsert(config);
        List<FieldRef> refs = fieldMap.get(key);
        if (refs != null) {
            for (FieldRef ref : refs) {
                setField(ref.bean, ref.field, value);
            }
        }
    }

    public String get(String key) {
        SysConfigMapper mapper = applicationContext.getBean(SysConfigMapper.class);
        SysConfig config = mapper.selectById(key);
        return config == null ? null : config.getConfigValue();
    }

    private void setField(Object bean, Field field, String value) {
        if (value == null) {
            return;
        }
        try {
            field.setAccessible(true);
            field.set(bean, value);
            field.setAccessible(false);
        } catch (Exception e) {
            log.error("[ConfigService]DCC 注入失败 field:{}", field.getName(), e);
        }
    }

    private static class FieldRef {
        final Object bean;
        final Field field;

        FieldRef(Object bean, Field field) {
            this.bean = bean;
            this.field = field;
        }
    }
}
