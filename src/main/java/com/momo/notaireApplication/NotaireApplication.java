package com.momo.notaireApplication;

import com.momo.notaireApplication.exception.handling.RestResponseEntityExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RestResponseEntityExceptionHandler.class)
public class NotaireApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotaireApplication.class, args);
	}

}
