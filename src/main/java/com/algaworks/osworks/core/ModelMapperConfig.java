package com.algaworks.osworks.core;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Anotação componete Spring com o objetivo de configurações de Beans
@Configuration
public class ModelMapperConfig {

	//O Bean indica que esse método ele instacia/inicializa o Bean do tipo ModelMapper que será gerenciado pelo Spring
	//..e portanto vai ser disponibilizado para injeção de dependÊncia em outras classes.
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
