# BankTransferExample

How to start the BankTransferExample application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/banktransferexample-1.0-SNAPSHOT.jar server config.yml`

How to use the application
---

Send a HTTP POST to http://localhost:8096/transfer with a Content-Type: application/json HTTP header, and a
body like the following:

```
{
	"amount": "BRL 1131.01",
	"fromAccount": {"iban": "FR9476231310567227640169067", "fullName": "Duncan Roberts", "currency": "EUR"},
	"toAccount": {"iban": "BR8712345678123451234567890C1", "fullName": "R. da Silva", "currency": "BRL"}
}
```

Look in `src/main/resources/seed-demo.sql` for a list of valid IBANs, starting account balances etc. Schema
in `src/main/resources/create.sql`.

Health Check
---

To see your application's health, go to URL `http://localhost:8097/healthcheck`
