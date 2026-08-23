create table public.case_record
(
    id              varchar(50) not null
        constraint case_record_pk
            primary key,
    court           varchar(250),
    canton          varchar(50),
    chamber         varchar(100),
    branch          varchar(100),
    proceedingtype  varchar(50),
    proceduralcode  varchar(50),
    appealedcourt   varchar(250),
    appealeddata    varchar(100),
    appealeddocket  varchar(50),
    docketnumber    varchar(16),
    publicationdate timestamp,
    language        varchar(10),
    title           text,
    legalarea       varchar(50),
    regeste         varchar(100),
    abstractde      text,
    abstractfr      text,
    abstractit      text,
    fulltext        text,
    outcome         varchar(100),
    sourceurl       varchar(1024),
    pdfurl          varchar(1024),
    externalid      varchar(100)
);

create index case_record_abstractde_index
    on public.case_record (abstractde);

create index case_record_abstractfr_index
    on public.case_record (abstractfr);

create index case_record_abstractit_index
    on public.case_record (abstractit);

create index case_record_fulltext_index
    on public.case_record (fulltext);

create index case_record_title_index
    on public.case_record (title);

