CREATE TABLE users (
  uuid UUID PRIMARY KEY,
  name VARCHAR UNIQUE NOT NULL,
  password VARCHAR NOT NULL
);

CREATE TABLE brands (
    uuid UUID PRIMARY KEY ,
    name VARCHAR NOT NULL
);

CREATE TABLE categories (
    uuid UUID PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE items (
  uuid UUID PRIMARY KEY,
  name VARCHAR UNIQUE NOT NULL,
  description VARCHAR NOT NULL,
  price NUMERIC NOT NULL,
  brand_id UUID NOT NULL,
  category_id UUID NOT NULL,
  CONSTRAINT brand_id_fkey FOREIGN KEY (brand_id)
                   REFERENCES brands (uuid) MATCH SIMPLE
                   ON UPDATE NO ACTION ON DELETE NO ACTION,

  CONSTRAINT cat_id_fkey FOREIGN KEY (category_id)
                   REFERENCES categories (uuid) MATCH SIMPLE
                   ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE orders (
    uuid UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    payment_id UUID UNIQUE NOT NULL,
    items JSONB NOT NULL,
    total NUMERIC,
    CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (uuid) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE users (
    uuid UUID PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL ,
    password VARCHAR NOT NULL
);