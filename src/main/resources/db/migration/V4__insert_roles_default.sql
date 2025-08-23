INSERT INTO auth.roles ("uuid", description, "name")
SELECT
  'a1b2c3d4-e5f6-7890-1234-567890abcdef'::UUID,
  'PUBLIC',
  'Publica'
WHERE NOT EXISTS (
  SELECT 1 FROM auth.roles WHERE "uuid" = 'a1b2c3d4-e5f6-7890-1234-567890abcdef'::UUID
);