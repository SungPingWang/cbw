package com.csprs.cbw;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ServletComponentScan
public class CsprsCbwApplication implements CommandLineRunner{
	
	@Autowired
	DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(CsprsCbwApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("DataSource = " + dataSource);
	}

}
