const db = require('./db')
const helper = require('../helper')
const config = require('../config')

/**
 * 
 * @param {Trả về kiểu object} user 
 * @returns object
 */
async function getMultiple(page = 1){
    const offset = helper.getOffset(page, config.listPerPage)
    const sql = `SELECT * FROM USERS LIMIT ${offset}, ${config.listPerPage}`
    const rows = await db.query(sql)
    const users = helper.emptyOrRows(rows)
    return {
        users
    }
}

/**
 * 
 * @param {Trả về kiểu object} user 
 * @returns object
 */
async function login(data){
    if (!data.phoneNumber || !data.password) {
        throw new Error('Phone number and password must be defined');
    }
    const sql = `SELECT * FROM USERS WHERE phone_number=? AND password=?`;
    const params = [data.phoneNumber, data.password];
    const rows = await db.query(sql, params);
    const users = helper.emptyOrRows(rows);
    return users;
}

async function create(user){
    const sql = `INSERT INTO USERS (username, phone_number, password) VALUES (?, ?, ?)`
    const params = [user.username, user.phoneNumber, user.password]
    const rows = await db.query(sql, params)
    let msg = 'User created successfully';
    if (rows.affectedRows){
        msg = 'User created successfully';
    }
    return {msg};
}

async function checkPhoneNumber(data){
    const sql = `SELECT * FROM USERS WHERE phone_number=?`;
    const params = [data.phoneNumber];
    const rows = await db.query(sql, params);
    const users = helper.emptyOrRows(rows);
    return users;
}

module.exports = {
    getMultiple,
    login, 
    create,
    checkPhoneNumber
}