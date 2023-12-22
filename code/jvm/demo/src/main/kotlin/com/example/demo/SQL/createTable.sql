create table player(
    id serial,
    username varchar(30) unique not null,
    password varchar(30) not null,
    primary key (id),
    constraint invalid_username check (username ~ '^[a-zA-Z0-9_]+$'),
    constraint invalid_password check (password ~ '^[a-zA-Z0-9_]+$'),
    constraint invalid_username_length check (length(username) >= 3 and length(username) <= 30),
    constraint invalid_password_length check (length(password) >= 6 and length(password) <= 30)
);

create table authentication(
    player_id integer,
    token varchar(36) not null,
    createdAt date not null,
    lastUsedAt date not null,
    primary key (player_id),
    constraint player_id foreign key (player_id) references player(id)
);

create table game(
    id serial,
    board varchar(1350) not null,
    state varchar(11) not null default 'Playing',
    player_B integer not null,
    player_W integer not null,
    board_size integer not null,
    code integer,
    primary key (id),
    constraint invalid_board_size check(board_size in (15, 19)),
    constraint invalid_state check(state in ('Playing', 'Ended B', 'Ended W', 'Ended D', 'B Forfeited', 'W Forfeited')),
    constraint invalid_player_B foreign key (player_B) references player(id),
    constraint invalid_player_W foreign key (player_W) references player(id)
);

create table lobby(
    id serial,
    player1_id integer not null,
    player2_id integer,
    game_id integer,
    rules varchar(8) not null default 'Pro',
    variant varchar(30) not null default 'Freestyle',
    board_size integer not null default 15,
    state varchar(7) not null default 'Waiting',
    primary key (id),
    constraint player1_id foreign key (player1_id) references player(id),
    constraint player2_id foreign key (player2_id) references player(id),
    constraint game_id foreign key (game_id) references game(id),
    constraint same_player check(player1_id <> player2_id),
    constraint invalid_rules check(rules in ('Default', 'Pro', 'Long Pro')),
    constraint invalid_variant check(variant in ('Freestyle', 'Swap after 1st move')),
    constraint invalid_player_state check (state in ('Waiting', 'Playing', 'Played', 'Left'))
);

create table ranking(
    player_id integer not null,
    rank integer not null,
    played_games integer not null,
    won_games integer not null,
    lost_games integer not null,
    primary key(player_id),
    constraint player_id foreign key (player_id) references player(id),
    constraint invalid_games check (played_games >= won_games + lost_games)
);