DROP DATABASE IF EXISTS quanlybanhang;
CREATE DATABASE IF NOT EXISTS quanlybanhang;

USE quanlybanhang;

CREATE TABLE products (
  id CHAR(36) PRIMARY KEY,
  sku VARCHAR(32) NOT NULL UNIQUE,
  name VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  price DECIMAL(10, 2) NOT NULL,
  quantity SMALLINT UNSIGNED NOT NULL DEFAULT 0,
  is_active BOOLEAN NOT NULL DEFAULT 1,
  sold SMALLINT UNSIGNED NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE coupons (
  id CHAR(36) PRIMARY KEY,
  coupon_code VARCHAR(32) NOT NULL UNIQUE,
  discount_type ENUM('percentage', 'fixed_amount') NOT NULL,
  discount_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
  issue_date DATE NOT NULL,
  expiry_date DATE NOT NULL,
  minimum_purchase_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
  -- -1 tức là không giới hạn số lần đổi
  max_redemptions MEDIUMINT NOT NULL DEFAULT -1,
  redemptions MEDIUMINT UNSIGNED NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) Engine=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE invoices (
  id CHAR(36) PRIMARY KEY,
  coupon_id CHAR(36),
  invoice_code VARCHAR(32) NOT NULL UNIQUE,
  total_amount DECIMAL(10, 2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_coupon_id FOREIGN KEY(coupon_id) REFERENCES coupons(id)
) Engine=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE invoice_items (
  id CHAR(36) PRIMARY KEY,
  invoice_id CHAR(36) NOT NULL,
  product_id CHAR(36) NOT NULL,
  quantity SMALLINT UNSIGNED NOT NULL DEFAULT 1,
  unit_price DECIMAL(10, 2) NOT NULL,
  total_price DECIMAL(10, 2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_invoice_id FOREIGN KEY(invoice_id) REFERENCES invoices(id),
  CONSTRAINT fk_product_id FOREIGN KEY(product_id) REFERENCES products(id)
) Engine=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DELIMITER $$

CREATE TRIGGER coupons_before_insert
BEFORE INSERT ON coupons
FOR EACH ROW
BEGIN
  IF NEW.discount_type = 'percentage' THEN
    IF NEW.discount_amount < 0 OR NEW.discount_amount > 100 THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Percentage discount value must be >=0 and <= 100';
    END IF;
  ELSEIF NEW.discount_type = 'fixed_amount' THEN
    IF NEW.discount_amount < 0 THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Fixed amount discount value must be >=0';
    END IF;
  END IF;

  IF NEW.max_redemptions < -1 THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Max redemptions must be >= -1';
  END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER coupons_before_update
BEFORE UPDATE ON coupons
FOR EACH ROW
BEGIN
  IF NEW.discount_type = 'percentage' THEN
    IF NEW.discount_amount < 0 OR NEW.discount_amount > 100 THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Percentage discount value must be >=0 and <=100';
    END IF;
  ELSEIF NEW.discount_type = 'fixed_amount' THEN
    IF NEW.discount_amount < 0 THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Fixed amount discount value must be >=0';
    END IF;
  END IF;
  
  IF NEW.max_redemptions < -1 THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Max redemptions must not be negative number';
  END IF;

  IF NEW.max_redemptions <> -1 AND NEW.redemptions > NEW.max_redemptions THEN
      SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Redemptions could not be larger than max redemptions';
  END IF;
END$$

DELIMITER ;
