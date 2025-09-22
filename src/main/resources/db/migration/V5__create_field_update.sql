DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'auth'
      AND table_name = 'roles'
      AND column_name = 'created'
  ) THEN
    ALTER TABLE auth.roles ADD COLUMN created TIMESTAMP;
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'auth'
      AND table_name = 'roles'
      AND column_name = 'updated'
  ) THEN
    ALTER TABLE auth.roles ADD COLUMN updated TIMESTAMP;
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'auth'
      AND table_name = 'roles'
      AND column_name = 'user_update_id'
  ) THEN
    ALTER TABLE auth.roles ADD COLUMN "user_update_id" uuid DEFAULT gen_random_uuid();
    ALTER TABLE auth.roles ALTER COLUMN "user_update_id" SET NOT NULL;
    ALTER TABLE auth.roles ALTER COLUMN "user_update_id" DROP DEFAULT;
  END IF;
END$$;


DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'plan_control'
      AND table_name = 'products'
      AND column_name = 'user_update_id'
  ) THEN
    ALTER TABLE plan_control.products ADD COLUMN "user_update_id" uuid NULL;
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'auth'
      AND table_name = 'roles'
      AND column_name = 'created'
  ) THEN
    ALTER TABLE auth.roles ADD COLUMN created TIMESTAMP;
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'auth'
      AND table_name = 'roles'
      AND column_name = 'updated'
  ) THEN
    ALTER TABLE auth.roles ADD COLUMN updated TIMESTAMP;
  END IF;
END$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = 'auth'
      AND table_name = 'roles'
      AND column_name = 'user_update_id'
  ) THEN
    ALTER TABLE auth.roles ADD COLUMN "user_update_id" uuid NULL;
  END IF;
END$$;

DO $$
BEGIN
  -- Adiciona a coluna se não existir
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = 'auth'
      AND table_name = 'roles'
      AND column_name = 'user_update_id'
  ) THEN
    ALTER TABLE auth.roles
    ADD COLUMN user_update_id uuid;
  END IF;

  -- Adiciona a FK se não existir
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint
    WHERE conname = 'fk_roles_user_update'
  ) THEN
    ALTER TABLE auth.roles
    ADD CONSTRAINT fk_roles_user_update
    FOREIGN KEY (user_update_id)
    REFERENCES auth.users (uuid);
  END IF;
END
$$;

DO $$
BEGIN
  -- Adiciona a coluna se não existir
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = 'auth'
      AND table_name = 'users'
      AND column_name = 'user_update_id'
  ) THEN
    ALTER TABLE auth.users
    ADD COLUMN user_update_id uuid;
  END IF;

  -- Adiciona a FK se não existir
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint
    WHERE conname = 'fk_users_user_update'
  ) THEN
    ALTER TABLE auth.users
    ADD CONSTRAINT fk_users_user_update
    FOREIGN KEY (user_update_id)
    REFERENCES auth.users (uuid);
  END IF;
END
$$;