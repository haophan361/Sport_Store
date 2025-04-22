DROP DATABASE IF EXISTS Sport_Store;

CREATE DATABASE Sport_Store;

USE Sport_Store;

CREATE TABLE `brands`(
	`brand_id` INT PRIMARY KEY AUTO_INCREMENT,
    `brand_name` NVARCHAR(100)
);

CREATE TABLE `categories`(
	`category_id` INT PRIMARY KEY AUTO_INCREMENT,
    `category_image` VARCHAR(400),
    `category_name` NVARCHAR(100)
);

CREATE TABLE `discounts`(
	`discount_id` INT PRIMARY KEY AUTO_INCREMENT,
    `discount_percentage` INT,
    `discount_start_date` DATETIME,
    `discount_end_date` DATETIME,
    `is_active` TINYINT(1)
);

CREATE TABLE `colors`(
	`color_id` INT PRIMARY KEY AUTO_INCREMENT,
    `color` NVARCHAR(50)
);

CREATE TABLE `products`(
	`product_id` VARCHAR(100) PRIMARY KEY,
    `product_name` NVARCHAR(200),
    `product_detail` LONGTEXT,
    `category_id`INT,
    `brand_id` INT,
    `is_active` TINYINT(1),
    FOREIGN KEY (brand_id) REFERENCES brands(brand_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

CREATE TABLE `product_options`(
	`option_id` INT PRIMARY KEY AUTO_INCREMENT,
    `option_size` NVARCHAR(50),
    `option_quantity` INT,
    `option_cost` DECIMAL,
    `discount_id` INT,
    `color_id` INT,
    `product_id` VARCHAR(100),
    `is_active` TINYINT(1),
    FOREIGN KEY (color_id) REFERENCES colors(color_id),
    FOREIGN KEY (discount_id) REFERENCES discounts(discount_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);

CREATE TABLE `images`(
	`image_id` INT PRIMARY KEY AUTO_INCREMENT,
    `image_url` VARCHAR(400)
);

CREATE TABLE `product_img`(
	`product_id` VARCHAR(100),
	`color_id` INT,
    `image_id` INT,
    PRIMARY KEY(product_id,color_id,image_id),
    FOREIGN KEY(product_id) REFERENCES products(product_id),
    FOREIGN KEY (color_id) REFERENCES colors(color_id),
    FOREIGN KEY(image_id) REFERENCES images(image_id)
);
    

CREATE TABLE `employees`(
	`employee_id` VARCHAR(100) PRIMARY KEY,
    `employee_email` VARCHAR(100),
    `employee_name` VARCHAR(300),
    `employee_phone` VARCHAR(10),
    `employee_address` NVARCHAR(300),
    `employee_date_of_birth` DATE,
    `employee_gender` TINYINT(1)
);

CREATE TABLE `customers`(
	`customer_id` VARCHAR(100) PRIMARY KEY,
	`customer_email` VARCHAR(100),
    `customer_name` NVARCHAR(300),
    `customer_date_of_birth` DATE,
    `customer_phone` VARCHAR(10)
);

CREATE TABLE `receiver_info`(
	`receiver_id` VARCHAR(100) PRIMARY KEY,
    `receiver_name` NVARCHAR(300),
    `receiver_phone` VARCHAR(10),
    `receiver_city` NVARCHAR(100),
    `receiver_district` NVARCHAR(100),
    `receiver_ward` NVARCHAR(100),
    `receiver_street` NVARCHAR(100),
    `customer_id` VARCHAR(100),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE `carts`(
	`cart_id` VARCHAR(100) PRIMARY KEY,
    `option_id` INT,
    `customer_id` VARCHAR(100),
    `cart_quantity` INT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (option_id) REFERENCES product_options(option_id)
);

CREATE TABLE `bills`(
	`bill_id` VARCHAR(100) PRIMARY KEY,
    `bill_total_cost` DECIMAL(10,2) DEFAULT 0,	
    `bill_purchase_date` DATETIME,
    `bill_confirm_date` DATETIME,
    `bill_status_payment` TINYINT(1),
    `bill_receive_date` DATETIME,
    `is_active` TINYINT(1),
    `receiver_id` VARCHAR(100),
    `employee_id` VARCHAR(100),
    FOREIGN KEY(receiver_id) REFERENCES receiver_info(receiver_id),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);  
   
CREATE TABLE `bill_details`(
	`bill_detail_id` VARCHAR(100) PRIMARY KEY,
	`bill_id` VARCHAR(100),
    `option_id` INT,
    `product_name` NVARCHAR(200),
    `product_cost` DECIMAL(10,2) DEFAULT 0,
    `product_quantity` INT,
    UNIQUE (bill_id, option_id),
    FOREIGN KEY (bill_id) REFERENCES bills(bill_id),
    FOREIGN KEY (option_id) REFERENCES product_options(option_id)
);

CREATE TABLE `bill_supplies`(
	`bill_supply_id` VARCHAR(100) PRIMARY KEY,
    `supplier_name` NVARCHAR(300),
    `supplier_phone` VARCHAR(10),
    `supplier_address` VARCHAR(300),
    `bill_supply_cost` DECIMAL(10,2),
    `bill_supply_date` DATETIME	
);

CREATE TABLE `bill_supply_details`(
	`bill_supply_detail_id` VARCHAR(100) PRIMARY KEY,
    `bill_supply_id` VARCHAR(100),
    `product_name` NVARCHAR(200),
    `option_id` INT,
    `option_cost` DECIMAL (10,2),
    `option_quantity` INT,
    FOREIGN KEY (bill_supply_id) REFERENCES bill_supplies(bill_supply_id),
    FOREIGN KEY (option_id) REFERENCES product_options(option_id)
);

CREATE TABLE `accounts`(
	`email` VARCHAR(100) PRIMARY KEY,
    `password` VARCHAR(100),
    `role` ENUM("CUSTOMER","EMPLOYEE","ADMIN"),
    `is_online` TINYINT(1),
    `is_active` TINYINT(1)
);

CREATE TABLE `tokens`(
	`token_id` VARCHAR(100) PRIMARY KEY,
    `user_token` VARCHAR(512),
    `token_expiration_time` DATETIME,
    `user_email` VARCHAR(100)
);