create table tasks (
                     id uuid primary key,
                     title varchar(200) not null,
                     description text,
                     status varchar(20) not null,
                     due_date_time timestamp with time zone not null,
                     created_at timestamp with time zone not null,
                     updated_at timestamp with time zone not null
);

create index idx_tasks_due_date_time on tasks(due_date_time);
create index idx_tasks_status on tasks(status);
