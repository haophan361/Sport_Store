DROP DATABASE IF EXISTS Sport_Store;

CREATE DATABASE Sport_Store;

USE Sport_Store;

CREATE TABLE `brands`(
	`brand_id` VARCHAR(12) PRIMARY KEY,
    `brand_name` NVARCHAR(100)
);

CREATE TABLE `categories`(
	`category_id` VARCHAR(12) PRIMARY KEY,
    `category_image` VARCHAR(400),
    `category_name` NVARCHAR(100)
);

CREATE TABLE `discounts`(
	`discount_id` VARCHAR(12) PRIMARY KEY,
    `discount_percentage` INT,
    `discount_start_date` DATETIME,
    `discount_end_date` DATETIME,
    `is_active` TINYINT(1)
);

CREATE TABLE `products`(
	`product_id` VARCHAR(100) PRIMARY KEY,
    `product_name` NVARCHAR(200),
    `category_id` VARCHAR(12),
    `brand_id` VARCHAR(12),
    `product_detail` LONGTEXT,
    `discount_id` VARCHAR(12),
    FOREIGN KEY (brand_id) REFERENCES brands(brand_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    FOREIGN KEY (discount_id) REFERENCES discounts(discount_id)
);

CREATE TABLE `product_options`(
	`option_id` VARCHAR(12) PRIMARY KEY,
    `option_color` NVARCHAR(50),
    `option_size` VARCHAR(10),
    `option_quantity` INT DEFAULT 0,
    `option_cost` DECIMAL(10,2) NOT NULL,
    `is_active` TINYINT(1),
    `create_date` DATETIME,
	`product_id` VARCHAR(100),
    UNIQUE(product_id, option_color, option_size),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE `images`(
	`image_id` INT AUTO_INCREMENT PRIMARY KEY,
    `product_option_id` VARCHAR(100),
    `image_url` VARCHAR(400),
    FOREIGN KEY (product_option_id) REFERENCES product_options(option_id)
);

CREATE TABLE `users`(
	`user_id` VARCHAR(100) PRIMARY KEY,
	`user_email` VARCHAR(100),
    `user_password` VARCHAR(100),
    `user_name` NVARCHAR(300),
    `user_role` ENUM("CUSTOMER","EMPLOYEE","ADMIN"),
    `user_gender` TINYINT(1),
    `user_date_of_birth` DATE, 
    `user_phone` VARCHAR(10),
    `is_active` TINYINT(1)
);

CREATE TABLE `receiver_info`(
	`receiver_id` VARCHAR(100) PRIMARY KEY,
    `receiver_name` NVARCHAR(300),
    `receiver_phone` VARCHAR(10),
    `receiver_city` NVARCHAR(100),
    `receiver_district` NVARCHAR(100),
    `receiver_ward` NVARCHAR(100),
    `receiver_street` NVARCHAR(100),
    `user_id` VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE `carts`(
	`cart_id` VARCHAR(100) PRIMARY KEY,
    `product_option_id` VARCHAR(100),
    `user_id` VARCHAR(100),
    `cart_quantity` INT NOT NULL,
    UNIQUE (user_id, product_option_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (product_option_id) REFERENCES product_options(option_id)
);

CREATE TABLE `comments`(
	`comment_id` VARCHAR(100) PRIMARY KEY,
    `comment_content` NVARCHAR(500),
    `comment_rate` INT,
    `user_id` VARCHAR(100),
    `product_id` VARCHAR(100),
    `comment_datetime` DATETIME,
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE `coupons`(
	`coupon_id` VARCHAR(50) PRIMARY KEY,
    `coupon_percentage` INT,
    `coupon_start_date` DATETIME,
    `coupon_expiration_date` DATETIME,
    `coupon_attempts_left` INT
);

CREATE TABLE `bills`(
	`bill_id` VARCHAR(100) PRIMARY KEY,
    `bill_total_cost` DECIMAL(10,2) DEFAULT 0,	
    `bill_purchase_date` DATETIME,
    `bill_confirm_date` DATETIME,
    `bill_status` TINYINT(1),
    `receiver_id` VARCHAR(100),
    `bill_receive_date` DATETIME,
    `is_active` TINYINT(1),
    `employee_id` VARCHAR(100),
    `coupon_id` VARCHAR(50),
    FOREIGN KEY(receiver_id) REFERENCES receiver_info(receiver_id),
    FOREIGN KEY (employee_id) REFERENCES users(user_id),
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id)
);  
   
CREATE TABLE `bill_details`(
	`bill_detail_id` VARCHAR(100) PRIMARY KEY,
	`bill_id` VARCHAR(100),
    `product_option_id` VARCHAR(100),
    `product_name` NVARCHAR(200),
    `product_cost` DECIMAL(10,2) DEFAULT 0,
    `product_quantity` INT,
    UNIQUE (bill_id, product_option_id),
    FOREIGN KEY (bill_id) REFERENCES bills(bill_id),
    FOREIGN KEY (product_option_id) REFERENCES product_options(option_id)
);

CREATE TABLE `tokens`(
	`token_id` VARCHAR(100) PRIMARY KEY,
    `user_token` VARCHAR(512),
    `token_expiration_time` DATETIME,
    `user_email` VARCHAR(100)
);