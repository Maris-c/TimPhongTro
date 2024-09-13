const db = require('./db')
const helper = require('../helper')
const config = require('../config')
const { query } = require('express')

async function getMultiple(page = 1){
    const offset = helper.getOffset(page, config.listPerPage)
    const sql = `SELECT 
                    ROOMS.id,
                    username AS author, 
                    phone_number,
                    get_address_as_json(address_id) AS address, 
                    interior, 
                    area, 
                    price, 
                    deposits, 
                    title, 
                    description, 
                    upload_time,
                    get_image_as_json(rooms.id) as image
                    FROM ROOMS  
                    JOIN USERS ON ROOMS.author = USERS.id
                    ORDER BY upload_time DESC
                    LIMIT ${offset}, ${config.listPerPage}`
    const rows = await db.query(sql)
    const data = helper.emptyOrRows(rows)
    return data
}

async function searchRoom(query){
    const sql = `SELECT 
                    ROOMS.id,
                    username AS author, 
                    phone_number,
                    get_address_as_json(address_id) AS address, 
                    interior, 
                    area, 
                    price, 
                    deposits, 
                    title, 
                    description, 
                    upload_time,
                    get_image_as_json(rooms.id) as image
                    FROM ROOMS 
                    JOIN USERS ON ROOMS.author = USERS.id
                    WHERE title LIKE ? OR description LIKE ?`
    const values = [`%${query}%`, `%${query}%`];
    const rows = await db.query(sql, values);
    const data = helper.emptyOrRows(rows);
    return data;
}

async function searchRoomByProvince(query){
    if (query === undefined) {
        throw new Error('Province is undefined');
    }
    const sql = `SELECT 
    ROOMS.id,
    username AS author, 
    phone_number,
    get_address_as_json(ROOMS.address_id) AS address, 
    interior, 
    area, 
    price, 
    deposits, 
    title, 
    description, 
    upload_time,
    get_image_as_json(rooms.id) as image
    FROM ROOMS 
    JOIN USERS ON ROOMS.author = USERS.id
    JOIN addresses ON rooms.address_id = addresses.address_id
    JOIN provinces on addresses.province_code = provinces.code
    WHERE provinces.full_name = ?`
    const values = [query];
    const rows = await db.query(sql, values);
    const data = helper.emptyOrRows(rows);
    return data;
}

async function searchRoomByDistrict(query){
    if (query === undefined) {
        throw new Error('Province is undefined');
    }
    const sql = `SELECT 
    ROOMS.id,
    username AS author, 
    phone_number,
    get_address_as_json(ROOMS.address_id) AS address, 
    interior, 
    area, 
    price, 
    deposits, 
    title, 
    description, 
    upload_time,
    get_image_as_json(rooms.id) as image
    FROM ROOMS 
    JOIN USERS ON ROOMS.author = USERS.id
    JOIN addresses ON rooms.address_id = addresses.address_id
    JOIN districts on addresses.district_code = districts.code
    WHERE districts.full_name = ?`
    const values = [query];
    const rows = await db.query(sql, values);
    const data = helper.emptyOrRows(rows);
    return data;
}

async function searchRoomByWard(query){
    if (query === undefined) {
        throw new Error('Province is undefined');
    }
    const sql = `SELECT 
    ROOMS.id,
    username AS author, 
    phone_number,
    get_address_as_json(ROOMS.address_id) AS address, 
    interior, 
    area, 
    price, 
    deposits, 
    title, 
    description, 
    upload_time,
    get_image_as_json(rooms.id) as image
    FROM ROOMS 
    JOIN USERS ON ROOMS.author = USERS.id
    JOIN addresses ON rooms.address_id = addresses.address_id
    JOIN wards on addresses.ward_code = wards.code
    WHERE wards.full_name = ?`
    const values = [query];
    const rows = await db.query(sql, values);
    const data = helper.emptyOrRows(rows);
    return data;
}

/**
 * 
 * @param {Trả về kiểu object} room 
 * @returns object
 */
async function create(room){
    const sql = `INSERT INTO ROOMS (author, address_id, interior, area, price, deposits, title, description) 
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)`;
    const params = [room.author, room.address_id, interior, room.area, room.price, room.deposits, room.title, room.description];
    const rs = await db.query(sql, params);
    
    const roomId = rs.insertId;
    return {id: roomId};
}

async function update(id, room){
    const sql = `UPDATE ROOMS SET title=${room.title}, 
                    description=${room.description},
                    author=${room.author},
                    WHERE id=${id}`
    const rs = await db.query(sql)
    let msg = 'Error in updated room'
    if(rs){
        msg = 'Room updated sussessfully !!!'
    }
    return {msg}
}

async function remote(id){
    const sql = `DELETE ROOMS WHERE id=${id}`
    const rs = await db.query(sql)
    let msg = 'Error in deleted room'
    if(rs){
        msg = 'Room deleted sussessfully !!!'
    }
    return {msg}
}

async function search(id){
    const sql = `SELECT * FROM ROOMS WHERE id=${id}`
    const rows = await db.query(sql)
    const data = helper.emptyOrRows(rows)
    return data
}

module.exports = {
    getMultiple,
    create,
    update,
    remote,
    searchRoom,
    searchRoomByProvince,
    searchRoomByDistrict,
    searchRoomByWard,
    search
}