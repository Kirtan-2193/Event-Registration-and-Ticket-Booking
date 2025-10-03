create table events (
                        event_id varchar(255) not null,
                        description varchar(350),
                        end_data_time timestamp(6) not null,
                        event_name varchar(255) not null,
                        event_status varchar(255) check (event_status in ('OPEN','CLOSED')),
                        start_date_time timestamp(6) not null,
                        venue varchar(255) not null,
                        primary key (event_id)
);