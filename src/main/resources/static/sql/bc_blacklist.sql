CREATE TABLE bc_blacklist (
        bc_blacklist_idx INT AUTO_INCREMENT PRIMARY KEY,
        target_idx INT NOT NULL,
        business_idx INT NOT NULL,
        reason TEXT NOT NULL,
        status ENUM('block', 'unblock') DEFAULT 'block',
        blocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        unblocked_at DATETIME DEFAULT NULL
);

ALTER TABLE bc_blacklist
    ADD CONSTRAINT fk_bc_blacklist_target
        FOREIGN KEY (target_idx) REFERENCES regular_user(idx)
            ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE bc_blacklist
    ADD CONSTRAINT fk_bc_blacklist_business
        FOREIGN KEY (business_idx) REFERENCES business_user(business_idx)
            ON DELETE CASCADE ON UPDATE CASCADE;