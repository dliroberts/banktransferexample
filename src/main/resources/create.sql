create table customers (
	id bigint auto_increment primary key,
	fullName varchar(255) not null,
	createdAt timestamp not null default current_timestamp(),
	updatedAt timestamp not null default current_timestamp()
);

create table accounts (
	iban varchar(34) primary key,
	currencyCode varchar(3) not null,
	balance decimal not null default 0,
	customerId int not null,
	createdAt timestamp not null default current_timestamp(),
	updatedAt timestamp not null default current_timestamp()
);

alter table accounts
	add foreign key (customerId)
	references customers(id);

create table transactions (
	id bigint auto_increment primary key,
	fromIban varchar(34) not null,
	fromAmount decimal not null,
	fromCurrency varchar(3) not null,
	toIban varchar(34) not null,
	toAmount decimal not null,
	toCurrency varchar(3) not null,
	exchangeRate	 decimal not null,
	createdAt timestamp not null default current_timestamp(),
	updatedAt timestamp not null default current_timestamp()
);

create table exchangeRates (
	fromCurrency varchar(3) not null,
	toCurrency varchar(3) not null,
	rate decimal not null,
	createdAt timestamp not null default current_timestamp(),
	updatedAt timestamp not null default current_timestamp(),
	primary key(fromCurrency, toCurrency, rate)
);
