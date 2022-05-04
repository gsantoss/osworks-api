package com.algaworks.osworks.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.domain.model.Cliente;
import com.algaworks.osworks.domain.repository.ClienteRepository;
import com.algaworks.osworks.domain.service.CadastroClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CadastroClienteService cadastroCliente;
	
	@GetMapping
	public List<Cliente> listar() {
		return clienteRepository.findAll();
		//return clienteRepository.findByNome("Joao");
		//return clienteRepository.findByNomeContaining("ia");
	}
	
	/**Ete método vai buscar por ID e para isso devemos colocar além dos parâmetros uma anotação
	que indica que tal parâmetro fará a busca por id*/
	@GetMapping("/{clienteId}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
		Optional<Cliente>  cliente = clienteRepository.findById(clienteId);
		
		//Se o cliente está presente, então...
		if(cliente.isPresent()) {
			//Retorna um cliente com o código http = 200 (Sucesso - ok)
			return ResponseEntity.ok(cliente.get());
		}
		
		//Se não estpa presente nehum cliente então retorna o código http 404 - Not Found
		return ResponseEntity.notFound().build();
	}
	
	/**Este método irá adicionar um cliente e para isso precisamos fazer a anotação do parâmetro
	com  o @RequestBody para que o spring tranforme o JSON que vem no corpo da requisição para um objeto do 
	tipo cliente*/
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) //Anotação para enviar o STATUS Codigo 201 = Status de criação
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) {
		return cadastroCliente.salvar(cliente);
	}
	
	@PutMapping("/{clienteId}")
	public ResponseEntity<Cliente> atualizar(@Valid @PathVariable Long clienteId, @RequestBody Cliente cliente){
		
		if(!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		//Preciso setar o id do cliente, pois senão ele acaba criando outro cliente
		cliente.setId(clienteId);
		cliente = cadastroCliente.salvar(cliente);
		
		return ResponseEntity.ok(cliente);
	}
	
	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Void> remover(@PathVariable Long clienteId){
		
		if(!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		cadastroCliente.excluir(clienteId);
		
		return ResponseEntity.noContent().build();
	}
}
