CREATE TABLE IF NOT EXISTS auth.roles (
	"uuid" uuid NOT NULL,
	description varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (uuid)
);

-- Permissions

ALTER TABLE auth.roles OWNER TO postgres;
GRANT ALL ON TABLE auth.roles TO postgres;