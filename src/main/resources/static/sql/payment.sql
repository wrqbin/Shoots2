CREATE TABLE payment (
     payment_idx INT AUTO_INCREMENT PRIMARY KEY,
     match_idx INT NOT NULL,
     seller_idx INT NOT NULL,
     buyer_idx INT NOT NULL,
     payment_method VARCHAR(10) NOT NULL,
     payment_amount DECIMAL(10, 2) NOT NULL,
     payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     payment_status VARCHAR(10) NOT NULL,
     merchant_uid VARCHAR(30) NOT NULL,
     imp_uid VARCHAR(30) NOT NULL
);

#                          FOREIGN KEY (match_idx) REFERENCES match_post(match_idx),  -- 외래 키 제약
#                          FOREIGN KEY (buyer_idx) REFERENCES regular_user(idx),  -- 외래 키 제약
#                          FOREIGN KEY (seller_idx) REFERENCES business_user(business_idx)  -- 외래 키 제약

ALTER TABLE payment ADD UNIQUE (merchant_uid);

ALTER TABLE payment
    ADD CONSTRAINT fk_payment_match
        FOREIGN KEY (match_idx) REFERENCES match_post(match_idx);

ALTER TABLE payment
    ADD CONSTRAINT fk_payment_business
        FOREIGN KEY (seller_idx) REFERENCES business_user(business_idx);

ALTER TABLE payment
    ADD CONSTRAINT fk_payment_regular
        FOREIGN KEY (buyer_idx) REFERENCES regular_user(idx);
