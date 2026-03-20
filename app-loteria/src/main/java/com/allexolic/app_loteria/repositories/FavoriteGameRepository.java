package com.allexolic.app_loteria.repositories;

import com.allexolic.app_loteria.entities.FavoriteGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteGameRepository extends JpaRepository<FavoriteGame, Long> {
    interface CheckJogoFavorito {
        Integer getMaxMatch();
        Boolean getJaSorteado();
    }

    @Query(value = """
    select
      max(
        (case when d.bola1  = any(:nums) then 1 else 0 end) +
        (case when d.bola2  = any(:nums) then 1 else 0 end) +
        (case when d.bola3  = any(:nums) then 1 else 0 end) +
        (case when d.bola4  = any(:nums) then 1 else 0 end) +
        (case when d.bola5  = any(:nums) then 1 else 0 end) +
        (case when d.bola6  = any(:nums) then 1 else 0 end) +
        (case when d.bola7  = any(:nums) then 1 else 0 end) +
        (case when d.bola8  = any(:nums) then 1 else 0 end) +
        (case when d.bola9  = any(:nums) then 1 else 0 end) +
        (case when d.bola10 = any(:nums) then 1 else 0 end) +
        (case when d.bola11 = any(:nums) then 1 else 0 end) +
        (case when d.bola12 = any(:nums) then 1 else 0 end) +
        (case when d.bola13 = any(:nums) then 1 else 0 end) +
        (case when d.bola14 = any(:nums) then 1 else 0 end) +
        (case when d.bola15 = any(:nums) then 1 else 0 end)
      ) as maxMatch,
      bool_or(
        (d.bola1  = any(:nums)) and
        (d.bola2  = any(:nums)) and
        (d.bola3  = any(:nums)) and
        (d.bola4  = any(:nums)) and
        (d.bola5  = any(:nums)) and
        (d.bola6  = any(:nums)) and
        (d.bola7  = any(:nums)) and
        (d.bola8  = any(:nums)) and
        (d.bola9  = any(:nums)) and
        (d.bola10 = any(:nums)) and
        (d.bola11 = any(:nums)) and
        (d.bola12 = any(:nums)) and
        (d.bola13 = any(:nums)) and
        (d.bola14 = any(:nums)) and
        (d.bola15 = any(:nums))
      ) as jaSorteado
    from arquivo_loteria_detalhe d
    """, nativeQuery = true)
    CheckJogoFavorito checkJogoFavorito(@Param("nums") int[] nums);

    List<FavoriteGame> findAllByOrderByIdAsc();
}
