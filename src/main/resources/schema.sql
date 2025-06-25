CREATE TABLE INSURANCE
(
    id                             BIGINT PRIMARY KEY AUTO_INCREMENT,
    personal_identification_number VARCHAR(20),
    type                           VARCHAR(50),
    monthly_cost DOUBLE,
    vehicle_registration_number    VARCHAR(20),
    discount_applied               BOOLEAN,
    discount_campaign              VARCHAR(100)
);
