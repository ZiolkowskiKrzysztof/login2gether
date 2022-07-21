# login2gether

Simple two-factor authentication app

## How to run

In sbt put:

```bash
run
```

## Routes
You should be able to curl localhost:8080

```scala
healthCheckRoutes:

GET /healthCheck 

// loginRoutes:

POST /login with body:

{"username":"user",
"password":"password"}

POST /permissions/{user_that_tries_to_give_permission_UUID} with body:

{"forUser":"4de8718e-e014-40ab-8f2d-40cf25229544"}

// userRoutes:

GET /users 

POST /users with body:

{"username":"user",
"password":"password"}

// secretRoutes - to update:

GET /secrets/{uuid}

POST /secrets with body:

//todo



```
