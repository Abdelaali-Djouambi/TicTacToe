package com.example.tictactoe.service;

import com.example.tictactoe.dto.GameDTO;
import com.example.tictactoe.dto.PlayDTO;
import com.example.tictactoe.exceptions.BadRequestException;
import com.example.tictactoe.exceptions.ErrorCode;
import com.example.tictactoe.exceptions.NotAllowedException;
import com.example.tictactoe.exceptions.ResourceNotFoundException;
import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.Player;
import com.example.tictactoe.repository.GameRepository;
import com.example.tictactoe.repository.PlayerRepository;
import com.example.tictactoe.util.GameUtil;
import com.example.tictactoe.util.ModelMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static com.example.tictactoe.model.Game.FRAME_VALUE;
import static com.example.tictactoe.model.Game.Status;
import static com.example.tictactoe.model.Game.Status.*;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    final GameRepository gameRepository;
    final PlayerRepository playerRepository;
    final ModelMapperUtil modelMapperUtil;

    final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, PlayerRepository playerRepository, ModelMapperUtil modelMapperUtil) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.modelMapperUtil = modelMapperUtil;
    }

    @Override
    public Optional<GameDTO> initGame(String aliasPlayer) {

        Optional<Player> optionalPlayer = playerRepository.findByAlias(aliasPlayer);

        Player player = optionalPlayer.orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.PLAYER_NOT_FOUND.getDefaultMessage() + aliasPlayer)
        );


        Status[] statuses = new Status[]{X_TURN, O_TURN, WAITING_OPPONENT};
        Long playerId = player.getId();
        Collection<Game> playerGames = gameRepository.findByStatusInAndPlayerX_IdOrStatusInAndPlayerO_Id(Arrays.asList(statuses), playerId, Arrays.asList(statuses), playerId);
        if (isPlayerAlreadyInGame(playerGames)) {
            throw new BadRequestException(ErrorCode.PLAYER_ALREADY_IN_GAME.getDefaultMessage() + aliasPlayer);
        }

        Collection<Game> waitingOpponentGames = gameRepository.findByStatus(WAITING_OPPONENT);
        Game game = GameUtil.startGame(player, waitingOpponentGames.stream().findFirst().orElse(null));
        game = gameRepository.save(game);

        if (game.getPlayerO() != null){
            logger.info("New game starting between player X {} and Player O {}, player X to start.", game.getPlayerX().getAlias(), game.getPlayerO().getAlias());
        }else{
            logger.info("Game pending for player O");
        }

        return Optional.of(modelMapperUtil.mapGameToGameDTO(game));
    }

    @Override
    public Optional<GameDTO> gameStatus(Long gameId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        Game game = optionalGame.orElseThrow(
                () -> new ResourceNotFoundException("Game :" + gameId + " Does not exist")
        );
        return Optional.of(modelMapperUtil.mapGameToGameDTO(game));
    }

    @Override
    public Optional<GameDTO> play(PlayDTO playDTO) {
        Optional<Player> optionalPlayer = playerRepository.findByAlias(playDTO.getPlayerAlias());

        if (!optionalPlayer.isPresent()) {
            throw new ResourceNotFoundException(ErrorCode.PLAYER_NOT_FOUND.getDefaultMessage());
        }


        Optional<Game> optionalGame = gameRepository.findById(playDTO.getGameId());
        Game game = optionalGame.orElseThrow(
                () -> new ResourceNotFoundException("Game :" + playDTO.getGameId() + " Does not exist")
        );
        if (game.getStatus() == WINNER_O || game.getStatus() == WINNER_X || game.getStatus() == DRAW) {
            throw new NotAllowedException("Game already ended " + game.getStatus());
        }
        if (game.getStatus() != O_TURN && game.getStatus() != X_TURN) {
            throw new NotAllowedException("Game still waiting for second player");
        }
        if (!isPlayerOfGame(playDTO, game)) {
            throw new NotAllowedException("Player " + playDTO.getPlayerAlias() + " not in game");
        }
        if (!isPlayerTurn(playDTO, game)) {
            throw new BadRequestException("It is not the player " + playDTO.getPlayerAlias() + " 's turn");
        }
        if (game.getBoard().get(playDTO.getPosition()) != FRAME_VALUE.EMPTY) {
            throw new BadRequestException("Game board position: " + playDTO.getPosition() + " already filled");
        }
        if (game.getPlayerX().getAlias().equals(playDTO.getPlayerAlias())) {
            game.getBoard().put(playDTO.getPosition(), FRAME_VALUE.X);
            logger.info("Player X plays on fram {}.", playDTO.getPosition());
            game.setStatus(O_TURN);
        } else {
            game.getBoard().put(playDTO.getPosition(), FRAME_VALUE.O);
            logger.info("Player O plays on fram {}.", playDTO.getPosition());
            game.setStatus(X_TURN);
        }
        Status gameStatus = GameUtil.checkBoardState(game.getBoard());
        if (gameStatus != null) {
            switch (gameStatus) {
                case WINNER_X:
                    game.setStatus(WINNER_X);
                    logger.info("Game ended, winner player X {}.", game.getPlayerX().getAlias());
                    break;
                case WINNER_O:
                    game.setStatus(WINNER_O);
                    logger.info("Game ended, winner player O {}.", game.getPlayerX().getAlias());
                    break;
                case DRAW:
                    game.setStatus(DRAW);
                    logger.info("Game ended, winner Draw.");
                    break;
                default:
            }
        }
        game = gameRepository.save(game);
        GameUtil.printGameMap(game.getBoard());
        return Optional.of(modelMapperUtil.mapGameToGameDTO(game));
    }

    @Override
    public Optional<GameDTO> findById(Long gameId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        Game game = optionalGame.orElseThrow(
                () -> new ResourceNotFoundException("Game " + gameId + " not found")
        );
        GameDTO gameDTO = modelMapperUtil.mapGameToGameDTO(game);
        return Optional.of(gameDTO);
    }

    @Override
    public Optional<GameDTO> cancelGame(Long gameId) {
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        Game game = optionalGame.orElseThrow(
                () -> new ResourceNotFoundException("Game " + gameId + " not found")
        );
        if (game.getStatus() != X_TURN && game.getStatus() != O_TURN && game.getStatus() != WAITING_OPPONENT) {
            throw new BadRequestException("Game with status of " + game.getStatus() + " can't be cancelled");
        }
        game.setStatus(CANCELED);
        gameRepository.save(game);
        logger.info("Game {} is canceled.", gameId);
        GameDTO gameDTO = modelMapperUtil.mapGameToGameDTO(game);
        return Optional.of(gameDTO);
    }

    private boolean isPlayerTurn(PlayDTO playDTO, Game game) {
        return game.getPlayerX().getAlias().equals(playDTO.getPlayerAlias()) && game.getStatus().equals(X_TURN)
                || game.getPlayerO().getAlias().equals(playDTO.getPlayerAlias()) && game.getStatus().equals(O_TURN);
    }

    private boolean isPlayerOfGame(PlayDTO playDTO, Game game) {
        return game.getPlayerX().getAlias().equals(playDTO.getPlayerAlias()) || game.getPlayerO().getAlias().equals(playDTO.getPlayerAlias());
    }

    private boolean isPlayerAlreadyInGame(Collection<Game> playerGames) {
        return playerGames != null && !playerGames.isEmpty();
    }

}
