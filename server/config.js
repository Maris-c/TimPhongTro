/*
    Khai báo các thông tin của database
*/

const config = {
    db:{
        // Nếu sài máy khác thì gọi ip hoặc có host thì gọi host
        host: "localhost",
        user: "ad_db_ttn",
        password: "admin",
        database: "ttn",
        connectTimeout: 60000,
        multipleStatements: true
    },
    // Hiển thị 10 dữ liệu
    listPerPage: 10, 
}

module.exports = config;