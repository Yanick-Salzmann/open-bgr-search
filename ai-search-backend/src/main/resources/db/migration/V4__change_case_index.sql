drop index case_record_abstractde_index;
drop index case_record_abstractfr_index;
drop index case_record_abstractit_index;
drop index case_record_fulltext_index;
drop index case_record_title_index;

create index case_record_abstractde_index
    on public.case_record using gin (to_tsvector('english', abstractde));

create index case_record_abstractfr_index
    on public.case_record using gin (to_tsvector('english', abstractfr));

create index case_record_abstractit_index
    on public.case_record using gin (to_tsvector('english', abstractit));

create index case_record_fulltext_index
    on public.case_record using gin (to_tsvector('english', fulltext));

create index case_record_title_index
    on public.case_record using gin (to_tsvector('english', title));

create index case_record_outcome_index
    on public.case_record using gin (to_tsvector('english', outcome));

create index case_record_regeste_index
    on public.case_record using gin (to_tsvector('english', regeste));