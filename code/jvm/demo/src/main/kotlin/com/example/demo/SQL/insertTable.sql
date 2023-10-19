insert into Player(username, password) values   ('Vasco', 'password1'),
                                                ('Jose', 'password2'),
                                                ('Sergio', 'password3');

insert into lobby (player1_id, player2_id, game_id, rules, variant, board_size) values (1, null, null, default, default, default);
--update lobby set state = 'Left' where player1_id = 1;
update ranking set rank = 2000 where player_id = 1;
insert into lobby (player1_id, player2_id, game_id, rules, variant, board_size) values (2, null, null, default, default, default);
insert into lobby (player1_id, player2_id, game_id, rules, variant, board_size) values (3, null, null, default, default, default);

--insert into ranking(player_id, rank, played_games, won_games, lost_games) values (1,1000,50,45,5);
--Raise exception insert into lobby (player1_id, player2_id, game_id, rules, variant, board_size) values (3, null, null, default, default, default);
update game set state = 'Ended B' where id = 1;
