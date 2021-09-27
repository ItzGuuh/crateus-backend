create TABLE USERS (
                       id UUID primary key DEFAULT gen_random_uuid(),
                       name varchar(30),
                       username varchar(20),
                       email varchar(100) unique,
                       hash varchar(100),
                       birthday timestamp,
                       created_at timestamp
)