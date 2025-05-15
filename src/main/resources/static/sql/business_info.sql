create table business_info(
      b_info_idx INT AUTO_INCREMENT PRIMARY KEY,
      business_idx INT,
      parking VARCHAR(120),
      shower BOOL,
      lounge BOOL,
      field_type VARCHAR(120),
      open_time VARCHAR(120),
      close_time VARCHAR(120),
      cctv BOOL,
      kiosk BOOL,
      rental  VARCHAR(255)
);

ALTER TABLE business_info
    ADD CONSTRAINT fk_business_info_business
        FOREIGN KEY (business_idx) REFERENCES business_user(business_idx)
            ON DELETE CASCADE ON UPDATE CASCADE;