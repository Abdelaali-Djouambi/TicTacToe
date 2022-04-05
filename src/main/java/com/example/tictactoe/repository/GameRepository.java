package com.example.tictactoe.repository;

import com.example.tictactoe.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface GameRepository extends JpaRepository<Game,Long> {
    Collection<Game> findByStatusInAndPlayerX_IdOrStatusInAndPlayerO_Id(List<Game.Status> statuses,Long playerXId,List<Game.Status> status,Long playerOId);
    Collection<Game> findByStatus(Game.Status status);
}
