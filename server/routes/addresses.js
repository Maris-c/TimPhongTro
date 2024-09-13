const express = require('express')
const router = express.Router()
const usersService = require('../services/addresses')

//GET: read
router.get('/provinces', async function(req, res, next){
    try{
        res.json(await usersService.getProvices())
    }catch(err){
        console.error(`Error: `, err.message)
        next(err)
    }
})

router.get('/districts', async function(req, res, next){
    try{
        const province = req.query.province
        res.json(await usersService.getDistricts(province))
    }catch(err){
        console.error(`Error: `, err.message)
        next(err)
    }
})

router.get('/wards', async function(req, res, next){
    try{
        const district = req.query.district
        res.json(await usersService.getWards(district))
    }catch(err){
        console.error(`Error: `, err.message)
        next(err)
    }
})

router.post('/address/add', async function(req, res, next){
    try{
        const data = req.body
        res.json(await usersService.create(data))
    }catch(err){
        console.error(`Error: `, err.message)
        next(err)
    }
})

module.exports = router