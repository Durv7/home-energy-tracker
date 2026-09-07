ALTER TABLE users
DROP INDEX uk_user_email;

ALTER TABLE users
ADD COLUMN active_email varchar(255)
GENERATED ALWAYS AS (
    CASE
        WHEN is_deleted = FALSE THEN email
        ELSE NULL
    END
) STORED;

CREATE UNIQUE INDEX uk_users_active_email
ON users(`active_email`);