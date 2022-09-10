package com.prodain.scf;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.prodain.scf.common.repo.ExtendedEntityGraphQuerydslRepository;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.prodain.scf.model.repo", repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class, repositoryBaseClass = ExtendedEntityGraphQuerydslRepository.class)
@EntityScan(basePackages = {"com.prodain.scf.model.entity"})
public class ScfApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScfApplication.class, args);
	}
	
	
	@Bean
	public TaskExecutor taskExecutor()
	{
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(5);
		taskExecutor.initialize();
		
		return taskExecutor;
	}
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@PostConstruct
	public void init()
	{
		objectMapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

}
