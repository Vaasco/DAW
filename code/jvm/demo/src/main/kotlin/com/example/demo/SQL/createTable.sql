create table player(
	id serial,
    token varchar(30) unique not null,--TODO("Adjust the max length of the token")
	username varchar(30) unique not null,
	primary key (id)
);

create table game(
	id serial,
	state varchar(7) not null,
    rules varchar(8) not null,
    variant varchar(30) not null,
	primary key (id),
    constraint invalid_state check(state in ('Playing', 'Ended B', 'Ended W', 'Ended D')),
    constraint invalid_rules check(rules in ('Pro', 'Long Pro')),--TODO("Add more rules maybe?")
    constraint invalid_variant check(variant in ('Freestyle', 'Swap after 1st move'))--TODO("Add more variants maybe?"
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