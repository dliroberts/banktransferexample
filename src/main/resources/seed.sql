insert into customers (fullName) values ('Duncan Roberts');
insert into customers (fullName) values ('Nacnud Strebor');
insert into customers (fullName) values ('Sue Smith');
insert into customers (fullName) values ('Joe Bloggs');
insert into customers (fullName) values ('Ronaldinho Da Silva');

insert into accounts (iban, currencyCode, balance, customerId) values ('GB55ZAFY89851748597528', 'GBP', 3.14, 1);
insert into accounts (iban, currencyCode, balance, customerId) values ('FR9476231310567227640169067', 'EUR', 43298.11, 1);
insert into accounts (iban, currencyCode, balance, customerId) values ('ES2364265841767173822054', 'EUR', 123.01, 2);
insert into accounts (iban, currencyCode, balance, customerId) values ('GB22KVUM18028477988401', 'GBP', 144, 3);
insert into accounts (iban, currencyCode, balance, customerId) values ('GB26JAYK66540091518150', 'GBP', 20.99, 4);
insert into accounts (iban, currencyCode, balance, customerId) values ('BR8712345678123451234567890C1', 'BRL', 23, 5);

-- all exchange rates are listed unidirectionally, with the 'from' currency code being the one with the
-- lower alphabetic sort order.
insert into exchangeRates (fromCurrencyLowerAlpha, toCurrencyHigherAlpha, rate) values ('EUR', 'GBP', 0.875415693);
insert into exchangeRates (fromCurrencyLowerAlpha, toCurrencyHigherAlpha, rate) values ('EUR', 'USD', 1.179051);
insert into exchangeRates (fromCurrencyLowerAlpha, toCurrencyHigherAlpha, rate) values ('BRL', 'GBP', 0.198671416);
insert into exchangeRates (fromCurrencyLowerAlpha, toCurrencyHigherAlpha, rate) values ('BRL', 'EUR', 0.22694523);
insert into exchangeRates (fromCurrencyLowerAlpha, toCurrencyHigherAlpha, rate) values ('BRL', 'USD', 0.26758);
insert into exchangeRates (fromCurrencyLowerAlpha, toCurrencyHigherAlpha, rate) values ('GBP', 'USD', 1.346847);
