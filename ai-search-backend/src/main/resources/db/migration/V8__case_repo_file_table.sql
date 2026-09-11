create table case_repo_file
(
    id        serial
        constraint case_repo_file_pk
            primary key,
    file_path varchar(1024) not null
        constraint case_repo_file_unique_file
            unique,
    sha       varchar(40)   not null
);

