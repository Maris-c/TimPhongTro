const db = require('./db')
const helper = require('../helper')
const config = require('../config')

async function getProvices(){
    const sql = `SELECT * FROM PROVINCES`
    const rows = await db.query(sql)
    const data = helper.emptyOrRows(rows)
    return {
        data
    }
}

async function getDistricts(province){
    const sql = `SELECT DISTRICTS.* FROM DISTRICTS
                JOIN PROVINCES ON DISTRICTS.PROVINCE_CODE = PROVINCES.CODE
                WHERE PROVINCES.FULL_NAME = ?`
    const rows = await db.query(sql, [province])
    const data = helper.emptyOrRows(rows)
    return {
        data
    }
}

async function getWards(districts){
    const sql = `SELECT WARDS.* FROM WARDS
                JOIN DISTRICTS ON WARDS.DISTRICT_CODE = DISTRICTS.CODE
                WHERE DISTRICTS.FULL_NAME = ?`
    const rows = await db.query(sql, [districts])
    const data = helper.emptyOrRows(rows)
    return {
        data
    }
}

/**
 * 
 * @param {*} data 
 * @returns id address
 */
async function create(data){
    const provinceResult = await db.query(`SELECT code FROM PROVINCES WHERE FULL_NAME = ?`, [data.province])
    const provinceCode = provinceResult[0]?.code

    const districtsResult = await db.query(`SELECT code FROM DISTRICTS WHERE FULL_NAME = ?`, [data.district])
    const districtCode = districtsResult[0]?.code

    const wardsResult = await db.query(`SELECT code FROM WARDS WHERE FULL_NAME = ?`, [data.ward])
    const wardCode = wardsResult.find(result => result.code)?.code

    if (!provinceCode || !districtCode || !wardCode || !data.road) {
        throw new Error('Missing required fields');
    }

    const sql = `INSERT INTO ADDRESSES (province_code, district_code, ward_code, road) VALUES (?, ?, ?, ?)`
    const params = [provinceCode, districtCode, wardCode, data.road]
    const result = await db.query(sql, params)

    const addressId = result.insertId
    return { id: addressId}
}

module.exports = {
    getProvices,
    getDistricts,
    getWards,
    create
}