// In addresses.js
const express = require('express')
const router = express.Router()
const usersService = require('../services/users')

router.post('/login', async function(req, res, next) {
    try {
        const data = req.body;
        res.json(await usersService.login(data));
    } catch (err) {
        console.error(`Error while getting user`, err.message);
        next(err);
    }
});

router.post('/register', async function(req, res, next) {
    try {
        const data = req.body;
        res.json(await usersService.create(data));
    } catch (err) {
        console.error(`Error while register users`, err.message);
        next(err);
    }
});

router.post('/users/checkPhoneNumber', async function(req, res, next) {
    try {
        const data = req.body;
        res.json(await usersService.checkPhoneNumber(data));
    } catch (err) {
        console.error(`Error while checking phone number`, err.message);
        next(err);
    }
});

module.exports = router;