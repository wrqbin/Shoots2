CREATE TABLE match_post (
    match_idx INT(10) AUTO_INCREMENT PRIMARY KEY,
    writer INT(10),
    match_date DATE NOT NULL,
    match_time VARCHAR(10) NOT NULL,
    player_max INT(2) NOT NULL,
    player_min INT(2) NOT NULL,
    player_gender CHAR(1) NOT NULL,
    match_level VARCHAR(10) NOT NULL,
    team_style VARCHAR(15) NOT NULL,
    price INT(10) NOT NULL,
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- FOREIGN KEY (WRITER) REFERENCES BUSINESS_USER(BUSINESS_IDX) ON DELETE CASCADE

ALTER TABLE match_post
    ADD CONSTRAINT fk_match_post_business
        FOREIGN KEY (writer) REFERENCES business_user(business_idx)
            ON DELETE CASCADE ON UPDATE CASCADE;




