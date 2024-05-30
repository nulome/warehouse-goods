drop table IF EXISTS products;

create TABLE IF NOT EXISTS products (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
	article integer UNIQUE NOT NULL,
	title varchar(255) NOT NULL,
	description varchar(100),
	category varchar(255) NOT NULL,
	price double precision NOT NULL,
	quantity integer NOT NULL,
	date_create date NOT NULL,
	date_update timestamp
);

