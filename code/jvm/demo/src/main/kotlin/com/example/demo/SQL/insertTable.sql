insert into Player(username, token, password) values 	('Vasco', 'token1', 'password1'),
                                                        ('Jose', 'token2', 'password2'),
                                                        ('Sergio', 'token3', 'password3');

insert into lobby (player1_id, player2_id, game_id, rules, variant, board_size) values (1, null, null, default, default, default);
--update lobby set state = 'Left' where player1_id = 1;
insert into lobby (player1_id, player2_id, game_id, rules, variant, board_size) values (2, null, null, default, default, default);
insert into lobby (player1_id, player2_id, game_id, rules, variant, board_size) values (3, null, null, default, default, default);


-- Raise exception insert into lobby (player1_id, player2_id, game_id, rules, variant, board_size) values (3, null, null, default, default, default);
update game set state = 'Ended B' where id = 1;
