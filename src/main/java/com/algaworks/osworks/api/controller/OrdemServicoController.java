package com.algaworks.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.api.model.OrdemServicoInput;
import com.algaworks.osworks.api.model.OrdemServicoModel;
import com.algaworks.osworks.domain.model.OrdemServico;
import com.algaworks.osworks.domain.repository.OrdemServicoRepository;
import com.algaworks.osworks.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServicoServico;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	//Estamos injetando uma instância do model Mapper para receber a tranferência de dados da Ordem de Serviço
	//Além disso o precisamos transformar o modelMap em uma instância gerenciada pelo Spring dentro da Classe ModelMapperConfig(com.algaworks.osworks.core)
	@Autowired
	private ModelMapper modelMapper;
	
	//O método abaixa recebe uma ordemServicoInput e converte essa ordemServicoInput em uma entidade
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)//Anotação para status de sucesso (http: 201) quando se cria algo
	public OrdemServicoModel criar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
		
		OrdemServico ordemServico = toEntity(ordemServicoInput);
		return toModel(gestaoOrdemServicoServico.criar(ordemServico));
	}
	
	@GetMapping
	public List<OrdemServicoModel> listar(){
		return toColletionModel(ordemServicoRepository.findAll());
	}
	
	@GetMapping({"/{ordemServicoId}"})
	public ResponseEntity<OrdemServicoModel> buscar(@PathVariable Long ordemServicoId){
		Optional<OrdemServico>	ordemServico =  ordemServicoRepository.findById(ordemServicoId);
		
		//Se a ordem de serviço estiver presente
		if(ordemServico.isPresent()) {
			OrdemServicoModel ordemServicoModel = toModel(ordemServico.get());
			//...então retorne a ordem de serviço
			return	ResponseEntity.ok(ordemServicoModel);
		}
		
		//...Se não tiver presente então retorne código: 404 (Not Found)... Não encontrado
	
		return ResponseEntity.notFound().build();
	}
	
	
	//Usamos a anaotação Putting, pois esse recurso é idempotente, ou seja, ele não vai gerar
	//efeitos colaterais se rqusitado várias vezes....E usamos a Status NO_CONTENT, porque a resposta
	//para essa solictiação não precisará retornar nada ná pagina	
	@PutMapping("/{ordemServicoId}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	private void finalizar(@PathVariable Long ordemServicoId) {
		gestaoOrdemServicoServico.finalizar(ordemServicoId);
	}
	
	//Método que tranfere os atributos de uma Ordem de Serviço em Ordem De Servico Model
	private OrdemServicoModel toModel(OrdemServico ordemServico) {
		return modelMapper.map(ordemServico, OrdemServicoModel.class);
	}
	
	
	private List<OrdemServicoModel> toColletionModel(List<OrdemServico> ordensServico){
		
		//O método Stream() retornará uma fluxo, ou seja uma sequencia de elementos que suporta operações de:
		//agragação, de trnasformação. O .map() vai aplicar uma função a todos os elementos um-a-um desse Stream() e retornará
		//um novo stream() como reultado;
		//Ou seja, no final das contas esse método vai tranfermar uma lista de Ordem de Serviço em uma lista de Ordem de Serviço Model
		return ordensServico.stream().map(ordemServico -> toModel(ordemServico))
				.collect(Collectors.toList()); 
	}
	
	//Método que tranforma uma ordemSevicoInput em uma OrdemServico
	
	private OrdemServico toEntity(OrdemServicoInput ordemServicoInput) {
		return modelMapper.map(ordemServicoInput, OrdemServico.class);
	}
}
