revol-home-task
=============

Money transfers between accounts.

Executable jar can be found [here](https://github.com/eseev-german/revol-home-task/releases).

Application starts up on 8081 port. 

Available api
---

### Get all accounts

```http
GET /accounts
```
#### Request example
```http
GET http://localhost:8081/accounts
```
#### Response example
```javascript
[
  {
    "balance": "90",
    "id": "1"
  },
  {
    "balance": "110",
    "id": "2"
  },
  {
    "balance": "100",
    "id": "3"
  },
  {
    "balance": "10",
    "id": "4"
  }
]
```
---

### Get account by id
```http
GET /accounts/{accountId}
```
#### Request example
```http
GET http://localhost:8081/accounts/1
```
#### Response example
```javascript
  {
    "balance": "90",
    "id": "1"
  }
```
---

### Create account
```http
POST /accounts/
```
#### Request example
```http
POST http://localhost:8081/accounts
Content-Type: application/json

{
  "balance":"90"
}
```
#### Response example
```javascript
  {
    "balance": "90",
    "id": "1"
  }
```
---

### Transfer money from one account to another
```http
POST /accounts/transfers
```
#### Request example
```http
POST http://localhost:8081/accounts/transfers
Content-Type: application/json

{
  "amount":"10",
  "destinationAccount": "2",
  "sourceAccount": "1"
}
```


Tests
---

To run tests use
```bash
gradlew test
```

### Integration testing

Integration tests available in `test/java/integration`

#### Api testing

End-to-end test available in `test/java/integration/revol/home/task/api`.
These tests will set up almost identical server copy on **8081** port.


