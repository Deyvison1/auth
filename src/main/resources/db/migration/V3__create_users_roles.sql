-- public.users_roles definição

-- Drop table

-- DROP TABLE public.users_roles;

CREATE TABLE IF NOT EXISTS auth.users_roles (
                                    roles_uuid uuid NOT NULL,
                                    user_uuid uuid NOT NULL
);

-- Permissions

ALTER TABLE auth.users_roles OWNER TO postgres;
GRANT ALL ON TABLE auth.users_roles TO postgres;


-- public.users_roles chaves estrangeiras

ALTER TABLE auth.users_roles ADD CONSTRAINT fkgt7jn41bq2j481eor4ikmlvsd FOREIGN KEY (user_uuid) REFERENCES auth.users("uuid");
ALTER TABLE auth.users_roles ADD CONSTRAINT fkq5xw9crpf2g54wsw36hai5k5h FOREIGN KEY (roles_uuid) REFERENCES auth.roles("uuid");