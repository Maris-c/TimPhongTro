const express = require('express')
const router = express.Router()
const roomsService = require('../services/rooms')

//GET: read
router.get('/rooms', async function(req, res, next){
    try{
        const page = req.query.page
        res.json(await roomsService.getMultiple(page))
    }catch(err){
        console.error(`Error: `, err.message)
        next(err)
    }
})

router.get('/searchRoom', async function(req, res, next){
    try{
        const query = req.query.query
        res.json(await roomsService.searchRoom(query))
    }catch(err){
        console.error(`Error: `, err.message)
        next(err)
    }
})

router.get('/searchRoom/province', async function(req, res, next){
    try{
        const query = req.query.query
        res.json(await roomsService.searchRoomByProvince(query))
    }catch(err){
        console.error(`Error: `, err.message)
        next(err)
    }
})

router.get('/searchRoom/district', async function(req, res, next){
    try{
        const query = req.query.query
        res.json(await roomsService.searchRoomByDistrict(query))
    }catch(err){
        console.error(`Error: `, err.message)
        next(err)
    }
})

router.get('/searchRoom/ward', async function(req, res, next){
    try{
        const query = req.query.query
        res.json(await roomsService.searchRoomByWard(query))
    }catch(err){
        console.error(`Error: `, err.message)
        next(err)
    }
})

// POST: creating
router.post('/rooms/add', async function(req, res, next){
    try{
        const body = req.body
        res.json(await roomsService.create(body))
    }catch(err){
        console.error(`Error when creating room`, err.message)
        next(err)
    }
})

//PUT: update
router.put('/:id', async function(req, res, next){
    try {
        const id = req.params.id
        res.json(await roomsService.update(id))
    } catch (err) {
        console.error(`Error when updating book`, err.message)
        next(err)
    }
})

//DELETE: delete
router.delete('/:id', async function(req, res, next){
    try {
        const id = req.params.id
        res.json(await roomsService.delete(id))
    } catch (err) {
        console.error(`Error when deleting book`, err.message)
        next(err)
    }
})

//GET: search
router.search('/:query', async function(req, res, next){
    try {
        const id = req.params.query
        res.json(await roomsService.searchRoom(express.query))
    } catch (err) {
        console.error(`Lỗi khi tìm phòng`, err.message)
        next(err)
    }
})

module.exports = router