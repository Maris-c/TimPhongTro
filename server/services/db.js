const mysql = require('mysql2/promise')
const config = require('../config')


/* 
    Nhận 1 câu truy vấn
    async: bất đồng bộ
        - Khi app gọi API lấy db xuống, khoảng thời gian bị đó app bị đơ nếu gọi đồng bộ
            do đó tất cả phương thức gọi từ server về là bất đồng bộ
    Khi gọi hàm sài await query();
    async == await

    Thực hiện sql bất kỳ
*/
async function query(sql, params){
    const conn = await mysql.createConnection(config.db)
    const [rs, ] = await conn.execute(sql, params)
    return rs
}

/*
    Gọi thủ tục trên db
*/
async function callSpSearch(id){
    const conn = await mysql.createConnection(config.db)
    const [rs, ] = await conn.execute('CALL sp_search_room_by_id(' + id + ')')
    return rs
}

/*
    Muốn bên ngoài gọi được function thì phải exports ra bên ngoài các funtion
    Có thể gọi
        - query()
        - callSpSearch()
*/
module.exports = {
    query,
    callSpSearch
}