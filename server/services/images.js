const db = require('./db')
const helper = require('../helper')
const config = require('../config')

async function getMultiple(query){
    const sql = `SELECT 
                ROOMS.id,
                username AS author, 
                get_address_as_json(address_id) AS address, 
                interior, 
                area, 
                price, 
                deposits, 
                title, 
                description, 
                upload_time,
                TO_BASE64(image) as image
                FROM ROOMS 
                JOIN USERS ON ROOMS.author = USERS.id
                JOIN favorites ON rooms.id = favorites.room_id
                WHERE favorites.user_id = ?`
    const params = [query.user_id]
    const rows = await db.query(sql, params)
    const data = helper.emptyOrRows(rows)
    return data
}

async function create(data){
    const sql = `INSERT INTO IMAGES (room_id, uri_image) VALUES (?, ?)`
    const params = [data.room_id, data.uri_image]
    const rows = await db.query(sql, params)
    let message = 'Image created successfully'
    return {message}
}

async function remove(data){
    const sql = `DELETE FROM FAVORITES WHERE user_id = ? AND room_id = ?`
    const params = [data.user_id, data.room_id]
    await db.query(sql, params)
    let message = 'Favorite remoted successfully'
    return {message}
}

module.exports = {
    getMultiple,
    create,
    remove
}