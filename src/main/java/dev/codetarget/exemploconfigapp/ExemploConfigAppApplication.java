package dev.codetarget.exemploconfigapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import dev.codetarget.exemploconfigapp.config.ApiFactoryConfig;

@SpringBootApplication
public class ExemploConfigAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExemploConfigAppApplication.class, args).close();
	}

	@Component
	public static class Runner implements ApplicationRunner {

        @Autowired
		private ApiFactoryConfig config;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			System.out.println("Buscando dados da API");
			System.out.println("URL     : " + config.getHostApi());
			System.out.println("Usuario : " + config.getUserApi());
			System.out.println("Senha   : " + config.getPasswordApi());
			System.out.println("EndPoint Token: " + config.getEndPointAuth());
			System.out.println("EndPoint Categorias: " + config.getEndPointCategorias());
		}
	}
}
