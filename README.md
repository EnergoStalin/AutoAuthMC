# AutoAuth
![](https://github.com/EnergoStalin/AutoAuthMC/actions/workflows/publish.yml/badge.svg)

A small mod for managing client side of basic authentication for you.

## Features
- Manage basic authentication process on client for servers using /register /login scheme.

## Benefits
- You don't ever need to manage passwords for such servers manually.

## Passwords store
All passwords stored in json file located in **.minecraft/config/AutoAuth/passwords.json**

File stucture is following.
```json
[
  {
    "ip": "127.0.0.1",
    "user": "EnergoStalin",
    "pass": "sus"
  }
]
```
