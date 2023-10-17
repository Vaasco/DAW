create table player(
	id serial,
	username varchar(30) unique not null,
	lobby_id integer,
	primary key (id),
	foreign key (lobby_id) references lobby(id)
);

create table game(
	id serial,
	state varchar(30), --TODO(Não temos bem a certeza do que pôr no state)
	primary key (id)
);

create table Lobby(
	id serial,
	player1_id integer not null,
	player2_id integer not null,
	game_id integer not null,
	board_size integer not null,
	primary key(id),
	constraint player1_id foreign key (player1_id) references player(id),
	constraint player2_id foreign key (player2_id) references player(id),
	constraint game_id foreign key (game_id) references game(id),
	constraint same_player check(player1_id <> player2_id)
);

drop table Lobby;