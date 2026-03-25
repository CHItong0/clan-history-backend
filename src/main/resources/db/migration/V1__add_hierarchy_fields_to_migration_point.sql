-- Add hierarchy columns
ALTER TABLE migration_point
  ADD COLUMN parent_id BIGINT NULL DEFAULT NULL
  COMMENT 'Self-referential FK for hierarchical point structure'
  AFTER route_id;

ALTER TABLE migration_point
  ADD COLUMN city VARCHAR(100) NULL DEFAULT NULL
  COMMENT 'City name'
  AFTER province;

ALTER TABLE migration_point
  ADD COLUMN district VARCHAR(100) NULL DEFAULT NULL
  COMMENT 'District/county name'
  AFTER city;

-- Add foreign key constraint
ALTER TABLE migration_point
  ADD CONSTRAINT fk_migration_point_parent
  FOREIGN KEY (parent_id) REFERENCES migration_point(id)
  ON DELETE SET NULL;

-- Add unique constraint to enforce single-path for NON-ROOT points
-- Note: MySQL unique index allows multiple NULLs (NULL != NULL), so root points (parent_id = NULL)
-- are enforced at the service layer (see MigrationServiceImpl)
CREATE UNIQUE INDEX uk_migration_point_route_parent
  ON migration_point(route_id, parent_id);

-- Index for hierarchical queries
CREATE INDEX idx_migration_point_parent_id ON migration_point(parent_id);

-- Rollback:
-- ALTER TABLE migration_point DROP FOREIGN KEY fk_migration_point_parent;
-- ALTER TABLE migration_point DROP INDEX uk_migration_point_route_parent;
-- ALTER TABLE migration_point DROP COLUMN district;
-- ALTER TABLE migration_point DROP COLUMN city;
-- ALTER TABLE migration_point DROP COLUMN parent_id;
