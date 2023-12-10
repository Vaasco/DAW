--Function that creates a game once there's two players are ready to play with the same rules
create or replace function create_game()
    returns trigger
    language plpgsql
as
$$
declare pId integer;
        p1Rank integer;
        p2Rank integer;
        gId integer;
        pId_loop record;
        r integer;
        player_b integer;
        player_w integer;
        newBoard varchar(100);
begin
    r := floor(random() * 2);
    for pId_loop in (select * from lobby l where (new.rules = l.rules and new.variant = l.variant and new.board_size = l.board_size and l.state = 'Waiting')) loop
            pId := pId_loop.player1_id;
            if(pId is not null) then
                select rank into p1Rank from ranking where player_id = pId;
                select rank into p2Rank from ranking where player_id = new.player1_id;
                if (ABS(p1Rank - p2Rank) < 150) then
                    if (r = 0) then
                        player_b = pId;
                        player_w = new.player1_id;
                    else
                        player_b = new.player1_id;
                        player_w = pId;
                    end if;
                    newBoard := 'B' || new.board_size || new.rules || new.variant || E'\n{}';
                    insert into game (board, state, player_b, player_w, board_size) values
                        (newBoard,default, player_b, player_w, new.board_size) returning id into gId;
                    update lobby set player2_id = new.player1_id, game_id = gId, state = 'Playing' where player1_id = pId;
                    return old;
                end if;
            end if;
        end loop;
    return new;
end;
$$;

create or replace trigger create_game_trigger
    before insert on lobby
    for each row
execute function create_game();

--Function that prohibits a player from playing two games simultaneously
create or replace function one_game_check()
    returns trigger
    language plpgsql
as
$$
begin
    if exists (select id
               from game
               where (state = 'Playing' or state = 'Waiting') and
                    (player_w = new.player1_id or player_b = new.player1_id or
                     player_b = new.player2_id or player_b = new.player2_id)) then
        raise exception 'You cannot play two games at the same time!';
    end if;
    return new;
end;
$$;

create or replace trigger one_game_check_trigger
    before insert on lobby
    for each row
execute function one_game_check();

--Function used to update the data after the game ends
create or replace function post_game_asserts()
    returns trigger
    language plpgsql
as
$$
declare Winner integer;
        Loser integer;
begin
    if (old.state <> 'Playing') then
        raise exception 'The game has already ended!';
    end if;
    if (new.state <> 'Playing') then
        update lobby set state = 'Played' where game_id = new.id;
        if (new.state = 'Ended D') then
            update ranking set played_games = played_games + 1 where player_id = new.player_B or player_id = new.player_W;
            return new;
        elseif (new.state = 'Ended B' or new.state = 'W Forfeited') then
            Winner := new.player_B;
            Loser := new.player_W;
        elseif (new.state = 'Ended W' or new.state = 'B Forfeited') then
            Winner := new.player_W;
            Loser := new.player_B;
        end if;
        update ranking set won_games = won_games + 1, played_games = played_games + 1, rank = rank + 100 where player_id = Winner;
        update ranking set lost_games = lost_games + 1, played_games = played_games + 1, rank = greatest(0, rank - 50 )where player_id = Loser;
    end if;
    return new;
end;
$$;

create or replace trigger post_game_asserts_trigger
    after update on game
    for each row
execute function post_game_asserts();

--Function that automatically creates a ranking for a player
create or replace function create_ranking()
    returns trigger
    language plpgsql
as
$$
begin
    insert into ranking values (new.id, 0, 0, 0, 0);
    return new;
end;
$$;

create or replace trigger create_ranking_trigger
    after insert on player
    for each row
execute function create_ranking();