# Simple car rental booking service design document

### Function description
* The car reservation service interface is designed based on the registered users. All the design documents do not consider the pre information such as user registration.
* A user can only book one or more inventory vehicles of one model at a time, and the time period must be unified.
* Changing the vehicle model is not allowed when updating the order。
* It allows a user to book the same vehicle for any time period for multiple times.
* In order to prevent inventory oversold, the inventory information will be refreshed to redis first, and an equal length list will be used to store inventory data. When the inventory is 0, the MySQL inventory information will be updated
* Query from redis when obtaining inventory quantity.

### Inventory redis list data format
`"car model": [id,id,id]`

### Table structure design

* Inventory information table(t_car_stock)

| column      | type    | desc                       |
|-------------|---------|----------------------------|
| id          | BIGINT  | primary key                |
| car_model   | VARCHAR | car model                  |
| status      | TINYINT | status  1 enable 0 disable |
| create_time | BIGINT | create time                |
| update_time | BIGINT | update time                |

* Pre order table(t_booking_order)

| column      | type    | desc                                               |
|-------------|---------|----------------------------------------------------|
| id          | BIGINT | primary key                                        |
| user_id     | VARCHAR | user unique tag                                    |
| status      | TINYINT | status    1: success 2: failed 3: cancel 4: delete |
| create_time |    BIGINT     | create time                                        |
| update_time |   BIGINT      | update time                                        |

* Order detail table(t_order_detail)

| column      | type    | desc           |
|-------------|---------|----------------|
| id          | BIGINT | primary key    |
| order_id    | BIGINT | order id       |
| car_model   | VARCHAR | car model      |
| booking_num | INTEGER | booking number |
| start_time  |    BIGINT     | start time     |
| end_time    |   BIGINT      | end time       |
| status      | TINYINT | status         |
| create_time |    BIGINT     | create time    |
| update_time |   BIGINT      | update time    |

* Order log table(t_order_log)

| column      | type    | desc                                             |
|-------------|---------|--------------------------------------------------|
| id          | BIGINT | primary key                                      |
| order_id    | BIGINT | order id                                         |
| car_model   | VARCHAR | car model                                        |
| booking_num | INTEGER | booking number                                   |
| start_time  |    BIGINT     | start time                                       |
| end_time    |   BIGINT      | end time                                         |
| status      | TINYINT | status   1: create 2: update 3: cancel 4: delete |
| version     |   INTEGER      | operate version                                  |
| create_time |    BIGINT     | create time                                      |
| update_time |   BIGINT      | update time                                      |

### API Design

* Query the available models and inventory

`GET /car/list`

  response
```json
{
  "code": 200,
  "data": [
    {
      "carModel": "string",
      "id": 0,
      "stock": 0
    }
  ],
  "message": "string",
  "ok": true
}
```

* Booking Car

`POST /car/booking`

requestBody
```json
{
  "bookingNum": 0,
  "carModel": "string",
  "endTime": 0,
  "startTime": 0,
  "userId": "string"
}
```
response
```json
{
  "code": 200,
  "data": 1506888488058585089,
  "message": "string",
  "ok": true
}
```

* Update Booking 

`PUT /car/booking`

requestBody
```json
{
  "bookingNum": 0,
  "carModel": "string",
  "endTime": 0,
  "orderId": 0,
  "startTime": 0,
  "userId": "string"
}
```
response
```json
{
  "code": 200,
  "data": 1506888488058585089,
  "message": "string",
  "ok": true
}
```

* Cancel Booking

`DELETE /car/booking`

requestBody
```json
{
  "carModel": "string",
  "orderId": 0,
  "userId": "string"
}
```
response
```json
{
  "code": 200,
  "data": 1506888488058585089,
  "message": "string",
  "ok": true
}
```

* Booking history

`GET /car/booking/history`

parameters
```json
{
  "userId": "uid",
  "pageNo": 1,
  "pageSize": 10
}
```
response
```json
{
  "code": 0,
  "data": [
    {
      "createTime": 0,
      "id": 0,
      "orderDetailList": [
        {
          "bookingNum": 0,
          "carModel": "string",
          "createTime": 0,
          "endTime": 0,
          "id": 0,
          "orderId": 0,
          "startTime": 0,
          "status": 0,
          "updateTime": 0
        }
      ],
      "status": 0,
      "updateTime": 0,
      "userId": "string"
    }
  ],
  "message": "string",
  "ok": true
}
