package br.com.giulianabezerra.demorestclientsb;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class DemoRestclientSbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRestclientSbApplication.class, args);
	}

	@Bean
	RestClient crudClient() {
		return RestClient
				.create("https://crudcrud.com/api/ee51bf0f169a4612ba9b8f563a5110e0");
	}

	@Bean
	ApplicationRunner runner(CrudService crudService) {
		return args -> {
			var leia = new Cat("leia", "preta", 4.5);
			// Create
			System.out.println(crudService.createCat(leia));

			// Read and Update - Modificar o id para um existente!
			// final var id = "65b98605658e2403e8763af6";
			// System.out.println(crudService.getCat(id));
			// leia = leia.withWeight(4.0);
			// crudService.updateCat(id, leia);
			// System.out.println(crudService.getCat(id));
			// crudService.deleteCat(id);
			// System.out.println(crudService.listCats());
		};
	}
}

@Service
class CrudService {
	private final RestClient restClient;

	public CrudService(RestClient restClient) {
		this.restClient = restClient;
	}

	public Cat createCat(Cat cat) {
		return restClient.post()
				.uri("/cats")
				.contentType(MediaType.APPLICATION_JSON)
				.body(cat)
				.retrieve()
				.body(Cat.class);
	}

	public Cat getCat(String id) {
		return restClient.get()
				.uri("/cats/{id}", id)
				.retrieve()
				.body(Cat.class);
	}

	public void updateCat(String id, Cat cat) {
		restClient.put()
				.uri("/cats/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.body(cat)
				.retrieve()
				.toBodilessEntity();
	}

	public void deleteCat(String id) {
		restClient.delete()
				.uri("/cats/{id}", id)
				.retrieve()
				.toBodilessEntity();
	}

	public List<Cat> listCats() {
		return restClient.get()
				.uri("/cats")
				.retrieve()
				.body(new ParameterizedTypeReference<>() {
				});
	}
}

record Cat(String name, String color, Double weight) {
	public Cat withWeight(Double newWeight) {
		return new Cat(name, color, newWeight);
	}
}