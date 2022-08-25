package com.gerenciadordepersonagem.personagem.service;

import com.gerenciadordepersonagem.personagem.model.CreatePersonagemRequest;
import com.gerenciadordepersonagem.personagem.model.InformacoesPersonagem;
import com.gerenciadordepersonagem.personagem.model.Personagem;
import com.gerenciadordepersonagem.personagem.repository.PersonagemRepository;
import com.gerenciadordepersonagem.security.service.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PersonagemService {
    private PersonagemRepository repository;
    private JwtTokenUtil jwtTokenUtil;
    private final int MAX_PERSONAGENS = 20;

    public Personagem create (String authorization, CreatePersonagemRequest request) {
        String token = authorization.split(" ")[1].trim();
        String ownerId = jwtTokenUtil.getOwnerId(token);

        String nome = request.getNome();
        Double idade = request.getIdade();
        String nacionalidade = request.getNacionalidade();
        String aparencia = request.getAparencia();

        boolean isTotalItemsReached = isTotalItemsReached(authorization);
        if(isTotalItemsReached){
            throw new IllegalStateException("Você atingiu o máximo de personagens, exclua algum para poder criar outro");
        }
        boolean isComplete = isCreatePersonagemRequestValid(request);
        if (!isComplete) {
            throw new IllegalStateException("Você deve fornecer nome,idade,aparencia e nacionalidade");
        }
        Personagem personagem = new Personagem(ownerId, nome, idade, nacionalidade, aparencia);
        if (isNameNotValid(personagem)) {
            throw new IllegalStateException("Nome inválido");
        }
        if (isIdadeNotValid(personagem)) {
            throw new IllegalStateException("Idade deve ser maior do que 0");
        }
        personagem.setExperiencia(1000.0);
        updateNivelByExperiencia(personagem);
        InformacoesPersonagem informacoesPersonagem = new InformacoesPersonagem();
        informacoesPersonagem.setTotalPontos(30.0);
        personagem.setInformacoesPersonagem(informacoesPersonagem);
        repository.save(personagem);
        return repository.save(personagem);
    }

    public void delete (String id) {
        repository.deleteById(id);
    }
    public Personagem read (String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Personagem com a id %s não encontrado", id)));
    }

    public Map <String, Object> readAll (String authorization, int page, int size) {
        int pageMaxSize = 8;
        if (size < 1 || size > pageMaxSize) {
            throw new IllegalStateException(String.format("Tamanho máximo da página: %d.\n" + "Primeira página: 1.", pageMaxSize));
        }
        String token = authorization.split(" ")[1].trim();
        String ownerId = jwtTokenUtil.getOwnerId(token);
        page -= 1;

        List <Personagem> personagemList;
        Pageable paging = PageRequest.of(page, size);

        Page <Personagem> personagemPage;
        personagemPage = repository.findAllByOwnerId(ownerId, paging);
        personagemList = personagemPage.getContent();
        Map <String, Object> response = new HashMap <>();
        response.put("personagens", personagemList);
        response.put("currentPage", personagemPage.getNumber() + 1);
        response.put("totalItems", personagemPage.getTotalElements());
        response.put("totalPages", personagemPage.getTotalPages());
        return response;
    }
    public Map <String, Object> readAllByName (String authorization, int page, int size, String name) {
        int pageMaxSize = 8;
        if (size < 1 || size > pageMaxSize) {
            throw new IllegalStateException(String.format("Tamanho máximo da página: %d.\n" + "Primeira página: 1.", pageMaxSize));
        }
        String token = authorization.split(" ")[1].trim();
        String ownerId = jwtTokenUtil.getOwnerId(token);
        page -= 1;

        List <Personagem> personagemList;
        Pageable paging = PageRequest.of(page, size);

        Page <Personagem> personagemPage;
        personagemPage = repository.findAllByOwnerIdAndNome(ownerId, name, paging);
        personagemList = personagemPage.getContent();
        Map <String, Object> response = new HashMap <>();
        response.put("personagens", personagemList);
        response.put("currentPage", personagemPage.getNumber() + 1);
        response.put("totalItems", personagemPage.getTotalElements());
        response.put("totalPages", personagemPage.getTotalPages());
        return response;
    }

    public Personagem update (String authorization, Personagem personagem) {
        boolean sameOwnerId = isOwnerIdProvidedInJwtTheSameAsPersonagemOwnerId(authorization, personagem);
        if (!sameOwnerId) {
            throw new IllegalStateException("Apenas o dono pode editar seu personagem");
        }
        if (personagem.getExperiencia() < 1000 || personagem.getExperiencia() > 100000) {
            throw new IllegalStateException("Valor de experiência deve existir no intervalo [1000,100000]");
        }
        if (isNameNotValid(personagem)) {
            throw new IllegalStateException("Nome Inválido");
        }

        updateNivelByExperiencia(personagem);
        if (!isDistribuicaoPontosValid(personagem)) {
            throw new IllegalStateException("Foram distribuidos mais pontos do que disponíveis");
        }
        if (isIdadeNotValid(personagem)) {
            throw new IllegalStateException("Idade deve ser maior do que 0");
        }

        atualizarInformacoesPersonagem(personagem);
        return repository.save(personagem);
    }

    public boolean isCreatePersonagemRequestValid (CreatePersonagemRequest request) {
        try {
            request.getNome().length();
            request.getNacionalidade().length();
            request.getAparencia().length();
            request.getIdade().longValue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateNivelByExperiencia (Personagem personagem) {
        personagem.setNivel(Math.floor(((personagem.getExperiencia() - 1000) / 1000) + 1));
    }

    public boolean isDistribuicaoPontosValid (Personagem personagem) {
        InformacoesPersonagem inf = personagem.getInformacoesPersonagem();
        Double totalPontos = inf.getTotalPontos();
        double pontosGastos = inf.getForca() + inf.getAgilidade() + inf.getAtletismo() + inf.getDestreza() + inf.getPontaria() + inf.getFurtividade() + inf.getPercepcao() + inf.getInteligencia() + inf.getSabedoria() + inf.getCarisma() + inf.getDiplomacia() + inf.getEnganacao() + inf.getIntimidacao() + inf.getVontade() + inf.getSentirMotivacao();

        return !(pontosGastos > totalPontos);
    }
    public void atualizarInformacoesPersonagem (Personagem personagem) {
        InformacoesPersonagem informacoesPersonagem = personagem.getInformacoesPersonagem();
        informacoesPersonagem.atualizarInformacoesDerivadas();
        personagem.setInformacoesPersonagem(informacoesPersonagem);
    }

    public boolean isNameNotValid (Personagem personagem) {
        String nome = personagem.getNome();
        return nome.isEmpty() || nome.isBlank();
    }
    public boolean isIdadeNotValid (Personagem personagem) {
        Double idade = personagem.getIdade();
        return idade <= 0;
    }
    public boolean isOwnerIdProvidedInJwtTheSameAsPersonagemOwnerId (String authorization, Personagem personagem) {
        String token = authorization.split(" ")[1].trim();
        String ownerId = jwtTokenUtil.getOwnerId(token);
        String personagemOwnerId = personagem.getOwnerId();
        return ownerId.equals(personagemOwnerId);
    }

    public boolean isTotalItemsReached (String authorization) {
        Map <String, Object> personagemPages = readAll(authorization, 1, 1);
        Object objectTotalItems = personagemPages.get("totalItems");
        String stringTotalItems = objectTotalItems.toString();
        int totalItems = Integer.parseInt(stringTotalItems);
        return totalItems >= MAX_PERSONAGENS;
    }
}