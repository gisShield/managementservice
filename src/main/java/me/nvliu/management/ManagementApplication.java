package me.nvliu.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.DispatcherServlet;

@EnableScheduling
@SpringBootApplication
@MapperScan("me.nvliu.management.web.dao")
@ServletComponentScan
public class ManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
	}

	/*@Bean
	public DispatcherServletBeanPostProcessor dispatcherServletBeanPostProcessor() {
		return new DispatcherServletBeanPostProcessor();
	}

	public static class DispatcherServletBeanPostProcessor implements BeanPostProcessor {
		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			if (bean instanceof DispatcherServlet) {
				((DispatcherServlet) bean).setDispatchOptionsRequest(true);
			}
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			return bean;
		}
	}*/
}
