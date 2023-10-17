--Function that creates a game once there's two players are ready to play with the same rules
create or replace function create_game()
    returns trigger
    language plpgsql
as
$$
declare pId integer;
        gId integer;
begin
    select player1_id into pId from lobby l where (new.rules = l.rules and new.variant = l.variant and new.board_size = l.board_size and game_id is null and l.state = 'Waiting');
    if(pId is not null) then
        insert into game (state, player_b, player_w, rules, variant, board_size) values(default, pId, new.player1_id, new.rules, new.variant, new.board_size) returning id into gId;
        update lobby set player2_id = new.player1_id, game_id = gId, state = 'Playing' where player1_id = pId;
        return old;
    end if;
    return new;
end;
$$;

create or replace trigger create_game_trigger
    before insert on lobby
    for each row
execute function create_game();

--Function that prohibits a player from playing two games simoutaneously
create or replace function one_game_check()
    returns trigger
    language plpgsql
as
$$
begin
    if exists (select id from lobby where (state = 'Playing' or state = 'Waiting') and (player1_id = new.player1_id or player2_id = new.player1_id or player1_id = new.player2_id or player2_id = new.player2_id)) then
        raise exception 'You cannot play two games at the same time!';
        return old;
    end if;
    return new;
end;
$$;

create or replace trigger one_game_check_trigger
    before insert on lobby
    for each row
execute function one_game_check();