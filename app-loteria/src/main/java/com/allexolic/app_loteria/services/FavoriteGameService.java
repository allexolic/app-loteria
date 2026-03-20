package com.allexolic.app_loteria.services;

import com.allexolic.app_loteria.entities.FavoriteGame;
import com.allexolic.app_loteria.repositories.FavoriteGameRepository;
import com.allexolic.app_loteria.services.dto.FavoriteGameCreateRequest;
import com.allexolic.app_loteria.services.dto.FavoriteGameResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteGameService {
    private final FavoriteGameRepository favoriteGameRepository;

    @Transactional
    public FavoriteGameResponse create(FavoriteGameCreateRequest req) {
        List<Integer> bolas = validateBalls(req.balls());
        val newJogo = new FavoriteGame();
        setBolas(newJogo, bolas);
        return toResponse(favoriteGameRepository.save(newJogo));
    }

    @Transactional
    public FavoriteGameResponse recalculate(Long jogoId) {
        FavoriteGame jogo = favoriteGameRepository.findById(jogoId)
                .orElseThrow(() -> new IllegalArgumentException("Jogo favorito não encontrado: " + jogoId));
        int[] numeros = getBolasAsIntArray(jogo);
        var checkedJogoFavorito = favoriteGameRepository.checkJogoFavorito(numeros);
        int maxMatch = checkedJogoFavorito.getMaxMatch() == null ? 0 : checkedJogoFavorito.getMaxMatch();
        boolean isAwarded = Boolean.TRUE.equals(checkedJogoFavorito.getJaSorteado());
        jogo.setTotalNumerosSorteados(maxMatch);
        jogo.setJogoSorteado(isAwarded);
        jogo = favoriteGameRepository.save(jogo);
        return toResponse(jogo);
    }

    private List<Integer> validateBalls(List<Integer> bolas) {
        if (bolas == null || bolas.size() != 15) {
            throw new IllegalArgumentException("Informe exatamente 15 números.");
        }
        for (Integer n : bolas) {
            if (n == null || n < 1 || n > 25) {
                throw new IllegalArgumentException("Cada número deve estar entre 1 e 25.");
            }
        }
        Set<Integer> set = new HashSet<>(bolas);
        if (set.size() != 15) {
            throw new IllegalArgumentException("Não pode haver números repetidos.");
        }
        return bolas.stream().sorted().collect(Collectors.toList());
    }

    private void setBolas(FavoriteGame j, List<Integer> b) {
        j.setBola1(b.get(0).intValue());
        j.setBola2(b.get(1).intValue());
        j.setBola3(b.get(2).intValue());
        j.setBola4(b.get(3).intValue());
        j.setBola5(b.get(4).intValue());
        j.setBola6(b.get(5).intValue());
        j.setBola7(b.get(6).intValue());
        j.setBola8(b.get(7).intValue());
        j.setBola9(b.get(8).intValue());
        j.setBola10(b.get(9).intValue());
        j.setBola11(b.get(10).intValue());
        j.setBola12(b.get(11).intValue());
        j.setBola13(b.get(12).intValue());
        j.setBola14(b.get(13).intValue());
        j.setBola15(b.get(14).intValue());
    }

    private int[] getBolasAsIntArray(FavoriteGame favoriteGame) {
        return new int[] {
                favoriteGame.getBola1(), favoriteGame.getBola2(), favoriteGame.getBola3(), favoriteGame.getBola4(), favoriteGame.getBola5(),
                favoriteGame.getBola6(), favoriteGame.getBola7(), favoriteGame.getBola8(), favoriteGame.getBola9(), favoriteGame.getBola10(),
                favoriteGame.getBola11(), favoriteGame.getBola12(), favoriteGame.getBola13(), favoriteGame.getBola14(), favoriteGame.getBola15()
        };
    }

    private FavoriteGameResponse toResponse(FavoriteGame favoriteGame) {
        List<Integer> bolas = List.of(
                favoriteGame.getBola1(), favoriteGame.getBola2(), favoriteGame.getBola3(), favoriteGame.getBola4(), favoriteGame.getBola5(),
                favoriteGame.getBola6(), favoriteGame.getBola7(), favoriteGame.getBola8(), favoriteGame.getBola9(), favoriteGame.getBola10(),
                favoriteGame.getBola11(), favoriteGame.getBola12(), favoriteGame.getBola13(), favoriteGame.getBola14(), favoriteGame.getBola15()
        );

        return new FavoriteGameResponse(favoriteGame.getId(), bolas, favoriteGame.isJogoSorteado(), favoriteGame.getTotalNumerosSorteados());
    }

    public List<FavoriteGameResponse> listGames() {
        return favoriteGameRepository.findAllByOrderByIdAsc().stream().map(this::toResponse).toList();
    }
}
