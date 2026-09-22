CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    address VARCHAR(500),
    city VARCHAR(100),
    country VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    sku VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100) NOT NULL,
    image_url VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS inventory (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT UNIQUE REFERENCES products(id) ON DELETE CASCADE,
    quantity INT NOT NULL DEFAULT 0,
    reserved_quantity INT NOT NULL DEFAULT 0,
    min_threshold INT NOT NULL DEFAULT 10,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(100) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id),
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL
);

-- Initial seed data for testing and demonstration
INSERT INTO customers (id, name, email, phone, address, city, country) VALUES
(1, 'Alice Johnson', 'alice@example.com', '+1-555-0192', '123 Tech Blvd', 'San Francisco', 'USA'),
(2, 'Bob Smith', 'bob@example.com', '+1-555-0144', '456 Innovation Way', 'Austin', 'USA'),
(3, 'Carol Williams', 'carol@example.com', '+1-555-0178', '789 Enterprise Rd', 'Seattle', 'USA')
ON CONFLICT (id) DO NOTHING;

INSERT INTO products (id, name, description, price, sku, category, image_url, status) VALUES
(1, 'Cloud Native Kubernetes Mug', 'Ceramic 15oz mug with Kubernetes cluster architecture logo', 19.99, 'SKU-MUG-001', 'Merchandise', 'https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=400', 'ACTIVE'),
(2, 'GitOps Pipeline Hoodie', 'Premium fleece hoodie for DevOps engineers', 59.99, 'SKU-HOD-002', 'Apparel', 'https://images.unsplash.com/photo-1556905055-8f358a7a47b2?w=400', 'ACTIVE'),
(3, 'Argo Rollouts Canary Plush', 'Soft canary mascot plushie for progressive delivery fans', 24.50, 'SKU-PLS-003', 'Swag', 'https://images.unsplash.com/photo-1566576912321-d58ddd7a6088?w=400', 'ACTIVE'),
(4, 'Prometheus Metrics Sticker Pack', 'High quality vinyl stickers for laptops and servers', 9.99, 'SKU-STK-004', 'Stickers', 'https://images.unsplash.com/photo-1572375992501-4b0892d50c69?w=400', 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

INSERT INTO inventory (id, product_id, quantity, reserved_quantity, min_threshold) VALUES
(1, 1, 150, 5, 20),
(2, 2, 80, 2, 10),
(3, 3, 200, 10, 25),
(4, 4, 500, 0, 50)
ON CONFLICT (id) DO NOTHING;

-- Reset sequence values
SELECT setval('customers_id_seq', (SELECT MAX(id) FROM customers));
SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));
SELECT setval('inventory_id_seq', (SELECT MAX(id) FROM inventory));
