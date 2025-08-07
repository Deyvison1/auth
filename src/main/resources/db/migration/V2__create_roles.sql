CREATE TABLE IF NOT EXISTS public.roles (
	"uuid" uuid NOT NULL,
	description varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (uuid)
);

-- Permissions

ALTER TABLE public.roles OWNER TO postgres;
GRANT ALL ON TABLE public.roles TO postgres;