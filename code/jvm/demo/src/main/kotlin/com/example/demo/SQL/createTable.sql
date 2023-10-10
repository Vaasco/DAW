create table player(
	id serial,
	username varchar(30) unique not null,
	primary key (id)
);

create table game(
	id serial,
	state varchar(30), --TODO(Não temos bem a certeza do que pôr no state)
	primary key (id)
);

create table player_game(
	player_id integer not null,
	game_id integer not null,
	primary key(player_id, game_id),
	constraint game_id foreign key (game_id) references game(id),
	constraint player_id foreign key (player_id) references player(id)
);
