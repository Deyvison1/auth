-- public.users definição

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE IF NOT EXISTS auth.users (
                              account_non_expired bool NULL,
                              account_non_locked bool NULL,
                              enabled bool NULL,
                              created timestamp(6) NOT NULL,
                              updated timestamp(6) NULL,
                              "uuid" uuid NOT NULL,
                              nick varchar(30) NOT NULL,
                              email varchar(40) NOT NULL,
                              full_name varchar(255) NULL,
                              "password" varchar(255) NOT NULL,
                              user_name varchar(255) NULL,
                              CONSTRAINT users_nick_key UNIQUE (nick),
                              CONSTRAINT users_pkey PRIMARY KEY (uuid)
);

-- Permissions

ALTER TABLE auth.users OWNER TO postgres;
GRANT ALL ON TABLE auth.users TO postgres;