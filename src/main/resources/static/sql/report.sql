DROP TABLE IF EXISTS report;

CREATE TABLE report (
report_idx INT AUTO_INCREMENT PRIMARY KEY,
reporter varchar(30) NOT NULL ,
reported_content varchar(30) NOT NULL ,
category varchar(10) NOT NULL CHECK (category in ('POST','COMMENT')),
content text,
detail text,
post_idx INT DEFAULT 0,
comment_idx INT DEFAULT 0,
registry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

SHOW TABLE STATUS WHERE Name = 'report';
SHOW FULL COLUMNS FROM report;

CREATE USER 'shoots'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'shoots'@'localhost' WITH GRANT OPTION;
CREATE DATABASE shoots;











ALTER TABLE report
    ADD CONSTRAINT fk_report_post
        FOREIGN KEY (post_idx) REFERENCES post(post_idx);

ALTER TABLE report
    ADD CONSTRAINT fk_report_comment
        FOREIGN KEY (comment_idx) REFERENCES post_comment(post_idx);
