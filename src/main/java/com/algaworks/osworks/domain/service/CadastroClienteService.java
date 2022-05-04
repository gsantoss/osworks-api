package com.algaworks.osworks.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.osworks.domain.exception.NegocioException;
import com.algaworks.osworks.domain.model.Cliente;
import com.algaworks.osworks.domain.repository.ClienteRepository;

/**A anotação indica que a classe é um serviço, um componetente do Spring. O spring vai instaciar
um objeto dessa classe e este objeto va se tornar disponível para ser injetado em qualquer outro componente do spring, como por exemplo: ClienteController*/
@Service 
public class CadastroClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente salvar(Cliente cliente) {	
		Cliente clienteExistente =  clienteRepository.findByEmail(cliente.getEmail());
		
		if(clienteExistente != null && !clienteExistente.equals(cliente)) {
			 throw new NegocioException("Já existe um cliente cadastrado com esse email");
		}
		
		return clienteRepository.save(cliente);
	}
	
	public void excluir(Long idCliente) {
		clienteRepository.deleteById(idCliente);
	}
}
