package com.dmall.os.web.config;

import com.dmall.os.order.spi.BaseSpi;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 自动注册SPI实现类为Spring Bean
 */
@Component
public class BeanRegistry implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String spiClassStr = environment.getProperty("spiClass");
        if (!StringUtils.hasLength(spiClassStr)) {
            return;
        }

        Stream.of(spiClassStr.split(",")).map(spiClass -> {
            Class<? extends BaseSpi> clazz = null;
            try {
                clazz = (Class<? extends BaseSpi>) Class.forName(spiClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return clazz;
        }).forEach(spiClass -> {
            Class<? extends BaseSpi> spiImplClass = StreamSupport.stream(ServiceLoader.load(spiClass).spliterator(), false)
                                                                 .sorted(Comparator.comparingInt(BaseSpi::getIndex).reversed())
                                                                 .findFirst()
                                                                 .get()
                                                                 .getClass();
            registry.registerBeanDefinition(spiImplClass.getSimpleName(), new RootBeanDefinition(spiImplClass));
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
